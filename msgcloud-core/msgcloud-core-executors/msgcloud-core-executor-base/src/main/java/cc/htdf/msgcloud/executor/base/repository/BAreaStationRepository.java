package cc.htdf.msgcloud.executor.base.repository;

import cc.htdf.msgcloud.executor.base.domain.po.BAreaStationPO;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
public interface BAreaStationRepository {

    List<BAreaStationPO> getAllAreaStation();
}
