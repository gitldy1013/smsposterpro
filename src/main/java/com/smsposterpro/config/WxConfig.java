package com.smsposterpro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weixin")
public class WxConfig {
    private String appid;
    private String secret;
}
