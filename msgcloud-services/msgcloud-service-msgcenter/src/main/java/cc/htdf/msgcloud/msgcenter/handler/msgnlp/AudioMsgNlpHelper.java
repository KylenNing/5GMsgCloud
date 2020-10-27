package cc.htdf.msgcloud.msgcenter.handler.msgnlp;

import cc.htdf.msgcloud.common.domain.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * author: JT
 * date: 2020/8/13
 * title:
 */
@Slf4j
@Component("audioMsgNlpHelper")
public class AudioMsgNlpHelper implements MsgNlpHelper {
    @Override
    public Msg nlpAnalysisMsg(Msg msg) {
        log.info("Audio文本NLP处理");

        return null;
    }
}
