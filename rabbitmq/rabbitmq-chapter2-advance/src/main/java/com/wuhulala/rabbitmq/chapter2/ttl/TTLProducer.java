package com.wuhulala.rabbitmq.chapter2.ttl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wuhulala.rabbitmq.util.ChannelFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/27<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class TTLProducer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = ChannelFactory.getChannelInstance();

        channel.exchangeDeclare("ttl-exchange", BuiltinExchangeType.DIRECT);
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("ttl-exchange", "ttl", null, ("ttl-" + i).getBytes());
            Thread.sleep(31000);
        }

        channel.close();
        channel.getConnection().close();
    }


}
