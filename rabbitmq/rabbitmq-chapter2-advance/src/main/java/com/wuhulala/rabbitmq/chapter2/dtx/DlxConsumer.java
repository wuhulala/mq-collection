package com.wuhulala.rabbitmq.chapter2.dtx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wuhulala.rabbitmq.util.ChannelFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 *
 * @author wuhulala<br>
 * @date 2019/5/27<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class DlxConsumer {


    public static void main(String[] args) {
        Channel channel = ChannelFactory.getChannelInstance();

        try {
            channel.exchangeDeclare("normal-exchange", BuiltinExchangeType.DIRECT, false);
            channel.exchangeDeclare("dlx-exchange", BuiltinExchangeType.DIRECT, false);


            // 普通TTL队列设置
            Map<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("x-message-ttl", 10 * 1000); // 设置 20s超时
            arguments.put("x-dead-letter-exchange", "dlx-exchange"); //
            arguments.put("x-dead-letter-routing-key", "dlx");
            channel.queueDeclare("normal-queue", false, false, false, arguments);
            channel.queueBind("normal-queue", "normal-exchange", "dlx");

            // 死信交换机、死信队列开启
            channel.queueDeclare("dlx-queue", true, false, false, null);
            channel.queueBind("dlx-queue", "dlx-exchange", "dlx");

            channel.basicPublish("normal-exchange", "dlx",null, "hello dlx".getBytes());
            // 不设置消费者

            channel.basicConsume("normal-queue", false, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    channel.basicNack(envelope.getDeliveryTag(), false, false);
                    System.out.println("拒绝收信成功。");
                }
            });

//            channel.close();
//            channel.getConnection().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
