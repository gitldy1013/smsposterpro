package com.smsposterpro.service.wx.impl;

import com.alibaba.fastjson.JSONObject;
import com.smsposterpro.vo.AccessToken;
import com.smsposterpro.vo.ResBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
class WeChatServiceImplTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WeChatServiceImpl weChatService;

    @Test
    void getToken() throws InterruptedException {
        AccessToken token1 = weChatService.getToken();
        Thread.sleep(5000L);
        AccessToken token2 = weChatService.getToken();
        log.info("返回数据:{}", token1);
        log.info("返回数据:{}", token2);
    }

    @Test
    public void createMenu() {
        JSONObject req = new JSONObject();
        ResponseEntity<ResBean> resBeanResponseEntity = restTemplate.postForEntity("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + weChatService.getToken().getAccess_token(), req, ResBean.class);
    }
}
