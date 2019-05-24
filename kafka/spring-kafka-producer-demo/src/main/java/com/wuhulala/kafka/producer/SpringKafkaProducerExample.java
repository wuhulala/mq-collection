package com.wuhulala.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class SpringKafkaProducerExample {

    @Autowired
    private KafkaTemplate<String, String> producer;


    public void testSend(){
        String[] strings = new String[1000000];
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000000; i++) {
            strings[i] = Integer.toString(i) ;
        }
        long end = System.currentTimeMillis();
        List<ListenableFuture<SendResult<String, String>>> futures = new ArrayList<>();
        System.out.println("prepare use time ： [" + (end - start) + "]");

        for (int i = 0; i < 100000; i++) {
            futures.add(producer.sendDefault(strings[i], strings[i] + ":::::::::new"));
        }
        end = System.currentTimeMillis();
        for (ListenableFuture<SendResult<String, String>> future : futures){
            if(future.isDone()){
                try {
                    System.out.println(future.get().getProducerRecord().key() + "is ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("send use time ： [" + (end - start) + "]");
    }

}