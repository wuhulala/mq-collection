package com.wuhulala.rabbitmq.chapter2.tx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.wuhulala.rabbitmq.util.ChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/26<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public class TxProducer {

    private static final Logger logger = LoggerFactory.getLogger(TxProducer.class);

    public static void main(String[] args) {
        Channel channel = ChannelFactory.getChannelInstance();

        logger.info("开启通道成功");
        try {
            channel.exchangeDeclare("tx-exchange", "direct");

            channel.txSelect();
            channel.basicPublish("tx-exchange", "tx", MessageProperties.PERSISTENT_BASIC, "hello tx".getBytes());
            int i = 1 / 0;
            channel.txCommit();
            logger.info("消息发送成功！");
        } catch (Exception e) {
            logger.error("消息发送失败", e);
            try {
                channel.txRollback();
            } catch (IOException e1) {
                logger.error("消息回滚失败", e1);
            }
        }
        ChannelFactory.closeChannel(channel);
        logger.info("关闭通道成功");

    }


}
