package cc.htdf.msgcloud.executor.base.repository.impl;

import cc.htdf.msgcloud.executor.base.domain.po.BAreaStationPO;
import cc.htdf.msgcloud.executor.base.repository.BAreaStationRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
@Repository
public class BAreaStationRepositoryImpl implements BAreaStationRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<BAreaStationPO> getAllAreaStation() {
        List<BAreaStationPO> areaStationList = mongoTemplate.findAll(BAreaStationPO.class);
        return areaStationList;
    }
}