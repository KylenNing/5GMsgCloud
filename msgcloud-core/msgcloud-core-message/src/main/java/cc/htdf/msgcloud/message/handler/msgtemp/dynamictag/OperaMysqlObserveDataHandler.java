package cc.htdf.msgcloud.message.handler.msgtemp.dynamictag;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.message.annotation.DynamicTag;
import cc.htdf.msgcloud.message.annotation.DynamicTags;
import cc.htdf.msgcloud.message.domain.po.D5gDayWeatherPO;
import cc.htdf.msgcloud.message.domain.po.D5gHourWeatherPO;
import cc.htdf.msgcloud.message.domain.po.D5gObserveWeatherPO;
import cc.htdf.msgcloud.message.handler.DynamicTagHandler;
import cc.htdf.msgcloud.message.mapper.D5gDayWeatherMapper;
import cc.htdf.msgcloud.message.mapper.D5gHourWeatherMapper;
import cc.htdf.msgcloud.message.mapper.D5gObserveWeatherMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * @Author: ningyq
 * @Date: 2020/10/17
 * @Description: TODO
 */
@Slf4j
@DynamicTags(tags = {
        @DynamicTag(type = "observe-tj", name="天津实况温度", value = "{observe-tj-temp}"),
        @DynamicTag(type = "observe-tj", name="天津实况最高温度", value = "{observe-tj-maxtemp}"),
        @DynamicTag(type = "observe-tj", name="天津实况最低温度", value = "{observe-tj-mintemp}"),
        @DynamicTag(type = "observe-tj", name="天津实况风力", value = "{observe-tj-wins}"),
        @DynamicTag(type = "observe-tj", name="天津实况风向", value = "{observe-tj-wind}"),
        @DynamicTag(type = "observe-tj", name="天津实况湿度", value = "{observe-tj-humdity}"),
        @DynamicTag(type = "observe-tj", name="天津实况日出时间", value = "{observe-tj-sunrise}"),
        @DynamicTag(type = "observe-tj", name="天津实况日落时间", value = "{observe-tj-sunset}"),
        @DynamicTag(type = "observe-tj", name="天津实况天气现象", value = "{observe-tj-pehno}")

})
@Component
public class OperaMysqlObserveDataHandler implements DynamicTagHandler {

    @Resource
    private D5gDayWeatherMapper d5gDayWeatherMapper;

    @Resource
    private D5gHourWeatherMapper d5gHourWeatherMapper;

    @Resource
    private D5gObserveWeatherMapper d5gObserveWeatherMapper;

    @Override
    public Map<String, Object> execute(Map<String, Object> param) throws ParseException {

        if (Objects.isNull(param)) {
            return ImmutableMap.of();
        }
        Object areaObj = param.get(NlpParamName.LOCATION_CITY);
        if (Objects.isNull(areaObj)) {
            areaObj = param.get(NlpParamName.LOCATION_COUNTY);
            if(Objects.isNull(areaObj)){
                areaObj = "天津市";
            }
        }
        D5gDayWeatherPO dayWeatherPO = d5gDayWeatherMapper.getLastDataByAreaname(String.valueOf(areaObj));
        D5gHourWeatherPO hourWeatherPO = d5gHourWeatherMapper.getLastDataByAreaname(String.valueOf(areaObj));
        D5gObserveWeatherPO observeWeatherPO = d5gObserveWeatherMapper.getLastDataByAreaname((String) areaObj);
        if(Objects.isNull(observeWeatherPO)){
            throw new BusinessException(500,"实况数据表无{}数据",areaObj);
        }
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> forecastDayList = new ArrayList<>();
        forecastDayList.add(transformForecastData(dayWeatherPO,hourWeatherPO,observeWeatherPO));
        result.put("data",forecastDayList);
        return result;
    }

    private Map<String, Object> transformForecastData(D5gDayWeatherPO d5gDayWeatherPO, D5gHourWeatherPO d5gHourWeatherPO, D5gObserveWeatherPO d5gObserveWeatherPO) {
        Map<String,Object> temp = new HashMap<>();
        temp.put("{observe-tj-temp}", d5gObserveWeatherPO.getTemp());
        temp.put("{observe-tj-pehno}",d5gHourWeatherPO.getPhenomena());
        temp.put("{observe-tj-sunrise}",d5gDayWeatherPO.getSunRise());
        temp.put("{observe-tj-sunset}", d5gDayWeatherPO.getSunSet());
        temp.put("{observe-tj-maxtemp}",d5gObserveWeatherPO.getMaxTemp());
        temp.put("{observe-tj-mintemp}",d5gObserveWeatherPO.getMinTemp());
        temp.put("{observe-tj-humdity}",d5gObserveWeatherPO.getHumdity());
        temp.put("{observe-tj-wind}",d5gObserveWeatherPO.getWindDir());
        temp.put("{observe-tj-wins}", d5gObserveWeatherPO.getWindGrade());
        return temp;
    }
}