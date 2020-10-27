package cc.htdf.msgcloud.common.constants;

/**
 * author: JT
 * date: 2020/8/14
 * title:
 */
public class MsgType {

    private MsgType () {}

    /**
     * 自动回复
     */
    public static final String AUTO_REPLAY = "autoReplay";

    /**
     * 即时下发
     */
    public static final String IMMEDIATE_DOWN = "immediateDown";

    /**
     * 触发
     */
    public static final String TRIGGER_DOWN = "triggerDown";

    /**
     * 上行
     */
    public static final String UP = "up";

    /**
     * 回执
     */
    public static final String UP_CALLBACK = "upCallback";
}
