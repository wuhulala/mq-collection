package com.wuhulala.rabbitmq.chapter1.exchange.header;

import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import static com.wuhulala.rabbitmq.chapter1.exchange.topic.Constants.P_ROUTING_KEY_1;

public class HeaderProducer1 {


    public static void main(String[] args) throws Exception {


        // 3. 使用connection创建channel
        Channel channel = ChannelFactory.getChannelInstance();
        channel.exchangeDeclare(Constants.HEADER_EXCHANGE_NAME, "headers");


        AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                .headers(ImmutableMap.<String, Object>of("a", "A", "b", "B")).build();

        // 4. 通过channel发送消息
        String msg = "msg";
        System.out.println("Producer1 send ::: " + msg);
        channel.basicPublish(Constants.HEADER_EXCHANGE_NAME, P_ROUTING_KEY_1, props, msg.getBytes());


        // 5. 关闭连接
        channel.close();
        channel.getConnection().close();
    }

}