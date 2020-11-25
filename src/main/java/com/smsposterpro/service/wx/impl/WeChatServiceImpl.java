package com.smsposterpro.service.wx.impl;

import com.smsposterpro.annotation.Hkey;
import com.smsposterpro.config.WxConfig;
import com.smsposterpro.dto.WeChatMessageBo;
import com.smsposterpro.handler.wx.Hander;
import com.smsposterpro.service.wx.WeChatService;
import com.smsposterpro.vo.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService, ApplicationContextAware {
    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private AccessToken accessToken;

    private RestTemplate restTemplate = new RestTemplate();

    private HashMap<String, Hander> handers = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Hander> beansOfType = applicationContext.getBeansOfType(Hander.class);
        for (Hander hander : beansOfType.values()) {
            Hkey hkey = hander.getClass().getAnnotation(Hkey.class);
            handers.put(hkey.name(), hander);
        }
    }


    @Override
    public Object processRequest(WeChatMessageBo msg) {
        String msgType = msg.getMsgType();
        String event = msg.getEvent();
        String eventKey = msg.getEventKey();
        Hander hander = handers.get(msgType + "." + event + "." + eventKey);
        if (hander != null) {
            return hander.hander(msg);
        }
        return null;
    }

    public AccessToken getToken() {
        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}

        if (accessToken.getCurrentTime() == 0L || (System.currentTimeMillis() - accessToken.getCurrentTime()) >= (Long.parseLong(accessToken.getExpires_in()) * 1000)) {
            accessToken = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?appid=" + wxConfig.getAppid() + "&secret=" + wxConfig.getSecret() + "&grant_type=client_credential", AccessToken.class);
            assert accessToken != null;
        }
        accessToken.setCurrentTime(System.currentTimeMillis());
        return accessToken;
    }
}
