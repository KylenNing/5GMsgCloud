package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgMaterialLabelDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgMaterialPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgMaterialLabelVO;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface MsgMaterialService {

    List<MsgMaterialPageDTO> getMaterialList(CMsgUserPO user, String materialType, String materialName);

    MsgMaterialPageDTO getMaterialPage(CMsgUserPO user, String labelId, String materialType, String materialName, Integer pageNum, Integer pageSize);

    String createMaterialLabel(CMsgUserPO user, MsgMaterialLabelVO msgMaterialLabelVO);

    List<MsgMaterialLabelDTO> getMaterialLabel(CMsgUserPO user, String materialType);

    String uploadMaterial(CMsgUserPO user, MultipartFile file, String labelId) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

    String moveMaterial(CMsgUserPO user, String materialIds, String materialLabelId);

    String deleteMaterialByIds(String materialIds);

    String deleteMaterialLabelById(String materialLabelId);
}
