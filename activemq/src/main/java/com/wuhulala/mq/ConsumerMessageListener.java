package com.wuhulala.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/26
 */
public class ConsumerMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessage(Message message) {
        TextMessage tm = (TextMessage) message;
        try {
            log.info("ConsumerMessageListener收到了文本消息：\t" + tm.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}