package cc.htdf.msgcloud.gateway.service;

import cc.htdf.msgcloud.gateway.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.gateway.mapper.CMsgUserMapper;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * author: JT
 * date: 2020/8/19
 * title:
 */
@Service
public class CMsgUserServiceImpl extends ServiceImpl<CMsgUserMapper, CMsgUserPO> implements CMsgUserService {

    @Override
    public CMsgUserPO findByUseraccoount(String userAccount) {
        Condition condition = new Condition();
        condition.eq("USER_ACCOUNT", userAccount);
        return this.selectOne(condition);
    }
}
