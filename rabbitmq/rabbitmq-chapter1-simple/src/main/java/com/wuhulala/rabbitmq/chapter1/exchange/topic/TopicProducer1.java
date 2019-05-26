package com.wuhulala.rabbitmq.chapter1.exchange.topic;

import com.rabbitmq.client.Channel;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import static com.wuhulala.rabbitmq.chapter1.exchange.topic.Constants.P_ROUTING_KEY_1;

public class TopicProducer1 {


    public static void main(String[] args) throws Exception {


        // 3. 使用connection创建channel
        Channel channel = ChannelFactory.getChannelInstance();
        channel.exchangeDeclare(Constants.TOPIC_EXCHANGE_NAME, "topic");

        // 4. 通过channel发送消息
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            String msg = "msg1_A.B.C";
            System.out.println("send ::: " + msg);
            channel.basicPublish(Constants.TOPIC_EXCHANGE_NAME, P_ROUTING_KEY_1, null, msg.getBytes());
        }
        long end = System.currentTimeMillis();

        // 5. 关闭连接
        channel.close();
        channel.getConnection().close();
    }

}