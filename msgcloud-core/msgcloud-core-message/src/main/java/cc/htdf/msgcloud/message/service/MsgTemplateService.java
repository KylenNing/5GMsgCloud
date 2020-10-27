package cc.htdf.msgcloud.message.service;

import cc.htdf.msgcloud.message.domain.po.BMsgKeywordPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplatePO;

import java.util.LinkedList;
import java.util.Map;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
public interface MsgTemplateService {

    LinkedList<MsgTemplatePO> findByChannelIdAndAction(Long channelId, String action);

    LinkedList<MsgTemplatePO> findByGroupId(Integer groupId);

    //查找关键字是否配置组
    BMsgKeywordPO findGroupByChannelIdAndAction(Long channelId, String action);

    Map<String, MsgTemplatePO> findByTemplateIds(LinkedList<String> templateIds);
}
