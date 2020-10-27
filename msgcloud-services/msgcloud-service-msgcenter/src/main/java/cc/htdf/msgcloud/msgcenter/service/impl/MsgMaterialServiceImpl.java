package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.utils.StorageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgMaterialLabelDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgMaterialPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.po.MsgMaterialLabelPO;
import cc.htdf.msgcloud.msgcenter.domain.po.MsgMaterialPO;
import cc.htdf.msgcloud.msgcenter.domain.po.MsgTemplatePO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgMaterialLabelVO;
import cc.htdf.msgcloud.msgcenter.mapper.MsgMaterialLabelMapper;
import cc.htdf.msgcloud.msgcenter.mapper.MsgMaterialMapper;
import cc.htdf.msgcloud.msgcenter.mapper.MsgTemplateMapper;
import cc.htdf.msgcloud.msgcenter.service.MsgMaterialService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import com.google.common.io.Files;
import io.minio.errors.*;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class MsgMaterialServiceImpl implements MsgMaterialService {

    @Resource
    private MsgMaterialMapper msgMaterialMapper;

    @Resource
    private MsgMaterialLabelMapper msgMaterialLabelMapper;

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Override
    public List<MsgMaterialPageDTO> getMaterialList(CMsgUserPO user, String materialType, String materialName) {
        List<MsgMaterialPageDTO> msgTemplateMaterialPageDTOList = new ArrayList<>();
        boolean isInsert = Objects.isNull(materialName) || materialName.equals("");
        // 处理全部图片
        EntityWrapper<MsgMaterialPO> wrapperAll = new EntityWrapper();
        wrapperAll.eq("MATERIAL_TYPE", materialType);
        wrapperAll.in("CREATED_ORG", user.getOrgs());
        wrapperAll.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(materialType) && !materialType.equals("")) {
            wrapperAll.like("MATERIAL_NAME", materialName);
        }
        List<MsgMaterialPO> listsAll = msgMaterialMapper.selectList(wrapperAll);
        MsgMaterialPageDTO pageDTO = new MsgMaterialPageDTO();
        pageDTO.setLabelName("全部素材");
        pageDTO.setTotolSize(listsAll.size());
        msgTemplateMaterialPageDTOList.add(pageDTO);
        // 处理图片分类标签
        EntityWrapper<MsgMaterialLabelPO> wrapper = new EntityWrapper();
        wrapper.eq("MATERIAL_TYPE", materialType);
        wrapper.in("CREATED_ORG", user.getOrgs());
        List<MsgMaterialLabelPO> lists = msgMaterialLabelMapper.selectList(wrapper);
        for (MsgMaterialLabelPO po : lists) {
            List<MsgMaterialPO> labelLists;
            if (isInsert) {
                labelLists = msgMaterialMapper.getMaterialListBylabelId(materialType, po.getId());
            } else {
                labelLists = msgMaterialMapper.getMaterialListByNameAndlabelId(materialType, "%" + materialName + "%", po.getId());
            }
            MsgMaterialPageDTO pageLabelDTO = new MsgMaterialPageDTO();
            MsgMaterialLabelPO msgMaterialLabelPO = msgMaterialLabelMapper.selectById(po.getId());
            pageLabelDTO.setLabelId(msgMaterialLabelPO.getId());
            pageLabelDTO.setLabelName(msgMaterialLabelPO.getLabelName());
            pageLabelDTO.setTotolSize(labelLists.size());
            msgTemplateMaterialPageDTOList.add(pageLabelDTO);
        }
        return msgTemplateMaterialPageDTOList;
    }

    @Override
    public MsgMaterialPageDTO getMaterialPage(CMsgUserPO user, String labelId, String materialType, String materialName, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgMaterialPO> wrapper = new EntityWrapper();
        wrapper.eq("MATERIAL_TYPE", materialType);
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(labelId) && !labelId.equals("")) {
            wrapper.eq("MATERIAL_LABEL_ID", labelId);
        }
        if (Objects.nonNull(materialType) && !materialType.equals("")) {
            wrapper.like("MATERIAL_NAME", materialName);
        }
        int totalSize = msgMaterialMapper.selectCount(wrapper);
        Page<MsgMaterialPO> page = new Page<>(pageNum, pageSize);
        List<MsgMaterialPO> lists = msgMaterialMapper.selectPage(page, wrapper);
        MsgMaterialPageDTO pageDTO = new MsgMaterialPageDTO();
        pageDTO.setTotolSize(totalSize);
        pageDTO.setData(lists);
        return pageDTO;
    }

    @Override
    public String createMaterialLabel(CMsgUserPO user, MsgMaterialLabelVO msgMaterialLabelVO) {
        Date date = new Date();
        String labelId = msgMaterialLabelVO.getId();
        Boolean insert = Objects.isNull(labelId) || labelId.equals("");
        String materialType = msgMaterialLabelVO.getMaterialType();
        String labelName = msgMaterialLabelVO.getLabelName();
        // 限定标签名称不可重复
        EntityWrapper<MsgMaterialLabelPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.eq("MATERIAL_TYPE", materialType);
        wrapper.eq("LABEL_NAME", labelName);
        List<MsgMaterialLabelPO> lists = msgMaterialLabelMapper.selectList(wrapper);
        if (insert && lists.size() != 0) {
            return "用户组已存在";
        }
        MsgMaterialLabelPO msgMaterialLabelPO = new MsgMaterialLabelPO();
        msgMaterialLabelPO.setMaterialType(materialType);
        msgMaterialLabelPO.setLabelName(labelName);
        msgMaterialLabelPO.setUpdatedTime(date);
        if (insert) {
            String materialLabelId = UUID.randomUUID().toString().replace("-", "");
            msgMaterialLabelPO.setId(materialLabelId);
            msgMaterialLabelPO.setCreatedOrg(String.valueOf(user.getOrganId()));
            msgMaterialLabelPO.setCreatedBy(String.valueOf(user.getId()));
            msgMaterialLabelPO.setCreatedTime(new Date());
            msgMaterialLabelMapper.insert(msgMaterialLabelPO);
        } else {
            msgMaterialLabelPO.setId(labelId);
            msgMaterialLabelPO.setUpdatedBy(String.valueOf(user.getId()));
            msgMaterialLabelPO.setUpdatedTime(new Date());
            msgMaterialLabelPO.setCreatedOrg(msgMaterialLabelVO.getCreatedOrg());
            msgMaterialLabelPO.setCreatedBy(msgMaterialLabelVO.getCreatedBy());
            msgMaterialLabelPO.setCreatedTime(msgMaterialLabelVO.getCreatedTime());
            msgMaterialLabelMapper.updateById(msgMaterialLabelPO);
        }
        return "保存成功";
    }

    @Override
    public List<MsgMaterialLabelDTO> getMaterialLabel(CMsgUserPO user, String materialType) {
        List<MsgMaterialLabelDTO> msgMaterialLabelDTOList = new ArrayList<>();
        EntityWrapper<MsgMaterialLabelPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.eq("MATERIAL_TYPE", materialType);
        List<MsgMaterialLabelPO> lists = msgMaterialLabelMapper.selectList(wrapper);
        for (MsgMaterialLabelPO msgMaterialLabelPO : lists) {
            MsgMaterialLabelDTO msgMaterialLabelDTO = new MsgMaterialLabelDTO();
            msgMaterialLabelDTO.setId(msgMaterialLabelPO.getId());
            msgMaterialLabelDTO.setMaterialType(msgMaterialLabelPO.getMaterialType());
            msgMaterialLabelDTO.setLabelName(msgMaterialLabelPO.getLabelName());
            msgMaterialLabelDTOList.add(msgMaterialLabelDTO);
        }
        return msgMaterialLabelDTOList;
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

    @Override
    public String uploadMaterial(CMsgUserPO user, MultipartFile file, String labelId) throws IOException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, RegionConflictException, InvalidResponseException, InvalidKeyException {
        String objectName = file.getOriginalFilename();
        final InputStream inputStream = file.getInputStream();
        //上传文件至本地Minio
        String extName = Files.getFileExtension(objectName);
        String contentType = StorageUtil.mediaType(extName);
        String bucketName;
        String materialType;
        if ("png".equals(extName) || "jpg".equals(extName) || "jpeg".equals(extName) || "gif".equals(extName)) {
            bucketName = "imgs";
            materialType = "1";
        } else if ("mp4".equals(extName)) {
            bucketName = "video";
            materialType = "3";
        } else {
            bucketName = "audio";
            materialType = "2";
        }
        // 将流复制
        ByteArrayOutputStream baos = cloneInputStream(inputStream);
        String materialLocalUrlSl = null;
        long materialLocalSizeSl = 0;
        if (!"2".equals(materialType) && !"3".equals(materialType)) { //视频音频的情况不制造缩略图
            // 缩略图
            File fileTemp = new File("D:\\cs");
            if (!fileTemp.exists()) {//如果文件夹不存在
                fileTemp.mkdir();//创建文件夹
            }
            File FileSl = new File("D:\\cs\\" + objectName);
            Thumbnails.of(new ByteArrayInputStream(baos.toByteArray())).scale(0.01).toFile(FileSl);
            InputStream inputStreamSl = new FileInputStream("D:\\cs\\" + objectName);
            // 缩略图上传Minio
            String fileNameSl = StorageUtil.uploadFile(bucketName, inputStreamSl, extName, contentType);
            materialLocalUrlSl = bucketName + "/" + fileNameSl;
            materialLocalSizeSl = FileSl.length();
        }
        // 主图上传Minio
        String fileNameMain = StorageUtil.uploadFile(bucketName, new ByteArrayInputStream(baos.toByteArray()), extName, contentType);
        String materialLocalUrlMain = bucketName + "/" + fileNameMain;
        long materialLocalSizeMain = file.getSize();
        //上传文件至远端服务器
        ResponseData responseData = Api.uploadFile(
                new ByteArrayInputStream(baos.toByteArray()),
                objectName,
                contentType,
                1);
        Media media = JSONObject.parseObject(responseData.getData().toString(), Media.class);
        //将文件信息插入素材表
        MsgMaterialPO msgTemplateMaterialPO = new MsgMaterialPO();
        String materialId = UUID.randomUUID().toString().replace("-", "");
        msgTemplateMaterialPO.setId(materialId);
        msgTemplateMaterialPO.setMaterialLabelId(labelId);
        msgTemplateMaterialPO.setMaterialType(materialType);
        msgTemplateMaterialPO.setMaterialName(objectName);
        msgTemplateMaterialPO.setMaterialSuffix(contentType);
        // 本地原图地址
        msgTemplateMaterialPO.setMaterialLocalUrl(materialLocalUrlMain);
        // 本地原图大小
        msgTemplateMaterialPO.setMaterialLocalSize(materialLocalSizeMain);
        // 本地缩略图地址
        msgTemplateMaterialPO.setMaterialLocalSlUrl(materialLocalUrlSl);
        // 本地缩略图大小
        msgTemplateMaterialPO.setMaterialLocalSlSize(materialLocalSizeSl);
        // 外网原图地址
        msgTemplateMaterialPO.setMaterialWebUrl(media.getUrl());
        // 外网原图大小
        msgTemplateMaterialPO.setMaterialWebSize(materialLocalSizeMain);
        // 外网缩略图地址
        msgTemplateMaterialPO.setMaterialWebSlUrl(media.getThumbUrl());
        // 外网缩略图大小
        msgTemplateMaterialPO.setMaterialWebSlSize(materialLocalSizeSl);
        msgTemplateMaterialPO.setMaterialWebId(media.getId());
        msgTemplateMaterialPO.setCreatedOrg(String.valueOf(user.getOrganId()));
        msgTemplateMaterialPO.setCreatedBy(String.valueOf(user.getId()));
        msgTemplateMaterialPO.setCreatedTime(new Date());
        msgMaterialMapper.insert(msgTemplateMaterialPO);
        return "上传成功";
    }

    @Override
    public String moveMaterial(CMsgUserPO user, String materialIds, String materialLabelId) {
        List<String> materialIdList = Arrays.asList(materialIds.split(","));
        for (String materialId : materialIdList) {
            MsgMaterialPO msgMaterialPO = msgMaterialMapper.selectById(materialId);
            msgMaterialPO.setMaterialLabelId(materialLabelId);
            msgMaterialPO.setUpdatedBy(String.valueOf(user.getId()));
            msgMaterialPO.setUpdatedTime(new Date());
            msgMaterialMapper.updateById(msgMaterialPO);
        }
        return "移动成功";
    }

    @Override
    public String deleteMaterialByIds(String materialIds) {
        List<String> materialIdList = Arrays.asList(materialIds.split(","));
        for (String materialId : materialIdList) {
            List<MsgTemplatePO> MsgTemplatePOList = msgTemplateMapper.getTemplateByMaterialId(materialId);
            if (CollectionUtils.isNotEmpty(MsgTemplatePOList)) {
                return "所选图片中有已经与模板绑定的图片";
            }
            msgMaterialMapper.deleteById(materialId);
        }
        return "删除成功";
    }

    @Override
    public String deleteMaterialLabelById(String materialLabelId) {
        msgMaterialLabelMapper.deleteById(materialLabelId);
        msgMaterialMapper.updateByMaterialLabelId(materialLabelId);
        return "删除成功";
    }
}
