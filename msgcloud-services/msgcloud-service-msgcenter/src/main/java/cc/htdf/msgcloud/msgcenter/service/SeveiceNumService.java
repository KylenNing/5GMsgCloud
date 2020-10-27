package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.SeveiceNumDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author: renxh
 * @Date: 2020/8/12
 * @Description:
 */
public interface SeveiceNumService {

    MsgPageDTO getSeveiceNumListByPage(CMsgUserPO user, Integer pageNum, Integer pageSize);

    List<SeveiceNumDTO> getSeveiceNumList(CMsgUserPO user);

    void deleteById(String id);

    void updateseveiceNumById(CMsgUserPO user, SeveiceNumDTO dto);

    String addSeveiceNum(CMsgUserPO user, SeveiceNumDTO dto);

    MsgChatbot findByChannelId(Long channelId);

    MsgChatbot findByChatbotId(String chatbotId);

    String uploadMaterial(MultipartFile file) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

}
