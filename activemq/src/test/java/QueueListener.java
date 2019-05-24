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
@ContextConfiguration("classpath*:spring-mq-listener.xml")
public class QueueListener {


    @Test
    public void listener(){
        System.out.println("-----------启动容器---------");
        while(true){
            //System.out.println("-------接受消息--------");

        }
    }

}
