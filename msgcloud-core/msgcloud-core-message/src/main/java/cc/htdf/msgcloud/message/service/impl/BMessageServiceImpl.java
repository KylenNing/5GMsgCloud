package cc.htdf.msgcloud.message.service.impl;

import cc.htdf.msgcloud.message.domain.po.BMessagePO;
import cc.htdf.msgcloud.message.mapper.BMessageServiceMapper;
import cc.htdf.msgcloud.message.service.BMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * author: JT
 * date: 2020/8/22
 * title:
 */
@Service
public class BMessageServiceImpl extends ServiceImpl<BMessageServiceMapper, BMessagePO> implements BMessageService {


    @Async("MsgStatusChangePool")
    @Override
    public void updateMessageStatus(Integer messageId, Integer status) {
        BMessagePO message = new BMessagePO();
        message.setId(messageId);
        message.setMessageStatus(status);
        this.updateById(message);
    }
}
