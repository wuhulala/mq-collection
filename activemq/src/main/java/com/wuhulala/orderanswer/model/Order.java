package com.wuhulala.orderanswer.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wuhulala
 * @version 1.0
 * @updateTime 2016/12/29
 */
public class Order implements Serializable {
    private int id;

    private String serialNumber;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private int status;
    private String phone;
    private String nickName;

    public Order(int id, String serialNumber, String name, String phone) {

        this.phone = phone;
        this.id = id;
        this.createTime = new Date();
        this.serialNumber = serialNumber;
        this.status = 0;
        this.nickName = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
