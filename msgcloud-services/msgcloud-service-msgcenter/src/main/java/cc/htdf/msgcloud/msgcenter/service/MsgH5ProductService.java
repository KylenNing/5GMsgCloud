package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.*;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgH5ProductVO;
import io.minio.errors.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
public interface MsgH5ProductService {

    List<CMsgOrganTreeDTO> getOrganList(CMsgUserPO user);

    MsgH5ProductPageDTO getH5ProductListByPage(CMsgUserPO user, String title, Integer organId, Integer modular, Integer type, Integer status, String startTime, String endTime, Integer pageNum, Integer pageSize);

    String createH5Product(CMsgUserPO user, MsgH5ProductVO msgH5ProductVO);

    String updateH5Product(CMsgUserPO user, MsgH5ProductVO msgProductVO);

    String deleteH5ProductById(String productId);

    String offH5ProductById(String productId);

    MsgH5ProductDTO uploadH5Material(MultipartFile file) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

    MsgH5ProductDTO uploadH5MaterialRe(MultipartFile file) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

    MsgH5ProductDTO getH5ProductById(String productId);

    MsgH5ProductDTO getH5PhoneProductById(String productId);

    String examine(String productId, String reason);

    MsgH5ProductPageDTO getH5ProductList(Integer organId, String title, boolean isMain, Integer[] modular, String tag, Integer pageNum, Integer pageSize);

    MsgH5PhoneProductDTO getH5WeatherDescribe(Integer organId, Integer modular);

    MsgH5ProductPageDTO getH5Product(CMsgUserPO user, String title, Integer organId, Integer modular, Integer type, String startTime, String endTime, Integer pageNum, Integer pageSize);

    Map getChoiceList();

    String addBrowseCountProductById(String productId);

    List<MsgH5TagTreeDTO> getTagTree(Integer moduleId);

    List<MsgH5ProductDTO> getTourProduct(CMsgUserPO user);

    List<Map<String, Object>> getTagByModular(Integer modular);

    MsgH5PhoneRecommendDTO getH5MainList(Integer organId);

    List<Object> getH5ScienceMainList(String modularId, String type,
			Integer pageNum, Integer pageSize,String title);

}
