package cc.htdf.msgcloud.message.service;

import cc.htdf.msgcloud.message.domain.po.MsgProductSendPO;
import com.baomidou.mybatisplus.service.IService;

/**
 * author: JT
 * date: 2020/9/3
 * title:
 */
public interface MsgProductSendService  extends IService<MsgProductSendPO> {

    void updateMessageStatus(Integer messageId, Integer sending);

}
