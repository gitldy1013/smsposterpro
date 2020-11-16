package com.smsposterpro.handler.wx.impl;

import com.smsposterpro.annotation.Hkey;
import com.smsposterpro.dto.NewItems;
import com.smsposterpro.dto.WeChatMessageBo;
import com.smsposterpro.dto.WeChatMessageVo;
import com.smsposterpro.handler.wx.Hander;

@Hkey(name = "event.CLICK.V02_HELP")
public class ForHelp implements Hander {
    private String value = "你好,请拨打电话\n";

    //联系客服
    @Override
    public Object hander(WeChatMessageBo msg) {
        WeChatMessageVo<NewItems> out = new WeChatMessageVo();
        //把原来的发送方设置为接收方
        out.setToUserName(msg.getFromUserName());
        //把原来的接收方设置为发送方
        out.setFromUserName(msg.getToUserName());
        //获取接收的消息类型
        String msgType = msg.getMsgType();
        //设置消息创建时间
        out.setCreateTime(System.currentTimeMillis());
        out.setMsgType("text");
        out.setContent(value);
        return out;
    }
}
