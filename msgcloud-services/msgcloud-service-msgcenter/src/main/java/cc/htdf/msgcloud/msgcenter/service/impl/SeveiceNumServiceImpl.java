package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.common.utils.StorageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.SeveiceNumDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.po.SeveiceNumPO;
import cc.htdf.msgcloud.msgcenter.mapper.SeveiceNumMapper;
import cc.htdf.msgcloud.msgcenter.service.SeveiceNumService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.io.Files;
import io.minio.errors.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SeveiceNumServiceImpl implements SeveiceNumService {
    @Resource
    private SeveiceNumMapper seveiceNumMapper;

    @Override
    public MsgPageDTO getSeveiceNumListByPage(CMsgUserPO user, Integer pageNum, Integer pageSize) {
        EntityWrapper<SeveiceNumPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        List<SeveiceNumPO> lst = seveiceNumMapper.selectList(wrapper);
        MsgPageDTO pageDTO = new MsgPageDTO();
        pageDTO.setTotolSize(lst.size());
        pageDTO.setData(PageUtil.startPage(lst, pageNum, pageSize));
        return pageDTO;
    }

    @Override
    public List<SeveiceNumDTO> getSeveiceNumList(CMsgUserPO user) {
        List<SeveiceNumDTO> resultlst = new ArrayList<>();
        EntityWrapper<SeveiceNumPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        List<SeveiceNumPO> lst = seveiceNumMapper.selectList(wrapper);
        for (int i = 0; i < lst.size(); i++) {
            SeveiceNumDTO dto = new SeveiceNumDTO();
            dto.setId(lst.get(i).getId());
            dto.setChannelId(lst.get(i).getChannelId());
            dto.setChatbotId(lst.get(i).getChatbotId());
            dto.setChatbotName(lst.get(i).getChatbotName());
            dto.setAppId(lst.get(i).getAppId());
            dto.setAppSecret(lst.get(i).getAppSecret());
            dto.setCspCode(lst.get(i).getCspCode());
            dto.setLogoUrl(lst.get(i).getLogoUrl());
            dto.setCreatedOrg(lst.get(i).getCreatedOrg());
            dto.setCreatedBy(lst.get(i).getCreatedBy());
            dto.setCreatedTime(lst.get(i).getCreatedTime());
            dto.setUpdatedBy(lst.get(i).getUpdatedBy());
            dto.setUpdatedTime(lst.get(i).getUpdatedTime());
            resultlst.add(dto);
        }
        return resultlst;
    }

    @Override
    public void deleteById(String id) {
        seveiceNumMapper.deleteById(id);
    }

    @Override
    public void updateseveiceNumById(CMsgUserPO user, SeveiceNumDTO dto) {
        SeveiceNumPO po = new SeveiceNumPO();
        po.setId(dto.getId());
        po.setAppId(dto.getAppId());
        po.setAppSecret(dto.getAppSecret());
        po.setCspCode(dto.getCspCode());
        po.setLogoUrl(dto.getLogoUrl());
        po.setChannelId(dto.getChannelId());
        po.setChatbotId(dto.getChatbotId());
        po.setChatbotName(dto.getChatbotName());
        po.setCreatedBy(dto.getCreatedBy());
        po.setCreatedOrg(dto.getCreatedOrg());
        po.setCreatedTime(dto.getCreatedTime());
        po.setUpdatedBy(String.valueOf(user.getId()));
        po.setUpdatedTime(new Date());
        seveiceNumMapper.updateseveiceNumById(po);
    }

    @Override
    public String addSeveiceNum(CMsgUserPO user, SeveiceNumDTO dto) {
        SeveiceNumPO po = new SeveiceNumPO();
        po.setAppId(dto.getAppId());
        po.setAppSecret(dto.getAppSecret());
        po.setCspCode(dto.getCspCode());
        po.setLogoUrl(dto.getLogoUrl());
        po.setChannelId(dto.getChannelId());
        po.setChatbotId(dto.getChatbotId());
        po.setChatbotName(dto.getChatbotName());
        po.setCreatedBy(String.valueOf(user.getId()));
        po.setCreatedOrg(String.valueOf(user.getOrganId()));
        po.setCreatedTime(new Date());
        try {
            seveiceNumMapper.insertSeveiceNum(po);
            return "成功插入服务号！";
        } catch (Exception e) {
            e.printStackTrace();
            return "接入号或者ChannelId已存在，请更换信息再新建！";
        }
    }

    @Override
    public MsgChatbot findByChannelId(Long channelId) {
        SeveiceNumPO seveiceNumPO = seveiceNumMapper.findByChannleId(channelId);
        MsgChatbot chatbot = new MsgChatbot();
        BeanUtils.copyProperties(seveiceNumPO, chatbot);
        chatbot.setServiceId(seveiceNumPO.getId());
        return chatbot;
    }

    @Override
    public MsgChatbot findByChatbotId(String chatbotId) {
        SeveiceNumPO seveiceNumPO = seveiceNumMapper.findByChatbotId(chatbotId);
        MsgChatbot msgChatbot = new MsgChatbot();
        BeanUtils.copyProperties(seveiceNumPO, msgChatbot);
        msgChatbot.setServiceId(seveiceNumPO.getId());
        return msgChatbot;
    }

    @Override
    public String uploadMaterial(MultipartFile file) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        String objectName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        //上传文件至本地Minio
        String extName = Files.getFileExtension(objectName);
        String contentType = StorageUtil.mediaType(extName);
        String fileName = StorageUtil.uploadFile("logo", inputStream, extName, contentType);
        return "logo" + "/" + fileName;
    }
}
