package com.smsposterpro.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Hkey {
    //区分不同的请求类型
    String name() default "";
}
