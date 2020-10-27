package cc.htdf.msgcloud.message.handler.msgtemp.strategy;

import cc.htdf.msgcloud.common.constants.MsgTemplateStrategyConstant;
import cc.htdf.msgcloud.message.annotation.DynamicTag;
import cc.htdf.msgcloud.message.annotation.DynamicTags;
import cc.htdf.msgcloud.message.handler.DynamicTagHandler;
import cc.htdf.msgcloud.message.handler.MsgTemplateStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Component(MsgTemplateStrategyConstant.DynamicTagTemplateStrategy)
public class DynamicTagTemplateStrategy implements MsgTemplateStrategy {

    private Pattern pattern = Pattern.compile("\\{.*?\\}" );

    @Resource
    private ApplicationContext applicationContext;

    public String handlerText(String text, Map<String, Object> param) throws ParseException {

        Set<String> variableSet = new HashSet<>();
        variableSet.addAll(this.variables(text));
        variableSet.addAll(this.variables(text));

        Map<String, DynamicTagHandler> dynamicTagHandlerMap = applicationContext.getBeansOfType(DynamicTagHandler.class);
        Map<String, DynamicTagHandler> variableHandler = new HashMap<>();
        for (DynamicTagHandler dynamicTagHandler : dynamicTagHandlerMap.values()) {
            DynamicTags dynamicTags = dynamicTagHandler.getClass().getAnnotation(DynamicTags.class);
            for (DynamicTag dynamicTag : dynamicTags.tags()) {
                variableHandler.put(dynamicTag.value(), dynamicTagHandler);
            }
        }
        Set<DynamicTagHandler> dynamicTagHandlers = new HashSet<>();
        if (!Objects.isNull(variableSet) && !variableSet.isEmpty()) {
            for (String variable : variableSet) {
                if (!Objects.isNull(variableHandler.get(variable))) {
                    dynamicTagHandlers.add(variableHandler.get(variable));
                }
            }
        }

        Map<String, Object> variableValues = new HashMap<>();
        for (DynamicTagHandler dynamicTagHandler : dynamicTagHandlers) {
            Map<String, Object> result = dynamicTagHandler.execute(param);
            if (!Objects.isNull(result)) {
                variableValues.putAll(result);
            }
        }
        if(variableValues.keySet().contains("data")){
            String resText = "";
            String paramText = text;
            for (Map.Entry<String, Object> entry : variableValues.entrySet()) {
                if(entry.getKey().equals("data")){
                    List<Map<String, Object>> innerValues = (List<Map<String, Object>>) entry.getValue();
                    for(Map<String, Object> map : innerValues){
                        for (Map.Entry<String, Object> innerEntry : map.entrySet()){
                            text = text.replace(innerEntry.getKey(), String.valueOf(innerEntry.getValue()));
                        }
                        resText += text;
                        text = paramText;
                    }
                }else {
                    resText = resText.replace(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            text = resText;
        }else {
            for (Map.Entry<String, Object> entry : variableValues.entrySet()) {
                text = text.replace(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return text;
    }

    private Set<String> variables(String text) {
        Set<String> variableSet = new HashSet<>();
        Matcher matcher = this.pattern.matcher(text);
        while (matcher.find()) {
            Optional<String> tempOp = Optional.ofNullable(matcher.group());
            if (tempOp.isPresent()) {
                String temp = tempOp.get();
                variableSet.add(temp);
            }
        }
        return variableSet;
    }

}
