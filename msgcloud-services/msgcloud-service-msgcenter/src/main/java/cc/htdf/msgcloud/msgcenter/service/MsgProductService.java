package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgAreaTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgProductVO;
import io.minio.errors.*;

import java.awt.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
public interface MsgProductService {

    MsgProductPageDTO getProductList(CMsgUserPO user, String productName, Integer msgStatus, Integer pageNum, Integer pageSize);

    List<MsgAreaTreeDTO> getAreaList(CMsgUserPO user);

    String createProduct(CMsgUserPO user, MsgProductVO msgProductVO);

    String updateProduct(CMsgUserPO user, MsgProductVO msgProductVO);

    String deleteProductById(String productId);

    MsgProductDTO getProductById(String productId);

    String makeProduct(CMsgUserPO user, String productId) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException, AWTException, InterruptedException;
}
