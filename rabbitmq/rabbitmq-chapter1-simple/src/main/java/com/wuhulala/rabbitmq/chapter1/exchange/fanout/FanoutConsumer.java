package com.wuhulala.rabbitmq.chapter1.exchange.fanout;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import java.io.IOException;

import static com.wuhulala.rabbitmq.chapter1.exchange.direct.Constants.QUEUE_NAME;
import static com.wuhulala.rabbitmq.chapter1.exchange.fanout.Constants.FANOUT_EXCHANGE_NAME;


public class FanoutConsumer {

    public static void main(String[] args) throws Exception {

        Channel channel = ChannelFactory.getChannelInstance();

        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received is = '" + msg + "'");
            }
        };

        channel.queueBind(QUEUE_NAME, FANOUT_EXCHANGE_NAME, "hello");
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }

}