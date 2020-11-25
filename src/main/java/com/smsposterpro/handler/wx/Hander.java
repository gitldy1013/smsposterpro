package com.smsposterpro.handler.wx;

import com.smsposterpro.dto.WeChatMessageBo;

public interface Hander {
    Object hander(WeChatMessageBo bo);
}
