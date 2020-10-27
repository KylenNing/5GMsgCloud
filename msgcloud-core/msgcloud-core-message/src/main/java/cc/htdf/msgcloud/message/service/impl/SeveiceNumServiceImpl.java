package cc.htdf.msgcloud.message.service.impl;

import cc.htdf.msgcloud.message.domain.po.SeveiceNumPO;
import cc.htdf.msgcloud.message.mapper.SeveiceNumMapper;
import cc.htdf.msgcloud.message.service.SeveiceNumService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Service
public class SeveiceNumServiceImpl extends ServiceImpl<SeveiceNumMapper, SeveiceNumPO> implements SeveiceNumService {

    @Resource
    private SeveiceNumMapper seveiceNumMapper;

    @Override
    public SeveiceNumPO findByChannelId(Long channelId) {
        Condition condition = new Condition();
        condition.where("CHANNEL_ID", channelId);
        return this.selectOne(condition);
    }

    @Override
    public SeveiceNumPO findByServiceId(String serviceId) {
        return seveiceNumMapper.selectById(serviceId);
    }
}
