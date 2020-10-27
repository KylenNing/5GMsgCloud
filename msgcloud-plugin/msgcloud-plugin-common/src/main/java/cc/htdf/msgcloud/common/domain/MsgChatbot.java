package cc.htdf.msgcloud.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * author: JT
 * date: 2020/8/28
 * title:
 */
@Setter
@Getter
public class MsgChatbot {

    private String serviceId;

    private String chatbotId;

    private String channelId;

    /**
     * SIP  浙江移动专用
     */
    private String sip;

    private String appId;

    private String cspCode;

    public void setSip(String sip) {
        this.sip = sip;
        String tmp = sip.replace("sip:", "");
        this.chatbotId = tmp.split("@")[0];
    }

    public void setChatbotId(String chatbotId) {
        this.chatbotId = chatbotId;
        this.sip = "sip:" + chatbotId + "@botplatform.rcs.chinamobile.com";
    }

}
