package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.utils.HtmlToImageUtils;
import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.common.utils.StorageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgAreaTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgProductPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgProductVO;
import cc.htdf.msgcloud.msgcenter.mapper.MsgAreaMapper;
import cc.htdf.msgcloud.msgcenter.mapper.MsgProductMapper;
import cc.htdf.msgcloud.msgcenter.mapper.MsgProductPictureMapper;
import cc.htdf.msgcloud.msgcenter.mapper.MsgProductSendMapper;
import cc.htdf.msgcloud.msgcenter.service.MsgProductService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Service
public class MsgProductServiceImpl implements MsgProductService {

    @Value("${path.chromedriver}")
    private String chromedriverPath;

    @Resource
    private MsgProductMapper msgProductMapper;

    @Resource
    private MsgAreaMapper msgAreaMapper;

    @Resource
    private MsgProductSendMapper msgProductSendMapper;

    @Resource
    private MsgProductPictureMapper msgProductPictureMapper;

    @Override
    public MsgProductPageDTO getProductList(CMsgUserPO user, String productName, Integer msgStatus, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgProductPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(productName) && !productName.equals("")) {
            wrapper.like("PRODUCT_TITLE", productName);
        }
        if (Objects.nonNull(msgStatus)) {
            wrapper.eq("MESSAGE_STATUS", msgStatus);
        }
        List<MsgProductPO> lists = msgProductMapper.selectList(wrapper);
        List<MsgProductDTO> data = new ArrayList<>();
        for (MsgProductPO msgProductPO : lists) {
            MsgProductDTO msgProductDTO = new MsgProductDTO();
            msgProductDTO.setId(msgProductPO.getId());
            msgProductDTO.setProductTitle(msgProductPO.getProductTitle());
            msgProductDTO.setProductStyle(msgProductPO.getProductStyle());
            msgProductDTO.setProductArea(msgProductPO.getProductArea());
            // 区域名称
            MsgAreaPO msgAreaPO = msgAreaMapper.selectById(msgProductPO.getProductArea());
            String areaName;
            if (msgAreaPO.getLevel() == 2) {
                areaName = msgAreaPO.getAreaName();
            } else {
                MsgAreaPO PO = msgAreaMapper.selectById(msgAreaPO.getParentId());
                areaName = PO.getAreaName() + "_" + msgAreaPO.getAreaName();
            }
            msgProductDTO.setProductAreaName(areaName);
            msgProductDTO.setProductDescribe(msgProductPO.getProductDescribe());
            msgProductDTO.setMessageStatus(msgProductPO.getMessageStatus());
            msgProductDTO.setCreatedOrg(msgProductPO.getCreatedOrg());
            msgProductDTO.setCreatedBy(msgProductPO.getCreatedBy());
            msgProductDTO.setCreatedTime(msgProductPO.getCreatedTime());
            data.add(msgProductDTO);
        }
        MsgProductPageDTO pageDTO = new MsgProductPageDTO();
        pageDTO.setTotolSize(data.size());
        pageDTO.setData(PageUtil.startPage(data, pageNum, pageSize));
        return pageDTO;
    }

    @Override
    public List<MsgAreaTreeDTO> getAreaList(CMsgUserPO user) {
        List<MsgAreaPO> areaPOList = msgAreaMapper.getAreaList();
        List<MsgAreaTreeDTO> treeNodes = new ArrayList<>();
        MsgAreaTreeDTO msgAreaTreeDTO;
        for (MsgAreaPO msgAreaPO : areaPOList) {
            msgAreaTreeDTO = new MsgAreaTreeDTO();
            msgAreaTreeDTO.setId(msgAreaPO.getId());
            msgAreaTreeDTO.setAreaName(msgAreaPO.getAreaName());
            msgAreaTreeDTO.setParentId(msgAreaPO.getParentId());
            if (msgAreaPO.getLevel() == 1) {
                msgAreaTreeDTO.setDisabled(true);
            } else {
                msgAreaTreeDTO.setDisabled(false);
            }
            treeNodes.add(msgAreaTreeDTO);
        }
        return makeTree(treeNodes, String.valueOf(user.getOrganArea()));
    }

    @Override
    public String createProduct(CMsgUserPO user, MsgProductVO msgProductVO) {
        MsgProductPO msgProductPO = new MsgProductPO();
        msgProductPO.setMessageStatus(1);
        msgProductPO.setCreatedOrg(String.valueOf(user.getOrganId()));
        msgProductPO.setCreatedBy(String.valueOf(user.getId()));
        msgProductPO.setCreatedTime(new Date());
        msgProductMapper.insert(makeMsgProductPO(msgProductPO, msgProductVO));
        return "创建成功";
    }

    @Override
    public String updateProduct(CMsgUserPO user, MsgProductVO msgProductVO) {
        MsgProductPO msgProductPO = new MsgProductPO();
        msgProductPO.setId(msgProductVO.getId());
        msgProductPO.setUpdatedBy(String.valueOf(user.getId()));
        msgProductPO.setUpdatedTime(new Date());
        msgProductPO.setCreatedOrg(msgProductVO.getCreatedOrg());
        msgProductPO.setCreatedBy(msgProductVO.getCreatedBy());
        msgProductPO.setCreatedTime(msgProductVO.getCreatedTime());
        msgProductMapper.updateById(makeMsgProductPO(msgProductPO, msgProductVO));
        return "更新成功";
    }

    @Override
    public String deleteProductById(String productId) {
        msgProductMapper.deleteById(Integer.valueOf(productId));
        return "删除成功";
    }

    @Override
    public MsgProductDTO getProductById(String productId) {
        MsgProductDTO msgProductDTO = new MsgProductDTO();
        MsgProductPO msgProductPO = msgProductMapper.selectById(productId);
        if (msgProductPO != null) {
            msgProductDTO.setId(msgProductPO.getId());
            msgProductDTO.setProductTitle(msgProductPO.getProductTitle());
            msgProductDTO.setProductStyle(msgProductPO.getProductStyle());
            msgProductDTO.setProductArea(msgProductPO.getProductArea());
            msgProductDTO.setProductDescribe(msgProductPO.getProductDescribe());
            msgProductDTO.setMessageStatus(msgProductPO.getMessageStatus());
            msgProductDTO.setCreatedOrg(msgProductPO.getCreatedOrg());
            msgProductDTO.setCreatedBy(msgProductPO.getCreatedBy());
            msgProductDTO.setCreatedTime(msgProductPO.getCreatedTime());
            msgProductDTO.setUpdatedBy(msgProductPO.getUpdatedBy());
            msgProductDTO.setUpdatedTime(msgProductPO.getUpdatedTime());
        }
        return msgProductDTO;
    }

    @Override
    public String makeProduct(CMsgUserPO user, String productId) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException, AWTException, InterruptedException {
        MsgProductPO msgProductPO = msgProductMapper.selectById(productId);
        MsgAreaPO msgAreaPO = msgAreaMapper.selectById(msgProductPO.getProductArea());
//        if (msgAreaPO.getLevel() == 2) {// 市级(自身+其下属区)
//            List<MsgAreaPO> msgAreaPOList = msgAreaMapper.selectByparentId(String.valueOf(msgProductPO.getProductArea()));
//            for (MsgAreaPO msgAreaPOs : msgAreaPOList) {
//                makeProducts(user, msgAreaPOs.getId(), msgProductPO, msgAreaPOs.getAreaName());
//            }
//        } else {// 区级(自身)
        String result = makeProducts(user, msgAreaPO.getId(), msgProductPO, msgAreaPO.getAreaName());
//        }
        if ("1".equals(result)) {
            // 更新产品配置表状态字段
            msgProductPO.setMessageStatus(2);
            msgProductMapper.updateById(msgProductPO);
            return "生成成功";
        } else {
            return "上传远端服务器返回空";
        }
    }

    private String makeProducts(CMsgUserPO user, int areaId, MsgProductPO msgProductPO, String areaName) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InterruptedException, AWTException {
        String pictureName = msgProductPO.getProductTitle() + "_" + areaName + ".png";
        String productStyle = msgProductPO.getProductStyle();
        String url = "http://msgcloud.80php.com/template/template_" + productStyle + "/index.html?code=" + areaId + "&id=" + msgProductPO.getId();
        InputStream inputStream = HtmlToImageUtils.htmlToImageByUrl(url, chromedriverPath);
        // 将流复制
        ByteArrayOutputStream baos = cloneInputStream(inputStream);
        // 图片上传Minio
        String fileNameMain = StorageUtil.uploadFile(
                "image",
                new ByteArrayInputStream(baos.toByteArray()),
                "png",
                "image/png");
        //上传文件至远端服务器
        ResponseData responseData = Api.uploadFile(
                new ByteArrayInputStream(baos.toByteArray()),
                pictureName,
                "image",
                1);
        if (responseData.getData() == null) {
            return "0";
        }
        Media media = JSONObject.parseObject(responseData.getData().toString(), Media.class);
        if (media != null) {
            // 先插入产品图片信息表
            MsgProductPicturePO msgProductPicturePO = new MsgProductPicturePO();
            msgProductPicturePO.setPictureName(pictureName);
            msgProductPicturePO.setPictureLocalUrl("image/" + fileNameMain);
            msgProductPicturePO.setPictureSize(media.getSize());
            msgProductPicturePO.setPictureWebId(media.getId());
            msgProductPicturePO.setPictureWebUrl(media.getUrl());
            msgProductPicturePO.setPictureWebSlUrl(media.getThumbUrl());
            msgProductPictureMapper.insert(msgProductPicturePO);
            // 再插入产品发送表
            MsgProductSendPO msgProductSendPO = new MsgProductSendPO();
            msgProductSendPO.setProductTitle(msgProductPO.getProductTitle());
            msgProductSendPO.setProductDescribe(msgProductPO.getProductDescribe());
            msgProductSendPO.setPictureId(msgProductPicturePO.getId());
            msgProductSendPO.setProductSendArea(areaId);
            msgProductSendPO.setMessageStatus(0);// 待审核
            msgProductSendPO.setCreatedOrg(String.valueOf(user.getOrganId()));
            msgProductSendPO.setCreatedBy(String.valueOf(user.getId()));
            msgProductSendPO.setCreatedTime(new Date());
            msgProductSendMapper.insert(msgProductSendPO);
            return "1";
        } else {
            return "0";
        }
    }

    private ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MsgProductPO makeMsgProductPO(MsgProductPO msgProductPO, MsgProductVO msgProductVO) {
        msgProductPO.setProductTitle(msgProductVO.getProductTitle());
        msgProductPO.setProductStyle(msgProductVO.getProductStyle());
        msgProductPO.setProductArea(msgProductVO.getProductArea());
        msgProductPO.setProductDescribe(msgProductVO.getProductDescribe());
        return msgProductPO;
    }

    private List<MsgAreaTreeDTO> makeTree(List<MsgAreaTreeDTO> treeNodes, String parentId) {
        List<MsgAreaTreeDTO> trees = new ArrayList<>();
        for (MsgAreaTreeDTO msgAreaTreeDTO : treeNodes) {
            if (parentId.equals(String.valueOf(msgAreaTreeDTO.getParentId()))) {
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
