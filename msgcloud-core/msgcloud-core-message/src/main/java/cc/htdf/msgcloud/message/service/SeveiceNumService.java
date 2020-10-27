package cc.htdf.msgcloud.message.service;

import cc.htdf.msgcloud.message.domain.po.SeveiceNumPO;
import com.baomidou.mybatisplus.service.IService;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
public interface SeveiceNumService extends IService<SeveiceNumPO> {

    SeveiceNumPO findByChannelId(Long channelId);

    SeveiceNumPO findByServiceId(String serviceId);

}
