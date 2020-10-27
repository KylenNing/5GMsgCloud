package cc.htdf.msgcloud.message.handler.msgtemp.dynamictag;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.message.annotation.DynamicTag;
import cc.htdf.msgcloud.message.annotation.DynamicTags;
import cc.htdf.msgcloud.message.domain.po.D5gHourWeatherPO;
import cc.htdf.msgcloud.message.handler.DynamicTagHandler;
import cc.htdf.msgcloud.message.mapper.D5gHourWeatherMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
        @DynamicTag(type = "forecast-hour", name="预报温度", value = "{forecast_hour_temp}"),
        @DynamicTag(type = "forecast-hour", name="预报降水", value = "{forecast_hour_prec}"),
        @DynamicTag(type = "forecast-hour", name="预报时间", value = "{forecast_hour_time}"),
        @DynamicTag(type = "forecast-hour", name="预报湿度", value = "{forecast_hour_rh}"),
        @DynamicTag(type = "forecast-hour", name="预报风向", value = "{forecast_hour_wind}"),
        @DynamicTag(type = "forecast-hour", name="预报风速", value = "{forecast_hour_wins}"),
        @DynamicTag(type = "forecast-hour", name="预报天气现象", value = "{forecast_hour_pehno}")
})
@Component
public class OperaMysqlHourDataHandler implements DynamicTagHandler {

    @Resource
    private D5gHourWeatherMapper d5gHourWeatherMapper;

    @Override
    public Map<String, Object> execute(Map<String, Object> param) throws ParseException {

        if (Objects.isNull(param)) {
            return ImmutableMap.of();
        }
        Object areaObj = param.get(NlpParamName.LOCATION_CITY);
        if (Objects.isNull(areaObj)) {
            areaObj = param.get(NlpParamName.LOCATION_COUNTY);
        }
        Object startTimeObj = param.get(NlpParamName.TIMES_START);
        Object endTimeObj = param.get(NlpParamName.TIMES_END);
        if (Objects.isNull(areaObj)) {
            return ImmutableMap.of();
        }
        if (Objects.isNull(areaObj)) {
            return ImmutableMap.of();
        }
        //获取小时预报最新数据时间
        String lastDataTime = d5gHourWeatherMapper.getLastDate();
        List<String> dateList = new ArrayList<>();
        dateList.add(String.valueOf(startTimeObj));
        dateList.add(String.valueOf(endTimeObj));
        EntityWrapper<D5gHourWeatherPO> wrapper = new EntityWrapper<>();
        wrapper.eq("areaname",areaObj);
        wrapper.eq("publish_date",lastDataTime);
        wrapper.in("valid_date",dateList);
        List<D5gHourWeatherPO> dataList = d5gHourWeatherMapper.selectList(wrapper);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> observeList = new ArrayList<>();
        for(D5gHourWeatherPO po : dataList){
            observeList.add(transformForecastData(po));
        }
        result.put("data",observeList);
        return result;
    }

    private Map<String, Object> transformForecastData(D5gHourWeatherPO po) {
        Map<String,Object> temp = new HashMap<>();
        temp.put("{forecast_hour_time}", DateUtils.formatDateToStr(po.getValidDate(), "yyyy-MM-dd HH:00:00"));
        temp.put("{forecast_hour_rh}",po.getHumidity());
        temp.put("{forecast_hour_temp}",po.getTemp());
        temp.put("{forecast_hour_wind}",po.getWindDir());
        temp.put("{forecast_hour_wins}",po.getWindSpeed());
        temp.put("{forecast_hour_pehno}", po.getPhenomena());
        return temp;
    }
}