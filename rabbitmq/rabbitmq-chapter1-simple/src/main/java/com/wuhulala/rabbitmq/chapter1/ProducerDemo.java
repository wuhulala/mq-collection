package com.wuhulala.rabbitmq.chapter1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerDemo {

    public static final String EXCHANGE_NAME = "wuhulala-exchanger";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂, 设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .headers(null)
                .build();

        // 2. 创建连接
        Connection connection = factory.newConnection();

        // 3. 使用connection创建channel
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 4. 通过channel发送消息
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            String msg = "hello rabbitmq! ---- " + i;
            // 不指定exchange的情况下, 使用默认的exchange, routingKey与队列名相等
            channel.basicPublish(EXCHANGE_NAME, "hello", null, msg.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("send used::: " + (end - begin) + "ms");

        // 5. 关闭连接
        channel.close();
        connection.close();
    }

}