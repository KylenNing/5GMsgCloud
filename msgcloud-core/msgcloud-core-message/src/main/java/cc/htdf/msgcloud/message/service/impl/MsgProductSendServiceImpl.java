package cc.htdf.msgcloud.message.service.impl;

import cc.htdf.msgcloud.message.domain.po.MsgProductSendPO;
import cc.htdf.msgcloud.message.mapper.MsgProductSendMapper;
import cc.htdf.msgcloud.message.service.MsgProductSendService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * author: JT
 * date: 2020/9/3
 * title:
 */
@Service
public class MsgProductSendServiceImpl extends ServiceImpl<MsgProductSendMapper, MsgProductSendPO> implements MsgProductSendService {

    @Async("MsgStatusChangePool")
    @Override
    public void updateMessageStatus(Integer messageId, Integer status) {
        MsgProductSendPO msgProductSendPO = new MsgProductSendPO();
        msgProductSendPO.setId(messageId);
        msgProductSendPO.setMessageStatus(status);
        this.updateById(msgProductSendPO);
    }
}
