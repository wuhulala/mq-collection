package com.wuhulala.rabbitmq.chapter1.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.wuhulala.rabbitmq.chapter1.util.ChannelFactory;

import static com.wuhulala.rabbitmq.chapter1.exchange.fanout.Constants.FANOUT_EXCHANGE_NAME;

public class FanoutProducer {


    public static void main(String[] args) throws Exception {
        Channel channel = ChannelFactory.getChannelInstance();

        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");

        String msg = "hello rabbitmq! ---- ";
        channel.basicPublish(FANOUT_EXCHANGE_NAME, "hello", null, msg.getBytes());


        channel.close();
        channel.getConnection().close();
    }

}