package cc.htdf.msgcloud.executor.base.repository.impl;

import cc.htdf.msgcloud.executor.base.domain.po.BAreaStationForecastPO;
import cc.htdf.msgcloud.executor.base.repository.BAreaStationForecastRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
@Repository
public class BAreaStationForecastRepositoryImpl implements BAreaStationForecastRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<BAreaStationForecastPO> getAllAreaStation() {
        List<BAreaStationForecastPO> areaStationList = mongoTemplate.findAll(BAreaStationForecastPO.class);
        return areaStationList;
    }
}