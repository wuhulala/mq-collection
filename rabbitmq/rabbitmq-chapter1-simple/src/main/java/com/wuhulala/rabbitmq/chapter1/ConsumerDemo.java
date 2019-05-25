package com.wuhulala.rabbitmq.chapter1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import static com.wuhulala.rabbitmq.chapter1.ProducerDemo.EXCHANGE_NAME;

public class ConsumerDemo {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂, 设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");


        Connection conn = factory.newConnection();
        //创建消息通道
        Channel channel = conn.createChannel();

        //创建hello队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" Waiting for msg....");
        //创建消费者，并接受消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received is = '" + msg + "'");
            }
        };
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "hello");
        channel.basicConsume(QUEUE_NAME, true, consumer);

        
    }

}