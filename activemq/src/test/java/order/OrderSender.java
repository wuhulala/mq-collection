package order;

import com.wuhulala.orderanswer.model.Order;
import com.wuhulala.orderanswer.order.OrderSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/order/spring-mq-sender.xml")
public class OrderSender {

    @Autowired
    private OrderSystem orderSystem;
    @Test
    public void send(){
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int id = ++i;
            String serialNumber = sdf.format(new Date())+getString(i+"",6);
            String name = "name"+i;
            String phone = "phone"+i;
            orderSystem.sendOrderToMQ(new Order(id,serialNumber,name,phone));

        }
    }

    public String  getString(String src , int len){

        while(src.length() < len){
            src = "0" + src ;
        }
        return src ;
    }

}
