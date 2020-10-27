package cc.htdf.msgcloud.msgcenter.handler;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.msgcenter.handler.msgnlp.MsgNlpHelper;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: JT
 * date: 2020/8/13
 * title:
 */
@Component
public class MsgNlpStrategyHelper implements MsgNlpHelper {

    @Resource
    private Map<String, MsgNlpHelper> msgNlpHelperMap = new ConcurrentHashMap<>(3);


    private Map<String, String> strategyMap = new HashMap<String, String>(3){{{
        put("text", "textMsgNlpHelper");
        put("text/plain", "textMsgNlpHelper");
        put("text/plain;charset=UTF-8", "textMsgNlpHelper");
        put("image", "imageMsgNlpHelper");
        put("text", "textMsgNlpHelper");
    }}};

    /**
     * 根据ContentType获取NLP处理策略
     * @param msgUpInfo
     * @return
     */
    public MsgNlpHelper getMsgNlpHelperStrategy(MsgUpInfo msgUpInfo) {
        String content = msgUpInfo.getContentType();
        String strategyName = strategyMap.get(content);
        if (Objects.isNull(strategyName)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到数据类型[{}]相关策略", strategyName);
        }
        MsgNlpHelper msgNlpHelper = msgNlpHelperMap.get(strategyName);
        if (Objects.isNull(msgNlpHelper)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到数据类型[{}]相关策略", strategyName);
        }
        return msgNlpHelper;
    }

    @Override
    public Msg nlpAnalysisMsg(Msg msg) {
        MsgNlpHelper msgNlpHelper = this.getMsgNlpHelperStrategy(msg.getMsgUpInfo());
        return msgNlpHelper.nlpAnalysisMsg(msg);
    }
}
