package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgAreaTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgUserDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgUserLabelDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgUserLabelVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgUserVO;
import cc.htdf.msgcloud.msgcenter.enums.ActiveTypeEnum;
import cc.htdf.msgcloud.msgcenter.enums.SexTypeEnum;
import cc.htdf.msgcloud.msgcenter.enums.TravelTypeEnum;
import cc.htdf.msgcloud.msgcenter.enums.WorkTypeEnum;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MsgUserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class MsgUserServiceImpl implements MsgUserService {

    @Resource
    private MsgUserLabelMapper msgUserLabelMapper;

    @Resource
    private MsgUserMapper msgUserMapper;

    @Resource
    private MsgUserToLabelMapper msgUserToLabelMapper;

    @Resource
    private MsgAreaMapper msgAreaMapper;

    @Resource
    private MsgUserBehaviorMapper msgUserBehaviorMapper;

    @Resource
    private MsgUserAttributeMapper msgUserAttributeMapper;

    @Override
    public MsgPageDTO getUserLabelList(CMsgUserPO user, String userLabelName, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgUserLabelPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(userLabelName) && !userLabelName.equals("")) {
            wrapper.like("LABEL_NAME", userLabelName);
        }
        int totalSize = msgUserLabelMapper.selectCount(wrapper);
        Page<MsgUserLabelPO> page = new Page<>(pageNum, pageSize);
        List<MsgUserLabelPO> lists = msgUserLabelMapper.selectPage(page, wrapper);
        MsgPageDTO pageDTO = new MsgPageDTO();
        pageDTO.setTotolSize(totalSize);
        pageDTO.setData(lists);
        return pageDTO;
    }

    @Override
    public MsgPageDTO getUserList(String userLabelId, String userTel, CMsgUserPO user, String userName, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgUserPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(userName) && !userName.equals("")) {
            wrapper.like("USER_NAME", userName);
        }
        if (Objects.nonNull(userTel) && !userTel.equals("")) {
            wrapper.like("USER_TEL", userTel);
        }
        List<MsgUserPO> lists = msgUserMapper.selectList(wrapper);
        List<MsgUserDTO> msgTemplateListDTO = new ArrayList<>();
        for (MsgUserPO msgUserPO : lists) {
            if (Objects.nonNull(userLabelId) && !userLabelId.equals("")) {
                String label = msgUserToLabelMapper.getLabelByUserId(msgUserPO.getId()).getLabelId();
                if (!label.equals(userLabelId)) {
                    continue;
                }
            }
            MsgUserDTO msgUserDTO = new MsgUserDTO();
            String labelId = msgUserToLabelMapper.getLabelByUserId(msgUserPO.getId()).getLabelId();
            msgUserDTO.setId(msgUserPO.getId());
            msgUserDTO.setUserLableName(msgUserLabelMapper.selectById(labelId).getLabelName());
            msgUserDTO.setUserName(msgUserPO.getUserName());
            msgUserDTO.setUserTel(msgUserPO.getUserTel());
            msgUserDTO.setCreatedTime(msgUserPO.getCreatedTime());
            msgTemplateListDTO.add(msgUserDTO);
        }
        MsgPageDTO pageDTO = new MsgPageDTO();
        pageDTO.setTotolSize(msgTemplateListDTO.size());
        pageDTO.setData(PageUtil.startPage(msgTemplateListDTO, pageNum, pageSize));
        return pageDTO;
    }

    @Override
    public String createUserLabel(CMsgUserPO user, MsgUserLabelVO msgUserLabelVO) {
        String labelId = msgUserLabelVO.getId();
        Boolean insert = Objects.isNull(labelId) || labelId.equals("");
        String labelName = msgUserLabelVO.getLabelName();
        String labelRemarks = msgUserLabelVO.getLabelRemarks();
        // 限定用户标签名称不可重复
        EntityWrapper<MsgUserLabelPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.eq("LABEL_NAME", labelName);
        List<MsgUserLabelPO> lists = msgUserLabelMapper.selectList(wrapper);
        if (insert && lists.size() != 0) {
            return "用户组已存在";
        }
        MsgUserLabelPO msgUserLabelPO = new MsgUserLabelPO();
        msgUserLabelPO.setLabelName(labelName);
        msgUserLabelPO.setLabelRemarks(labelRemarks);
        if (insert) {
            String userLabelId = UUID.randomUUID().toString().replace("-", "");
            msgUserLabelPO.setId(userLabelId);
            msgUserLabelPO.setCreatedOrg(String.valueOf(user.getOrganId()));
            msgUserLabelPO.setCreatedBy(String.valueOf(user.getId()));
            msgUserLabelPO.setCreatedTime(new Date());
            msgUserLabelMapper.insert(msgUserLabelPO);
        } else {
            msgUserLabelPO.setId(labelId);
            msgUserLabelPO.setUpdatedBy(String.valueOf(user.getId()));
            msgUserLabelPO.setUpdatedTime(new Date());
            msgUserLabelPO.setCreatedOrg(msgUserLabelVO.getCreatedOrg());
            msgUserLabelPO.setCreatedBy(msgUserLabelVO.getCreatedBy());
            msgUserLabelPO.setCreatedTime(msgUserLabelVO.getCreatedTime());
            msgUserLabelMapper.updateById(msgUserLabelPO);
        }
        return "保存成功";
    }

    @Override
    public String createUser(CMsgUserPO user, MsgUserVO msgUserVO) {
        // 保存用户表
        MsgUserPO msgUserPO = new MsgUserPO();
        msgUserPO.setUserName(msgUserVO.getUserName());
        msgUserPO.setUserTel(msgUserVO.getUserTel());
        Boolean insert = Objects.isNull(msgUserVO.getId()) || msgUserVO.getId().equals("");
        if (insert) {
            // 保存用户基本信息表
            MsgUserAttributePO msgUserAttributePO = new MsgUserAttributePO();
            msgUserAttributePO.setSex(msgUserVO.getSex());
            msgUserAttributePO.setAge(msgUserVO.getAge());
            msgUserAttributePO.setWork(msgUserVO.getWork());
            msgUserAttributePO.setTravel(msgUserVO.getTravel());
            msgUserAttributePO.setRemarks(msgUserVO.getAttributeRemarks());
            msgUserAttributeMapper.insert(msgUserAttributePO);
            // 保存用户行为特征表
            MsgUserBehaviorPO msgUserBehaviorPO = new MsgUserBehaviorPO();
            msgUserBehaviorPO.setAreaId(msgUserVO.getArea());
            msgUserBehaviorPO.setActive(msgUserVO.getActive());
            msgUserBehaviorPO.setAddress(msgUserVO.getAddress());
            msgUserBehaviorPO.setChangeAddress(msgUserVO.getChangeAddress());
            msgUserBehaviorMapper.insert(msgUserBehaviorPO);
            // 保存用户表
            String userId = UUID.randomUUID().toString().replace("-", "");
            msgUserPO.setId(userId);
            msgUserPO.setUserAttributeId(msgUserAttributePO.getId());
            msgUserPO.setUserBehaviorId(msgUserBehaviorPO.getId());
            msgUserPO.setCreatedOrg(String.valueOf(user.getOrganId()));
            msgUserPO.setCreatedBy(String.valueOf(user.getId()));
            msgUserPO.setCreatedTime(new Date());
            msgUserMapper.insert(msgUserPO);
        } else {
            // 保存用户基本信息表
            MsgUserAttributePO msgUserAttributePO = new MsgUserAttributePO();
            msgUserAttributePO.setId(Integer.valueOf(msgUserVO.getUserAttributeId()));
            msgUserAttributePO.setSex(msgUserVO.getSex());
            msgUserAttributePO.setAge(msgUserVO.getAge());
            msgUserAttributePO.setWork(msgUserVO.getWork());
            msgUserAttributePO.setTravel(msgUserVO.getTravel());
            msgUserAttributePO.setRemarks(msgUserVO.getAttributeRemarks());
            msgUserAttributeMapper.updateById(msgUserAttributePO);
            // 保存用户行为特征表
            MsgUserBehaviorPO msgUserBehaviorPO = new MsgUserBehaviorPO();
            msgUserBehaviorPO.setId(Integer.valueOf(msgUserVO.getUserBehaviorId()));
            msgUserBehaviorPO.setAreaId(msgUserVO.getArea());
            msgUserBehaviorPO.setActive(msgUserVO.getActive());
            msgUserBehaviorPO.setAddress(msgUserVO.getAddress());
            msgUserBehaviorPO.setChangeAddress(msgUserVO.getChangeAddress());
            msgUserBehaviorMapper.updateById(msgUserBehaviorPO);
            // 保存用户表
            msgUserPO.setId(msgUserVO.getId());
            msgUserPO.setUserAttributeId(Integer.valueOf(msgUserVO.getUserAttributeId()));
            msgUserPO.setUserBehaviorId(Integer.valueOf(msgUserVO.getUserBehaviorId()));
            msgUserPO.setUpdatedBy(String.valueOf(user.getId()));
            msgUserPO.setUpdatedTime(new Date());
            msgUserPO.setCreatedOrg(msgUserVO.getCreatedOrg());
            msgUserPO.setCreatedBy(msgUserVO.getCreatedBy());
            msgUserPO.setCreatedTime(msgUserVO.getCreatedTime());
            msgUserMapper.updateById(msgUserPO);
            msgUserToLabelMapper.deleteByUserId(msgUserVO.getId());
        }
        // 保存用户关系表
        MsgUserToLabelPO msgUserToLabelPO = new MsgUserToLabelPO();
        msgUserToLabelPO.setId(UUID.randomUUID().toString().replace("-", ""));
        msgUserToLabelPO.setUserId(msgUserPO.getId());
        msgUserToLabelPO.setLabelId(msgUserVO.getLabelId());
        msgUserToLabelMapper.insert(msgUserToLabelPO);
        return "保存成功";
    }

    @Override
    public String deleteUserLabelByIds(String userLabelIds) {
        int userCount = msgUserToLabelMapper.getUserCountByLabelId(userLabelIds);
        if (userCount != 0) {
            return "该用户组下存在用户";
        }
        msgUserLabelMapper.deleteById(userLabelIds);
        return "删除成功";
    }

    @Override
    public String deleteUserByIds(String userIds) {
        MsgUserPO msgUserPO = msgUserMapper.selectById(userIds);
        msgUserAttributeMapper.deleteById(msgUserPO.getUserAttributeId());
        msgUserBehaviorMapper.deleteById(msgUserPO.getUserBehaviorId());
        msgUserMapper.deleteById(userIds);
        msgUserToLabelMapper.deleteByUserId(userIds);
        return "删除成功";
    }

    @Override
    public MsgUserLabelDTO getUserLabelById(String userLabelId) {
        MsgUserLabelDTO msgUserLabelDTO = new MsgUserLabelDTO();
        MsgUserLabelPO msgUserLabelPO = msgUserLabelMapper.selectById(userLabelId);
        msgUserLabelDTO.setId(msgUserLabelPO.getId());
        msgUserLabelDTO.setLabelName(msgUserLabelPO.getLabelName());
        msgUserLabelDTO.setLabelRemarks(msgUserLabelPO.getLabelRemarks());
        msgUserLabelDTO.setCreatedOrg(msgUserLabelPO.getCreatedOrg());
        msgUserLabelDTO.setCreatedTime(msgUserLabelPO.getCreatedTime());
        msgUserLabelDTO.setCreatedUser(msgUserLabelPO.getCreatedBy());
        msgUserLabelDTO.setUpdatedTime(msgUserLabelPO.getUpdatedTime());
        msgUserLabelDTO.setUpdatedUser(msgUserLabelPO.getUpdatedBy());
        return msgUserLabelDTO;
    }

    @Override
    public MsgUserDTO getUserById(String userId) {
        MsgUserDTO msgUserDTO = new MsgUserDTO();
        MsgUserPO msgUserPO = msgUserMapper.selectById(userId);
        msgUserDTO.setId(msgUserPO.getId());
        msgUserDTO.setUserName(msgUserPO.getUserName());
        msgUserDTO.setUserTel(msgUserPO.getUserTel());
        msgUserDTO.setCreatedOrg(msgUserPO.getCreatedOrg());
        msgUserDTO.setCreatedTime(msgUserPO.getCreatedTime());
        msgUserDTO.setCreatedUser(msgUserPO.getCreatedBy());
        msgUserDTO.setUpdatedTime(msgUserPO.getUpdatedTime());
        msgUserDTO.setUpdatedUser(msgUserPO.getUpdatedBy());
        MsgUserToLabelPO msgUserToLabelPO = msgUserToLabelMapper.getLabelByUserId(userId);
        MsgUserLabelPO msgUserLabelPO = msgUserLabelMapper.selectById(msgUserToLabelPO.getLabelId());
        msgUserDTO.setUserLableId(msgUserLabelPO.getId());
        msgUserDTO.setUserLableName(msgUserLabelPO.getLabelName());
        // 用户基本信息
        msgUserDTO.setUserAttributeId(String.valueOf(msgUserPO.getUserAttributeId()));
        MsgUserAttributePO msgUserAttributePO = msgUserAttributeMapper.selectById(msgUserPO.getUserAttributeId());
        msgUserDTO.setSex(msgUserAttributePO.getSex());
        msgUserDTO.setAge(msgUserAttributePO.getAge());
        msgUserDTO.setWork(msgUserAttributePO.getWork());
        msgUserDTO.setTravel(msgUserAttributePO.getTravel());
        msgUserDTO.setAttributeRemarks(msgUserAttributePO.getRemarks());
        //用户行为特征
        msgUserDTO.setUserBehaviorId(String.valueOf(msgUserPO.getUserBehaviorId()));
        MsgUserBehaviorPO msgUserBehaviorPO = msgUserBehaviorMapper.selectById(msgUserPO.getUserBehaviorId());
        msgUserDTO.setArea(msgUserBehaviorPO.getAreaId());
        msgUserDTO.setActive(msgUserBehaviorPO.getActive());
        msgUserDTO.setAddress(msgUserBehaviorPO.getAddress());
        msgUserDTO.setChangeAddress(msgUserBehaviorPO.getChangeAddress());
        return msgUserDTO;
    }

    @Override
    public List<MsgUserLabelDTO> getUserLabel(CMsgUserPO user) {
        List<MsgUserLabelDTO> msgUserLabelDTOList = new ArrayList<>();
        EntityWrapper<MsgUserLabelPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        List<MsgUserLabelPO> lists = msgUserLabelMapper.selectList(wrapper);
        for (MsgUserLabelPO msgUserLabelPO : lists) {
            MsgUserLabelDTO msgUserLabelDTO = new MsgUserLabelDTO();
            msgUserLabelDTO.setId(msgUserLabelPO.getId());
            msgUserLabelDTO.setLabelName(msgUserLabelPO.getLabelName());
            msgUserLabelDTOList.add(msgUserLabelDTO);
        }
        return msgUserLabelDTOList;
    }

    @Override
    public List<MsgAreaTreeDTO> getAreaList() {
        List<MsgAreaPO> areaPOList = msgAreaMapper.getAreaList();
        List<MsgAreaTreeDTO> treeNodes = new ArrayList<>();
        MsgAreaTreeDTO msgAreaTreeDTO;
        for (MsgAreaPO msgAreaPO : areaPOList) {
            msgAreaTreeDTO = new MsgAreaTreeDTO();
            msgAreaTreeDTO.setId(msgAreaPO.getId());
            msgAreaTreeDTO.setAreaName(msgAreaPO.getAreaName());
            msgAreaTreeDTO.setParentId(msgAreaPO.getParentId());
            treeNodes.add(msgAreaTreeDTO);
        }
        return makeTree(treeNodes);
    }

    @Override
    public Map getSexList() {
        Map map = new HashMap();
        SexTypeEnum[] lists = SexTypeEnum.values();
        for (SexTypeEnum sexTypeEnum : lists) {
            map.put(sexTypeEnum.getKey(), sexTypeEnum.getValue());
        }
        return map;
    }

    @Override
    public Map getWorkList() {
        Map map = new HashMap();
        WorkTypeEnum[] lists = WorkTypeEnum.values();
        for (WorkTypeEnum workTypeEnum : lists) {
            map.put(workTypeEnum.getKey(), workTypeEnum.getValue());
        }
        return map;
    }

    @Override
    public Map getTravelList() {
        Map map = new HashMap();
        TravelTypeEnum[] lists = TravelTypeEnum.values();
        for (TravelTypeEnum travelTypeEnum : lists) {
            map.put(travelTypeEnum.getKey(), travelTypeEnum.getValue());
        }
        return map;
    }

    @Override
    public Map getActiveList() {
        Map map = new HashMap();
        ActiveTypeEnum[] lists = ActiveTypeEnum.values();
        for (ActiveTypeEnum activeTypeEnum : lists) {
            map.put(activeTypeEnum.getKey(), activeTypeEnum.getValue());
        }
        return map;
    }

    private List<MsgAreaTreeDTO> makeTree(List<MsgAreaTreeDTO> treeNodes) {
        List<MsgAreaTreeDTO> trees = new ArrayList<>();
        for (MsgAreaTreeDTO msgAreaTreeDTO : treeNodes) {
            if ("1".equals(String.valueOf(msgAreaTreeDTO.getParentId()))) {
                trees.add(findChildren(msgAreaTreeDTO, treeNodes));
            }
        }
        return trees;
    }

    private MsgAreaTreeDTO findChildren(MsgAreaTreeDTO DTO, List<MsgAreaTreeDTO> DTOList) {
        for (MsgAreaTreeDTO msgAreaTreeDTO : DTOList) {
            if (DTO.getId() == msgAreaTreeDTO.getParentId()) {
                if (DTO.getChildren() == null) {
                    DTO.setChildren(new ArrayList<>());
                }
                DTO.add(findChildren(msgAreaTreeDTO, DTOList));
            }
        }
        return DTO;
    }
}
