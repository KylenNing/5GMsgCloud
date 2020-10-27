package cc.htdf.msgcloud.msgcenter.handler.msgup;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgReceiptCallbackInfo;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.Objects;


/**
 * author: JT
 * date: 2020/8/12
 * title:
 */
public class MsgUpConvertMobile {

    private MsgUpConvertMobile() {
    }

    private static final Base64 base64 = new Base64();

    /**
     * author: renxh
     * date: 2020/8/13
     * title: mobileMsgXML解析返回MsgUpInfo对象
     */
    public static MsgUpInfo anayliseXml(String xmlStr) {

        MsgUpInfo msgUpInfo = new MsgUpInfo();
        Document doc = null;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xmlStr);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            // inboundMessage节点
            Element subElt = rootElt.element("inboundMessage");
            // destinationAddress的内容
            String destinationAddress = subElt.elementText("destinationAddress");
            // senderAddress的内容
            String senderAddress = subElt.elementText("senderAddress");
            // origUser的内容
            String origUser = subElt.elementText("origUser");
            // messageId的内容
            String messageId = subElt.elementText("messageId");
            // contentType的内容
            String contentType = subElt.elementText("contentType");
            // bodyText的内容
            String bodyText = subElt.elementText("bodyText");
            // conversationID的内容
            String conversationID = subElt.elementText("conversationID");
            // contributionID的内容
            String contributionID = subElt.elementText("contributionID");
//            // serviceCapability的内容
//            List<Element> list = subElt.elements("serviceCapability");
//            Element e0 = list.get(0);
//            String capabilityId = e0.elementText("capabilityId");
//            String version = e0.elementText("version");
//            msgUpInfo.setChannelId(Long.parseLong("234"));
            msgUpInfo.setContentType(contentType);
            msgUpInfo.setAppId(destinationAddress);
            msgUpInfo.setContributionId(contributionID);
            msgUpInfo.setConversationId(conversationID);
            String[] address = senderAddress.split("@");
            msgUpInfo.setDestination(address[0].split(":")[1]);
//            msgUpInfo.setMedia(null);  // 当contentType= image、audio时取这个自带
            msgUpInfo.setMsgId(messageId);
            msgUpInfo.setText(new String(base64.decode(bodyText), "UTF-8"));

        } catch (DocumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return msgUpInfo;
    }

    /**
     * author: gzx
     * date: 2020/9/9
     * title: 回执消息xml解析
     */
    public static MsgReceiptCallbackInfo anayliseReceiptCallbackXml(String xmlStr) {
        MsgReceiptCallbackInfo msgReceiptCallbackInfo = new MsgReceiptCallbackInfo();
        Document doc;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xmlStr);
            Element rootElt = doc.getRootElement();
            Element subElt = rootElt.element("deliveryInfo");
            String address = subElt.elementText("address");
            String messageId = subElt.elementText("messageId");
            String deliveryStatus = subElt.elementText("deliveryStatus");
            String description = subElt.elementText("description");

            String[] addres = address.split("@");
            msgReceiptCallbackInfo.setAddress(addres[0].split(":")[1]);
            msgReceiptCallbackInfo.setMessageId(messageId);
            msgReceiptCallbackInfo.setDeliveryStatus(deliveryStatus);
            msgReceiptCallbackInfo.setDescription(description);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return msgReceiptCallbackInfo;
    }

    public static MsgUpInfo analysisMsgFileInfo(Document document, MsgUpInfo msgUpInfo) {
        Element rootEl = document.getRootElement();
        Element fileinfoEl = rootEl.element("file-info");
        if (Objects.isNull(fileinfoEl)){
            return msgUpInfo;
        }
        Element dataEl = fileinfoEl.element("data");
        Element filenameEl = fileinfoEl.element("file-name");
        Element filesizeEl = fileinfoEl.element("file-size");
        Element contentTypeEl = fileinfoEl.element("content-type");
        Media media = msgUpInfo.getMedia();
        if (Objects.isNull(media)) {
            media = new Media();
        }
        media.setUrl(dataEl.attributeValue("url"));
        media.setName(filenameEl.getText());
        media.setSize(Long.valueOf(filesizeEl.getText()));
        msgUpInfo.setContentType(contentTypeEl.getText());
        msgUpInfo.setMedia(media);
        return msgUpInfo;
    }
}
