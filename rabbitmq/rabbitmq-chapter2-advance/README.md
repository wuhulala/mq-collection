[TOC]

# RabbitMQ 高级特性

## 消息可靠性投递（可靠性发送）

> 当消息的生产者将消息发送出去之后，消息 到底有没有正确地到达服务器呢?如果不进行特殊配置，默认情况下发送消息的操作是不会返回任何信息给生产者的，也就是默认情况下生产者是不知道消息有没有正确地到达服务器。如果在消息到达服务器之前己经丢失，持久化操作也解决不了这个问题，因为消息根本没有到达服务器，何谈持久化?

针对以上的问题，RabbitMQ提供了以下的两种解决方案：
- 通过事务机制实现:
- 通过发送方确认 (publisher confirm) 机制实现

### 事务机制
RabbitMQ 客户端中与事务机制相关的方法有三个: 
1. channel.txSelect 开启事务
2. channel.txCommit 提交事务
3. channel.txRollback 事务回滚

和JDBC的事务三个步骤类似，都是三步走


#### 代码实现
```java
public static void main(String[] args) {
        Channel channel = ChannelFactory.getChannelInstance();

        logger.info("开启通道成功");
        try {
            channel.exchangeDeclare("tx-exchange", "direct");

            channel.txSelect();
            channel.basicPublish("tx-exchange", "tx", MessageProperties.PERSISTENT_BASIC, "hello tx".getBytes());
            int i = 1 / 0;
            channel.txCommit();
            logger.info("消息发送成功！");
        } catch (Exception e) {
            logger.error("消息发送失败", e);
            try {
                channel.txRollback();
            } catch (IOException e1) {
                logger.error("消息回滚失败", e1);
            }
        }
        ChannelFactory.closeChannel(channel);
        logger.info("关闭通道成功");

    }
```
事务确实能够解决消息发送方和 RabbitMQ 之间消息确认的问题，'只有消息成功被 RabbitMQ 接收，事务才能提交成功，
否则便可在捕获异常之后进行事务回滚，与此同时可以使用**消息重发机制**来保证消息不丢失。

除了这种方案保证消息发送的可靠性，还有其它什么方案呢？

从 AMQP 协议层面来看并没有更好的办法，但是 RabbitMQ 提供了一个改进方案，即发送方确认机制，详情请看下一节的介绍。

### 发送方确认机制

生产者将信道设置成 confmn C确认)模式，一旦信道进入 confmn 模式，所有在该信道上面发布的消息都会被指派一个唯一的 IDC从 l 开始)，
一旦消息被投递到所有匹配的队列之后， RabbitMQ 就会发送一个确认 CBasic.Ack) 给生产者(包含消息的唯一 ID)，这就使得生产者知晓消息已经正确到达了目的地了。
如果消息和队列是可持久化的，那么确认消息会在消息写入磁盘之后发出。 
RabbitMQ 回传给生产者的确认消息中的 deliveryTag 包含了确认消息的序号，此外 RabbitMQ 也可以设置 channel.basicAck 方法中的 multiple 参数，表示到 这个序号之前的所有消息都己经得到了处理
候的确认之间的异同。

#### 为什么比事务性能好
事务机制在一条消息发送之后会使发送端**阻塞**，以等待 RabbitMQ 的回应，之后才能继续发送下一条消息。

相比之下， 发送方确认机制最大的好处在于它是**异步**的，一旦发布一条消息，生产者应用程序就可以在**等信道返回确认的同时**继续发送下一条消息，当消息最终得到确认后，生产者应用程序便可以通过回调方法来处理该确认消息，
如果 RabbitMQ 因为自身内部错误导致消息丢失，就会发送一条 nack（Basic.Nack, not ack) 命令，生产者应用程序同样可以在回调方法中处理该 nack 命令。
生产者通过调用 channel.confirmSelect 方法(即 Confirm.Select 命令)将信道设置为 confirm 模式，
之后 RabbitMQ 会返回 Confirm.Select-Ok 命令表示同意生产者将当前信道设置为 confirm 模式。
所有被发送的后续消息都被 ack 或者 nack 一次，不会出现一条消息既被ack又被nack的情况，并且 RabbitMQ 也并没有对消息被 confirm 的快慢做任何保证。


#### 示例代码
```
public static void main(String[] args) {
        Channel channel = ChannelFactory.getChannelInstance();

        logger.info("开启通道成功");
        try {
            channel.exchangeDeclare("confirm-exchange", "direct");
            // 开启Confirm模式
            AMQP.Confirm.SelectOk ok = channel.confirmSelect();

            channel.basicPublish("confirm-exchange", "confirm", MessageProperties.PERSISTENT_BASIC, "hello tx".getBytes());
            logger.info("消息发送成功！");
        } catch (Exception e) {
            logger.error("消息发送失败", e);
        }
        // 6. 设置监听
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("-------服务端 ACK Success--------");
            }

            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------服务端 ACK Failed--------");
            }
        });

    }
```

### 测试一下QPS

服务器环境 部署在windows（i78700 6C 3.2GHZ 16G）上的一个docker容器上面，运行时发现CPU飙到4.3GHZ，利用率30% ，因为分配给整个docker了2C。2/8 = 25%


1. Confirm 机制

![](docs/images/confirm-speed.png)

2. 事务机制

![](docs/images/tx-speed.png)

docker 容器内部负载

![](docs/images/docker-internel.png)


可以看到上面，事务机制的时候的qps，会有二十倍性能的下降，这是为什么呢，我猜因为不是批量提交。


测试一下100条批量提交

![](docs/images/tx-batch-speed.png)

基本和confirm持平。但是由于confirm机制是异步机制，也就是代码在顺序上正常执行，如果需要确认消息正常发送到broker，使用的是callback机制。和future类似。所以在现实编码中，使用这两种的哪一个，可以对照future的机制。


**QPS略微下降的原理**
> 对于持久化的消息来说，两者都需要等待消息确认落盘之后才会返回(调用 Linux内核的fsync方法)。在同步等待的方式下， publisher confirm 机制发送一条消息需要通 信交互的命令是 2 条:Basic.Publish 和 Basic .Ack; 事务机制是 3 条 :Basic.Publish、
Tx.Commmit/.Commit-Ok (或者 Tx.Rollback/.Rollback-Ok) ， 事务机制多了一个命令帧报文的交互，所以 QPS 会略微下降。

### 持久化存储
// TODO

### 延迟队列
* 在订单系统中， 一个用户下单之后通常有 30 分钟的时间进行支付，如果 30 分钟之内没有支付成功，那么这个订单将进行异常处理，这时就可以使用延迟队列来处理这些 订单了 。
* 用户希望通过手机远程遥控家里的智能设备在指定的时间进行工作。这时候就可以将 用户指令发送到延迟队列，当指令设定的时间到了再将指令推送到智能设备。
// TODO

### 

## 消息可靠性接收
### 消息接收的几种情况

#### 幂等
目前实现的最多的就是`At least one`语义,也就是保证消费者至少接收到一次消息,所以会存在重复消息的情况，需要我们业务代码进行幂等的处理.

1. 可以给发送的消息一个唯一ID，将ID存储在分布式缓存（或数据库）中，这样在每次接收到消息之后，进行比较，看是否是重复的消息。
* 需要保证缓存一定是高可用的

#### 可靠性消费
假如消费者，在消费信息，然后ack了，但是刚开始处理就宕机了。
1. 方案1： 如果消息特别重要的话，借助数据库存储每次接收到的消息，然后设置标志位为0,如果处理成功就为1，如果处理失败为2（人工介入排查错误）。开启一个分布式任务，检测标志位为0并且超时（>= 20min）的。
2. 方案2： TODO 

#### 消息顺序
假如消费者处理的消息需要依赖发送者发送消息的顺序，那么就需要一些机制来保证了,比如Producer发送的是m1、m2、m3,那么Consumer接收到的应该也是m1、m2、m3。


#### 消费失败，重回队列

#### 消息者慢，限流

#### 消费者断开连接，TTL队列/消息

#### Return消息机制

#### 死信队列(DLX)
消息被拒绝、消息过期、无法入队，该何去何从

#### 多消费者，消息分发


### Rabbit MQ 可靠性传输总结

消息可靠传输一般是业务系统接入消息中间件时首要考虑的问题，一般消息中间件的消息 传输保障分为三个层级。

* At most once 消息可能会丢，但绝不会重复传输
* At least one 消息绝不会丢，但可能会重复传输
* Exactly once 每条消息肯定会被传输一次且仅传输一次

RabbitMQ 支持其中的"最多一次"和"最少一次"。其中"最少一次"投递实现需要考虑 以下这个几个方面的内容:
1. 消息生产者需要开启事务机制或者 publisher confirm 机制，以确保消息可以可靠地传 输到 RabbitMQ 中。
2. 消息生产者需要配合使用 mandatory 参数或者备份交换器来确保消息能够从交换器 路由到队列中，进而能够保存下来而不会被丢弃。
3. 消息和队列都需要进行持久化处理，以确保 RabbitMQ 服务器在遇到异常情况时不会造成消息丢失。
4. 消费者在消费消息的同时需要将 autoAck 设置为 false，然后通过手动确认的方式去 确认己经正确消费的消息，以避免在消费端引起不必要的消息丢失。
