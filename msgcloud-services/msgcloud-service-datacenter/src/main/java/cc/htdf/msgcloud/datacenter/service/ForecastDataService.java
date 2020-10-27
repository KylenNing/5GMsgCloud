package cc.htdf.msgcloud.datacenter.service;


import cc.htdf.msgcloud.datacenter.domain.po.D5gDayWeatherPO;
import cc.htdf.msgcloud.datacenter.domain.po.D5gHourWeatherPO;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
public interface ForecastDataService {

    List<D5gHourWeatherPO> getHoursForecast(Integer dataSourceArea, Integer areacode);

    List<D5gDayWeatherPO> getDaysForecast(Integer dataSourceArea, Integer areacode);

    List<D5gDayWeatherPO> getDaysForecastByAreacodeAndCount(Integer dataSourceArea, Integer areacode, Integer count);
}
