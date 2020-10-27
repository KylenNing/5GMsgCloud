package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.GroupPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.BModulePO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;

import java.util.List;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/10/6
 * @Description: TODO
 */
public interface MsgGroupService {

    Map getAllGroup(CMsgUserPO user, Integer currentPage, Integer pageSize, Integer moduleId, String groupName);

    String createGroup(CMsgUserPO user, GroupPageDTO dto);

    String deleteGroupById(Integer id);

    List<BModulePO> getAllModules();

    GroupPageDTO getGroupInfoById(CMsgUserPO user, Integer groupId);
}
