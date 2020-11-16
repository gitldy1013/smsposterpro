package com.smsposterpro.handler.wx;

import com.smsposterpro.dto.WeChatMessageBo;

public interface Hander {
    public Object hander(WeChatMessageBo bo);
}
