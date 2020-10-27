package cc.htdf.msgcloud.gateway.service;

import cc.htdf.msgcloud.gateway.domain.po.CMsgUserPO;
import com.baomidou.mybatisplus.service.IService;

/**
 * author: JT
 * date: 2020/8/19
 * title:
 */
public interface CMsgUserService extends IService<CMsgUserPO> {

    CMsgUserPO findByUseraccoount(String userAccount);

}
