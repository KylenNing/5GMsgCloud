package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Component
public class MsgTemplateStrategyHelper {


    @Resource
    private Map<String, MsgTemplateStrategy> templateStrategyMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initTemplateStrategy() {
        for (Map.Entry<String, MsgTemplateStrategy> entry : templateStrategyMap.entrySet()) {
        }
    }


    public MsgTemplateStrategy fetchMsgTemplateStrategy(String templateName) {
        MsgTemplateStrategy templateStrategy = templateStrategyMap.get(templateName);
        if (Objects.isNull(templateStrategy)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到消息模板处理策略，模板策略编码[{}]", templateName);
        }
        return templateStrategy;
    }

}
