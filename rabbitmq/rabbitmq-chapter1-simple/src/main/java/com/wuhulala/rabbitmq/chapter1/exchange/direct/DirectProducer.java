package com.wuhulala.rabbitmq.chapter1.exchange.direct;

import com.rabbitmq.client.Channel;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import static com.wuhulala.rabbitmq.chapter1.exchange.direct.Constants.DIRECT_EXCHANGE_NAME;

public class DirectProducer {


    public static void main(String[] args) throws Exception {
        Channel channel = ChannelFactory.getChannelInstance();

        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, "direct");

        String msg = "hello rabbitmq! ---- ";
        channel.basicPublish(DIRECT_EXCHANGE_NAME, "hello", null, msg.getBytes());


        channel.close();
        channel.getConnection().close();
    }

}