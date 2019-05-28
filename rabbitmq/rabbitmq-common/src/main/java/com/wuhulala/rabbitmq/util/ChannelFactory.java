package com.wuhulala.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/26<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class ChannelFactory {

    private static final Logger logger = LoggerFactory.getLogger(ChannelFactory.class);

    public static Channel getChannelInstance() {
        // 1. 创建连接工厂, 设置属性
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        //factory.setPassword("");

        try {
            Connection conn = factory.newConnection();
            //创建消息通道
            return conn.createChannel();
        } catch (Exception e) {
            logger.error("创建channel失败", e);
            throw new RuntimeException("创建channel失败,请检查配置参数", e);
        }

    }

    public static void closeChannel(Channel channel) {
        try {
            channel.close();
            channel.getConnection().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
