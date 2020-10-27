package cc.htdf.msgcloud.message.handler;

import java.text.ParseException;
import java.util.Map;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 */
public interface DynamicTagHandler {

    Map<String, Object> execute(Map<String, Object> param) throws ParseException;

}
