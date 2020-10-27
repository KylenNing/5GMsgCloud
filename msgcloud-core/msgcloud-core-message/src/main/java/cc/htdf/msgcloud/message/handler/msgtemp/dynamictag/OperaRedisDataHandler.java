package cc.htdf.msgcloud.message.handler.msgtemp.dynamictag;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.constants.RedisKeyConstants;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.common.utils.WeatherUtils;
import cc.htdf.msgcloud.message.annotation.DynamicTag;
import cc.htdf.msgcloud.message.annotation.DynamicTags;
import cc.htdf.msgcloud.message.handler.DynamicTagHandler;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/8/10
 * @Description: TODO
 */
@Slf4j
@DynamicTags(tags = {
        @DynamicTag(type = "observe", name="观测温度", value = "{observe_temp}"),
        @DynamicTag(type = "observe", name="观测降水", value = "{observe_prec}"),
        @DynamicTag(type = "observe", name="观测时间", value = "{observe_time}"),
        @DynamicTag(type = "observe", name="观测湿度", value = "{observe_rh}"),
        @DynamicTag(type = "observe", name="观测风向", value = "{observe_wind}"),
        @DynamicTag(type = "observe", name="观测风速", value = "{observe_wins}"),
        @DynamicTag(type = "observe", name="观测天气现象", value = "{observe_pehno}")
})
@Component
public class OperaRedisDataHandler implements DynamicTagHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> execute(Map<String, Object> param) throws ParseException {
//        if (Objects.isNull(param)) {
//            return ImmutableMap.of();
//        }
//        Object areaObj = param.get(NlpParamName.LOCATION_CITY);
//        Object startTimeObj = param.get(NlpParamName.TIMES_START);
//        Object endTimeObj = param.get(NlpParamName.TIMES_END);
//        Object areaObj = "天津市";
//        Object startTimeObj = "2020-10-20 18:00:00";
//        Object endTimeObj = null;
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
        /**
         * 获取筛选时间
         */
        Date startDate = null, endDate = null;
        if (!Objects.isNull(startTimeObj) && !Objects.equals("", startTimeObj)) {
            String startDateStr = String.valueOf(startTimeObj);
            startDate = DateUtils.parseDate(startDateStr, "yyyy-MM-dd HH:mm:ss");
        }
        if (!Objects.isNull(endTimeObj)&& !Objects.equals("", endTimeObj)) {
            String endDateStr = String.valueOf(endTimeObj);
            endDate = DateUtils.parseDate(endDateStr, "yyyy-MM-dd HH:mm:ss");
        }

        /**
         * 获取最新存储时间
         */
        Object lastTimeObj = redisTemplate.opsForValue().get(RedisKeyConstants.OBSERVE_LASTTIME);
        String lastTime = null;
        if (Objects.isNull(lastTimeObj) || Objects.equals("", lastTimeObj)) {
            lastTime = DateUtils.formatDateToStr(new Date(), "yyyyMMddHH0000");
        } else {
            lastTime = String.valueOf(lastTimeObj);
        }

        /**
         * 获取区域数据
         */
        String dataKey = "msgcloud:001:observe:" + areaObj;
        Map<String, String> observeDataMap = redisTemplate.opsForHash().entries(dataKey);

        /**
         * 构造返回数据
         */
        Map<String, Object> result = new HashMap<>();
        result.put("{location_area}", areaObj);

        /**
         * 筛选数据
         */
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            Date time = null;
            if (Objects.isNull(startDate) && Objects.isNull(endDate)) {
                time = DateUtils.parseDate(lastTime, "yyyyMMddHHmmss");
            } else {
                time = Objects.isNull(startDate) ? endDate : startDate;
            }
            String data = observeDataMap.get(lastTime);
            if (!Objects.isNull(data) && !Objects.equals("", data)) {
                String[] dataArr = data.split(",");
                Map<String, Object> temp = transformObserveData(
                        time,
                        dataArr
                );
                result.putAll(temp);
            }
        } else if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            String startTime = DateUtils.formatDateToStr(
                    startDate, "yyyyMMddHH0000"
                    );
            String endTime = DateUtils.formatDateToStr(
                    endDate, "yyyyMMddHH0000"
            );
            List<Map.Entry<String, String>> tmpList = observeDataMap.entrySet().stream()
                    .filter(entry -> {
                        Long tmp = Long.valueOf(entry.getKey());
                        return tmp >= Long.valueOf(startTime) && tmp <= Long.valueOf(endTime);
                    }).collect(Collectors.toList());
            List<Map<String, Object>> observeList = new ArrayList<>();
            for (Map.Entry<String, String> entry : tmpList) {
                Map<String, Object> tmp = transformObserveData(
                        DateUtils.parseDate(entry.getKey(), "yyyyMMddHHmmss"), entry.getValue().split(",")
                );
                observeList.add(tmp);
            }
            result.put("data", observeList);
        }
        return result;
    }

    private Map<String, Object> transformObserveData(Date date, String[] dataArr) {
        Map<String,Object> temp = new HashMap<>();
        temp.put("{observe_time}", DateUtils.formatDateToStr(date, "yyyy-MM-dd HH:00:00"));
        temp.put("{observe_prec}",dataArr[0]);
        temp.put("{observe_rh}",dataArr[1]);
        temp.put("{observe_temp}",dataArr[2]);
        temp.put("{observe_wind}",dataArr[3]);
        temp.put("{observe_wins}",dataArr[4]);
        temp.put("{observe_pehno}", WeatherUtils.getWeatherPhenomenonByCode(Integer.valueOf(dataArr[5])));
        return temp;
    }


    //    @Override
    public Map<String, Object> executeBak(Map<String, Object> param) throws ParseException {
        if (Objects.isNull(param)) {
            return new HashMap<>();
        }
        Object observeAreaObj = param.get(NlpParamName.LOCATION_CITY);
        Object observeIntervalObj = param.get("{observe_interval}");
        if (Objects.isNull(observeAreaObj) || Objects.isNull(observeIntervalObj)) {
            return new HashMap<>();
        }

        String observe_area = String.valueOf(observeAreaObj);
        Integer observe_interval = Integer.valueOf(String.valueOf(observeIntervalObj));
        String key = "msgcloud:001:observe:" + observe_area;
        Map<String,String> valueMap = redisTemplate.opsForHash().entries(key);
        if(valueMap.isEmpty()){
            Map<String,Object> resMap = new HashMap<>();
            resMap.put("warn","请检查地区参数是否准确");
            return resMap;
        }
        String lastTime = (String) redisTemplate.opsForValue().get("msgcloud:001:observe:lasttime");
        List<Map<String,Object>> resList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        for(int i=0; i < observe_interval; i++){
            String tempTime = DateUtils.formatDateToStr(DateUtils.addHour(DateUtils.parseDate(lastTime,"yyyyMMddHHmmss"),-i),"yyyyMMddHHmmss");
            timeList.add(tempTime);
        }
        for(String time : timeList){
            String[] valueArray = valueMap.get(time).split(",");
            Map<String,Object> nationMap = new HashMap<>();
            nationMap.put("{observe_area}",observe_area);
            nationMap.put("{observe_interval}",observe_interval);
            nationMap.put("{observe_time}",time);
            nationMap.put("{observe_prec}",valueArray[0]);
            nationMap.put("{observe_rh}",valueArray[1]);
            nationMap.put("{observe_temp}",valueArray[2]);
            nationMap.put("{observe_wind}",valueArray[3]);
            nationMap.put("{observe_wins}",valueArray[4]);
            nationMap.put("{observe_pehno}", WeatherUtils.getWeatherPhenomenonByCode(Integer.valueOf(valueArray[5])));
            resList.add(nationMap);
        }
        if(observe_interval == 1){
            param.putAll(resList.get(0));
            return param;
        }else {
            param.put("data",resList);
            return param;
        }

    }

}