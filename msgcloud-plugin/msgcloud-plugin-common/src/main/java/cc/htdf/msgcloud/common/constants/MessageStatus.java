package cc.htdf.msgcloud.common.constants;

/**
 * author: JT
 * date: 2020/8/22
 * title:
 */
public class MessageStatus {

    private MessageStatus(){}

    public static final Integer NOT_SENT = 1;

    public static final Integer SENDING = 2;

    public static final Integer SEND_SUCCESS = 3;

    public static final Integer SEND_FAILED = 4;
}
