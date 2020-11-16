package com.smsposterpro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "WeChatMessageBo",
        propOrder = {
                "msgId",
                "toUserName",
                "fromUserName",
                "createTime",
                "msgType",
                "event",
                "content",
                "picUrl",
                "mediaId",
                "eventKey"
        })
public class WeChatMessageBo {
    // 消息id
    @XmlElement(name = "MsgId", required = true)
    protected Long msgId;
    @XmlElement(name = "ToUserName", required = true)
    private String toUserName;
    @XmlElement(name = "FromUserName", required = true)
    private String fromUserName;
    @XmlElement(name = "CreateTime", required = true)
    private long createTime;
    @XmlElement(name = "MsgType", required = true)
    private String msgType;
    @XmlElement(name = "Event", required = true)
    private String event;
    // 文本内容
    @XmlElement(name = "Content", required = true)
    private String content;
    // 图片链接（由系统生成）
    @XmlElement(name = "PicUrl", required = true)
    private String picUrl;
    // 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
    @XmlElement(name = "MediaId", required = true)
    private String mediaId;
    @XmlElement(name = "EventKey", required = true)
    private String eventKey;
}
