package cc.htdf.msgcloud.datacenter.service;


import cc.htdf.msgcloud.datacenter.domain.po.D5gAirPO;
import cc.htdf.msgcloud.datacenter.domain.po.D5gObserveWeatherPO;

import java.util.List;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
public interface ObserveDataService {


    D5gObserveWeatherPO getObserveDataByAreacode(Integer dataSourceArea, Integer areacode);

    D5gAirPO getAirDataByAreacode(Integer dataSourceArea, Integer areacode);

    List<Map> getIndexDataByAreacode(Integer dataSourceArea, Integer areacode);
}
