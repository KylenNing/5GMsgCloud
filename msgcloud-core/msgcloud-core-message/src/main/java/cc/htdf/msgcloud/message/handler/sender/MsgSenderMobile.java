package cc.htdf.msgcloud.message.handler.sender;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.common.utils.HttpRequestUtils;
import cc.htdf.msgcloud.common.utils.SHA256Util;
import cc.htdf.msgcloud.common.utils.StorageUtil;
import cc.htdf.msgcloud.message.handler.MsgSender;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.feinno.msgctenter.sdk.dto.*;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import com.feinno.msgctenter.sdk.util.AESEncryptUtil;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 * 浙江移动消息发送
 */
@Slf4j
@Component
public class MsgSenderMobile implements MsgSender {

    private static String mobileSendUrl = "https://maap01.hdn.rcs.chinamobile.com/maapdiscovery/messaging/v1/outbound/{0}/requests";

    final static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

    @Override
    public ResponseData sendTextMsg(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<String> toUserList = receiver.getToUserList();
        String conversationID = createUUID();
        String contributionID = createUUID();
        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {

                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<imFormat>IM</imFormat>");
            xmlInfo.append("<subject>Test Message!  </subject>");
            xmlInfo.append("<contentType>text/plain</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<bodyText>" + mb.getText() + "</bodyText>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<storeSupported>true</storeSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("<receiptRequest>false</receiptRequest>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );

        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("文本消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("文本消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    @Override
    public ResponseData sendCardMsg(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<String> toUserList = receiver.getToUserList();
        List<Card> cardList = mb.getCard();
        Card card = null;
        if (cardList.size() > 0) {
            card = cardList.get(0);
        }
        String mediaUrl = card.getMedia().getUrl();
        String extName = parseSuffix(mediaUrl);
        String mediaContentType = StorageUtil.mediaType(extName);
        String thumbUrl = card.getMedia().getThumbUrl();
        String thumbExtName = parseSuffix(thumbUrl);
        String thumbnailContentType = StorageUtil.mediaType(thumbExtName);
        String conversationID = createUUID();
        String contributionID = createUUID();
        String telephoneNumber = "";
        StringBuilder bodyText = new StringBuilder();
        bodyText.append("{\"message\": {\"generalPurposeCard\": {\"layout\": {\"cardOrientation\": \"VERTICAL\",\"cardWidth\":\"HEDIUM_WIDTH\",\"style\": \"http://msgcloud.80php.com/styles/255.css?v=111\"},");
        bodyText.append("\"content\": {\"media\": { \"mediaUrl\":\"");
        bodyText.append(mediaUrl + "\",\"mediaContentType\": \"" + mediaContentType + "\",");
        bodyText.append("\"thumbnailUrl\":\"" + thumbUrl + "\",\"thumbnailContentType\": \"" + thumbnailContentType + "\",\"height\": \"MEDIUM_HEIGHT\"},");
        bodyText.append("\"title\": \"" + card.getTitle() + "\", \"description\": \"" + card.getNewAbstract() + "\"}}}}");
        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {

                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<senderName>MyName</senderName>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<bodyText><![CDATA[" + bodyText.toString() + "]]></bodyText>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );

        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("卡片消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("卡片消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    @Override
    public ResponseData sendCardMsgWithReceive(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<String> toUserList = receiver.getToUserList();
        List<Card> cardList = mb.getCard();
        Card card = null;
        if (cardList.size() > 0) {
            card = cardList.get(0);
        }
        List<SuggestItem> siList = card.getSuggestItemList();
        String mediaUrl = card.getMedia().getUrl();
        String extName = parseSuffix(mediaUrl);
        String mediaContentType = StorageUtil.mediaType(extName);
        String thumbUrl = card.getMedia().getThumbUrl();
        String thumbExtName = parseSuffix(thumbUrl);
        String thumbnailContentType = StorageUtil.mediaType(thumbExtName);
        String conversationID = createUUID();
        String contributionID = createUUID();
        String telephoneNumber = "";
        StringBuilder bodyText = new StringBuilder();
        bodyText.append("{\"message\": {\"generalPurposeCard\": {\"content\": {");
        //bodyText.append("\"layout\": {\"cardOrientation\": \"VERTICAL\",\"cardWidth\":\"HEDIUM_WIDTH\",\"style\": \"https://static.mocentre.cn/staticflie/h5css/iqy/255.css?v=123\"},");
        bodyText.append("\"media\": { \"mediaUrl\":\"");
        bodyText.append(mediaUrl + "\",\"mediaContentType\": \"" + mediaContentType + "\",");
        bodyText.append("\"thumbnailUrl\":\"" + thumbUrl + "\",\"thumbnailContentType\": \"" + thumbnailContentType + "\",\"height\": \"MEDIUM_HEIGHT\"},");
        bodyText.append("\"title\": \"" + card.getTitle() + "\", \"description\": \"" + card.getNewAbstract() + "\",");
        bodyText.append("\"suggestions\": [");
        for (int i = 0; i < siList.size(); i++) {
            SuggestItem si = siList.get(i);
            Integer buttonType = si.getButtonType();
            String buttonName = si.getButtonName();
            String value = si.getValue();
            bodyText.append("{");
            if (buttonType == 1) {
                bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_open_url\"}}");
            } else if (buttonType == 2) {
                bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 3) {
                bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
            }
            bodyText.append("}");
            if (i != siList.size() - 1) {
                bodyText.append(",");
            }
        }
        bodyText.append("]");
        bodyText.append("},\"layout\": {\"cardWidth\": \"MEDIUM_WIDTH\"}}}}");

        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {

                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<senderName>MyName</senderName>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<bodyText><![CDATA[" + bodyText.toString() + "]]></bodyText>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );
        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("多卡片消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("多卡片消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    @Override
    public ResponseData sendManyCardsMsgWithReceive(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<String> toUserList = receiver.getToUserList();
        List<Card> cardList = mb.getCard();
        String conversationID = createUUID();
        String contributionID = createUUID();
        String telephoneNumber = "";
        StringBuilder bodyText = new StringBuilder();
        bodyText.append("{\"message\": {");
        bodyText.append("\"generalPurposeCardCarousel\":{");
        bodyText.append("\"content\": [");
        for (int j = 0; j < cardList.size(); j++) {
            Card card = cardList.get(j);
            List<SuggestItem> siList = card.getSuggestItemList();
            String mediaUrl = card.getMedia().getUrl();
            String extName = parseSuffix(mediaUrl);
            String mediaContentType = StorageUtil.mediaType(extName);
            String thumbUrl = card.getMedia().getThumbUrl();
            String thumbExtName = parseSuffix(thumbUrl);
            String thumbnailContentType = StorageUtil.mediaType(thumbExtName);
            bodyText.append("{\"media\": { \"mediaUrl\":\"");
            bodyText.append(mediaUrl + "\",\"mediaContentType\": \"" + mediaContentType + "\",");
            bodyText.append("\"thumbnailUrl\":\"" + thumbUrl + "\",\"thumbnailContentType\": \"" + thumbnailContentType + "\",\"height\": \"MEDIUM_HEIGHT\"},");
            bodyText.append("\"title\": \"" + card.getTitle() + "\", \"description\": \"" + card.getNewAbstract() + "\",");
            bodyText.append("\"suggestions\": [");
            for (int i = 0; i < siList.size(); i++) {
                SuggestItem si = siList.get(i);
                Integer buttonType = si.getButtonType();
                String buttonName = si.getButtonName();
                String value = si.getValue();
                bodyText.append("{");
                if (buttonType == 1) {
                    bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                    bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_open_url\"}}");
                } else if (buttonType == 2) {
                    bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                    bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
                } else if (buttonType == 3) {
                    bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
                }
                bodyText.append("}");
                if (i != (siList.size() - 1)) {
                    bodyText.append(",");
                }
            }
            bodyText.append("]}");
            if (j != (cardList.size() - 1)) {
                bodyText.append(",");
            }
        }
        bodyText.append("],");
        bodyText.append("\"layout\": {\"cardWidth\": \"MEDIUM_WIDTH\"}}}}");
        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {

                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<bodyText><![CDATA[" + bodyText.toString() + "]]></bodyText>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );
        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("多卡片消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("多卡片消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    @Override
    public ResponseData sendSinglePictureMsg(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<String> toUserList = receiver.getToUserList();
        Media media = null;
        if (msgInfo.getMsgType() == 6) {
            media = mb.getVideo();
        } else if (msgInfo.getMsgType() == 5) {
            media = mb.getAudio();
        } else if (msgInfo.getMsgType() == 4) {
            media = mb.getImage();
        } else {
            List<Card> cardList = mb.getCard();
            Card card = new Card();
            if (cardList.size() > 0) {
                card = cardList.get(0);
            }
            media = card.getMedia();
        }

        String url = media.getUrl();
        Long size = media.getSize();
        String fileName = media.getName();
        String conversationID = createUUID();
        String contributionID = createUUID();
        StringBuilder bodyText = new StringBuilder();
        bodyText.append("<file xmlns=\"urn:gsma:params:xml:ns:rcs:rcs:fthttp\">");
        bodyText.append("<file-info type=\"thumbnail\">");
//        String thumbUrl = media.getThumbUrl();
//        //downloadByNIO2("", thumbUrl, "");
//        Long thumbSize = 0l;
//        File thumbFile = downLoadFromUrl("https://baikevideo.cdn.bcebos.com/media/mda-XQzA0kPaBN7yOxVq/6705e05066abd8aa907113608352aaf0.mp4", "6705e05066abd8aa907113608352aaf0.mp4", "F:/");
//        if (thumbFile != null && thumbFile.exists() && thumbFile.isFile()) {
//            thumbSize = thumbFile.length();
//        }
//
//        String currentDate = DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
//        String thumbExtName = parseSuffix(thumbUrl);
//        String thumbnailContentType = StorageUtil.mediaType(thumbExtName);
        String fileExtName = parseSuffix(fileName);
        String fileContentType = StorageUtil.mediaType(fileExtName);
        String thumbUrl = "http://5gcallback.80php.com/api/msgcenter/resource/view/image/3cde3882-c5ac-46b1-a8a0-9f17b1c7eb55.jpg";
        String thumbSize = "24171";
        String thumbnailContentType = "image/jpeg";
        String currentDate = DateUtils.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        bodyText.append("<file-size>" + thumbSize + "</file-size>");
        bodyText.append("<content-type>" + thumbnailContentType + "</content-type>");
        bodyText.append("<data url=\"" + thumbUrl + "\" until=\"" + currentDate + "\"/>");
        bodyText.append("</file-info>");
        bodyText.append("<file-info type=\"file\">");
        bodyText.append("<file-size>" + size + "</file-size>");
        bodyText.append("<file-name>" + fileName + "</file-name>");
        bodyText.append("<content-type>" + fileContentType + "</content-type>");
        bodyText.append("<data url=\"" + url + "\" until=\"" + currentDate + "\"/>");
        bodyText.append("</file-info></file>");
        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {

                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<senderName>MyName</senderName>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<contentType>application/vnd.gsma.rcs-ft-http+xml</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<bodyText><![CDATA[" + bodyText.toString() + "]]></bodyText>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );
        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("图片消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("图片消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    @Override
    public ResponseData sendMsgWithHoverMenu(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<String> toUserList = receiver.getToUserList();
        List<SuggestItem> siList = mb.getBottomButtons();
        String conversationID = createUUID();
        String contributionID = createUUID();
        StringBuilder bodyText = new StringBuilder();
        String endMark = "\r\n";
        bodyText.append("--next" + "&#xd;" + endMark);
        bodyText.append("Content-Type: text/plain" + "&#xd;" + endMark);
        bodyText.append("Content-Length: " + mb.getText().length() + "&#xd;" + endMark + endMark);
        bodyText.append(mb.getText() + "&#xd;" + endMark);
        bodyText.append("--next" + "&#xd;" + endMark);
        bodyText.append("Content-Type: application/vnd.gsma.botsuggestion.v1.0+json" + "&#xd;" + endMark);
        bodyText.append("Content-Length: 321" + "&#xd;" + endMark + endMark);
        bodyText.append("{\"suggestions\":[");
        for (int i = 0; i < siList.size(); i++) {
            SuggestItem si = siList.get(i);
            Integer buttonType = si.getButtonType();
            String buttonName = si.getButtonName();
            String value = si.getValue();
            bodyText.append("{");
            if (buttonType == 1) {
                bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 2) {
                bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 3) {
                bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
            }
            bodyText.append("}");
            if (i != (siList.size() - 1)) {
                bodyText.append(",");
            }
        }
        bodyText.append("]}" + "&#xd;" + endMark);
        bodyText.append("--next--");
        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {
                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<senderName>MyName</senderName>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<imFormat>IM</imFormat>");
            xmlInfo.append("<contentType>multipart/mixed; boundary=\"next\"</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<inReplyToContributionID>" + messageId + "</inReplyToContributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<bodyText>" + bodyText.toString() + "</bodyText>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );
        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    //带悬浮菜单的多卡片消息
    private ResponseData sendManyCardMsgWithHoverMenu(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<SuggestItem> suList = mb.getBottomButtons();
        List<String> toUserList = receiver.getToUserList();
        List<Card> cardList = mb.getCard();
        String conversationID = createUUID();
        String contributionID = createUUID();
        StringBuilder bodyText = new StringBuilder();

        String endMark = "\r\n";
        bodyText.append("--next" + "&#xd;" + endMark);
        bodyText.append("Content-Type: application/vnd.gsma.botmessage.v1.0+json" + "&#xd;" + endMark);
        bodyText.append("Content-Length: 12" + "&#xd;" + endMark + endMark);

        bodyText.append("{\"message\": {");
        bodyText.append("\"generalPurposeCardCarousel\":{");
        bodyText.append("\"content\": [");
        for (int j = 0; j < cardList.size(); j++) {
            Card card = cardList.get(j);
            List<SuggestItem> siList = card.getSuggestItemList();
            String mediaUrl = card.getMedia().getUrl();
            String extName = parseSuffix(mediaUrl);
            String mediaContentType = StorageUtil.mediaType(extName);
            String thumbUrl = card.getMedia().getThumbUrl();
            String thumbExtName = parseSuffix(thumbUrl);
            String thumbnailContentType = StorageUtil.mediaType(thumbExtName);
            bodyText.append("{\"media\": { \"mediaUrl\":\"");
            bodyText.append(mediaUrl + "\",\"mediaContentType\": \"" + mediaContentType + "\",");
            bodyText.append("\"thumbnailUrl\":\"" + thumbUrl + "\",\"thumbnailContentType\": \"" + thumbnailContentType + "\",\"height\": \"MEDIUM_HEIGHT\"},");
            bodyText.append("\"title\": \"" + card.getTitle() + "\", \"description\": \"" + card.getNewAbstract() + "\",");
            bodyText.append("\"suggestions\": [");
            for (int i = 0; i < siList.size(); i++) {
                SuggestItem si = siList.get(i);
                Integer buttonType = si.getButtonType();
                String buttonName = si.getButtonName();
                String value = si.getValue();
                bodyText.append("{");
                if (buttonType == 1) {
                    bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                    bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_open_url\"}}");
                } else if (buttonType == 2) {
                    bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                    bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
                } else if (buttonType == 3) {
                    bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
                }
                bodyText.append("}");
                if (i != (siList.size() - 1)) {
                    bodyText.append(",");
                }
            }
            bodyText.append("]}");
            if (j != (cardList.size() - 1)) {
                bodyText.append(",");
            }
        }
        bodyText.append("],");
        bodyText.append("\"layout\": {\"cardWidth\": \"MEDIUM_WIDTH\"}}}}" + "&#xd;" + endMark + endMark);
        bodyText.append("--next" + "&#xd;" + endMark);
        bodyText.append("Content-Type: application/vnd.gsma.botsuggestion.v1.0+json" + "&#xd;" + endMark);
        bodyText.append("Content-Length: 321" + "&#xd;" + endMark + endMark);

        bodyText.append("{\"suggestions\":[");
        for (int i = 0; i < suList.size(); i++) {
            SuggestItem si = suList.get(i);
            Integer buttonType = si.getButtonType();
            String buttonName = si.getButtonName();
            String value = si.getValue();
            bodyText.append("{");
            if (buttonType == 1) {
                bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 2) {
                bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 3) {
                bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
            }
            bodyText.append("}");
            if (i != (suList.size() - 1)) {
                bodyText.append(",");
            }
        }
        bodyText.append("]}" + "&#xd;" + endMark);
        bodyText.append("--next--");

        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {
                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<contentType>multipart/mixed; boundary=\"next\"</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<bodyText>" + bodyText.toString() + "</bodyText>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );
        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("多卡片消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("多卡片消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    //带悬浮菜单的单卡片消息
    private ResponseData sendCardMsgWithHoverMenu(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        StringBuilder xmlInfo = new StringBuilder();
        Receiver receiver = msgInfo.getReceiver();
        MsgBody mb = msgInfo.getMsg();
        List<SuggestItem> siList = mb.getBottomButtons();
        List<String> toUserList = receiver.getToUserList();
        List<Card> cardList = mb.getCard();
        Card card = null;
        if (cardList.size() > 0) {
            card = cardList.get(0);
        }
        List<SuggestItem> sList = card.getSuggestItemList();
        String mediaUrl = card.getMedia().getUrl();
        String extName = parseSuffix(mediaUrl);
        String mediaContentType = StorageUtil.mediaType(extName);
        String thumbUrl = card.getMedia().getThumbUrl();
        String thumbExtName = parseSuffix(thumbUrl);
        String thumbnailContentType = StorageUtil.mediaType(thumbExtName);
        String conversationID = createUUID();
        String contributionID = createUUID();
        StringBuilder bodyText = new StringBuilder();

        String endMark = "\r\n";
        bodyText.append("--next" + "&#xd;" + endMark);
        bodyText.append("Content-Type: application/vnd.gsma.botmessage.v1.0+json" + "&#xd;" + endMark);
        bodyText.append("Content-Length: 12" + "&#xd;" + endMark + endMark);

        bodyText.append("{\"message\": {\"generalPurposeCard\": {\"content\": {");
        bodyText.append("\"media\": { \"mediaUrl\":\"");
        bodyText.append(mediaUrl + "\",\"mediaContentType\": \"" + mediaContentType + "\",");
        bodyText.append("\"thumbnailUrl\":\"" + thumbUrl + "\",\"thumbnailContentType\": \"" + thumbnailContentType + "\",\"height\": \"MEDIUM_HEIGHT\"},");
        bodyText.append("\"title\": \"" + card.getTitle() + "\", \"description\": \"" + card.getNewAbstract() + "\",");
        bodyText.append("\"suggestions\": [");
        for (int i = 0; i < sList.size(); i++) {
            SuggestItem si = sList.get(i);
            Integer buttonType = si.getButtonType();
            String buttonName = si.getButtonName();
            String value = si.getValue();
            bodyText.append("{");
            if (buttonType == 1) {
                bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_open_url\"}}");
            } else if (buttonType == 2) {
                bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 3) {
                bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
            }
            bodyText.append("}");
            if (i != sList.size() - 1) {
                bodyText.append(",");
            }
        }
        bodyText.append("]");
        bodyText.append("},\"layout\": {\"cardWidth\": \"MEDIUM_WIDTH\"}}}}" + "&#xd;" + endMark + endMark);

        bodyText.append("--next" + "&#xd;" + endMark);
        bodyText.append("Content-Type: application/vnd.gsma.botsuggestion.v1.0+json" + "&#xd;" + endMark);
        bodyText.append("Content-Length: 321" + "&#xd;" + endMark + endMark);
        bodyText.append("{\"suggestions\":[");
        for (int i = 0; i < siList.size(); i++) {
            SuggestItem si = siList.get(i);
            Integer buttonType = si.getButtonType();
            String buttonName = si.getButtonName();
            String value = si.getValue();
            bodyText.append("{");
            if (buttonType == 1) {
                bodyText.append("\"action\": {\"urlAction\": {\"openUrl\": {\"url\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 2) {
                bodyText.append("\"action\": {\"dialerAction\": {\"dialPhoneNumber\": {\"phoneNumber\": \"");
                bodyText.append(value + "\"}},\"displayText\": \"" + buttonName + "\"}");
            } else if (buttonType == 3) {
                bodyText.append("\"reply\": {\"displayText\": \"" + buttonName + "\",\"postback\": {\"data\": \"set_by_chatbot_reply_no\"}}");
            }
            bodyText.append("}");
            if (i != (siList.size() - 1)) {
                bodyText.append(",");
            }
        }
        bodyText.append("]}" + "&#xd;" + endMark);
        bodyText.append("--next--");

        int j = ((int) Math.ceil((double) toUserList.size() / (double) 100));
        String address = "";
        boolean isSend = false;
        for (int k = 0; k < j; k++) {
            String messageId = createUUID();
            xmlInfo.append("<msg:outboundMessageRequest xmlns:msg=\"urn:oma:xml:rest:netapi:messaging:1\">");
            for (int i = k * 10; i < (k + 1) * 10; i++) {
                if (toUserList.get(i).startsWith("tel")) {
                    if (i == 0) {
                        address = toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                } else {
                    if (i == 0) {
                        address = "tel:" + toUserList.get(i);
                        xmlInfo.append("<address>" + address + "</address>");
                    }
                    String tel = "tel:" + toUserList.get(i);
                    xmlInfo.append("<destinationAddress>" + tel + "</destinationAddress>");
                }
                if (i == (toUserList.size() - 1)) {
                    break;
                }
            }
            MsgChatbot msgChatbot = msg.getMsgChatbot();
            xmlInfo.append("<senderAddress>" + msgChatbot.getSip() + "</senderAddress>");
            xmlInfo.append("<senderName>MyName</senderName>");
            xmlInfo.append("<outboundIMMessage>");
            xmlInfo.append("<subject>hello from the rest of us! </subject>");
            xmlInfo.append("<imFormat>IM</imFormat>");
            xmlInfo.append("<contentType>multipart/mixed; boundary=\"next\"</contentType>");
            xmlInfo.append("<conversationID>" + conversationID + "</conversationID>");
            xmlInfo.append("<contributionID>" + contributionID + "</contributionID>");
            xmlInfo.append("<shortMessageSupported>true</shortMessageSupported>");
            xmlInfo.append("<reportRequest>Sent</reportRequest>");
            xmlInfo.append("<reportRequest>Delivered</reportRequest>");
            xmlInfo.append("<reportRequest>Displayed</reportRequest>");
            xmlInfo.append("<reportRequest>Failed</reportRequest>");
            xmlInfo.append("<reportRequest>SMS</reportRequest>");
            xmlInfo.append("<messageId>" + messageId + "</messageId>");
            xmlInfo.append("<inReplyToContributionID>" + messageId + "</inReplyToContributionID>");
            xmlInfo.append("<serviceCapability>");
            xmlInfo.append("<capabilityId>ChatbotSA</capabilityId>");
            xmlInfo.append("<version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>");
            xmlInfo.append("</serviceCapability>");
            xmlInfo.append("<bodyText>" + bodyText.toString() + "</bodyText>");
            xmlInfo.append("</outboundIMMessage>");
            xmlInfo.append("<clientCorrelator>567895</clientCorrelator>");
            xmlInfo.append("</msg:outboundMessageRequest>");
            isSend = this.send(
                    xmlInfo.toString(),
                    MessageFormat.format(mobileSendUrl, msgChatbot.getSip()),
                    address
            );
        }
        ResponseData responseData = new ResponseData();
        if (isSend) {
            responseData.setCode(200);
            responseData.setMessage("卡片消息发送成功！");
        } else {
            responseData.setCode(500);
            responseData.setMessage("卡片消息发送失败！");
        }
        responseData.setData(
                ImmutableMap.of("body", xmlInfo)
        );
        return responseData;
    }

    @Override
    public ResponseData sendMsg(Msg msg) {
        MsgInfo msgInfo = msg.getMsgInfo();
        Receiver receiver = msgInfo.getReceiver();
        if (!Objects.isNull(receiver)) {
            List<String> users = receiver.getToUserList();
            if (!Objects.isNull(users)) {
                users = users.stream().map(user -> {
                    if (user.length() == 11) {
                        user = "+86" + user;
                    }
                    if (user.length() == 13) {
                        user = "+" + user;
                    }
                    return user;
                }).collect(Collectors.toList());
                receiver.setToUserList(users);
            }
        }
        MsgChatbot msgChatbot = msg.getMsgChatbot();
        if (Objects.isNull(msgChatbot.getChatbotId())) {
            throw new BusinessException(ExceptionCode.ERROR, "SenderAddress[svip]未填写！");
        }
        Integer msgType = msgInfo.getMsgType();
        //msgType: 3 文字消息
        if (msgType == 3) {
            if (msg.getMsgInfo().getMsg().getBottomButtons() == null) {
                return sendTextMsg(msg);
            } else {
                return sendMsgWithHoverMenu(msg);// 带悬浮菜单的文本消息
            }
            //msgType: 7 卡片消息
        } else if (msgType == 7) {
            List<Card> cardList = msgInfo.getMsg().getCard();
            //msg.Cards: 长度为1  单卡片消息
            if (cardList.size() == 1) {
                String title = cardList.get(0).getTitle();
                String content = cardList.get(0).getContent();
                //msg.Cards.标题和描述为空 单图片消息
                if (title == null && content == null) {
                    return sendSinglePictureMsg(msg);
                } else {
                    List<SuggestItem> suggestItemList = cardList.get(0).getSuggestItemList();
                    //msg.Cards.suggestItemList: 长度大于0 带建议返回的单卡片消息
                    if (suggestItemList.size() > 0) {
                        if (msg.getMsgInfo().getMsg().getBottomButtons() == null) {
                            return sendCardMsgWithReceive(msg);
                        } else {
                            return sendCardMsgWithHoverMenu(msg);// 带悬浮菜单
                        }
                    } else {
                        //msg.Cards: 长度为1  单卡片消息
                        if (msg.getMsgInfo().getMsg().getBottomButtons() == null) {
                            return sendCardMsg(msg);
                        } else {
                            return sendCardMsgWithHoverMenu(msg);// 带悬浮菜单
                        }
                    }
                }
            } else {
                if (msg.getMsgInfo().getMsg().getBottomButtons() == null) {
                    return sendManyCardsMsgWithReceive(msg);
                } else {
                    return sendManyCardMsgWithHoverMenu(msg);// 带悬浮菜单
                }
            }
        } else if (msgType == 4 || msgType == 5 || msgType == 6) {
            return sendSinglePictureMsg(msg);
        }
        ResponseData responseData = new ResponseData();
        responseData.setCode(500);
        responseData.setMessage("未找到消息发送策略！");
        return responseData;
    }

    public static String parseSuffix(String url) {
        Matcher matcher = pattern.matcher(url);
        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        String endUrl = spUrl[len - 1];

        if (matcher.find()) {
            String[] spEndUrl = endUrl.split("\\?");
            return spEndUrl[0].split("\\.")[1];
        }
        return endUrl.split("\\.")[1];
    }

    public static String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static void main(String[] args) {
        MsgInfo msgInfo = makeMsgInfo();
        //sendTextMsg(msgInfo);
    }

    public static MsgInfo makeMsgInfo() {
        MsgInfo msgInfo = new MsgInfo();
        MsgBody mb = new MsgBody();
        List<String> toUserList = new ArrayList<String>();
        toUserList.add("tel:+8617857992797");
        Receiver receiver = new Receiver();
        receiver.setToUserList(toUserList);
        msgInfo.setReceiver(receiver);
        List<Card> cardList = makeCardList();
        mb.setCard(cardList);
        mb.setText("这是一条测试消息！");
        msgInfo.setMsg(mb);
        return msgInfo;
    }

    public static List<Card> makeCardList() {
        List<Card> cardList = new ArrayList<Card>();
        Card c = new Card();
        c.setContent("card_content");
        c.setTitle("card_title");
        Media m = new Media();
        m.setUrl("http://i.weather.com.cn/images/cn/sjztj/2020/06/24/202006240927510B5386DC6043CC851537319A0893AA22.jpg");
        m.setThumbUrl("http://i.weather.com.cn/images/cn/sjztj/2020/06/24/202006240927510B5386DC6043CC851537319A0893AA22.jpg");
        m.setName("202006240927510B5386DC6043CC851537319A0893AA22.jpg");
        m.setSize(100000000l);
        c.setMedia(m);
        c.setSuggestItemList(makeSuggestItem());
        cardList.add(c);

        c = new Card();
        c.setContent("card_content1");
        c.setTitle("card_title1");
        Media m2 = new Media();
        m2.setUrl("http://i.weather.com.cn/images/cn/sjztj/2020/06/24/202006240927510B5386DC6043CC851537319A0893AA22.jpg");
        m2.setThumbUrl("http://i.weather.com.cn/images/cn/sjztj/2020/06/24/202006240927510B5386DC6043CC851537319A0893AA22.jpg");
        m.setName("202006240927510B5386DC6043CC851537319A0893AA22.jpg");
        m.setSize(100000000l);
        c.setMedia(m2);
        c.setSuggestItemList(makeSuggestItem());
        cardList.add(c);
        return cardList;
    }

    public static List<SuggestItem> makeSuggestItem() {
        List<SuggestItem> siList = new ArrayList<SuggestItem>();
        SuggestItem si = new SuggestItem();
        si.setButtonType(1);
        si.setButtonName("Open website or deep link");
        si.setValue("https://www.baidu.com");
        siList.add(si);

        si = new SuggestItem();
        si.setButtonType(2);
        si.setButtonName("拨号");
        si.setValue("13609852291");
        siList.add(si);

        si = new SuggestItem();
        si.setButtonType(3);
        si.setButtonName("No");
        siList.add(si);

        si = new SuggestItem();
        si.setButtonType(3);
        si.setButtonName("YES");
        siList.add(si);
        return siList;
    }

    public boolean send(String xmlInfo, String uri, String address) {
        String dateFormat = DateUtils.getUniversalTime();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/xml");
        headers.put("Content-Type", "text/xml");
        headers.put("Authorization", getToken(dateFormat));
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Address", address);
        headers.put("Date", dateFormat);
        headers.put("X-Office-Code", "DB");
        HttpResponse response = HttpRequestUtils.post(uri, xmlInfo, headers, "XML");
        if (response.getStatus() == HttpStatus.HTTP_CREATED) {
            return true;
        } else {
            log.error("========================浙江移动XML发送失败=====================");
            System.out.println(response.body());
            log.error("==============================================================");
            return false;
        }
    }

    public static String getToken(String dateFormat) {

        String passwordSHA256 = "4f95d15126615625250926f3747dd5861b745927ce58051295989de43deeb3d2";
        String token = SHA256Util.getSHA256(passwordSHA256 + dateFormat);
        String auth = AESEncryptUtil.base64Encode(
                String.format("iap_200609133064:%s", token).getBytes()
        );
        return "Basic " + auth;
    }

    public static File downLoadFromUrl(String urlStr, String fileName, String savePath) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }


            return file;
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
