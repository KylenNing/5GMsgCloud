package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 *
 *  上行下行等信息操作策略
 */
@Component
public class MsgOpStrategyHelper {

    @Resource
    private Map<String, MsgOpHandler> msgOpStrategyMap = new ConcurrentHashMap<>();

    public MsgOpHandler getOpStrategy(String msgOp) {

        // 获取消息上行、下行策略
        MsgOpHandler msgOpHandler = msgOpStrategyMap.get(msgOp);
        if (Objects.isNull(msgOpHandler)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到信息操作策略[上行、下行或其它]");
        }
        return msgOpHandler;
    }

}
