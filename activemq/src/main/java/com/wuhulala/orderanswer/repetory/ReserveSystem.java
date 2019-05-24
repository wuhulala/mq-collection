package com.wuhulala.orderanswer.repetory;

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
public class ReserveSystem implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessage(Message message) {
        TextMessage tm = (TextMessage) message;
        try {
            //log.info("OrderListener收到了文本消息：\t" + tm.getText());
            handleOrder(tm.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void handleOrder(String message){
        System.out.println("------库存系统操作开始------");
        System.out.println("读取：   "+message);
        System.out.println("减库存");
        System.out.println("------库存系统操作结束------");
    }
}