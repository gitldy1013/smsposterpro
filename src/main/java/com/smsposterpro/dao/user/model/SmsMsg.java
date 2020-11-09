package com.smsposterpro.dao.user.model;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Date;

@Data
public class SmsMsg {
    private Integer id;

    private String sendPhoneNum;

    private String context;

    private Date sendTime;

    private String recPhone;

    //@Data 注解的坑 字段名字不能存在同名大小写不同的字段 否则后出现的同名字段没有生成get和set方法
    //private Date sendtime;

    public static void main(String[] args) {
        Class<? extends Object> clazz = SmsMsg.class;
        Method[] methods = clazz.getMethods();
    }
}
