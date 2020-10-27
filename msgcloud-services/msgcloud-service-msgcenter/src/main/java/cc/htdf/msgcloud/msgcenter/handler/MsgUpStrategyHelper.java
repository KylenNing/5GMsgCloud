package cc.htdf.msgcloud.msgcenter.handler;

import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: JT
 * date: 2020/8/13
 * title:
 */
@Component
public class MsgUpStrategyHelper {

    @Resource
    private Map<String, MsgHandler> msgUpHandlerStrategy = new ConcurrentHashMap<>(2);

    public MsgHandler getStrategy(String msgStrategy) {
        MsgHandler msgHandler = msgUpHandlerStrategy.get(msgStrategy);
        if (Objects.isNull(msgHandler)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到[上行消息处理策略: {}]", msgStrategy);
        }
        return msgHandler;
    }

}
