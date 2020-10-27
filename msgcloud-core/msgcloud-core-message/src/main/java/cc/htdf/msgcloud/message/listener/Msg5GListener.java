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
import cc.htdf.msgcloud.message.handler.MsgOpHandler;
import cc.htdf.msgcloud.message.handler.MsgOpStrategyHelper;
import cc.htdf.msgcloud.message.handler.MsgSender;
import cc.htdf.msgcloud.message.handler.MsgSenderStrategyHelper;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 *
 *      消息监听
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = "${rocketmq.producer.group}", topic = QueueName.MSGCLOUD_MESSAGE)
@Component
public class Msg5GListener implements RocketMQListener<Msg> {

    @Resource
    private MsgOpStrategyHelper msgOpStrategyHelper;

    @Resource
    private MsgSenderStrategyHelper msgSenderStrategyHelper;

    @Override
    public void onMessage(Msg msg) {
        log.info("=================从队列接收到消息并进行处理=====================");
        log.info("接收到消息：{}", JSONObject.toJSONString(msg));
        String msgOpName = msg.getOperation();
        MsgOpHandler msgOpHandler = msgOpStrategyHelper.getOpStrategy(msgOpName);
        Msg generateMsg = null;
        try {
            generateMsg = msgOpHandler.generateMsg(msg);
        } catch (ParseException | IOException | InvalidResponseException | InvalidKeyException | NoSuchAlgorithmException | ServerException | ErrorResponseException | XmlParserException | InternalException | InvalidBucketNameException | InsufficientDataException | RegionConflictException e) {
            e.printStackTrace();
        }
        if (Objects.isNull(generateMsg)) {
            log.error("未匹配到该意图[{}]!", msg.getAction());
            return;
        }
        String channelBusiness = msg.getChannelBusiness();
        MsgSender sender = msgSenderStrategyHelper.fetchStrategy(channelBusiness);
        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(generateMsg.getMsgInfo().getMessageId());
        msgLog.setMsgType(generateMsg.getType());
        msgLog.setServiceBusiness(generateMsg.getChannelBusiness());
        msgLog.setMsgContent(JSONObject.toJSONString(generateMsg.getMsgInfo()));
        MsgChatbot msgChatbot = msg.getMsgChatbot();
        msgLog.setChatbotId(msgChatbot.getChatbotId());
        msgLog.setTimestamp(new Date());
        msgLog.setDate(DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            ResponseData<Map> responseData = sender.sendMsg(generateMsg);
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
            log.error("消息发送失败，消息: {}", JSONObject.toJSONString(generateMsg));
        }
        log.info(MarkerFactory.getMarker(LogConfig.LOG_MESSAGE), JSONObject.toJSONString(msgLog));
        log.info("==========================================================");


    }
}
