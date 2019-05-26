package com.wuhulala.rabbitmq.chapter1.exchange.header;

import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import java.io.IOException;

import static com.wuhulala.rabbitmq.chapter1.exchange.header.Constants.HEADER_EXCHANGE_NAME;

public class HeaderConsumer2 {

    public static final String QUEUE_NAME = "queue1";

    public static void main(String[] args) throws Exception {

        Channel channel = ChannelFactory.getChannelInstance();

        //创建queue1队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("consumer2 Waiting for msg.... ");
        //创建消费者，并接受消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("consumer2 -> Received is = '" + msg + "', and header is" + properties.getHeaders());
            }
        };
        channel.queueBind(QUEUE_NAME, HEADER_EXCHANGE_NAME, "", ImmutableMap.<String, Object>of("a", "A", "b", "Bb"));
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}