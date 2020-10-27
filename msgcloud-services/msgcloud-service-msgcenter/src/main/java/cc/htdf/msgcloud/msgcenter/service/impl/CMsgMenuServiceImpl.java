package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgMenuTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgMenuPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRoleMenuPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgMenuVO;
import cc.htdf.msgcloud.msgcenter.enums.ButtonTypeEnum;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgMenuMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgRoleMenuMapper;
import cc.htdf.msgcloud.msgcenter.service.CMsgMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class CMsgMenuServiceImpl implements CMsgMenuService {

    @Resource
    private CMsgMenuMapper cMsgMenuMapper;

    @Resource
    private CMsgRoleMenuMapper cMsgRoleMenuMapper;

    @Override
    public String createMenu(CMsgUserPO user, CMsgMenuVO cMsgMenuVO) {
        // 新建菜单表
        CMsgMenuPO cMsgMenuPO = new CMsgMenuPO();
        cMsgMenuVO.setCreatedOrg(String.valueOf(user.getOrganId()));
        cMsgMenuVO.setCreatedBy(String.valueOf(user.getId()));
        cMsgMenuVO.setCreatedTime(new Date());
        cMsgMenuMapper.insert(makeCMsgMenuPO(cMsgMenuPO, cMsgMenuVO));
        // 新建角色菜单关系表
        CMsgRoleMenuPO cMsgRoleMenuPO = new CMsgRoleMenuPO();
        cMsgRoleMenuPO.setRoleId(user.getRoleId());
        cMsgRoleMenuPO.setMenuId(cMsgMenuPO.getId());
        cMsgRoleMenuPO.setIsAvailable(1);
        cMsgRoleMenuMapper.insert(cMsgRoleMenuPO);
        return "创建成功";
    }

    private CMsgMenuPO makeCMsgMenuPO(CMsgMenuPO cMsgMenuPO, CMsgMenuVO cMsgMenuVO) {
        cMsgMenuPO.setMenuName(cMsgMenuVO.getLabel());
        cMsgMenuPO.setMenuCode(cMsgMenuVO.getMenuCode());
        cMsgMenuPO.setMenuLcon(cMsgMenuVO.getMenuLcon());
        cMsgMenuPO.setMenuUrl(cMsgMenuVO.getMenuUrl());
        cMsgMenuPO.setRequestMethod(cMsgMenuVO.getRequestMethod());
        cMsgMenuPO.setParentId(cMsgMenuVO.getParentId());
        cMsgMenuPO.setLevel(cMsgMenuVO.getLevel());
        cMsgMenuPO.setSort(cMsgMenuVO.getSort());
        cMsgMenuPO.setMenuType(cMsgMenuVO.getMenuType());
        if (cMsgMenuVO.getMenuType() == 2) {
            cMsgMenuPO.setButtonType(cMsgMenuVO.getButtonType());
        }
        if (cMsgMenuVO.getIsAvailable() == true) {
            cMsgMenuPO.setIsAvailable(1);
        } else {
            cMsgMenuPO.setIsAvailable(0);
            // 删除角色菜单关系表
            cMsgRoleMenuMapper.updateByRoleIdAndMenuId("0", "1", String.valueOf(cMsgMenuVO.getId()));
        }
        return cMsgMenuPO;
    }

    @Override
    public String updateMenu(CMsgUserPO user, CMsgMenuVO cMsgMenuVO) {
        CMsgMenuPO cMsgMenuPO = new CMsgMenuPO();
        cMsgMenuPO.setId(cMsgMenuVO.getId());
        cMsgMenuPO.setUpdatedBy(String.valueOf(user.getId()));
        cMsgMenuPO.setUpdatedTime(new Date());
        cMsgMenuPO.setCreatedOrg(cMsgMenuVO.getCreatedOrg());
        cMsgMenuPO.setCreatedBy(cMsgMenuVO.getCreatedBy());
        cMsgMenuPO.setCreatedTime(cMsgMenuVO.getCreatedTime());
        cMsgMenuMapper.updateById(makeCMsgMenuPO(cMsgMenuPO, cMsgMenuVO));
        return "更新成功";
    }

    @Override
    public List<CMsgMenuTreeDTO> getMenuTree(CMsgUserPO user) {
        List<CMsgMenuPO> CMsgMenuPOList = cMsgMenuMapper.getAllMenu(user.getId());
        List<CMsgMenuTreeDTO> treeNodes = new ArrayList<>();
        CMsgMenuTreeDTO cMsgMenuTreeDTO;
        for (CMsgMenuPO cMsgMenuPO : CMsgMenuPOList) {
            if (cMsgMenuPO.getIsAvailable() == 0) {
                continue;
            }
            cMsgMenuTreeDTO = makeCMsgMenuTreeDTO(cMsgMenuPO);
            treeNodes.add(cMsgMenuTreeDTO);
        }
        return makeTree(treeNodes, "0");
    }

    @Override
    public List<CMsgMenuTreeDTO> findMenuTreeAllByUserId(CMsgUserPO user) {
        List<CMsgMenuPO> CMsgMenuPOList = cMsgMenuMapper.getMenusByUserId(user.getId());
        List<CMsgMenuTreeDTO> treeNodes = new ArrayList<>();
        CMsgMenuTreeDTO cMsgMenuTreeDTO;
        for (CMsgMenuPO cMsgMenuPO : CMsgMenuPOList) {
            if (cMsgMenuPO.getIsAvailable() == 0) {
                continue;
            }
            cMsgMenuTreeDTO = makeCMsgMenuTreeDTO(cMsgMenuPO);
            treeNodes.add(cMsgMenuTreeDTO);
        }
        return makeTree(treeNodes, "0");
    }

    @Override
    public List<Map> getButtonTypeList() {
        List<Map> list = new ArrayList<>();
        ButtonTypeEnum[] lists = ButtonTypeEnum.values();
        for (ButtonTypeEnum buttonTypeEnum : lists) {
            Map map = new HashMap();
            map.put("key", buttonTypeEnum.getKey());
            map.put("value", buttonTypeEnum.getValue());
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map> getButtonDisPlayList(CMsgUserPO user, String menuId) {
        List<Map> list = new ArrayList<>();
        List<String> cMsgMenuPOList = cMsgMenuMapper.selectButtonByMenuId(user.getId(), menuId);
        for (String buttonType : cMsgMenuPOList) {
            Map map = new HashMap();
            map.put("name", ButtonTypeEnum.getValue(Integer.valueOf(buttonType)));
            map.put("display", true);
            list.add(map);
        }
        return list;
    }

    private List<CMsgMenuTreeDTO> makeTree(List<CMsgMenuTreeDTO> treeNodes, String organId) {
        List<CMsgMenuTreeDTO> trees = new ArrayList<>();
        for (CMsgMenuTreeDTO cMsgMenuTreeDTO : treeNodes) {
            if (organId.equals(String.valueOf(cMsgMenuTreeDTO.getParentId()))) {
                trees.add(findChildren(cMsgMenuTreeDTO, treeNodes));
            }
        }
        return trees;
    }

    private CMsgMenuTreeDTO findChildren(CMsgMenuTreeDTO DTO, List<CMsgMenuTreeDTO> DTOList) {
        for (CMsgMenuTreeDTO cMsgMenuTreeDTO : DTOList) {
            if (DTO.getId() == cMsgMenuTreeDTO.getParentId()) {
                if (DTO.getChildren() == null) {
                    DTO.setChildren(new ArrayList<>());
                }
                DTO.add(findChildren(cMsgMenuTreeDTO, DTOList));
            }
        }
        return DTO;
    }

    private CMsgMenuTreeDTO makeCMsgMenuTreeDTO(CMsgMenuPO cMsgMenuPO) {
        CMsgMenuTreeDTO cMsgMenuTreeDTO = new CMsgMenuTreeDTO();
        cMsgMenuTreeDTO.setId(cMsgMenuPO.getId());
        cMsgMenuTreeDTO.setLabel(cMsgMenuPO.getMenuName());
        cMsgMenuTreeDTO.setMenuCode(cMsgMenuPO.getMenuCode());
        cMsgMenuTreeDTO.setMenuLcon(cMsgMenuPO.getMenuLcon());
        cMsgMenuTreeDTO.setMenuUrl(cMsgMenuPO.getMenuUrl());
        cMsgMenuTreeDTO.setRequestMethod(cMsgMenuPO.getRequestMethod());
        cMsgMenuTreeDTO.setParentId(cMsgMenuPO.getParentId());
        cMsgMenuTreeDTO.setLevel(cMsgMenuPO.getLevel());
        cMsgMenuTreeDTO.setSort(cMsgMenuPO.getSort());
        cMsgMenuTreeDTO.setMenuType(cMsgMenuPO.getMenuType());
        if (cMsgMenuPO.getMenuType() == 2) {
            cMsgMenuTreeDTO.setButtonType(cMsgMenuPO.getButtonType());
        }
        if (cMsgMenuPO.getIsAvailable() == 0) {
            cMsgMenuTreeDTO.setIsAvailable(false);
        } else {
            cMsgMenuTreeDTO.setIsAvailable(true);
        }
        cMsgMenuTreeDTO.setCreatedOrg(cMsgMenuPO.getCreatedOrg());
        cMsgMenuTreeDTO.setCreatedBy(cMsgMenuPO.getCreatedBy());
        cMsgMenuTreeDTO.setCreatedTime(cMsgMenuPO.getCreatedTime());
        cMsgMenuTreeDTO.setUpdatedBy(cMsgMenuPO.getUpdatedBy());
        cMsgMenuTreeDTO.setUpdatedTime(cMsgMenuPO.getUpdatedTime());
        return cMsgMenuTreeDTO;
    }
}
