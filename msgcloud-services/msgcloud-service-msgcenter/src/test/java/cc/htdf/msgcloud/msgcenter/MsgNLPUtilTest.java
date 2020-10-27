package cc.htdf.msgcloud.msgcenter;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.msgcenter.utils.MsgNLPUtil;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import org.junit.Test;

/**
 * author: JT
 * date: 2020/8/24
 * title:
 */
public class MsgNLPUtilTest {

    @Test
    public void nlpAnalysisTextTest() {

        Msg msg = new Msg();
        MsgUpInfo msgUpInfo = new MsgUpInfo();
        msgUpInfo.setMsgId("123");
        msgUpInfo.setText("今天推荐去哪玩");
        msg.setMsgUpInfo(msgUpInfo);
        MsgNLPUtil.nlpAnalysisText(msg);
    }
}
