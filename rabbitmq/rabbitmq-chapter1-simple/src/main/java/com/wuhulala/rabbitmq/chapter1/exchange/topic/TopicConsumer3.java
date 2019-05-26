package com.wuhulala.rabbitmq.chapter1.exchange.topic;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import java.io.IOException;

import static com.wuhulala.rabbitmq.chapter1.exchange.topic.Constants.C_ROUTING_KEY_3;
import static com.wuhulala.rabbitmq.chapter1.exchange.topic.Constants.TOPIC_EXCHANGE_NAME;

public class TopicConsumer3 {

    public static final String QUEUE_NAME = "queue3";


    public static void main(String[] args) throws Exception {

        Channel channel = ChannelFactory.getChannelInstance();

        //创建queue3队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("consumer3 Waiting for msg use routingKey[" + C_ROUTING_KEY_3  + "]");
        //创建消费者，并接受消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("consumer3 -> Received is = '" + msg + "'");
            }
        };
        channel.queueBind(QUEUE_NAME, TOPIC_EXCHANGE_NAME, C_ROUTING_KEY_3);
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}