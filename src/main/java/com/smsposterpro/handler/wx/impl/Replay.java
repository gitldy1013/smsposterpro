package com.smsposterpro.handler.wx.impl;

import com.smsposterpro.annotation.Hkey;
import com.smsposterpro.dao.user.model.SmsMsg;
import com.smsposterpro.dto.NewItems;
import com.smsposterpro.dto.WeChatMessageBo;
import com.smsposterpro.dto.WeChatMessageVo;
import com.smsposterpro.handler.wx.Hander;
import com.smsposterpro.service.sms.SmsMsgService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Hkey(name = "text.null.null")
public class Replay implements Hander {

    @Autowired
    private SmsMsgService smsMsgService;
    private String unfind = "对不起，没找到你搜索的数据。\n要不，联系一下客服？或者我给你笑一个？\uE057";

    @Override
    //回复用户和图文消息
    public Object hander(WeChatMessageBo msg) {
        WeChatMessageVo<NewItems> out = new WeChatMessageVo();
        //把原来的发送方设置为接收方
        out.setToUserName(msg.getFromUserName());
        //把原来的接收方设置为发送方
        out.setFromUserName(msg.getToUserName());
        //获取接收的消息类型
        String msgType = msg.getMsgType();
        String content = msg.getContent();
        //设置消息创建时间
        out.setCreateTime(System.currentTimeMillis());
        out.setMsgType("news");
        //用户自定义数据
        List<SmsMsg> list = smsMsgService.findList(new SmsMsg());

        if (CollectionUtils.isEmpty(list)) {
            out.setMsgType("text");
            out.setContent(unfind);
            return out;
        } else {

            ArrayList<NewItems> items = new ArrayList<NewItems>(list.size());
            list.stream().forEach(item -> {
                NewItems newItems = new NewItems();
                newItems.setTitle(item.getRecPhone());
                newItems.setUrl(item.getSendPhoneNum());
                newItems.setDescription(item.getContext());
                newItems.setPicUrl(item.getSendPhoneNum());
                items.add(newItems);
            });
            out.setArticleCount(list.size());
            out.setItem(items);
            return out;
        }
    }
}
