package com.wuhulala.rabbitmq.chapter2.confirm;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.MessageProperties;
import com.wuhulala.rabbitmq.util.ChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/26<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class ConfirmProducer {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmProducer.class);

    public static void main(String[] args) {
        Channel channel = ChannelFactory.getChannelInstance();

        logger.info("开启通道成功");
        try {
            channel.exchangeDeclare("confirm-exchange", "direct");
            // 开启Confirm模式
            AMQP.Confirm.SelectOk ok = channel.confirmSelect();
            System.out.println(ok);
            for (int i = 0; i < 10000000; i++) {
                channel.basicPublish("confirm-exchange", "confirm", MessageProperties.PERSISTENT_BASIC, "hello tx".getBytes());
            }
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

//        channel.basicPublish("mandatory-exchange", "confirm",true, false, MessageProperties.PERSISTENT_BASIC, "hello tx".getBytes());
//        channel.addReturnListener(new ReturnListener() {
//            @Override
//            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey,
//                                     AMQP.BasicProperties properties, byte[] body) throws IOException {
//                System.out.println("没有到达目的地的消息：：" + new String(body));
//            }
//        });


    }

}
