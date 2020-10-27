package cc.htdf.msgcloud.msgcenter.utils;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/10/20
 * title:
 */
@Slf4j
public class ActionConvertUtil {

    public static Msg convert(Msg msg) {
        String action = msg.getAction();
        if ("天气".equals(action)) {
            return convertWeatherAction(msg);
        }
        return msg;
    }

    private static Msg convertWeatherAction(Msg msg) {
        Map<String, Object> param = msg.getParam();
        Object startObj = param.get(NlpParamName.TIMES_START);
        Object endObj = param.get(NlpParamName.TIMES_END);
        Object locationObj = param.get(NlpParamName.LOCATION_CITY);

        if (Objects.isNull(startObj) && Objects.isNull(endObj)) {
            log.info("未检测到时间参数！");
            msg.setAction("未匹配到真实意图");
            return msg;
        }
        String startTime = Objects.isNull(startObj) ? null : String.valueOf(startObj);
        String endTime = Objects.isNull(endObj) ? null : String.valueOf(endObj);
        String currectTime = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");



        if (startTime.compareTo(currectTime) <= 0 && Objects.isNull(endTime)) {
            //msg.setAction("实况小时数据");
            msg.setAction("未匹配到真实意图");
            if (startTime.endsWith("00:00:00")) {
                msg.setAction("天津实况天数据");
            }
            return msg;
        }

        if (startTime.compareTo(currectTime) > 0 && Objects.isNull(endTime)) {
            msg.setAction("预报小时数据");
            if (startTime.endsWith("00:00:00")) {
                msg.setAction("预报某天数据");
            }
            return msg;
        }

        if (startTime.compareTo(currectTime) >= 0 && endTime.compareTo(currectTime) > 0) {
            if (startTime.endsWith("00:00:00") && endTime.endsWith("00:00:00")) {
                //msg.setAction("预报某几天数据");
                msg.setAction("未匹配到真实意图");
                return msg;
            }
            if (!startTime.endsWith("00:00:00") || !endTime.endsWith("00:00:00")) {
                // msg.setAction("预报某时间段数据");
                msg.setAction("未匹配到真实意图");
                return msg;
            }
        }

        if (startTime.compareTo(currectTime) < 0 && endTime.compareTo(currectTime) > 0) {
            //msg.setAction("实况预报小时混合数据");
            msg.setAction("未匹配到真实意图");
            return msg;
        }
        return msg;
    }

}
