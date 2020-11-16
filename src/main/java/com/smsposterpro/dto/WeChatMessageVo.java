package com.smsposterpro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeChatMessageVo<T>{
    // 发送方的账号
    protected String FromUserName;
    // 接收方的账号(OpenID)
    protected String ToUserName;
    // 消息创建时间
    protected Long CreateTime;
    /**
     * 消息类型
     * text 文本消息
     * image 图片消息
     * voice 语音消息
     * video 视频消息
     * music 音乐消息
     * news 图文消息
     */
    protected String MsgType;
    // 语音
    @XmlElementWrapper(name = "Voice")
    private String[] MediaId;
    // 文本内容
    private String Content;

    private int ArticleCount;
    //图文消息
    @XmlElementWrapper(name = "Articles")
    private List<T> item;

}
