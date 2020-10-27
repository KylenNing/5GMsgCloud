package cc.htdf.msgcloud.common.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * author: JT
 * date: 2020/8/27
 * title:
 */
@Setter
@Getter
public class MsgLog {

    private String msgId;

    private String chatbotId;

    @JSONField(name = "@timestamp")
    private Date timestamp;

    private String date;

    private String msgType;

    private String serviceBusiness;

    private String msgContent;

    private MsgLogBody msgBody;

    private String status;

    private String statusBody;



}
