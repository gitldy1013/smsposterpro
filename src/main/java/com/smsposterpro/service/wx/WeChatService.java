package com.smsposterpro.service.wx;

import com.smsposterpro.dto.WeChatMessageBo;

public interface WeChatService {


    Object processRequest(WeChatMessageBo msg);
}
