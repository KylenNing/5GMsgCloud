package cc.htdf.msgcloud.datacenter.service.impl;

import cc.htdf.msgcloud.datacenter.domain.po.D5gDayWeatherPO;
import cc.htdf.msgcloud.datacenter.domain.po.D5gHourWeatherPO;
import cc.htdf.msgcloud.datacenter.mapper.D5gDayWeatherMapper;
import cc.htdf.msgcloud.datacenter.mapper.D5gHourWeatherMapper;
import cc.htdf.msgcloud.datacenter.service.ForecastDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Slf4j
@Service
public class ForecastDataServiceImpl implements ForecastDataService {

    @Resource
    private D5gHourWeatherMapper d5gHourWeatherMapper;

    @Resource
    private D5gDayWeatherMapper d5gDayWeatherMapper;

    /**
     * @Description: 根据数据源获取最新小时数据
     * @param  @dataSourceArea
     * @author ningyq
     * @date 2020/9/9
     */
    @Override
    public List<D5gHourWeatherPO> getHoursForecast(Integer dataSourceArea, Integer areacode) {

        log.info("请求小时预报的区域编码为{}",areacode);
        List<D5gHourWeatherPO> dataList = d5gHourWeatherMapper.getDataByDataSource(dataSourceArea,areacode);
        return dataList;
    }

    @Override
    public List<D5gDayWeatherPO> getDaysForecast(Integer dataSourceArea, Integer areacode) {
        log.info("请求天次预报的区域编码为{}",areacode);
        List<D5gDayWeatherPO> list = d5gDayWeatherMapper.getDataByDataSource(dataSourceArea,areacode);
        if(list.size() == 0){
            return new ArrayList<>();
        }else {
            return list.subList(0,7);
        }
    }

    @Override
    public List<D5gDayWeatherPO> getDaysForecastByAreacodeAndCount(Integer dataSourceArea, Integer areacode, Integer count) {
        log.info("请求天次预报的区域编码为{}",areacode);
        List<D5gDayWeatherPO> list = d5gDayWeatherMapper.getDataByDataSource(dataSourceArea,areacode);
        if(list.size() == 0){
            return new ArrayList<>();
        }else {
            return list.subList(0,count);
        }
    }
}