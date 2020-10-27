package cc.htdf.msgcloud.message.handler.msgtemp.dynamictag;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.message.annotation.DynamicTag;
import cc.htdf.msgcloud.message.annotation.DynamicTags;
import cc.htdf.msgcloud.message.domain.po.D5gLifeIndexPO;
import cc.htdf.msgcloud.message.handler.DynamicTagHandler;
import cc.htdf.msgcloud.message.mapper.D5gLifeIndexMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/10/17
 * @Description: TODO
 */
@Slf4j
@DynamicTags(tags = {
        @DynamicTag(type = "index", name="钓鱼指数", value = "{index_fishing}"),
        @DynamicTag(type = "index", name="穿衣指数", value = "{index_wearing}"),
        @DynamicTag(type = "index", name="洗车指数", value = "{index_wash_car}"),
        @DynamicTag(type = "index", name="紫外线指数", value = "{index_ultraviolet_rays}"),
        @DynamicTag(type = "index", name="火险等级", value = "{index_fire}"),
        @DynamicTag(type = "index", name="空气污染条件", value = "{index_air_pollution}"),
        @DynamicTag(type = "index", name="花粉浓度等级", value = "{index_pollen}"),
        @DynamicTag(type = "index", name="旅游指数", value = "{index_travel}"),
        @DynamicTag(type = "index", name="冬季取暖指数", value = "{index_warm}"),
        @DynamicTag(type = "index", name="肠道传染病发病指数", value = "{index_enteric_diseases}"),
        @DynamicTag(type = "index", name="感冒指数", value = "{index_cold}"),
        @DynamicTag(type = "index", name="舒适度指数", value = "{index_comfort}"),
        @DynamicTag(type = "index", name="晨练指数", value = "{index_morning_exercises}"),
        @DynamicTag(type = "index", name="一氧化碳中毒指数", value = "{index_carbon_monoxide}"),
        @DynamicTag(type = "index", name="呼吸道疾病指数", value = "{index_respiratory_disease}"),
        @DynamicTag(type = "index", name="负氧离子浓度等级", value = "{index_negative_oxygen_ion}"),
        @DynamicTag(type = "index", name="舒适度指数", value = "{index_comfort}"),
})
@Component
public class OperaMysqlIndexDataHandler implements DynamicTagHandler {

    @Resource
    private D5gLifeIndexMapper d5gLifeIndexMapper;

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

        //获取最新生活指数数据
        List<D5gLifeIndexPO> dataList = d5gLifeIndexMapper.getLastIndexList();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> lifeIndexList = new ArrayList<>();
        Map<String,String> lifeIndexMap = dataList.stream().
                collect(Collectors.toMap(D5gLifeIndexPO::getIndexName, D5gLifeIndexPO::getIndexContent));
        lifeIndexList.add(transformForecastData(lifeIndexMap));
        result.put("data",lifeIndexList);
        return result;
    }

    private Map<String, Object> transformForecastData(Map<String,String> lifeIndexMap) {
        Map<String,Object> temp = new HashMap<>();
        DynamicTagHandler dynamicTagHandler = new OperaMysqlIndexDataHandler();
        DynamicTags dynamicTags = dynamicTagHandler.getClass().getAnnotation(DynamicTags.class);
        for (DynamicTag dynamicTag : dynamicTags.tags()) {
            temp.put(dynamicTag.value(), lifeIndexMap.get(dynamicTag.name()));
        }
        return temp;
    }
}