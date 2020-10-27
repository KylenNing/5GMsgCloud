package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.constants.MsgType;
import cc.htdf.msgcloud.common.constants.QueueName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.common.domain.MsgDownInfo;
import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductSendDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductSendPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MsgProductSendService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.dto.MsgBody;
import com.feinno.msgctenter.sdk.dto.MsgInfo;
import com.feinno.msgctenter.sdk.dto.Receiver;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Service
public class MsgProductSendServiceImpl implements MsgProductSendService {

    @Resource
    private MsgAreaMapper msgAreaMapper;

    @Resource
    private SeveiceNumMapper seveiceNumMapper;

    @Resource
    private MsgProductPictureMapper msgProductPictureMapper;

    @Resource
    private MsgProductSendMapper msgProductSendMapper;

    @Resource
    private MsgUserLabelMapper msgUserLabelMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private MsgUserMapper msgUserMapper;

    @Resource
    private MsgUserToLabelMapper msgUserToLabelMapper;

    @Override
    public MsgProductSendPageDTO getProductSendList(CMsgUserPO user, String productName, String serviceId, Integer msgStatus, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgProductSendPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        if (Objects.nonNull(productName) && !productName.equals("")) {
            wrapper.like("PRODUCT_TITLE", productName);
        }
        if (Objects.nonNull(msgStatus)) {
            wrapper.eq("MESSAGE_STATUS", msgStatus);
        }
        if (Objects.nonNull(serviceId) && !serviceId.equals("")) {
            wrapper.eq("SERVICE_ID", serviceId);
        }
        if (Objects.nonNull(startTime) && !startTime.equals("")) {
            wrapper.between("CREATED_TIME", startTime, endTime);
        }
        wrapper.orderBy("CREATED_TIME", false);
        List<MsgProductSendPO> lists = msgProductSendMapper.selectList(wrapper);
        List<MsgProductSendDTO> data = new ArrayList<>();
        for (MsgProductSendPO msgProductSendPO : lists) {
            MsgProductSendDTO msgProductSendDTO = new MsgProductSendDTO();
            msgProductSendDTO.setId(msgProductSendPO.getId());
            msgProductSendDTO.setProductTitle(msgProductSendPO.getProductTitle());
            // 图片地址
            String pictureLocalUrl = msgProductPictureMapper.selectById(msgProductSendPO.getPictureId()).getPictureLocalUrl();
            msgProductSendDTO.setPictureLocalUrl(pictureLocalUrl);
            // 区域名称
            MsgAreaPO msgAreaPO = msgAreaMapper.selectById(msgProductSendPO.getProductSendArea());
            String areaName;
            if (msgAreaPO.getLevel() == 2) {
                areaName = msgAreaPO.getAreaName();
            } else {
                MsgAreaPO PO = msgAreaMapper.selectById(msgAreaPO.getParentId());
                areaName = PO.getAreaName() + "_" + msgAreaPO.getAreaName();
            }
            msgProductSendDTO.setProductSendAreaName(areaName);
            // 服务号，用户组
            makeUserLabeAndSeveiceNum(msgProductSendDTO, msgProductSendPO.getServiceId(), msgProductSendPO.getUserLabelId());
            msgProductSendDTO.setMessageStatus(msgProductSendPO.getMessageStatus());
            msgProductSendDTO.setCreatedBy(msgProductSendPO.getCreatedBy());
            msgProductSendDTO.setCreatedTime(msgProductSendPO.getCreatedTime());
            data.add(msgProductSendDTO);
        }
        MsgProductSendPageDTO pageDTO = new MsgProductSendPageDTO();
        pageDTO.setTotolSize(data.size());
        pageDTO.setData(PageUtil.startPage(data, pageNum, pageSize));
        return pageDTO;
    }

    private void makeUserLabeAndSeveiceNum(MsgProductSendDTO msgProductSendDTO, Integer serviceId, String userLabelId) {
        // 服务号
        SeveiceNumPO seveiceNumPO = seveiceNumMapper.selectById(serviceId);
        if (seveiceNumPO != null) {
            msgProductSendDTO.setServiceName(seveiceNumPO.getChatbotName());
        } else {
            msgProductSendDTO.setServiceName("暂无服务号");
        }
        // 用户组
        if (Objects.nonNull(userLabelId) && !userLabelId.equals("")) {
            if (userLabelId.equals("all")) {
                msgProductSendDTO.setUserLabelName("全部用户组");
            } else {
                List<String> labelList = Arrays.asList(userLabelId.split(","));
                StringBuilder userLabelName = new StringBuilder();
                for (String userLabelIds : labelList) {
                    MsgUserLabelPO msgUserLabelPO = msgUserLabelMapper.selectById(userLabelIds);
                    userLabelName.append(msgUserLabelPO.getLabelName());
                    userLabelName.append(" ");
                }
                msgProductSendDTO.setUserLabelName(userLabelName.toString());
            }
        } else {
            msgProductSendDTO.setUserLabelName("暂无用户组");
        }
    }

    @Override
    public MsgProductSendDTO getProductSendById(String productId) {
        MsgProductSendDTO msgProductSendDTO = new MsgProductSendDTO();
        MsgProductSendPO msgProductSendPO = msgProductSendMapper.selectById(productId);
        msgProductSendDTO.setProductTitle(msgProductSendPO.getProductTitle());
        msgProductSendDTO.setProductDescribe(msgProductSendPO.getProductDescribe());
        MsgProductPicturePO msgProductPicturePO = msgProductPictureMapper.selectById(msgProductSendPO.getPictureId());
        msgProductSendDTO.setPictureName(msgProductPicturePO.getPictureName());
        msgProductSendDTO.setPictureLocalUrl(msgProductPicturePO.getPictureLocalUrl());
        String productSendAreaName = msgAreaMapper.selectById(msgProductSendPO.getProductSendArea()).getAreaName();
        msgProductSendDTO.setProductSendAreaName(productSendAreaName);
        int messageStatus = msgProductSendPO.getMessageStatus();
        if (messageStatus == -1) {
            msgProductSendDTO.setReason(msgProductSendPO.getReason());
        }
        msgProductSendDTO.setMessageStatus(messageStatus);
        // 服务号，用户组
        makeUserLabeAndSeveiceNum(msgProductSendDTO, msgProductSendPO.getServiceId(), msgProductSendPO.getUserLabelId());
        return msgProductSendDTO;
    }

    @Override
    public String sendProduct(CMsgUserPO user, String productId, String serviceId, boolean isAll, String userLabelId) {
        Msg msg = new Msg();
        SeveiceNumPO seveiceNumPO = seveiceNumMapper.selectById(serviceId);
        msg.setChannelBusiness(seveiceNumPO.getCspCode());
        msg.setOperation("msgdown");
        msg.setType(MsgType.IMMEDIATE_DOWN);
        msg.setStatus(1);
        MsgChatbot msgChatbot = new MsgChatbot();
        msgChatbot.setChatbotId(seveiceNumPO.getChatbotId());
        msg.setMsgChatbot(msgChatbot);
        Long channelId = Long.valueOf(seveiceNumPO.getChannelId());

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setChannelId(channelId);
        msgInfo.setChannelType(2);

        Receiver receiver = new Receiver();
        receiver.setUserIdType(1);
        List<String> userPhoneList = new ArrayList<>();
        if (isAll) {
            msgInfo.setSendToAll(1);
            EntityWrapper<MsgUserPO> wrapper = new EntityWrapper();
            wrapper.in("created_org", user.getOrgs());
            userPhoneList = msgUserMapper.selectList(wrapper).stream().map(MsgUserPO::getUserTel)
                    .collect(Collectors.toList());
        } else {
            List<String> labelList = Arrays.asList(userLabelId.split(","));
            List<MsgUserPO> userPOList = getUserByLabelList(labelList);
            for (MsgUserPO po : userPOList) {
                userPhoneList.add(po.getUserTel());
            }
            msgInfo.setSendToAll(0);
        }
        receiver.setToUserList(userPhoneList);
        msgInfo.setReceiver(receiver);

        MsgBody msgBody = new MsgBody();
        Media media = new Media();
        MsgProductSendPO msgProductSendPO = msgProductSendMapper.selectById(productId);
        MsgProductPicturePO msgProductPicturePO = msgProductPictureMapper.selectById(msgProductSendPO.getPictureId());
        // 素材id，调用文件上传接口时返回的素材id
        media.setId(msgProductPicturePO.getPictureWebId());
        // 文件下载路径
        media.setUrl(msgProductPicturePO.getPictureWebUrl());
        // 缩略图url
        media.setThumbUrl(msgProductPicturePO.getPictureWebSlUrl());
        // 文件名称
        String pictureName = UUID.randomUUID().toString().replace("-", "");
        media.setName(pictureName + ".png");
        // 文件大小
        media.setSize(msgProductPicturePO.getPictureSize());
        // 文件类型
        media.setType(4);
        msgBody.setImage(media);
        msgInfo.setMsgType(4);
        msgInfo.setMsg(msgBody);

        MsgDownInfo msgDownInfo = new MsgDownInfo();
        msgDownInfo.setMessageId(Integer.valueOf(productId));
        msg.setMsgDownInfo(msgDownInfo);
        msg.setMsgInfo(msgInfo);
        rocketMQTemplate.convertAndSend(QueueName.MSGCLOUD_MESSAGE_IFNO, msg);
        // 更新产品发送表
        msgProductSendPO.setUpdatedBy(String.valueOf(user.getId()));
        msgProductSendPO.setUpdatedTime(new Date());
        msgProductSendPO.setServiceId(Integer.valueOf(serviceId));
        if (!isAll) {
            msgProductSendPO.setUserLabelId(userLabelId);
        } else {
            msgProductSendPO.setUserLabelId("all");
        }
        msgProductSendMapper.updateById(msgProductSendPO);
        return "发送成功";
    }

    @Override
    public String examine(String productId, String reason) {
        if (Objects.nonNull(reason) && !reason.equals("")) {
            msgProductSendMapper.examineNg(productId, reason);
        } else {
            msgProductSendMapper.examine(productId);
        }
        return "审核成功";
    }

    private List<MsgUserPO> getUserByLabelList(List<String> labelList) {
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

}
