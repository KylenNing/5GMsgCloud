package cc.htdf.msgcloud.message.handler.msgtemp.dynamictag;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.message.annotation.DynamicTag;
import cc.htdf.msgcloud.message.annotation.DynamicTags;
import cc.htdf.msgcloud.message.domain.po.D5gDayWeatherPO;
import cc.htdf.msgcloud.message.handler.DynamicTagHandler;
import cc.htdf.msgcloud.message.mapper.D5gDayWeatherMapper;
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
        @DynamicTag(type = "forecast-day", name="预报温度(白天)", value = "{forecast_day_temp}"),
        @DynamicTag(type = "forecast-day", name="预报降水(白天)", value = "{forecast_day_prec}"),
        @DynamicTag(type = "forecast-day", name="预报时间(白天)", value = "{forecast_day_time}"),
        @DynamicTag(type = "forecast-day", name="预报湿度(白天)", value = "{forecast_day_rh}"),
        @DynamicTag(type = "forecast-day", name="预报风向(白天)", value = "{forecast_day_wind}"),
        @DynamicTag(type = "forecast-day", name="预报风速(白天)", value = "{forecast_day_wins}"),
        @DynamicTag(type = "forecast-day", name="预报天气现象(白天)", value = "{forecast_day_pehno}"),
        @DynamicTag(type = "forecast-day", name="预报温度(夜间)", value = "{forecast_night_temp}"),
        @DynamicTag(type = "forecast-day", name="预报降水(夜间)", value = "{forecast_night_prec}"),
        @DynamicTag(type = "forecast-day", name="预报时间(夜间)", value = "{forecast_night_time}"),
        @DynamicTag(type = "forecast-day", name="预报湿度(夜间)", value = "{forecast_night_rh}"),
        @DynamicTag(type = "forecast-day", name="预报风向(夜间)", value = "{forecast_night_wind}"),
        @DynamicTag(type = "forecast-day", name="预报风速(夜间)", value = "{forecast_night_wins}"),
        @DynamicTag(type = "forecast-day", name="预报天气现象(夜间)", value = "{forecast_night_pehno}"),
        @DynamicTag(type = "forecast-day", name="日出时间", value = "{forecast_sunrise}"),
        @DynamicTag(type = "forecast-day", name="日落时间", value = "{forecast_sunset}"),
        @DynamicTag(type = "forecast-day", name="预报位置", value = "{forecast_area}")
})
@Component
public class OperaMysqlDayDataHandler implements DynamicTagHandler {

    @Resource
    private D5gDayWeatherMapper d5gDayWeatherMapper;

    @Override
    public Map<String, Object> execute(Map<String, Object> param) throws ParseException {
//        Object areaObj = "天津市";
//        Object startTimeObj = "2020-10-20 00:00:00";
//        Object endTimeObj = null;
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
        Object startTimeObj = param.get(NlpParamName.TIMES_START);
        Object endTimeObj = param.get(NlpParamName.TIMES_END);
        if (Objects.isNull(areaObj)) {
            return ImmutableMap.of();
        }

        //获取天次预报最新数据时间
        String lastDataTime = d5gDayWeatherMapper.getLastDate();
        List<String> dateList = new ArrayList<>();
        dateList.add(String.valueOf(startTimeObj));
        dateList.add(String.valueOf(endTimeObj));
        EntityWrapper<D5gDayWeatherPO> wrapper = new EntityWrapper<>();
        wrapper.eq("areaname",areaObj);
        wrapper.eq("publish_date",lastDataTime);
        wrapper.in("valid_date",dateList);
        List<D5gDayWeatherPO> dataList = d5gDayWeatherMapper.selectList(wrapper);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> forecastDayList = new ArrayList<>();
        for(D5gDayWeatherPO po : dataList){
            forecastDayList.add(transformForecastData(po));
        }
        result.put("data",forecastDayList);
        return result;
    }

    private Map<String, Object> transformForecastData(D5gDayWeatherPO po) {
        Map<String,Object> temp = new HashMap<>();
        temp.put("{forecast_day_time}", DateUtils.formatDateToStr(po.getValidDate(), "MM月dd日"));
        temp.put("{forecast_day_temp}",po.getDayTemp());
        temp.put("{forecast_day_wind}",po.getDayWindDir());
        if(!po.getDayWindSpeed().equals("微风")){
            po.setDayWindSpeed(po.getDayWindSpeed()+"级");
        }
        if(!po.getNightWindSpeed().equals("微风")){
            po.setNightWindSpeed(po.getNightWindSpeed()+"级");
        }
        temp.put("{forecast_day_wins}",po.getDayWindSpeed());
        temp.put("{forecast_day_pehno}", po.getDayPhenomena());
        temp.put("{forecast_night_temp}",po.getNightTemp());
        temp.put("{forecast_night_wind}",po.getNightWindDir());
        temp.put("{forecast_night_wins}",po.getNightWindSpeed());
        temp.put("{forecast_night_pehno}", po.getNightPhenomena());
        temp.put("{forecast_sunrise}",po.getSunRise());
        temp.put("{forecast_sunset}",po.getSunSet());
        temp.put("{forecast_area}", po.getAreaname());
        return temp;
    }
}