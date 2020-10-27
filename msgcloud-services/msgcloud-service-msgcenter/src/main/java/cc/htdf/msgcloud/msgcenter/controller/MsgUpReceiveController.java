package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.config.LogConfig;
import cc.htdf.msgcloud.common.constants.*;
import cc.htdf.msgcloud.common.domain.*;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.msgcenter.constant.MsgUpStrategyConstant;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgReceiptCallbackInfo;
import cc.htdf.msgcloud.msgcenter.handler.Audio2TextHandler;
import cc.htdf.msgcloud.msgcenter.handler.MsgHandler;
import cc.htdf.msgcloud.msgcenter.handler.MsgUpStrategyHelper;
import cc.htdf.msgcloud.msgcenter.handler.msgup.MsgUpConvertMobile;
import cc.htdf.msgcloud.msgcenter.service.SeveiceNumService;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.dto.AuditNotify;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import com.feinno.msgctenter.sdk.dto.StatusNotify;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * author: JT
 * date: 2020/8/12
 * title:
 * <p>
 * 上行消息接收模块
 */
@Slf4j
@RestController
@RequestMapping("/msgup")
public class MsgUpReceiveController {

    @Resource
    private MsgUpStrategyHelper msgUpStrategyHelper;

    @Resource
    private SeveiceNumService seveiceNumService;

    @Resource
    private Audio2TextHandler audio2TextHandler;

    /**
     * 神州泰岳上行消息接收
     *
     * @param sign
     * @param timeStamp
     * @param nonce
     * @param type
     * @param content
     * @return
     */
    @RequestMapping(value = {"/ultra/callback"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String callback(String sign, String timeStamp, String nonce, String type, @RequestBody String content) throws IOException {
        log.info("Type: {}, content: {}", type, content);
        MsgHandler msgCenterHandler = msgUpStrategyHelper.getStrategy(MsgUpStrategyConstant.MSGUP_HANDLER_ULTRA);
        Msg msg = new Msg();
        msg.setChannelBusiness(ChannelBusiness.CHANNLE_ULTRA);
        msg.setType(MsgType.AUTO_REPLAY);
        msg.setOperation(MsgOperation.MSG_UP);
        if ("debug".equals(type)) {
            return this.signDebug(sign, timeStamp, nonce, content);
        } else if (msgCenterHandler != null  && Api.isFrom5gmc(sign, timeStamp, nonce)) {
            content = Api.decode(content);
            log.info("type: {}, content: {}", type, content);
            if ("callback".equals(type)) {
                StatusNotify statusNotify = JSON.parseObject(content, StatusNotify.class);
                String contents = "收到终端号码:" + statusNotify.getDestination() + "发送的回执消息，状态为" + statusNotify.getState();

                log.info("=================接收到神州泰岳回执消息=====================");
                log.info("运营商: {}", ChannelBusiness.CHANNLE_ULTRA);
                log.info("消息id: {}", statusNotify.getMsgId());
                log.info("状态: {}", statusNotify.getState());
                log.info("终端号码: {}", statusNotify.getDestination());
                log.info("===========================================================");

                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(UUID.randomUUID().toString());
                msgLog.setMsgType(MsgType.UP_CALLBACK);
                msgLog.setTimestamp(new Date());
                msgLog.setServiceBusiness(ChannelBusiness.CHANNLE_ULTRA);
//                msgLog.setChatbotId(msgChatbot.getChatbotId());
                msgLog.setMsgContent(contents);
                MsgLogBody msgLogBody = new MsgLogBody();
                msgLogBody.setType(MsgLogType.JSON);
                msgLogBody.setBody(JSONObject.toJSONString(statusNotify));
                msgLog.setMsgBody(msgLogBody);
                msgLog.setStatusBody(MsgLogStatus.SEND_CALLBACK);
                msgLog.setDate(DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                log.info(MarkerFactory.getMarker(LogConfig.LOG_MESSAGE), JSONObject.toJSONString(msgLog));

                msgCenterHandler.onMessageStateCallback(statusNotify);
            } else if ("msgup".equals(type)) {
                MsgUpInfo msgUpInfo = (MsgUpInfo) JSON.parseObject(content, MsgUpInfo.class);
                msg.setMsgUpInfo(msgUpInfo);

                Long channelId = msgUpInfo.getChannelId();
                MsgChatbot msgChatbot = seveiceNumService.findByChannelId(channelId);
                msg.setMsgChatbot(msgChatbot);

                if ("audio".equals(msgUpInfo.getContentType())) {
                    String text = audio2TextHandler.handler(msg);
                    log.info("[语音识别]完成，识别后文字： {}", text);
                    msgUpInfo.setText(text);
                    msgUpInfo.setContentType("text");
                }

                log.info("=================接收到神州泰岳上行消息=====================");
                log.info("运营商: {}", ChannelBusiness.CHANNLE_ULTRA);
                log.info("ChannelId: {}", msgUpInfo.getChannelId());
                log.info("发送手机号: {}", msgUpInfo.getDestination());
                log.info("收到消息: {}", msgUpInfo.getText());
                log.info("===========================================================");

                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(UUID.randomUUID().toString());
                msgLog.setMsgType(MsgType.UP);
                msgLog.setTimestamp(new Date());
                msgLog.setServiceBusiness(ChannelBusiness.CHANNLE_ULTRA);
                msgLog.setChatbotId(msgChatbot.getChatbotId());
                msgLog.setMsgContent(msgUpInfo.getText());
                MsgLogBody msgLogBody = new MsgLogBody();
                msgLogBody.setType(MsgLogType.JSON);
                msgLogBody.setBody(JSONObject.toJSONString(msgUpInfo));
                msgLog.setMsgBody(msgLogBody);
                msgLog.setStatusBody(MsgLogStatus.RECEIVE_SUCCESS);
                msgLog.setDate(DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                log.info(MarkerFactory.getMarker(LogConfig.LOG_MESSAGE), JSONObject.toJSONString(msgLog));

                msgCenterHandler.onMessageUpNotify(msg);
            } else {
                AuditNotify auditNotify;
                if ("template".equals(type)) {
                    auditNotify = (AuditNotify) JSON.parseObject(content, AuditNotify.class);
                    msgCenterHandler.onTemplateAuditResultNotify(auditNotify);
                } else if ("material".equals(type)) {
                    auditNotify = (AuditNotify) JSON.parseObject(content, AuditNotify.class);
                    msgCenterHandler.onMaterialAuditResultNotify(auditNotify);
                }
            }

            return "";
        } else {
            return null;
        }
    }

    /**
     * 浙江移动回执消息接收
     *
     * @param svip
     * @param content
     * @return
     */
    @PostMapping("/mobile/callback/DeliveryInfoNotification/{svip}")
    public WebResponse mobileReceiptCallback(@PathVariable("svip") String svip, @RequestBody String content) {
        MsgReceiptCallbackInfo Info = MsgUpConvertMobile.anayliseReceiptCallbackXml(content);

        log.info("=================接收到浙江移动回执消息=====================");
        log.info("运营商: {}", ChannelBusiness.CHANNEL_MOBILE);
        log.info("Chatbot: {}", svip);
        log.info("发送手机号: {}", Info.getAddress());
        log.info("messageId: {}", Info.getMessageId());
        log.info("状态: {}", Info.getDeliveryStatus());
        log.info("描述: {}", Info.getDescription());
        log.info("===========================================================");

        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(UUID.randomUUID().toString());
        msgLog.setMsgType(MsgType.UP_CALLBACK);
        msgLog.setTimestamp(new Date());
        msgLog.setServiceBusiness(ChannelBusiness.CHANNEL_MOBILE);
        msgLog.setChatbotId(svip);
        msgLog.setMsgContent(Info.getDeliveryStatus());
        MsgLogBody msgLogBody = new MsgLogBody();
        msgLogBody.setType(MsgLogType.JSON);
        msgLogBody.setBody(JSONObject.toJSONString(Info));
        msgLog.setMsgBody(msgLogBody);
        msgLog.setStatusBody(MsgLogStatus.SEND_CALLBACK);
        msgLog.setDate(DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.info(MarkerFactory.getMarker(LogConfig.LOG_MESSAGE), JSONObject.toJSONString(msgLog));

        return WebResponseBuilder.ok();
    }


    /**
     * 浙江移动上行消息接收
     *
     * @param svip
     * @param content
     * @return
     */
    @PostMapping("/mobile/callback/InboundMessageNotification/{svip}")
    public WebResponse mobileCallback(@PathVariable("svip") String svip, @RequestBody String content) throws IOException {
        MsgHandler msgCenterHandler = msgUpStrategyHelper.getStrategy(MsgUpStrategyConstant.MSGUP_HANDLER_MOBILE);
        MsgUpInfo msgUpInfo = MsgUpConvertMobile.anayliseXml(content);
        Msg msg = new Msg();
        msg.setChannelBusiness(ChannelBusiness.CHANNEL_MOBILE);
        msg.setOperation(MsgOperation.MSG_UP);
        msg.setType(MsgType.AUTO_REPLAY);
        msg.setMsgUpInfo(msgUpInfo);

        MsgChatbot msgChatbot = new MsgChatbot();
        msgChatbot.setSip(svip);
        msgChatbot = seveiceNumService.findByChatbotId(msgChatbot.getChatbotId());
        msg.setMsgChatbot(msgChatbot);

        log.info("=================接收到浙江移动上行消息=====================");
        log.info("运营商: {}", ChannelBusiness.CHANNEL_MOBILE);
        log.info("Chatbot: {}", svip);
        log.info("发送手机号: {}", msgUpInfo.getDestination());
        log.info("收到消息: {}", msgUpInfo.getText());
        if (JSONUtil.isJson(msgUpInfo.getText())) {
            WebResponse webResponse = WebResponseBuilder.ok("接收到用户[{}]操作!", msgUpInfo.getDestination());
            JSONObject receiveJson = JSONObject.parseObject(msgUpInfo.getText());
            JSONObject responseJson = receiveJson.getJSONObject("response");
            if (Objects.isNull(responseJson)) {
                return webResponse;
            }
            JSONObject replayJson = responseJson.getJSONObject("reply");
            if (Objects.isNull(replayJson)) {
                return webResponse;
            }
            String displayText = replayJson.getString("displayText");
            if (Objects.isNull(displayText)) {
                return webResponse;
            }
            msgUpInfo.setText(displayText);
            log.info("消息转换成功: {}", msgUpInfo.getText().trim());
        } else if (msgUpInfo.getText().contains("<?xml")) {
            Document doc = null;
            try {
                doc = DocumentHelper.parseText(msgUpInfo.getText());
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            if (!Objects.isNull(doc)) {
                msgUpInfo = MsgUpConvertMobile.analysisMsgFileInfo(doc, msgUpInfo);
            }

            if (msgUpInfo.getContentType().contains("audio")) {
                String text = audio2TextHandler.handler(msg);
                msgUpInfo.setContentType("text");
                msgUpInfo.setText(text);
                log.info("[语音识别]完成，识别后文字： {}", text);
            }
        }
        log.info("===========================================================");

        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msgUpInfo.getMsgId());
        msgLog.setMsgType(MsgType.UP);
        msgLog.setTimestamp(new Date());
        msgLog.setServiceBusiness(ChannelBusiness.CHANNEL_MOBILE);
        msgLog.setChatbotId(msgChatbot.getChatbotId());
        msgLog.setMsgContent(msgUpInfo.getText());
        MsgLogBody msgLogBody = new MsgLogBody();
        msgLogBody.setType(MsgLogType.JSON);
        msgLogBody.setBody(JSONObject.toJSONString(msgUpInfo));
        msgLog.setMsgBody(msgLogBody);
        msgLog.setStatusBody(MsgLogStatus.RECEIVE_SUCCESS);
        msgLog.setDate(DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.info(MarkerFactory.getMarker(LogConfig.LOG_MESSAGE), JSONObject.toJSONString(msgLog));

        msgCenterHandler.onMessageUpNotify(msg);

        return WebResponseBuilder.ok();
    }

    private String signDebug(String sign, String timeStamp, String nonce, String content) {
        if (!Api.isFrom5gmc(sign, timeStamp, nonce)) {
            return "error";
        } else {
            content = Api.decode(content);
            JSONObject jsonObject = JSON.parseObject(content);
            Integer value = jsonObject.getIntValue("value");
            jsonObject.put("value", value + 1);
            return Api.encode(jsonObject.toJSONString());
        }
    }

}
