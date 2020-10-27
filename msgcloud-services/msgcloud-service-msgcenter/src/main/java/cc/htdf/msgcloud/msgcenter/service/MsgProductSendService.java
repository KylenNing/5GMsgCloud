package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductSendDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductSendPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
public interface MsgProductSendService {

    MsgProductSendPageDTO getProductSendList(CMsgUserPO user, String productName, String serviceId, Integer msgStatus, String startTime, String endTime, Integer pageNum, Integer pageSize);

    MsgProductSendDTO getProductSendById(String productId);

    String sendProduct(CMsgUserPO user, String productId, String serviceId, boolean isAll, String userLabelId);

    String examine(String productId, String reason);
}
