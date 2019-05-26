# RabbitMQ 高级特性

## 生产者消息确认

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

* RabbitMQ高级特性-消息可靠性投递
* RabbitMQ高级特性-幂等性保障
* RabbitMQ高级特性-Confirm确认消息
* RabbitMQ高级特性-Return消息机制
* RabbitMQ高级特性-消费端自定义监听
* RabbitMQ高级特性-消费端限流
* RabbitMQ高级特性-消费端ACK与重回队列
* RabbitMQ高级特性-TTL队列/消息
* RabbitMQ高级特性-死信队列(DLX)