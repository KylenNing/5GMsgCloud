package cc.htdf.msgcloud.message.listener;

import cc.htdf.msgcloud.common.config.LogConfig;
import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.constants.MsgLogStatus;
import cc.htdf.msgcloud.common.constants.MsgLogType;
import cc.htdf.msgcloud.common.constants.QueueName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.common.domain.MsgLog;
import cc.htdf.msgcloud.common.domain.MsgLogBody;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.message.handler.MsgSender;
import cc.htdf.msgcloud.message.handler.MsgSenderStrategyHelper;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/9/3
 * title:
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = "msgcloud-core-message-1", topic = QueueName.MSGCLOUD_MESSAGE_IFNO)
@Component
public class Msg5GProductListener implements RocketMQListener<Msg> {

    @Resource
    private MsgSenderStrategyHelper msgSenderStrategyHelper;

    @Override
    public void onMessage(Msg msg) {
        log.info("=================从队列接收到消息并进行处理=====================");
        log.info("接收到消息：{}", JSONObject.toJSONString(msg));
        String channelBusiness = msg.getChannelBusiness();
        MsgSender sender = msgSenderStrategyHelper.fetchStrategy(channelBusiness);
        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msg.getMsgInfo().getMessageId());
        msgLog.setMsgType(msg.getType());
        msgLog.setServiceBusiness(msg.getChannelBusiness());
        msgLog.setMsgContent(JSONObject.toJSONString(msg.getMsgInfo()));
        MsgChatbot msgChatbot = msg.getMsgChatbot();
        msgLog.setChatbotId(msgChatbot.getChatbotId());
        msgLog.setTimestamp(new Date());
        msgLog.setDate(DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            ResponseData<Map> responseData = sender.sendMsg(msg);
            Map<String, Object> responseMap = responseData.getData();
            Object body = responseMap.get("body");
            if (!Objects.isNull(body)) {
                MsgLogBody msgLogBody = new MsgLogBody();
                if (msg.getChannelBusiness().equals(ChannelBusiness.CHANNEL_MOBILE)) {
                    msgLogBody.setType(MsgLogType.XML);
                    msgLogBody.setBody(String.valueOf(body));
                } else {
                    msgLogBody.setType(MsgLogType.JSON);
                    msgLogBody.setBody(JSONObject.toJSONString(body));
                }
                msgLog.setMsgBody(msgLogBody);
            }
            msgLog.setStatusBody(MsgLogStatus.SEND_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            msgLog.setStatusBody(MsgLogStatus.SEND_FAILED);
            log.error("消息发送失败，消息: {}", JSONObject.toJSONString(msg));
        }
        log.info(MarkerFactory.getMarker(LogConfig.LOG_MESSAGE), JSONObject.toJSONString(msgLog));
        log.info("==========================================================");
    }

}
