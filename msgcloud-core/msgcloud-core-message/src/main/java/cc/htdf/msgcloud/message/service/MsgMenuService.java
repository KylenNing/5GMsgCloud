package cc.htdf.msgcloud.message.service;

import cc.htdf.msgcloud.message.domain.po.BMsgMenuPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplateButtonPO;

import java.util.List;

/**
 * author: JT
 * date: 2020/8/17
 * title:
 */
public interface MsgMenuService {

    List<BMsgMenuPO> findSuspensionMenusByKeyword(String keyword, String serviceId);

    List<BMsgMenuPO> findSuspensionMenusByGroupId(Integer groupId);

    List<MsgTemplateButtonPO> findTemplateButtonsByTemplateId(String templateId);

}
