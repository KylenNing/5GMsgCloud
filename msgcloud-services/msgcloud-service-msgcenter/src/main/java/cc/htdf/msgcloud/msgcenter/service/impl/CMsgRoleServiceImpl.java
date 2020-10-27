package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgRoleDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgOrganPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRoleMenuPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRolePO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgRoleVO;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgOrganMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgRoleMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgRoleMenuMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgUserMapper;
import cc.htdf.msgcloud.msgcenter.service.CMsgRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class CMsgRoleServiceImpl implements CMsgRoleService {

    @Resource
    private CMsgRoleMapper cMsgRoleMapper;

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Resource
    private CMsgRoleMenuMapper cMsgRoleMenuMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Override
    public String createRole(CMsgUserPO user, CMsgRoleVO cMsgRoleVO) {
        // 插入角色表
        CMsgRolePO cMsgRolePO = new CMsgRolePO();
        cMsgRoleVO.setCreatedOrg(String.valueOf(user.getOrganId()));
        cMsgRoleVO.setCreatedBy(String.valueOf(user.getId()));
        cMsgRoleVO.setCreatedTime(new Date());
        cMsgRoleMapper.insert(makeCMsgRolePO(cMsgRolePO, cMsgRoleVO));
        int roleId = cMsgRolePO.getId();
        // 插入角色权限关系表
        List<Integer> menuIds = cMsgRoleVO.getMenuId();
        for (Integer menuId : menuIds) {
            CMsgRoleMenuPO cMsgRoleMenuPO = new CMsgRoleMenuPO();
            cMsgRoleMenuPO.setRoleId(roleId);
            cMsgRoleMenuPO.setMenuId(menuId);
            cMsgRoleMenuPO.setIsAvailable(1);
            cMsgRoleMenuMapper.insert(cMsgRoleMenuPO);
        }
        return "创建成功";
    }

    @Override
    public String updateRole(CMsgUserPO user, CMsgRoleVO cMsgRoleVO) {
        // 先判断角色是否已经绑定了用户
        if (cMsgRoleVO.getIsAvailable() == false) {
            List<CMsgUserPO> lists = cMsgUserMapper.getUserByRoleId(String.valueOf(cMsgRoleVO.getId()));
            if (lists.size() != 0) {
                return "该角色已经绑定用户，不能删除";
            }
        }
        // 先更新角色表
        int roleId = cMsgRoleVO.getId();
        CMsgRolePO cMsgRolePO = new CMsgRolePO();
        cMsgRolePO.setId(roleId);
        cMsgRolePO.setUpdatedBy(String.valueOf(user.getId()));
        cMsgRolePO.setUpdatedTime(new Date());
        cMsgRolePO.setCreatedOrg(cMsgRoleVO.getCreatedOrg());
        cMsgRolePO.setCreatedBy(cMsgRoleVO.getCreatedBy());
        cMsgRolePO.setCreatedTime(cMsgRoleVO.getCreatedTime());
        cMsgRoleMapper.updateById(makeCMsgRolePO(cMsgRolePO, cMsgRoleVO));
        // 根据角色ID删除角色菜单关系表中相关数据(置成无效)
        cMsgRoleMenuMapper.deleteByRoleId(String.valueOf(roleId));
        // 再更新角色菜单关系表
        List<CMsgRoleMenuPO> cMsgRoleMenuPOLists = cMsgRoleMenuMapper.getMenu(String.valueOf(roleId));
        // 数据库存储的menuId
        List<Integer> menuIdStorage = new ArrayList<>();
        for (CMsgRoleMenuPO PO : cMsgRoleMenuPOLists) {
            menuIdStorage.add(PO.getMenuId());
        }
        // 用户选择的的menuId
        List<Integer> menuIdChoice = cMsgRoleVO.getMenuId();

        //交集(更新是否有效状态)
        List<Integer> collectIntersection = menuIdStorage.stream().filter(num -> menuIdChoice.contains(num))
                .collect(Collectors.toList());
        for (Integer menuIdIntersection : collectIntersection) {
            cMsgRoleMenuMapper.updateByRoleIdAndMenuId("1", String.valueOf(roleId), String.valueOf(menuIdIntersection));
        }

        //差集 存储-选择
        List<Integer> collectDifference = menuIdStorage.stream().filter(num -> !menuIdChoice.contains(num))
                .collect(Collectors.toList());
        for (Integer menuIdDifference : collectDifference) {
            if (user.getRoleId() == roleId) {
                cMsgRoleMenuMapper.updateByRoleIdAndMenuId("0", String.valueOf(roleId), String.valueOf(menuIdDifference));
            } else {
                cMsgRoleMenuMapper.deleteByRoleIdAndMenuId(String.valueOf(roleId), String.valueOf(menuIdDifference));
            }
        }

        //差集 选择-存储
        List<Integer> collectDifferences = menuIdChoice.stream().filter(num -> !menuIdStorage.contains(num))
                .collect(Collectors.toList());
        for (Integer menuIdDifferences : collectDifferences) {
            CMsgRoleMenuPO cMsgRoleMenuPO = new CMsgRoleMenuPO();
            cMsgRoleMenuPO.setRoleId(roleId);
            cMsgRoleMenuPO.setMenuId(menuIdDifferences);
            cMsgRoleMenuPO.setIsAvailable(1);
            cMsgRoleMenuMapper.insert(cMsgRoleMenuPO);
        }

        return "更新成功";
    }

    @Override
    public MsgPageDTO getRoleListByOrganId(String organId, Integer pageNum, Integer pageSize) {
        List<CMsgRoleDTO> cMsgRoleDTOList = makeCMsgRoleDTOList(cMsgOrganMapper.getOrganTree(organId));
        MsgPageDTO pageDTO = new MsgPageDTO();
        pageDTO.setTotolSize(cMsgRoleDTOList.size());
        pageDTO.setData(PageUtil.startPage(cMsgRoleDTOList, pageNum, pageSize));
        return pageDTO;
    }

    private List<CMsgRoleDTO> makeCMsgRoleDTOList(List<CMsgOrganPO> CMsgOrganPOList) {
        List<CMsgRoleDTO> cMsgRoleDTOList = new ArrayList<>();
        for (CMsgOrganPO cMsgOrganPO : CMsgOrganPOList) {
            List<CMsgRolePO> CMsgRolePOList = cMsgRoleMapper.getRoleByOrganId(String.valueOf(cMsgOrganPO.getId()));
            for (CMsgRolePO cMsgRolePO : CMsgRolePOList) {
                CMsgRoleDTO CMsgRoleDTO = makeCMsgRoleDTO(cMsgRolePO);
                CMsgRoleDTO.setOrganName(cMsgOrganMapper.selectById(cMsgRolePO.getOrganId()).getOrganName());
                cMsgRoleDTOList.add(CMsgRoleDTO);
            }
        }
        return cMsgRoleDTOList;
    }

    @Override
    public List<CMsgRoleDTO> getRoleList(String organId) {
        return makeCMsgRoleDTOList(cMsgOrganMapper.getOrganTree(organId));
    }

    @Override
    public CMsgRoleDTO getRoleByRoleId(String roleId) {
        CMsgRolePO cMsgRolePO = cMsgRoleMapper.selectById(roleId);
        List<CMsgRoleMenuPO> cMsgRoleMenuPOList = cMsgRoleMenuMapper.getMenuByRoleId(roleId);
        List<Integer> menuIds = new ArrayList<>();
        for (CMsgRoleMenuPO cMsgRoleMenuPO : cMsgRoleMenuPOList) {
            menuIds.add(cMsgRoleMenuPO.getMenuId());
        }
        CMsgRoleDTO cMsgRoleDTO = makeCMsgRoleDTO(cMsgRolePO);
        cMsgRoleDTO.setMenuId(menuIds);
        return cMsgRoleDTO;
    }

    private CMsgRoleDTO makeCMsgRoleDTO(CMsgRolePO cMsgRolePO) {
        CMsgRoleDTO cMsgRoleDTO = new CMsgRoleDTO();
        cMsgRoleDTO.setId(cMsgRolePO.getId());
        cMsgRoleDTO.setOrganId(cMsgRolePO.getOrganId());
        cMsgRoleDTO.setRoleName(cMsgRolePO.getRoleName());
        cMsgRoleDTO.setRoleType(cMsgRolePO.getRoleType());
        cMsgRoleDTO.setRoleDescribe(cMsgRolePO.getRoleDescribe());
        if (cMsgRolePO.getIsAvailable() == 0) {
            cMsgRoleDTO.setIsAvailable(false);
        } else {
            cMsgRoleDTO.setIsAvailable(true);
        }
        cMsgRoleDTO.setCreatedOrg(cMsgRolePO.getCreatedOrg());
        cMsgRoleDTO.setCreatedBy(cMsgRolePO.getCreatedBy());
        cMsgRoleDTO.setCreatedTime(cMsgRolePO.getCreatedTime());
        cMsgRoleDTO.setUpdatedBy(cMsgRolePO.getUpdatedBy());
        cMsgRoleDTO.setUpdatedTime(cMsgRolePO.getUpdatedTime());
        return cMsgRoleDTO;
    }

    private CMsgRolePO makeCMsgRolePO(CMsgRolePO cMsgRolePO, CMsgRoleVO cMsgRoleVO) {
        cMsgRolePO.setOrganId(cMsgRoleVO.getOrganId());
        cMsgRolePO.setRoleName(cMsgRoleVO.getRoleName());
        cMsgRolePO.setRoleType(cMsgRoleVO.getRoleType());
        cMsgRolePO.setRoleDescribe(cMsgRoleVO.getRoleDescribe());
        if (cMsgRoleVO.getIsAvailable() == true) {
            cMsgRolePO.setIsAvailable(1);
        } else {
            cMsgRolePO.setIsAvailable(0);
        }
        return cMsgRolePO;
    }
}
