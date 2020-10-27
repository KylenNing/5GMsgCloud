package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.domain.dto.*;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgGroupVO;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MsgGroupService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/10/6
 * @Description: TODO
 */
@Service
public class MsgGroupServiceImpl implements MsgGroupService {

    @Resource
    private BMsgGroupMapper bMsgGroupMapper;

    @Resource
    private BGroupTemplateMapper bGroupTemplateMapper;

    @Resource
    private BModuleMapper bModuleMapper;

    @Resource
    private BMsgMenuMapper bMsgMenuMapper;

    @Resource
    private BGroupMenuMapper bGroupMenuMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Resource
    private MsgMaterialMapper msgMaterialMapper;

    @Resource
    private MsgTemplateToButtonMapper msgTemplateToButtonMapper;

    @Resource
    private MsgTemplateButtonMapper msgTemplateButtonMapper;


    @Override
    public Map getAllGroup(CMsgUserPO user, Integer currentPage, Integer pageSize, Integer moduleId, String groupName) {
        EntityWrapper<BMsgGroupPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        if(Objects.nonNull(moduleId) && moduleId != 0){
            entityWrapper.eq("module_id",moduleId);
        }
        if(Objects.nonNull(groupName) && !groupName.equals("")){
            entityWrapper.like("group_name",groupName);
        }
        entityWrapper.orderBy("created_time",false);
        Page<BMsgGroupPO> page = new Page<>(currentPage, pageSize);
        List<BMsgGroupPO> pageList = bMsgGroupMapper.selectPage(page, entityWrapper);
        List<BMsgGroupPO> resList = new ArrayList<>();
        for(BMsgGroupPO po : pageList){
            po.setCreatedBy(cMsgUserMapper.selectById(po.getCreatedBy()).getUserName());
            resList.add(po);
        }
        int count = bMsgGroupMapper.selectCount(entityWrapper);
        Map resMap = new HashMap();
        resMap.put("DATA",addModuleNameInList(resList));
        resMap.put("TotalSize",count);
        return resMap;
    }

    @Override
    public String createGroup(CMsgUserPO user, GroupPageDTO dto) {
        String groupName = dto.getGroupName().trim();
        Integer moduleId = dto.getModuleId();
        BMsgGroupPO bMsgGroupPO = new BMsgGroupPO();
        bMsgGroupPO.setGroupName(groupName);
        bMsgGroupPO.setModuleId(moduleId);
        int createdBy = user.getId();
        int createdOrg = user.getOrganId();
        bMsgGroupPO.setCreatedBy(String.valueOf(createdBy));
        bMsgGroupPO.setCreatedOrg(createdOrg);
        Map paramMap = new HashMap();
        paramMap.put("group_name", groupName);
        int tempSize = 0;
        if(Objects.isNull(dto.getGroupId()) || dto.getGroupId() == 0){
            tempSize = bMsgGroupMapper.selectByMap(paramMap).size();
        }
        if (tempSize == 0) {
            if (dto.getGroupId() != 0 && dto.getGroupId() != null) {
                bMsgGroupPO.setUpdatedTime(new Date());
                bMsgGroupPO.setCreatedTime(new Date());
                for (BGroupTemplatePO gt : dto.getGtList()) {
                    gt.setUpdatedTime(new Date());
                    gt.setCreatedTime(new Date());
                }
                for (MsgMenuDTO menuDto : dto.getMenuList()) {
                    menuDto.setUpdatedTime(new Date());
                    menuDto.setCreatedTime(new Date());
                }
                deleteGroupById(dto.getGroupId());
            } else {
                bMsgGroupPO.setCreatedTime(new Date());
                for (BGroupTemplatePO kt : dto.getGtList()) {
                    kt.setCreatedTime(new Date());
                }
                for (MsgMenuDTO menuDto : dto.getMenuList()) {
                    menuDto.setCreatedTime(new Date());
                }
            }
            bMsgGroupMapper.insert(bMsgGroupPO);
            int groupId = bMsgGroupPO.getId();
            for (BGroupTemplatePO groupTemplate : dto.getGtList()) {
                groupTemplate.setGroupId(groupId);
                groupTemplate.setCreatedBy(createdBy);
                groupTemplate.setCreatedOrg(createdOrg);
                groupTemplate.setTemplateSort(groupTemplate.getTemplateSort());
                bGroupTemplateMapper.insert(groupTemplate);
            }
            for (MsgMenuDTO menu : dto.getMenuList()) {
                BMsgMenuPO msgMenuPO = new BMsgMenuPO();
                msgMenuPO.setMenuName(menu.getMenuName());
                msgMenuPO.setMenuType(menu.getMenuType());
                msgMenuPO.setMenuContent(menu.getMenuContent());
                msgMenuPO.setCreatedBy(createdBy);
                msgMenuPO.setCreatedTime(menu.getCreatedTime());
                msgMenuPO.setCreatedOrg(createdOrg);
                bMsgMenuMapper.insert(msgMenuPO);
                int menuId = msgMenuPO.getId();
                BGroupMenuPO groupMenuPO = new BGroupMenuPO();
                groupMenuPO.setGroupId(groupId);
                groupMenuPO.setMenuId(menuId);
                groupMenuPO.setMenuSort(menu.getMenuSort());
                groupMenuPO.setCreatedBy(createdBy);
                groupMenuPO.setCreatedTime(menu.getCreatedTime());
                groupMenuPO.setCreatedOrg(createdOrg);
                bGroupMenuMapper.insert(groupMenuPO);
            }
            return "成功添加卡片组！";
        } else {
            return "该卡片组已经存在，请更改卡片组名称";
        }
    }

    @Override
    public String deleteGroupById(Integer groupId) {
        bMsgGroupMapper.deleteGroupById(groupId);
        bGroupTemplateMapper.deleteByGroupId(groupId);
        List<Integer> menuIdList = bGroupMenuMapper.selectMenuIdByGroupId(groupId);
        bGroupMenuMapper.deleteByGroupId(groupId);
        for (int menuId : menuIdList) {
            bMsgMenuMapper.deleteByMenuId(menuId);
        }
        return "成功删除卡片组！";
    }

    @Override
    public List<BModulePO> getAllModules() {
        EntityWrapper<BModulePO> entityWrapper = new EntityWrapper();
        List<BModulePO> moduleList = bModuleMapper.selectList(entityWrapper);
        return moduleList;
    }

    @Override
    public GroupPageDTO getGroupInfoById(CMsgUserPO user, Integer groupId) {
        GroupPageDTO dto = new GroupPageDTO();
        BMsgGroupPO group = bMsgGroupMapper.getGroupById(groupId);
        dto.setGroupId(group.getId());
        dto.setGroupName(group.getGroupName());
        dto.setModuleId(group.getModuleId());
//        List<BGroupTemplatePO> groupTemplateList = bGroupTemplateMapper.getGroupTemplateByGroupId(groupId);
//        dto.setGtList(groupTemplateList);
        List<MsgTemplatePO> templateIdList = msgTemplateMapper.getTemplateIdByGroupId(groupId);
        List<MsgTemplateDTO> templateDTOList = makeTemplateList(templateIdList);
        dto.setTemplateList(templateDTOList);
        List<BGroupMenuPO> groupMenuList = bGroupMenuMapper.selectGroupMenuByGroupId(groupId);
        List<MsgMenuDTO> menuDTOList = new ArrayList<>();
        for (BGroupMenuPO groupMenu : groupMenuList) {
            MsgMenuDTO msgMenuDto = new MsgMenuDTO();
            BMsgMenuPO bMsgMenuPO = bMsgMenuMapper.selectMenuById(groupMenu.getMenuId());
            msgMenuDto.setMenuName(bMsgMenuPO.getMenuName());
            msgMenuDto.setMenuType(bMsgMenuPO.getMenuType());
            msgMenuDto.setMenuContent(bMsgMenuPO.getMenuContent());
            msgMenuDto.setMenuSort(groupMenu.getMenuSort());
            msgMenuDto.setCreatedOrg(bMsgMenuPO.getCreatedOrg());
            msgMenuDto.setCreatedBy(bMsgMenuPO.getCreatedBy());
            msgMenuDto.setCreatedTime(bMsgMenuPO.getCreatedTime());
            msgMenuDto.setUpdatedBy(bMsgMenuPO.getUpdatedBy());
            msgMenuDto.setUpdatedTime(bMsgMenuPO.getUpdatedTime());
            menuDTOList.add(msgMenuDto);
        }
        dto.setMenuList(menuDTOList);
        return dto;
    }

    List<MsgTemplateDTO> makeTemplateList(List<MsgTemplatePO> templateList) {
        List<MsgTemplateDTO> msgTemplateDTOList = new ArrayList<>();
        for (MsgTemplatePO msgTemplatePO : templateList) {
            MsgTemplateDTO msgTemplateDTO = new MsgTemplateDTO();
            // 模板id获取
            msgTemplateDTO.setId(msgTemplatePO.getId());
            // 模板类型获取
            msgTemplateDTO.setModuleId(msgTemplatePO.getModuleId());
            // 模板名称获取
            msgTemplateDTO.setTemplateName(msgTemplatePO.getTemplateName());
            // 模板模块获取
            msgTemplateDTO.setTemplateType(msgTemplatePO.getTemplateType());
            // 模板标题获取
            msgTemplateDTO.setTemplateTitle(msgTemplatePO.getTemplateTitle());
            // 模板内容获取
            msgTemplateDTO.setTemplateContent(msgTemplatePO.getTemplateContent());
            // 模板素材对象获取
            String templateImageId = msgTemplatePO.getTemplateImageId();
            MsgMaterialPO msgTemplateMaterialPO = msgMaterialMapper.selectById(templateImageId);
            if (msgTemplateMaterialPO != null) {
                MsgMaterialDTO msgTemplateMaterialDTO = new MsgMaterialDTO();
                msgTemplateMaterialDTO.setId(msgTemplateMaterialPO.getId());
                msgTemplateMaterialDTO.setMaterialName(msgTemplateMaterialPO.getMaterialName());
                msgTemplateMaterialDTO.setMaterialLocalUrl(msgTemplateMaterialPO.getMaterialLocalUrl());
                msgTemplateDTO.setMsgTemplateMaterialDTO(msgTemplateMaterialDTO);
            }
            // 模板按钮对象列表获取
            List<MsgTemplateButtonDTO> msgTemplateButtonDTOLists = new ArrayList<>();
            List<MsgTemplateToButtonPO> msgTemplateToButtonPOList = msgTemplateToButtonMapper.getMsgTemplateToButtonById(msgTemplatePO.getId());
            for (MsgTemplateToButtonPO msgTemplateToButtonPO : msgTemplateToButtonPOList) {
                MsgTemplateButtonDTO msgTemplateButtonDTO = new MsgTemplateButtonDTO();
                MsgTemplateButtonPO msgTemplateButtonPO = msgTemplateButtonMapper.getButtonById(msgTemplateToButtonPO.getButtonId());
                msgTemplateButtonDTO.setId(msgTemplateButtonPO.getId());
                msgTemplateButtonDTO.setButtonType(msgTemplateButtonPO.getButtonType());
                msgTemplateButtonDTO.setButtonName(msgTemplateButtonPO.getButtonName());
                msgTemplateButtonDTO.setButtonContent(msgTemplateButtonPO.getButtonContent());
                msgTemplateButtonDTOLists.add(msgTemplateButtonDTO);
            }
            msgTemplateDTO.setMsgTemplateButtonDTO(msgTemplateButtonDTOLists);
            msgTemplateDTOList.add(msgTemplateDTO);
        }
        return msgTemplateDTOList;
    }

    List<MsgGroupVO> addModuleNameInList(List<BMsgGroupPO> poList){
        EntityWrapper<BModulePO> entityWrapper = new EntityWrapper();
        Map<Integer,BModulePO> map = bModuleMapper.selectList(entityWrapper).stream().
                collect(Collectors.toMap(BModulePO::getId,module -> module));
        List<MsgGroupVO> resList = new ArrayList<>();
        for(BMsgGroupPO po : poList){
            MsgGroupVO vo = new MsgGroupVO();
            BeanUtils.copyProperties(po,vo);
            vo.setModuleName(map.get(po.getModuleId()).getName());
            resList.add(vo);
        }
        return resList;
    }
}