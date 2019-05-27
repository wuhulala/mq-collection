package com.wuhulala.rabbitmq.chapter2.persistence;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wuhulala.rabbitmq.util.ChannelFactory;

import java.io.IOException;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/27<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class PersistenceProducer {
    public static final AMQP.BasicProperties PERSISTENT_TEXT_PLAIN =
            new AMQP.BasicProperties("text/plain",
                    null,
                    null,
                    2, // 持久化
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null) ;
    public static void main(String[] args) {
        Channel channel = ChannelFactory.getChannelInstance();

        try {
            channel.exchangeDeclare("persistence-exchange", BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare("d", true, true, true, null);




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
