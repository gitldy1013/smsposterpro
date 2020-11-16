package com.smsposterpro.config;

import com.smsposterpro.vo.AccessToken;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class MyCacheConfig {
    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> method.getName() + "[" + Arrays.asList(params).toString() + "]";
    }

    @Bean("token")
    public AccessToken getToken() {
        return new AccessToken();
    }
}
