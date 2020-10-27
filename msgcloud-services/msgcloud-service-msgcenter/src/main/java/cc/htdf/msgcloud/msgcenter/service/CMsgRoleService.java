package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgRoleDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgRoleVO;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface CMsgRoleService {

    String createRole(CMsgUserPO user, CMsgRoleVO cMsgRoleVO);

    String updateRole(CMsgUserPO user, CMsgRoleVO cMsgRoleVO);

    MsgPageDTO getRoleListByOrganId(String organId, Integer pageNum, Integer pageSize);

    List<CMsgRoleDTO> getRoleList(String organId);

    CMsgRoleDTO getRoleByRoleId(String roleId);
}
