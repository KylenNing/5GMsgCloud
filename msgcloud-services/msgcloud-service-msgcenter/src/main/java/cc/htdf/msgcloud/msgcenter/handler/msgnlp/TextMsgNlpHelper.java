package cc.htdf.msgcloud.msgcenter.handler.msgnlp;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.msgcenter.utils.MsgNLPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * author: JT
 * date: 2020/8/13
 * title:
 */
@Slf4j
@Component("textMsgNlpHelper")
public class TextMsgNlpHelper implements MsgNlpHelper {
    @Override
    public Msg nlpAnalysisMsg(Msg msg) {

        return MsgNLPUtil.nlpAnalysisText(msg);
    }
}
