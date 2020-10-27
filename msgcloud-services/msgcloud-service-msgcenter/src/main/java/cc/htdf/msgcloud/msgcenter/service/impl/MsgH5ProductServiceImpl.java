package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.common.utils.StorageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.*;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgH5ProductVO;
import cc.htdf.msgcloud.msgcenter.enums.ChoiceTypeEnum;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MsgH5ProductService;

import com.alibaba.druid.support.calcite.DDLColumn;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.io.Files;

import io.minio.errors.*;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Service
public class MsgH5ProductServiceImpl implements MsgH5ProductService {

    @Resource
    private MsgH5ProductMapper msgH5ProductMapper;

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Resource
    private MsgH5TagMapper msgH5TagMapper;

    @Resource
    private BH5ProductTourMapper bh5ProductTourMapper;

    @Resource
    private MsgH5RelationMapper msgH5RelationMapper;

    @Override
    public List<CMsgOrganTreeDTO> getOrganList(CMsgUserPO user) {
        List<CMsgOrganTreeDTO> DTO = new ArrayList<>();
        if (user.getOrganId() == 1) {
            List<Integer> orgs = user.getOrgs();
            for (Integer org : orgs) {
                if (org == 1) {
                    continue;
                }
                CMsgOrganPO cMsgOrganPO = cMsgOrganMapper.selectById(org);
                CMsgOrganTreeDTO cMsgOrganTreeDTO = new CMsgOrganTreeDTO();
                cMsgOrganTreeDTO.setId(org);
                cMsgOrganTreeDTO.setLabel(cMsgOrganPO.getOrganName());
                DTO.add(cMsgOrganTreeDTO);
            }
        } else {
            CMsgOrganPO cMsgOrganPO = cMsgOrganMapper.selectById(user.getOrganId());
            CMsgOrganTreeDTO cMsgOrganTreeDTO = new CMsgOrganTreeDTO();
            cMsgOrganTreeDTO.setId(user.getOrganId());
            cMsgOrganTreeDTO.setLabel(cMsgOrganPO.getOrganName());
            DTO.add(cMsgOrganTreeDTO);
        }
        return DTO;
    }

    @Override
    public MsgH5ProductPageDTO getH5ProductListByPage(CMsgUserPO user, String title, Integer organId, Integer modular, Integer type, Integer status, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(title) && !title.equals("")) {
            wrapper.like("TITLE", title);
        }
        if (Objects.nonNull(organId)) {
            wrapper.eq("ORGAN_ID", organId);
        } else {
            wrapper.in("ORGAN_ID", user.getOrgs());
        }
        if (Objects.nonNull(modular)) {
            wrapper.eq("MODULAR", modular);
        }
        if (Objects.nonNull(type)) {
            wrapper.eq("TYPE", type);
        }
        if (Objects.nonNull(status)) {
            wrapper.eq("STATUS", status);
        }
        if (Objects.nonNull(startTime) && !startTime.equals("")) {
            wrapper.between("RELEASE_TIME", startTime, endTime);
        }
        int totalSize = msgH5ProductMapper.selectCount(wrapper);
        Page<MsgH5ProductPO> page = new Page<>(pageNum, pageSize);
        List<MsgH5ProductPO> lists = msgH5ProductMapper.selectPage(page, wrapper);
        List<MsgH5ProductDTO> data = new ArrayList<>();
        for (MsgH5ProductPO msgH5ProductPO : lists) {
            MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
            msgH5ProductDTO.setId(msgH5ProductPO.getId());
            msgH5ProductDTO.setTitle(msgH5ProductPO.getTitle());
            if (msgH5ProductPO.getOrganId() != null) {
                msgH5ProductDTO.setOrganName(cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName());
            }
            msgH5ProductDTO.setModular(msgH5ProductPO.getModular());
            msgH5ProductDTO.setType(msgH5ProductPO.getType());
            if (msgH5ProductPO.getType() != null && msgH5ProductPO.getType() != 1) {
                msgH5ProductDTO.setMediaUrl(msgH5ProductPO.getMediaUrl());
            }
//            msgH5ProductDTO.setDescribe(msgH5ProductPO.getDescribe());
            msgH5ProductDTO.setViewCount(msgH5ProductPO.getViewCount());
            msgH5ProductDTO.setUpCount(msgH5ProductPO.getUpCount());
            msgH5ProductDTO.setBrowseCount(msgH5ProductPO.getBrowseCount());
            msgH5ProductDTO.setCreatedTime(msgH5ProductPO.getCreatedTime());
            msgH5ProductDTO.setReleaseTime(msgH5ProductPO.getReleaseTime());
            msgH5ProductDTO.setStatus(msgH5ProductPO.getStatus());
            data.add(msgH5ProductDTO);
        }
        MsgH5ProductPageDTO pageDTO = new MsgH5ProductPageDTO();
        pageDTO.setTotolSize(totalSize);
        pageDTO.setData(data);
        return pageDTO;
    }


    @Override
    public String createH5Product(CMsgUserPO user, MsgH5ProductVO msgH5ProductVO) {
        MsgH5ProductPO msgH5ProductPO = new MsgH5ProductPO();
        msgH5ProductPO.setCreatedOrg(String.valueOf(user.getOrganId()));
        msgH5ProductPO.setCreatedBy(String.valueOf(user.getId()));
        msgH5ProductPO.setCreatedTime(new Date());
        msgH5ProductMapper.insert(makeMsgH5ProductPO(msgH5ProductPO, msgH5ProductVO));
        return "创建成功";
    }

    @Override
    public String updateH5Product(CMsgUserPO user, MsgH5ProductVO msgH5ProductVO) {
        MsgH5ProductPO msgH5ProductPO = new MsgH5ProductPO();
        msgH5ProductPO.setId(msgH5ProductVO.getId());
        msgH5ProductPO.setUpdatedBy(String.valueOf(user.getId()));
        msgH5ProductPO.setUpdatedTime(new Date());
        msgH5ProductPO.setCreatedOrg(msgH5ProductVO.getCreatedOrg());
        msgH5ProductPO.setCreatedBy(msgH5ProductVO.getCreatedBy());
        msgH5ProductPO.setCreatedTime(msgH5ProductVO.getCreatedTime());
        msgH5ProductMapper.updateById(makeMsgH5ProductPO(msgH5ProductPO, msgH5ProductVO));
        return "更新成功";
    }

    @Override
    public String deleteH5ProductById(String productId) {
        // 判定该文章是否已经绑定景点
        EntityWrapper<BH5ProductTourPO> wrapper = new EntityWrapper();
        wrapper.eq("RELATED_ARTICLE", productId);
        List<BH5ProductTourPO> lists = bh5ProductTourMapper.selectList(wrapper);
        if (lists.size() != 0) {
            return "该文章已经绑定景点,不可删除";
        }
        msgH5ProductMapper.deleteById(Integer.valueOf(productId));
        return "删除成功";
    }

    @Override
    public String offH5ProductById(String productId) {
        // 判定该文章是否已经绑定景点
        EntityWrapper<BH5ProductTourPO> wrapper = new EntityWrapper();
        wrapper.eq("RELATED_ARTICLE", productId);
        List<BH5ProductTourPO> lists = bh5ProductTourMapper.selectList(wrapper);
        if (lists.size() != 0) {
            return "该文章已经绑定景点,不可下架";
        }
        msgH5ProductMapper.offH5ProductById(productId);
        // 如果H5产品关系表里关联了此条数据则删除
        MsgH5RelationPO po = msgH5RelationMapper.selectByProductId(Integer.valueOf(productId));
        if (po != null) {
            msgH5RelationMapper.deleteById(po.getId());
        }
        return "下架成功";
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
    public MsgH5ProductDTO uploadH5Material(MultipartFile file) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return upload(file, false);
    }

    private MsgH5ProductDTO upload(MultipartFile file, boolean isRe) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        // 将流复制
        ByteArrayOutputStream baos = cloneInputStream(file.getInputStream());
        String extName = Files.getFileExtension(file.getOriginalFilename());
        String contentType = StorageUtil.mediaType(extName);
        String bucketName;
        MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
        if ("mp4".equals(extName)) {// 视频类不制造缩略图
            bucketName = "h5video";
            String fileName = StorageUtil.uploadFile(bucketName, new ByteArrayInputStream(baos.toByteArray()), extName, contentType);
            msgH5ProductDTO.setMediaUrl(bucketName + "/" + fileName);
        } else {// 图片类造缩略图
            // 原图
            bucketName = "h5image";
            String fileName = StorageUtil.uploadFile(bucketName, new ByteArrayInputStream(baos.toByteArray()), extName, contentType);
            if (isRe) {
                msgH5ProductDTO.setMediaRcUrl(bucketName + "/" + fileName);
            } else {
                msgH5ProductDTO.setMediaUrl(bucketName + "/" + fileName);
            }
            // 缩略图
            File fileTemp = new File("D:\\cs");
            if (!fileTemp.exists()) {//如果文件夹不存在
                fileTemp.mkdir();//创建文件夹
            }
            File FileSl = new File("D:\\cs\\" + file.getOriginalFilename());
            Thumbnails.of(new ByteArrayInputStream(baos.toByteArray())).scale(0.9).toFile(FileSl);
            InputStream inputStreamSl = new FileInputStream("D:\\cs\\" + file.getOriginalFilename());
            // 缩略图上传Minio
            String fileNameSl = StorageUtil.uploadFile(bucketName, inputStreamSl, extName, contentType);
            if (isRe) {
                msgH5ProductDTO.setMediaRcSlUrl(bucketName + "/" + fileNameSl);
            } else {
                msgH5ProductDTO.setMediaSlUrl(bucketName + "/" + fileNameSl);
            }
        }
        return msgH5ProductDTO;
    }

    @Override
    public MsgH5ProductDTO uploadH5MaterialRe(MultipartFile file) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return upload(file, true);
    }

    @Override
    public MsgH5ProductDTO getH5ProductById(String productId) {
        MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
        MsgH5ProductPO msgH5ProductPO = msgH5ProductMapper.selectById(productId);
        if (msgH5ProductPO != null) {
            msgH5ProductDTO.setId(msgH5ProductPO.getId());
            msgH5ProductDTO.setTitle(msgH5ProductPO.getTitle());
            msgH5ProductDTO.setOrganId(msgH5ProductPO.getOrganId());
            msgH5ProductDTO.setOrganName(cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName());
            msgH5ProductDTO.setModular(msgH5ProductPO.getModular());
            if (msgH5ProductPO.getTagId() != null) {
                List<String> tagIdList = Arrays.asList(msgH5ProductPO.getTagId().split(","));
                msgH5ProductDTO.setTagId(tagIdList);
            }
            msgH5ProductDTO.setType(msgH5ProductPO.getType());
            if (msgH5ProductPO.getType() != null && msgH5ProductPO.getType() == 2) {
                msgH5ProductDTO.setMediaSlUrl(msgH5ProductPO.getMediaSlUrl());
                if (msgH5ProductPO.getMediaRcSlUrl() != null) {
                    msgH5ProductDTO.setMediaRcSlUrl(msgH5ProductPO.getMediaRcSlUrl());
                }
            } else if (msgH5ProductPO.getType() != null && msgH5ProductPO.getType() == 3) {
                msgH5ProductDTO.setMediaSlUrl(msgH5ProductPO.getMediaSlUrl());
                msgH5ProductDTO.setMediaUrl(msgH5ProductPO.getMediaUrl());
            }
            msgH5ProductDTO.setDescribe(msgH5ProductPO.getDescribe());
            String reason = msgH5ProductPO.getReason();
            if (Objects.nonNull(reason) && !reason.equals("")) {
                msgH5ProductDTO.setReason(reason);
            }
            if (Objects.nonNull(msgH5ProductPO.getSuitable()) && !msgH5ProductPO.getSuitable().equals("")) {
                msgH5ProductDTO.setSuitable(Arrays.asList(msgH5ProductPO.getSuitable().split(",")));
            }
            if (Objects.nonNull(msgH5ProductPO.getAvoid()) && !msgH5ProductPO.getAvoid().equals("")) {
                msgH5ProductDTO.setAvoid(Arrays.asList(msgH5ProductPO.getAvoid().split(",")));
            }
            msgH5ProductDTO.setStatus(msgH5ProductPO.getStatus());
            msgH5ProductDTO.setReleaseTime(msgH5ProductPO.getReleaseTime());
            msgH5ProductDTO.setCreatedOrg(msgH5ProductPO.getCreatedOrg());
            msgH5ProductDTO.setCreatedBy(msgH5ProductPO.getCreatedBy());
            msgH5ProductDTO.setCreatedTime(msgH5ProductPO.getCreatedTime());
            msgH5ProductDTO.setUpdatedBy(msgH5ProductPO.getUpdatedBy());
            msgH5ProductDTO.setUpdatedTime(msgH5ProductPO.getUpdatedTime());
        }
        return msgH5ProductDTO;
    }

    @Override
    public MsgH5ProductDTO getH5PhoneProductById(String productId) {
        msgH5ProductMapper.updateViewCount(productId);
        MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
        MsgH5ProductPO msgH5ProductPO = msgH5ProductMapper.selectById(productId);
        if (msgH5ProductPO != null) {
            msgH5ProductDTO.setId(msgH5ProductPO.getId());
            msgH5ProductDTO.setTitle(msgH5ProductPO.getTitle());
            msgH5ProductDTO.setOrganId(msgH5ProductPO.getOrganId());
            msgH5ProductDTO.setOrganName(cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName());
            msgH5ProductDTO.setModular(msgH5ProductPO.getModular());
            msgH5ProductDTO.setType(msgH5ProductPO.getType());
            if (msgH5ProductPO.getType() != 1) {
                msgH5ProductDTO.setMediaUrl(msgH5ProductPO.getMediaUrl());
            }
            msgH5ProductDTO.setDescribe(msgH5ProductPO.getDescribe());
            String reason = msgH5ProductPO.getReason();
            if (Objects.nonNull(reason) && !reason.equals("")) {
                msgH5ProductDTO.setReason(reason);
            }
            if (Objects.nonNull(msgH5ProductPO.getSuitable()) && !msgH5ProductPO.getSuitable().equals("")) {
                msgH5ProductDTO.setSuitable(Arrays.asList(msgH5ProductPO.getSuitable().split(",")));
            }
            if (Objects.nonNull(msgH5ProductPO.getAvoid()) && !msgH5ProductPO.getAvoid().equals("")) {
                msgH5ProductDTO.setAvoid(Arrays.asList(msgH5ProductPO.getAvoid().split(",")));
            }
            msgH5ProductDTO.setStatus(msgH5ProductPO.getStatus());
            msgH5ProductDTO.setReleaseTime(msgH5ProductPO.getReleaseTime());
            msgH5ProductDTO.setViewCount(msgH5ProductPO.getViewCount());
            msgH5ProductDTO.setUpCount(msgH5ProductPO.getUpCount());
            msgH5ProductDTO.setBrowseCount(msgH5ProductPO.getBrowseCount());
            msgH5ProductDTO.setCreatedOrg(msgH5ProductPO.getCreatedOrg());
            msgH5ProductDTO.setCreatedBy(msgH5ProductPO.getCreatedBy());
            msgH5ProductDTO.setCreatedTime(msgH5ProductPO.getCreatedTime());
            msgH5ProductDTO.setUpdatedBy(msgH5ProductPO.getUpdatedBy());
            msgH5ProductDTO.setUpdatedTime(msgH5ProductPO.getUpdatedTime());
            if (Objects.nonNull(msgH5ProductPO.getTagId()) && !msgH5ProductPO.getTagId().equals("")) {
                String[] spTag = msgH5ProductPO.getTagId().split(",");
                List<Map> tagList = new ArrayList<>();
                for (String tag : spTag) {
                    Map<String, Object> tagm = new HashMap<>();
                    Map<String, Object> tagMap = msgH5TagMapper.getTagById(tag);
                    if (tagMap != null) {
                        tagm.put("tagName", tagMap.get("name"));
                        tagm.put("tagColor", tagMap.get("color"));
                    }
                    if (!tagm.isEmpty()) {
                        tagList.add(tagm);
                    }
                }
                msgH5ProductDTO.setTagName(tagList);
            }
        }
        return msgH5ProductDTO;
    }

    @Override
    public String examine(String productId, String reason) {
        if (Objects.nonNull(reason) && !reason.equals("")) {
            msgH5ProductMapper.examineNg(productId, reason);
        } else {
            msgH5ProductMapper.examine(productId);
            // 已发布的数据插入到H5产品关系表中
            MsgH5ProductPO msgH5ProductPO = msgH5ProductMapper.selectById(productId);
            if (msgH5ProductPO.getModular() != 8) {// 旅游的模块处理逻辑不在这
                MsgH5RelationPO msgH5RelationPO = new MsgH5RelationPO();
                msgH5RelationPO.setModular(msgH5ProductPO.getModular());
                msgH5RelationPO.setType(1);// 全模块先写死插入类型为1
                msgH5RelationPO.setProductId(msgH5ProductPO.getId());
                msgH5RelationMapper.insert(msgH5RelationPO);
            }
        }
        return "审核成功";
    }

    @Override
    public MsgH5ProductPageDTO getH5ProductList(Integer organId, String title, boolean isMain, Integer[] modular, String tag, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(organId)) {
            wrapper.eq("ORGAN_ID", organId);
        }
        if (Objects.nonNull(title) && !title.equals("")) {
            wrapper.like("TITLE", title);
        }
        if (Objects.nonNull(modular)) {
            wrapper.in("MODULAR", modular);
        }
        wrapper.eq("STATUS", 3);// 已发布
        wrapper.le("RELEASE_TIME", new Date());// 发布时间小于等于当前时间
        List<MsgH5ProductPO> lists = msgH5ProductMapper.selectList(wrapper);
        List<MsgH5ProductDTO> data = new ArrayList<>();
        for (MsgH5ProductPO msgH5ProductPO : lists) {
            MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
            msgH5ProductDTO.setId(msgH5ProductPO.getId());
            msgH5ProductDTO.setTitle(msgH5ProductPO.getTitle());
            msgH5ProductDTO.setOrganName(cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName());
            msgH5ProductDTO.setModular(msgH5ProductPO.getModular());
            // 标签名称获取
            List<String> tagIdList = Arrays.asList(msgH5ProductPO.getTagId().split(","));
            if (Objects.nonNull(tag) && !tag.equals("") && !tagIdList.contains(tag)) {
                continue;
            }
            List<Map> tagNames = new ArrayList<>();
            for (String tagId : tagIdList) {
                Map map = new HashMap();
                MsgH5TagPO msgH5TagPOt = msgH5TagMapper.selectById(tagId);
                map.put("name", msgH5TagPOt.getName());
                map.put("color", msgH5TagPOt.getColor());
                tagNames.add(map);
            }
            msgH5ProductDTO.setTagName(tagNames);
            // 养生主页不展示类型为视频的数据
            if (isMain && msgH5ProductPO.getType() != 2) {
                continue;
            }
            msgH5ProductDTO.setType(msgH5ProductPO.getType());
            if (msgH5ProductPO.getType() == 2) {
                msgH5ProductDTO.setMediaSlUrl(msgH5ProductPO.getMediaSlUrl());
            } else if (msgH5ProductPO.getType() == 3) {
                msgH5ProductDTO.setMediaSlUrl(msgH5ProductPO.getMediaSlUrl());
                msgH5ProductDTO.setMediaUrl(msgH5ProductPO.getMediaUrl());
            }
            if (Objects.nonNull(msgH5ProductPO.getSuitable()) && !msgH5ProductPO.getSuitable().equals("")) {
                List<String> suitableList = Arrays.asList(msgH5ProductPO.getSuitable().split(","));
                msgH5ProductDTO.setSuitable(suitableList);
            }
            if (Objects.nonNull(msgH5ProductPO.getAvoid()) && !msgH5ProductPO.getAvoid().equals("")) {
                List<String> avoidList = Arrays.asList(msgH5ProductPO.getAvoid().split(","));
                msgH5ProductDTO.setAvoid(avoidList);
            }
            msgH5ProductDTO.setReleaseTime(msgH5ProductPO.getReleaseTime());
            msgH5ProductDTO.setDescribe(msgH5ProductPO.getDescribe());
            msgH5ProductDTO.setViewCount(msgH5ProductPO.getViewCount());
            msgH5ProductDTO.setBrowseCount(msgH5ProductPO.getBrowseCount());
            msgH5ProductDTO.setUpCount(msgH5ProductPO.getUpCount());
            msgH5ProductDTO.setCreatedTime(msgH5ProductPO.getCreatedTime());
            data.add(msgH5ProductDTO);
        }
        MsgH5ProductPageDTO pageDTO = new MsgH5ProductPageDTO();
        pageDTO.setTotolSize(data.size());
        pageDTO.setData(PageUtil.startPage(data, pageNum, pageSize));
        return pageDTO;
    }

    @Override
    public MsgH5PhoneProductDTO getH5WeatherDescribe(Integer organId, Integer modular) {
        MsgH5PhoneProductDTO msgH5PhoneProductDTO = new MsgH5PhoneProductDTO();
        EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
        wrapper.orderBy("RELEASE_TIME", false);
        if (Objects.nonNull(organId)) {
            wrapper.eq("ORGAN_ID", organId);
        }
        wrapper.eq("MODULAR", modular);
        if (modular == 1 || modular == 2) {
            wrapper.eq("TYPE", 1);// 文字
        } else {
            wrapper.eq("TYPE", 2);// 图片
        }
        wrapper.eq("STATUS", 3);// 已发布
        wrapper.le("RELEASE_TIME", new Date());// 发布时间小于等于当前时间
        List<MsgH5ProductPO> lists = msgH5ProductMapper.selectList(wrapper);
        if (lists.size() == 0) {
            return null;
        }
        MsgH5ProductPO msgH5ProductPO = lists.get(0);
        msgH5PhoneProductDTO.setTitle(msgH5ProductPO.getTitle());
        msgH5PhoneProductDTO.setDescribe(msgH5ProductPO.getDescribe());
        msgH5PhoneProductDTO.setMediaUrl(msgH5ProductPO.getMediaUrl());
        msgH5PhoneProductDTO.setSuitable(msgH5ProductPO.getSuitable());
        msgH5PhoneProductDTO.setAvoid(msgH5ProductPO.getAvoid());
        return msgH5PhoneProductDTO;
    }

    @Override
    public MsgH5ProductPageDTO getH5Product(CMsgUserPO user, String title, Integer organId, Integer modular, Integer type, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
//        wrapper.in("ORGAN_ID", user.getOrgs());
        if (Objects.nonNull(organId)) {
            wrapper.eq("ORGAN_ID", organId);
        } else {
            wrapper.in("ORGAN_ID", user.getOrgs());
        }
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(title) && !title.equals("")) {
            wrapper.like("TITLE", title);
        }
        if (Objects.nonNull(modular)) {
            wrapper.eq("MODULAR", modular);
        }
        if (Objects.nonNull(type)) {
            wrapper.eq("TYPE", type);
        }
        if (Objects.nonNull(startTime) && !startTime.equals("")) {
            wrapper.between("RELEASE_TIME", startTime, endTime);
        }
        wrapper.eq("STATUS", 1);// 待审核
        List<MsgH5ProductPO> lists = msgH5ProductMapper.selectList(wrapper);
        List<MsgH5ProductDTO> data = new ArrayList<>();
        for (MsgH5ProductPO msgH5ProductPO : lists) {
            MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
            msgH5ProductDTO.setId(msgH5ProductPO.getId());
            msgH5ProductDTO.setTitle(msgH5ProductPO.getTitle());
            msgH5ProductDTO.setOrganName(cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName());
            msgH5ProductDTO.setModular(msgH5ProductPO.getModular());
            msgH5ProductDTO.setType(msgH5ProductPO.getType());
            if (msgH5ProductPO.getType() != 1) {
                msgH5ProductDTO.setMediaUrl(msgH5ProductPO.getMediaUrl());
            }
            if (Objects.nonNull(msgH5ProductPO.getSuitable()) && !msgH5ProductPO.getSuitable().equals("")) {
                msgH5ProductDTO.setSuitable(Arrays.asList(msgH5ProductPO.getSuitable().split(",")));
            }
            if (Objects.nonNull(msgH5ProductPO.getAvoid()) && !msgH5ProductPO.getAvoid().equals("")) {
                msgH5ProductDTO.setAvoid(Arrays.asList(msgH5ProductPO.getAvoid().split(",")));
            }
//            msgH5ProductDTO.setDescribe(msgH5ProductPO.getDescribe());
            msgH5ProductDTO.setReleaseTime(msgH5ProductPO.getReleaseTime());
            msgH5ProductDTO.setCreatedTime(msgH5ProductPO.getCreatedTime());
            data.add(msgH5ProductDTO);
        }
        MsgH5ProductPageDTO pageDTO = new MsgH5ProductPageDTO();
        pageDTO.setTotolSize(data.size());
        pageDTO.setData(PageUtil.startPage(data, pageNum, pageSize));
        return pageDTO;
    }

    @Override
    public Map getChoiceList() {
        Map map = new HashMap();
        ChoiceTypeEnum[] lists = ChoiceTypeEnum.values();
        for (ChoiceTypeEnum choiceTypeEnum : lists) {
            map.put(choiceTypeEnum.getKey(), choiceTypeEnum.getValue());
        }
        return map;
    }

    @Override
    public String addBrowseCountProductById(String productId) {
        msgH5ProductMapper.updateBrowseCount(productId);
        return "有效浏览数加一成功";
    }

    @Override
    public List<MsgH5TagTreeDTO> getTagTree(Integer moduleId) {
        if (moduleId == 6 || moduleId == 7) {
            moduleId = 5;
        }
        List<MsgH5TagPO> msgH5TagPOList = msgH5TagMapper.selectAllByModuleId(moduleId);
        List<MsgH5TagTreeDTO> treeNodes = new ArrayList<>();
        MsgH5TagTreeDTO msgH5TagTreeDTO;
        for (MsgH5TagPO msgH5TagPO : msgH5TagPOList) {
            msgH5TagTreeDTO = new MsgH5TagTreeDTO();
            msgH5TagTreeDTO.setId(msgH5TagPO.getId());
            msgH5TagTreeDTO.setLabel(msgH5TagPO.getName());
            msgH5TagTreeDTO.setParentId(msgH5TagPO.getParentId());
            msgH5TagTreeDTO.setLevel(msgH5TagPO.getLevel());
            msgH5TagTreeDTO.setType(msgH5TagPO.getType());
            int count = msgH5TagMapper.selectCountById(msgH5TagPO.getId());
            if (count == 0) {
                msgH5TagTreeDTO.setDisabled(true);
            } else {
                msgH5TagTreeDTO.setDisabled(false);
            }
            treeNodes.add(msgH5TagTreeDTO);
        }
        return makeTree(treeNodes, "0");
    }

    @Override
    public List<MsgH5ProductDTO> getTourProduct(CMsgUserPO user) {
        EntityWrapper<MsgH5ProductPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("modular", 8);
        entityWrapper.eq("status", 3);
        entityWrapper.in("created_org", user.getOrgs());
        List<MsgH5ProductPO> poList = msgH5ProductMapper.selectList(entityWrapper);
        List<MsgH5ProductDTO> dtoList = new ArrayList<>();
        for (MsgH5ProductPO po : poList) {
            MsgH5ProductDTO dto = new MsgH5ProductDTO();
            BeanUtils.copyProperties(po, dto);
            dto.setOrganName(cMsgOrganMapper.selectById(po.getOrganId()).getOrganName());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<Map<String, Object>> getTagByModular(Integer modular) {
        List<MsgH5TagPO> msgH5TagPOList = msgH5TagMapper.getTagByModular(modular);
        List<Map<String, Object>> tags = new ArrayList<>();
        for (MsgH5TagPO msgH5TagPO : msgH5TagPOList) {
            Map map = new HashMap();
            map.put("key", msgH5TagPO.getId());
            map.put("value", msgH5TagPO.getName());
            tags.add(map);
        }
        return tags;
    }

    @Override
    public MsgH5PhoneRecommendDTO getH5MainList(Integer organId) {
        MsgH5PhoneRecommendDTO msgH5PhoneRecommendDTO = new MsgH5PhoneRecommendDTO();
        EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(organId)) {
            wrapper.eq("ORGAN_ID", organId);
        }
        wrapper.eq("MODULAR", 4);
        wrapper.eq("STATUS", 3);// 已发布
        wrapper.le("RELEASE_TIME", new Date());// 发布时间小于等于当前时间
        List<MsgH5ProductPO> lists = msgH5ProductMapper.selectList(wrapper);
        List<MsgH5ProductDTO> data = new ArrayList<>();
        for (MsgH5ProductPO msgH5ProductPO : lists) {
            /*if (msgH5ProductPO.getMediaRcUrl() == null) {
                continue;
            }*/
            MsgH5ProductDTO msgH5ProductDTO = new MsgH5ProductDTO();
            msgH5ProductDTO.setId(msgH5ProductPO.getId());
            msgH5ProductDTO.setTitle(msgH5ProductPO.getTitle());
            msgH5ProductDTO.setMediaSlUrl(msgH5ProductPO.getMediaRcSlUrl());
            msgH5ProductDTO.setViewCount(msgH5ProductPO.getViewCount());
            data.add(msgH5ProductDTO);
        }
        msgH5PhoneRecommendDTO.setRecommend(data);
        msgH5PhoneRecommendDTO.setSelected(data);
        return msgH5PhoneRecommendDTO;
    }


    private List<MsgH5TagTreeDTO> makeTree(List<MsgH5TagTreeDTO> treeNodes, String parentId) {
        List<MsgH5TagTreeDTO> trees = new ArrayList<>();
        for (MsgH5TagTreeDTO msgH5TagTreeDTO : treeNodes) {
            if (parentId.equals(String.valueOf(msgH5TagTreeDTO.getParentId()))) {
                trees.add(findChildren(msgH5TagTreeDTO, treeNodes));
            }
        }
        return trees;
    }

    private MsgH5TagTreeDTO findChildren(MsgH5TagTreeDTO DTO, List<MsgH5TagTreeDTO> DTOList) {
        for (MsgH5TagTreeDTO msgH5TagTreeDTO : DTOList) {
            if (DTO.getId() == msgH5TagTreeDTO.getParentId()) {
                if (DTO.getChildren() == null) {
                    DTO.setChildren(new ArrayList<>());
                }
                DTO.add(findChildren(msgH5TagTreeDTO, DTOList));
            }
        }
        return DTO;
    }

    private MsgH5ProductPO makeMsgH5ProductPO(MsgH5ProductPO msgH5ProductPO, MsgH5ProductVO msgH5ProductVO) {
        msgH5ProductPO.setTitle(msgH5ProductVO.getTitle());
        msgH5ProductPO.setOrganId(msgH5ProductVO.getOrganId());
        msgH5ProductPO.setModular(msgH5ProductVO.getModular());
        if (msgH5ProductVO.getTagId() != null) {
            msgH5ProductPO.setTagId(msgH5ProductVO.getTagId());
        }
        msgH5ProductPO.setType(msgH5ProductVO.getType());
        if (msgH5ProductVO.getType() != null && msgH5ProductVO.getType() != 1) {
            if (msgH5ProductVO.getType() == 3) {
                msgH5ProductPO.setMediaUrl(msgH5ProductVO.getMediaUrl());
                msgH5ProductPO.setMediaSlUrl(msgH5ProductVO.getMediaSlUrl());
            } else {
                msgH5ProductPO.setMediaSlUrl(msgH5ProductVO.getMediaSlUrl());
                // 推荐图片以及推荐图片的缩略图
                if (msgH5ProductVO.getMediaRcUrl() != null && msgH5ProductVO.getMediaRcSlUrl() != null) {
                    msgH5ProductPO.setMediaRcUrl(msgH5ProductVO.getMediaRcUrl());
                    msgH5ProductPO.setMediaRcSlUrl(msgH5ProductVO.getMediaRcSlUrl());
                }
            }
        }
        msgH5ProductPO.setDescribe(msgH5ProductVO.getDescribe());
        msgH5ProductPO.setSuitable(msgH5ProductVO.getSuitable());
        msgH5ProductPO.setAvoid(msgH5ProductVO.getAvoid());
        msgH5ProductPO.setReleaseTime(msgH5ProductVO.getReleaseTime());
        msgH5ProductPO.setStatus(msgH5ProductVO.getStatus());
        return msgH5ProductPO;
    }
    
   

    @Override
    public List<Object> getH5ScienceMainList(String modularId, String type,
                                             Integer pageNum, Integer pageSize, String title) {
        // TODO Auto-generated method stub
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String date = format.format(new Date());
        List<Object> objList = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        if ((type == null || "".equals(type)) && !"8".equals(modularId)) {
            list = msgH5ProductMapper.getProductList(modularId, type, title,date);
            for (Map<String, Object> map : list) {
                List<Map<String, Object>> tagList = new ArrayList<>();
                String tagId = (String) map.get("tagId");
                if (tagId != null && !"".equals(tagId)) {
                    String[] spl = tagId.split(",");
                    for (String tag : spl) {
                        Map<String, Object> tagm = new HashMap<>();
                        Map<String, Object> tagMap = msgH5TagMapper.getTagById(tag);
                        if (tagMap != null) {
                            tagm.put("tagName", tagMap.get("name"));
                            tagm.put("tagColor", tagMap.get("color"));
                        }
                        if (!tagm.isEmpty()) {
                            tagList.add(tagm);
                        }
                    }
                } else {
                    tagList.add(new HashMap<>());
                }
                map.put("tagData", tagList);
            }
            data = list;
        } else if ("8".equals(modularId)) {
            list = msgH5ProductMapper.getProductListTravel(modularId, type, title,date);
            for (Map<String, Object> map : list) {
            	map.put("id", map.get("urid"));
            	String url = "http://tj.msgcloud.80php.com/#/travelDetail?id="+map.get("urid");
            	map.put("value", url);
            }
            data = list;
        } else {
            String[] typeSp = type.split(",");
            if (typeSp.length > 1) {
                for (String types : typeSp) {
                    Map<String, Object> mapdata = new HashMap<>();
                    List<Map<String, Object>> data1 = new ArrayList<>();
                    list = msgH5ProductMapper.getProductList(modularId, types, title,date);
                    for (Map<String, Object> map : list) {
                        List<Map<String, Object>> tagList = new ArrayList<>();
                        String tagId = (String) map.get("tagId");
                        if (tagId != null && !"".equals(tagId)) {
                            String[] spl = tagId.split(",");
                            for (String tag : spl) {
                                Map<String, Object> tagm = new HashMap<>();
                                Map<String, Object> tagMap = msgH5TagMapper.getTagById(tag);
                                if (tagMap != null) {
                                    tagm.put("tagName", tagMap.get("name"));
                                    tagm.put("tagColor", tagMap.get("color"));
                                }
                                if (!tagm.isEmpty()) {
                                    tagList.add(tagm);
                                }
                            }
                        } else {
                            tagList.add(new HashMap<>());
                        }
                        map.put("tagData", tagList);
                    }
                    if ((modularId != null) && ("1".equals(types))) {
                        list = list.size() > 5 ? list.subList(0, 5) : list;
                    }
                    if ((modularId != null) && ("2".equals(types))) {
                        list = list.size() > 3 ? list.subList(0, 3) : list;
                    }
                    data1 = list;
                    mapdata.put("type", types);
                    mapdata.put("data1", data1);
                    objList.add(mapdata);
                    data1 = new ArrayList<>();
                    list = new ArrayList<>();
                }
            } else {
                list = msgH5ProductMapper.getProductList(modularId, type, title,date);
                for (Map<String, Object> map : list) {
                    List<Map<String, Object>> tagList = new ArrayList<>();
                    String tagId = (String) map.get("tagId");
                    if (tagId != null && !"".equals(tagId)) {
                        String[] spl = tagId.split(",");
                        for (String tag : spl) {
                            Map<String, Object> tagm = new HashMap<>();
                            Map<String, Object> tagMap = msgH5TagMapper.getTagById(tag);
                            if (tagMap != null) {
                                tagm.put("tagName", tagMap.get("name"));
                                tagm.put("tagColor", tagMap.get("color"));
                            }
                            if (!tagm.isEmpty()) {
                                tagList.add(tagm);
                            }
                        }
                    } else {
                        tagList.add(new HashMap<>());
                    }
                    map.put("tagData", tagList);
                }
                data = list;
            }
        }

        MsgH5ProductPageDTO pageDTO = new MsgH5ProductPageDTO();
        if (pageNum != null && pageSize != null) {
            pageDTO.setTotolSize(data.size());
            pageDTO.setData(PageUtil.startPage(data, pageNum, pageSize));
            objList.add(pageDTO);
        } else {
            objList.add(data);
        }
        return objList;
    }

}
