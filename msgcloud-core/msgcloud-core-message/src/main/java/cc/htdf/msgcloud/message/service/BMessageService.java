package cc.htdf.msgcloud.message.service;

import cc.htdf.msgcloud.message.domain.po.BMessagePO;
import com.baomidou.mybatisplus.service.IService;

/**
 * author: JT
 * date: 2020/8/22
 * title:
 */
public interface BMessageService extends IService<BMessagePO> {

    void updateMessageStatus(Integer messageId, Integer status);

}
