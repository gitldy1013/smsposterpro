package com.smsposterpro.handler.wx.impl;

import com.smsposterpro.annotation.Hkey;
import com.smsposterpro.dao.model.SmsMsg;
import com.smsposterpro.dto.NewItems;
import com.smsposterpro.dto.WeChatMessageBo;
import com.smsposterpro.dto.WeChatMessageVo;
import com.smsposterpro.handler.wx.Hander;
import com.smsposterpro.service.sms.SmsMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Hkey(name = "event.subscribe.null")
@Service
public class Subscribe implements Hander {
    //用户订阅
    @Autowired
    private SmsMsgService smsMsgService;

    @Override
    public Object hander(WeChatMessageBo msg) {
        WeChatMessageVo<NewItems> out = new WeChatMessageVo<>();
        //把原来的发送方设置为接收方
        out.setToUserName(msg.getFromUserName());
        //把原来的接收方设置为发送方
        out.setFromUserName(msg.getToUserName());
        //获取接收的消息类型
        //String msgType = msg.getMsgType();
        //设置消息创建时间
        out.setCreateTime(System.currentTimeMillis());
        out.setMsgType("news");
        out.setArticleCount(4);
        ArrayList<NewItems> items = new ArrayList<>(4);
        //用户自定义数据
        List<SmsMsg> list = smsMsgService.findList(new SmsMsg());
        list.stream().forEach(item -> {
            NewItems newItems = new NewItems();
            newItems.setTitle(item.getRecPhone());
            newItems.setUrl(item.getSendPhoneNum());
            newItems.setDescription(item.getContext());
            newItems.setPicUrl(item.getSendPhoneNum());
            items.add(newItems);
        });
        out.setItem(items);
        return out;
    }
}
