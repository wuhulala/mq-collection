import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-mq-sender.xml")
public class QueueSender {

    @Resource
    private JmsTemplate jmsTemplate;
    @Test
    public void send(){
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendMqMessage(null,sdf.format(new Date())+ "send :spring activemq queue type message !");
        }
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
