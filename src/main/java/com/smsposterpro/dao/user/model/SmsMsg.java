package com.smsposterpro.dao.user.model;

import java.util.Date;

public class SmsMsg {
    private Integer id;

    private String sendPhoneNum;

    private String context;

    private Date sendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSendPhoneNum() {
        return sendPhoneNum;
    }

    public void setSendPhoneNum(String sendPhoneNum) {
        this.sendPhoneNum = sendPhoneNum == null ? null : sendPhoneNum.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}