package com.wuhulala.rabbitmq.chapter2.ttl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wuhulala.rabbitmq.util.ChannelFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/27<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class TTLConsumer {

    public static void main(String[] args) throws IOException {
        Channel channel = ChannelFactory.getChannelInstance();


        channel.exchangeDeclare("ttl-exchange", BuiltinExchangeType.DIRECT);

        Map<String, Object> props = new HashMap<String, Object>();
        props.put("x-expires", 20 * 1000); // 设置 20s超时
        channel.queueDeclare("ttl-queue", false, false, false, props);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received is = '" + msg + "'");
            }
        };
        channel.queueBind("ttl-queue", "ttl-exchange", "ttl");
        channel.basicConsume("ttl-queue", true, consumer);

    }


}
