package cc.htdf.msgcloud.common.domain;

import com.feinno.msgctenter.sdk.dto.MsgInfo;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * author: JT
 * date: 2020/8/13
 * title:
 */
@Setter
@Getter
public class Msg {

    /**
     * 渠道商:
     *      ultra 神州泰岳
     *      mobile 浙江移动
     */
    private String channelBusiness;

    /**
     * 操作：
     *      msgup 上行
     *      msgdown 下行
     */
    private String operation;

    /**
     * 信息类型
     *      autoReplay 自动回复
     *      immediateDown  即时下发
     *      triggerDown   触发下发
     *      up 上行消息
     */
    private String type;

    /**
     * 信息状态
     *      1  消息未发送
     *      2  消息发送中
     *      3  消息已发送
     */
    private Integer status;

    /**
     * 意图
     */
    private String action;


    /**
     * Chatbot 相关信息
     */
    private MsgChatbot msgChatbot;

    /**
     * 参数
     */
    private Map<String, Object> param;

    /**
     * 上行信息  上行上传消息使用
     */
    private MsgUpInfo msgUpInfo;

    /**
     * 下行信息  前台新建消息使用
     */
    private MsgDownInfo msgDownInfo;

    /**
     * 生成消息  发送消息使用
     */
    private MsgInfo msgInfo;



}
