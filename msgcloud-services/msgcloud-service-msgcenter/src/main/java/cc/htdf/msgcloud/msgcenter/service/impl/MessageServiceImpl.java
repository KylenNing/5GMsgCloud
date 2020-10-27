package cc.htdf.msgcloud.msgcenter.service.impl;


import cc.htdf.msgcloud.common.constants.MsgType;
import cc.htdf.msgcloud.common.constants.QueueName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgDownInfo;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.dto.*;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.AllMessageVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgMessageVO;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MessageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.feinno.msgctenter.sdk.dto.Receiver;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/8/14
 * @Description: TODO
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {


    @Resource
    private MsgUserMapper msgUserMapper;

    @Resource
    private BMessageMapper bMsgMessageMapper;

    @Resource
    private BMessageTemplateMapper bMessageTemplateMapper;

    @Resource
    private BMessageUserMapper bMessageUserMapper;

    @Resource
    private BMsgMenuMapper bMsgMenuMapper;

    @Resource
    private BMessageMenuMapper bMessageMenuMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private MsgUserToLabelMapper msgUserToLabelMapper;

    @Resource
    private SeveiceNumMapper seveiceNumMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Resource
    private BGroupTemplateMapper bGroupTemplateMapper;

    @Resource
    private BGroupMenuMapper bGroupMenuMapper;

    @Resource
    private MsgMaterialMapper msgMaterialMapper;

    @Resource
    private MsgTemplateToButtonMapper msgTemplateToButtonMapper;

    @Resource
    private MsgTemplateButtonMapper msgTemplateButtonMapper;

    @Override
    public void insertNewMessageToDatabase(CMsgUserPO user, MsgMessageVO messageVO) {
        BMessagePO bMessagePO = new BMessagePO();
        int createdBy = user.getId();
        int createdOrg = user.getOrganId();
        bMessagePO.setSendToAll(messageVO.getSendToAll());
        bMessagePO.setServiceId(messageVO.getServiceId());
        bMessagePO.setMessageStatus(1);
        bMessagePO.setMessageType(MsgType.IMMEDIATE_DOWN);
        bMessagePO.setCreatedBy(createdBy);
        bMessagePO.setCreatedTime(new Date());
        bMessagePO.setCreatedOrg(createdOrg);
        bMessagePO.setGroupId(messageVO.getGroupId());
        bMsgMessageMapper.insert(bMessagePO);
        int msgId = bMessagePO.getId();
        List<String> userIdList = new ArrayList<>();
        EntityWrapper<MsgUserPO> wrapper = new EntityWrapper();
        wrapper.in("created_org", user.getOrgs());
        if (messageVO.getSendToAll() == 1) {
            userIdList = msgUserMapper.selectList(wrapper).stream().map(MsgUserPO::getId)
                    .collect(Collectors.toList());
        } else {
            List<MsgUserPO> userPOList = getUserByLabelList(messageVO.getLabelList());
            for (MsgUserPO po : userPOList) {
                userIdList.add(po.getId());
            }
        }
        for (String userId : userIdList) {
            BMessageUserPO bMessageUserPO = new BMessageUserPO();
            bMessageUserPO.setUserId(userId);
            bMessageUserPO.setMessageId(msgId);
            bMessageUserPO.setCreatedBy(createdBy);
            bMessageUserPO.setCreatedTime(new Date());
            bMessageUserPO.setCreatedOrg(createdOrg);
            bMessageUserMapper.insert(bMessageUserPO);
        }
        if (Objects.isNull(messageVO.getGroupId()) || messageVO.getGroupId() == 0) {
            for (Map<String, String> template : messageVO.getTemplateList()) {
                BMessageTemplatePO bMessageTemplatePO = new BMessageTemplatePO();
                bMessageTemplatePO.setTemplateId(template.get("templateId"));
                bMessageTemplatePO.setTemplateSort(Integer.valueOf(template.get("templateSort")));
                bMessageTemplatePO.setMessageId(msgId);
                bMessageTemplatePO.setCreatedBy(createdBy);
                bMessageTemplatePO.setCreatedOrg(createdOrg);
                bMessageTemplatePO.setCreatedTime(new Date());
                bMessageTemplateMapper.insert(bMessageTemplatePO);
            }
            for (MsgMenuDTO menu : messageVO.getMenuList()) {
                BMsgMenuPO msgMenuPO = new BMsgMenuPO();
                msgMenuPO.setMenuName(menu.getMenuName());
                msgMenuPO.setMenuType(menu.getMenuType());
                msgMenuPO.setMenuContent(menu.getMenuContent());
                msgMenuPO.setCreatedBy(createdBy);
                msgMenuPO.setCreatedTime(new Date());
                msgMenuPO.setCreatedOrg(createdOrg);
                bMsgMenuMapper.insert(msgMenuPO);
                int menuId = msgMenuPO.getId();
                BMessageMenuPO bMessageMenuPO = new BMessageMenuPO();
                bMessageMenuPO.setMessageId(msgId);
                bMessageMenuPO.setMenuId(menuId);
                bMessageMenuPO.setMenuSort(menu.getMenuSort());
                bMessageMenuPO.setCreatedBy(createdBy);
                bMessageMenuPO.setCreatedTime(new Date());
                bMessageMenuPO.setCreatedOrg(createdOrg);
                bMessageMenuMapper.insert(bMessageMenuPO);
            }
        } else {
            List<Map<String, String>> templateList = new ArrayList<>();
            List<String> tempIdList = bGroupTemplateMapper.getTemplateIdByGroupId(messageVO.getGroupId());
            for (String id : tempIdList) {
                Map<String, String> templateMap = new HashMap<>();
                templateMap.put("templateId", id);
                templateList.add(templateMap);
            }
            messageVO.setTemplateList(templateList);
            List<MsgMenuDTO> menuDTOList = new ArrayList<>();
            List<Integer> menuIdList = bGroupMenuMapper.selectMenuIdByGroupId(messageVO.getGroupId());
            EntityWrapper<BMsgMenuPO> entityWrapper = new EntityWrapper<>();

            entityWrapper.in("id", menuIdList);
            List<BMsgMenuPO> menuList = new ArrayList<>();
            if (menuIdList.size() != 0) {
                menuList = bMsgMenuMapper.selectList(entityWrapper);
            }
            for (BMsgMenuPO menu : menuList) {
                MsgMenuDTO dto = new MsgMenuDTO();
                BeanUtils.copyProperties(menu, dto);
                dto.setMenuSort(bGroupMenuMapper.selectMenuSortByGroupAndMenu(messageVO.getGroupId(), menu.getId()));
                menuDTOList.add(dto);
            }
            messageVO.setMenuList(menuDTOList);
        }
        sendNewMessageToQueue(user, messageVO, msgId);

    }

    @Override
    public void sendNewMessageToQueue(CMsgUserPO user, MsgMessageVO messageVO, Integer messageId) {
        Msg msg = new Msg();
        SeveiceNumPO seveiceNumPO = seveiceNumMapper.selectById(messageVO.getServiceId());
        msg.setChannelBusiness(seveiceNumPO.getCspCode());
        msg.setOperation("msgdown");
        msg.setType(MsgType.IMMEDIATE_DOWN);
        msg.setStatus(1);
        MsgDownInfo msgDownInfo = new MsgDownInfo();
        msgDownInfo.setMessageId(messageId);
        LinkedList<String> templateList = new LinkedList<>();
        for (Map<String, String> map : messageVO.getTemplateList()) {
            templateList.add(map.get("templateId"));
        }
        LinkedList<Map<String, Object>> menuList = new LinkedList<>();
        for (MsgMenuDTO menu : messageVO.getMenuList()) {
            Map<String, Object> menuMap = new HashMap<>();
            menuMap.put("menuType", menu.getMenuType());
            menuMap.put("menuName", menu.getMenuName());
            menuMap.put("menuContent", menu.getMenuContent());
            menuMap.put("menuSort", menu.getMenuSort());
            menuList.add(menuMap);
        }
        msgDownInfo.setServiceId(messageVO.getServiceId());
        msgDownInfo.setTemplateList(templateList);
        msgDownInfo.setSendToAll(messageVO.getSendToAll());
        msgDownInfo.setMenuList(menuList);
        Receiver receiver = new Receiver();
        receiver.setUserIdType(1);
        List<String> userPhoneList = new ArrayList<>();
        EntityWrapper<MsgUserPO> wrapper = new EntityWrapper();
        wrapper.in("created_org", user.getOrgs());
        if (messageVO.getSendToAll() == 1) {
            userPhoneList = msgUserMapper.selectList(wrapper).stream().map(MsgUserPO::getUserTel)
                    .collect(Collectors.toList());
        } else {
            List<MsgUserPO> userPOList = getUserByLabelList(messageVO.getLabelList());
            for (MsgUserPO po : userPOList) {
                userPhoneList.add(po.getUserTel());
            }

        }
        receiver.setToUserList(userPhoneList);
        msgDownInfo.setReceiver(receiver);
        msg.setMsgDownInfo(msgDownInfo);
        rocketMQTemplate.convertAndSend(QueueName.MSGCLOUD_MESSAGE, msg);
    }

    @Override
    public Map getAllMessage(@LoginUser CMsgUserPO user, Integer msgStatus, String serviceId, Integer currentPage, Integer pageSize, String startTime, String endTime) throws ParseException {
        Map resMap = new HashMap();
        EntityWrapper<SeveiceNumPO> serviceNumWrapper = new EntityWrapper();
        List<SeveiceNumPO> serviceNumList = seveiceNumMapper.selectList(serviceNumWrapper);
        Map<String, SeveiceNumPO> serviceNumMap = serviceNumList.stream().
                collect(Collectors.toMap(SeveiceNumPO::getId, data -> data));
        EntityWrapper<BMessagePO> wrapper = new EntityWrapper();
        wrapper.in("created_org", user.getOrgs());
        if (Objects.nonNull(serviceId) && !serviceId.equals("")) {
            wrapper.eq("service_id", serviceId);
        }
        if (Objects.nonNull(msgStatus)) {
            wrapper.eq("message_status", msgStatus);
        }
        wrapper.orderBy("created_time", false);
        if (Objects.nonNull(startTime) && !startTime.equals("")) {
            wrapper.between("CREATED_TIME", startTime, endTime);
        }
        int totalSize = bMsgMessageMapper.selectCount(wrapper);
        if (Objects.isNull(currentPage) && Objects.isNull(pageSize)) {
            List<BMessagePO> dataList = bMsgMessageMapper.selectList(wrapper);
            dataList = replaceServiceIdToName(dataList, serviceNumMap);
            List<AllMessageVO> resList = addCreatedUserNameToMsgList(dataList, user);
            resMap.put("DATA", resList);
        } else {
            Page<BMessagePO> page = new Page<>(currentPage, pageSize);
            List<BMessagePO> pageList = bMsgMessageMapper.selectPage(page, wrapper);
            pageList = replaceServiceIdToName(pageList, serviceNumMap);
            List<AllMessageVO> resList = addCreatedUserNameToMsgList(pageList, user);
            resMap.put("DATA", resList);
        }
        resMap.put("totalSize", totalSize);
        return resMap;
    }

    @Override
    public MessagePageDTO getMessageRelatedInfoById(Integer msgId) {
        MessagePageDTO dto = new MessagePageDTO();
        BMessagePO msg = bMsgMessageMapper.getMsgById(msgId);
        dto.setMessage(msg);
        Integer groupId = msg.getGroupId();
        if (Objects.isNull(groupId) || groupId == 0) {
//            List<BMessageTemplatePO> messageTemplateList = bMessageTemplateMapper.getTemplateByMsgId(msgId);
            List<MsgTemplatePO> templateList = msgTemplateMapper.getTemplateByMsgId(msgId);
            List<MsgTemplateDTO> templateDTOList = makeTemplateList(templateList);
            List<BMessageMenuPO> messageMenuPOList = bMessageMenuMapper.getMenuByMsgId(msgId);
            List<MsgMenuDTO> menuDTOList = new ArrayList<>();
            for (BMessageMenuPO messageMenu : messageMenuPOList) {
                MsgMenuDTO msgMenuDto = new MsgMenuDTO();
                BMsgMenuPO bMsgMenuPO = bMsgMenuMapper.selectMenuById(messageMenu.getMenuId());
                msgMenuDto.setMenuName(bMsgMenuPO.getMenuName());
                msgMenuDto.setMenuType(bMsgMenuPO.getMenuType());
                msgMenuDto.setMenuContent(bMsgMenuPO.getMenuContent());
                msgMenuDto.setMenuSort(messageMenu.getMenuSort());
                msgMenuDto.setCreatedOrg(bMsgMenuPO.getCreatedOrg());
                msgMenuDto.setCreatedBy(bMsgMenuPO.getCreatedBy());
                msgMenuDto.setCreatedTime(bMsgMenuPO.getCreatedTime());
                msgMenuDto.setUpdatedBy(bMsgMenuPO.getUpdatedBy());
                msgMenuDto.setUpdatedTime(bMsgMenuPO.getUpdatedTime());
                menuDTOList.add(msgMenuDto);
            }
            if (templateList != null && !templateList.isEmpty()) {
                String templateId = templateList.get(0).getId();
                String templateType = msgTemplateMapper.selectById(templateId).getTemplateType();
                dto.setTemplateType(templateType);
            }
            dto.setTemplateList(templateDTOList);
            dto.setMenuList(menuDTOList);
        } else {
//            List<String> templateIdList = bGroupTemplateMapper.getTemplateIdByGroupId(groupId);
            List<MsgTemplatePO> templateIdList = msgTemplateMapper.getTemplateIdByGroupId(groupId);
            List<MsgTemplateDTO> templateDTOList = makeTemplateList(templateIdList);
            dto.setTemplateList(templateDTOList);
            List<MsgMenuDTO> menuDTOList = new ArrayList<>();
            List<Integer> menuIdList = bGroupMenuMapper.selectMenuIdByGroupId(groupId);
            EntityWrapper<BMsgMenuPO> entityWrapper = new EntityWrapper<>();
            entityWrapper.in("id", menuIdList);
            List<BMsgMenuPO> menuList = new ArrayList<>();
            if (menuIdList.size() != 0) {
                menuList = bMsgMenuMapper.selectList(entityWrapper);
            }
            for (BMsgMenuPO menu : menuList) {
                MsgMenuDTO msgMenuDTO = new MsgMenuDTO();
                BeanUtils.copyProperties(menu, msgMenuDTO);
                msgMenuDTO.setMenuSort(bGroupMenuMapper.selectMenuSortByGroupAndMenu(groupId, menu.getId()));
                menuDTOList.add(msgMenuDTO);
            }
            dto.setMenuList(menuDTOList);
        }
        List<String> lableList = new ArrayList<>();
        List<String> userIdList = bMessageUserMapper.getUsersByMsgId(msgId);
        for (String userId : userIdList) {
            String labelId = msgUserToLabelMapper.getLabelByUserId(userId).getLabelId();
            if (!lableList.contains(labelId)) {
                lableList.add(labelId);
            }
        }
        dto.setLableList(lableList);
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

    List<MsgUserPO> getUserByLabelList(List<String> labelList) {
        List<String> userIdList = new ArrayList<>();
        for (String labelId : labelList) {
            userIdList.addAll(msgUserToLabelMapper.getUserIdListByLabelId(labelId));
        }
        List<MsgUserPO> userList = new ArrayList<>();
        for (String userId : userIdList) {
            userList.add(msgUserMapper.getUserById(userId));
        }
        return userList;
    }

    List<BMessagePO> replaceServiceIdToName(List<BMessagePO> msgList, Map<String, SeveiceNumPO> numMap) {
        for (BMessagePO po : msgList) {
            try {
                po.setServiceId(numMap.get(po.getServiceId()).getChatbotName());
            } catch (Exception e) {
                log.info("该条消息无对应服务！");
            }
        }
        return msgList;
    }

    List<AllMessageVO> addCreatedUserNameToMsgList(List<BMessagePO> msgList, CMsgUserPO user) {
        EntityWrapper<CMsgUserPO> wrapper = new EntityWrapper<>();
        Map<Integer, CMsgUserPO> msgMap = cMsgUserMapper.selectList(wrapper).
                stream().collect(Collectors.toMap(CMsgUserPO::getId, data -> data));
        List<AllMessageVO> resList = new ArrayList<>();
        for (BMessagePO msg : msgList) {
            AllMessageVO allMessageVO = new AllMessageVO();
            BeanUtils.copyProperties(msg, allMessageVO);
            allMessageVO.setCreatedUserName(msgMap.get(msg.getCreatedBy()).getUserName());
            resList.add(allMessageVO);
        }
        return resList;
    }
}