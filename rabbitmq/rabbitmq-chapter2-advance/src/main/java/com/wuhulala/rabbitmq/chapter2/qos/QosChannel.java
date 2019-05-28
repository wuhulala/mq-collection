package com.wuhulala.rabbitmq.chapter2.qos;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wuhulala.rabbitmq.util.ChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/28<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class QosChannel {

    private static final Logger logger = LoggerFactory.getLogger(QosChannel.class);

    public static void main(String[] args) throws Exception {
        Channel channel = ChannelFactory.getChannelInstance();
        channel.exchangeDeclare("quick-exchange", BuiltinExchangeType.DIRECT);
        channel.queueDeclare("quick-queue", false, false, false, null);
        channel.queueBind("quick-queue", "quick-exchange", "quick");

        // 设置 限流
        // prefetchSize 最大的消息大小
        // prefetchCount 最大的消息条数
        // global 针对所有的消费者
        channel.basicQos(0, 3, true);

        // 发送 10000条
        for (int i = 0; i < 10000; i++) {
            channel.basicPublish("quick-exchange", "quick", null, ("msg" + i).getBytes());
        }

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    Channel channel2 = ChannelFactory.getChannelInstance();

                    channel2.basicConsume("quick-queue", false, new DefaultConsumer(channel2){
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            logger.info(envelope.getDeliveryTag() + ":::::" + new String(body));
                            channel2.basicAck(envelope.getDeliveryTag(), false);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

}
