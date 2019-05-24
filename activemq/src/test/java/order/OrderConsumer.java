package order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:order/spring-mq-consumer.xml")
public class OrderConsumer {


    @Test
    public void consume(){
        System.out.println("-----------启动容器  开始消费---------");
        //防止spring容器挂掉
        while(true){
            //System.out.println("-------接受消息--------");

        }
    }

}
