package cc.htdf.msgcloud.executor.base.repository;

import cc.htdf.msgcloud.executor.base.domain.po.BAreaStationForecastPO;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
public interface BAreaStationForecastRepository {

    List<BAreaStationForecastPO> getAllAreaStation();
}
