package com.wuhulala.orderanswer.order;

import com.alibaba.fastjson.JSON;
import com.wuhulala.orderanswer.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/29
 */
@Component
public class OrderSystem {
    @Autowired
    private JmsTemplate jmsTemplate;


    public void sendOrderToMQ(Order order){
        String message = JSON.toJSONString(order);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sendMqMessage(null,message);

    }
    /**
     * 说明:发送的时候如果这里没有显示的指定destination.将用spring xml中配置的destination
     * @param destination
     * @param message
     */
    public void sendMqMessage(Destination destination, final String message){
        if(null == destination){
            destination = jmsTemplate.getDefaultDestination();
        }
        jmsTemplate.send(destination, session -> session.createTextMessage(message));
        System.out.println("spring send message...");
    }
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

}
