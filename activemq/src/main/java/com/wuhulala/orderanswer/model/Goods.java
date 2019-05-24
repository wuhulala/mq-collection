package com.wuhulala.orderanswer.model;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/29
 */
public class Goods {
    private Long id;

    private String name;
    private String price;
    private int remind;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }
}
