package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.constants.MsgTemplateStrategyConstant;
import cc.htdf.msgcloud.msgcenter.domain.dto.*;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgTemplateButtonVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgTemplateVO;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MsgTemplateService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class MsgTemplateServiceImpl implements MsgTemplateService {

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Resource
    private MsgTemplateButtonMapper msgTemplateButtonMapper;

    @Resource
    private MsgTemplateTagMapper msgTemplateTagMapper;

    @Resource
    private MsgMaterialMapper msgMaterialMapper;

    @Resource
    private MsgTemplateToButtonMapper msgTemplateToButtonMapper;

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private BKeywordTemplateMapper cKeywordTemplateMapper;

    @Resource
    private BMsgKeywordMapper cMsgKeywordMapper;

    @Resource
    private MsgH5ProductMapper msgH5ProductMapper;

    @Resource
    private BModuleMapper bModuleMapper;

    @Resource
    private BGroupTemplateMapper bGroupTemplateMapper;

    @Resource
    private BH5ProductTourMapper bh5ProductTourMapper;

    @Override
    public MsgTemplatePageDTO getTemplateList(CMsgUserPO user, String templateName, Integer organId, Integer modularId, Integer pageNum, Integer pageSize) {
        EntityWrapper<MsgTemplatePO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        if (Objects.nonNull(templateName) && !templateName.equals("")) {
            wrapper.like("TEMPLATE_TITLE", templateName);
        }
        if (Objects.nonNull(modularId)) {
            wrapper.eq("MODULE_ID", modularId);
        }
        if (Objects.nonNull(organId)) {
            wrapper.eq("CREATED_ORG", organId);
        }
        int totalSize = msgTemplateMapper.selectCount(wrapper);
        Page<MsgTemplatePO> page = new Page<>(pageNum, pageSize);
        List<MsgTemplatePO> lists = msgTemplateMapper.selectPage(page, wrapper);
        List<MsgTemplateListDTO> msgTemplateListDTO = new ArrayList<>();
        for (MsgTemplatePO msgTemplatePO : lists) {
            MsgTemplateListDTO DTO = new MsgTemplateListDTO();
            DTO.setId(msgTemplatePO.getId());
            DTO.setModuleName(bModuleMapper.selectById(msgTemplatePO.getModuleId()).getName());
            DTO.setTemplateTitle(msgTemplatePO.getTemplateTitle());
            DTO.setTemplateType(msgTemplatePO.getTemplateType());
            DTO.setCreatedOrg(cMsgOrganMapper.selectById(msgTemplatePO.getCreatedOrg()).getOrganName());
            DTO.setCreatedBy(cMsgUserMapper.selectById(msgTemplatePO.getCreatedBy()).getUserName());
            DTO.setTemplateContent(msgTemplatePO.getTemplateContent());
            DTO.setCreatedTime(msgTemplatePO.getCreatedTime());
            msgTemplateListDTO.add(DTO);
        }
        MsgTemplatePageDTO pageDTO = new MsgTemplatePageDTO();
        pageDTO.setTotolSize(totalSize);
        pageDTO.setData(msgTemplateListDTO);
        return pageDTO;
    }

    @Override
    public List<MsgTemplateDTO> getTemplateLists(CMsgUserPO user, String templateType) {
        List<MsgTemplateDTO> msgTemplateDTOList = new ArrayList<>();
        EntityWrapper<MsgTemplatePO> wrapper = new EntityWrapper();
        if (StringUtils.isNotEmpty(templateType)) {
            wrapper.eq("TEMPLATE_TYPE", templateType);
        }
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        List<MsgTemplatePO> lists = msgTemplateMapper.selectList(wrapper);
        for (MsgTemplatePO msgTemplatePO : lists) {
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
            msgTemplateDTO.setCreatedBy(cMsgUserMapper.selectById(msgTemplatePO.getCreatedBy()).getUserName());
            msgTemplateDTO.setCreatedTime(msgTemplatePO.getCreatedTime());
            msgTemplateDTOList.add(msgTemplateDTO);
        }
        return msgTemplateDTOList;
    }

    @Override
    public MsgTemplateDTO getTemplateById(String templateId) {
        MsgTemplateDTO msgTemplateDTO = new MsgTemplateDTO();
        MsgTemplatePO msgTemplatePO = msgTemplateMapper.getTemplateById(templateId);
        // 模板id获取
        msgTemplateDTO.setId(templateId);
        // 模板类型获取
        msgTemplateDTO.setModuleId(msgTemplatePO.getModuleId());
        // 模板名称获取
        msgTemplateDTO.setTemplateName(msgTemplatePO.getTemplateName());
        // 模板类型获取
        msgTemplateDTO.setTemplateType(msgTemplatePO.getTemplateType());
        // 模板标题获取
        msgTemplateDTO.setTemplateTitle(msgTemplatePO.getTemplateTitle());
        // 模板内容获取
        msgTemplateDTO.setTemplateContent(msgTemplatePO.getTemplateContent());
        // 动态模板ID获取
        msgTemplateDTO.setDynamicTemplateId(msgTemplatePO.getDynamicTemplateId());
        // 模板素材对象获取
        String templateImageId = msgTemplatePO.getTemplateImageId();
        MsgMaterialDTO msgTemplateMaterialDTO = new MsgMaterialDTO();
        MsgMaterialPO msgTemplateMaterialPO = msgMaterialMapper.selectById(templateImageId);
        if (msgTemplateMaterialPO != null) {
            msgTemplateMaterialDTO.setId(msgTemplateMaterialPO.getId());
            msgTemplateMaterialDTO.setMaterialName(msgTemplateMaterialPO.getMaterialName());
            msgTemplateMaterialDTO.setMaterialLocalUrl(msgTemplateMaterialPO.getMaterialLocalUrl());
            msgTemplateDTO.setMsgTemplateMaterialDTO(msgTemplateMaterialDTO);
        }
        // 模板按钮对象列表获取
        List<MsgTemplateButtonDTO> msgTemplateButtonDTOLists = new ArrayList<>();
        List<MsgTemplateToButtonPO> msgTemplateToButtonPOList = msgTemplateToButtonMapper.getMsgTemplateToButtonById(templateId);
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
        return msgTemplateDTO;
    }

    @Override
    public String deleteTemplateById(String templateId) {
        //查询该卡片是否已经绑定卡片组
        int count = bGroupTemplateMapper.getCountByTemplateId(templateId);
        if (count != 0) {
            return "该卡片已经绑定卡片组，不可删除";
        }
        // 删除模板表中相关内容
        msgTemplateMapper.deleteById(templateId);
        // 删除模板按钮关系表中相关内容
        msgTemplateToButtonMapper.deleteByTemplateId(templateId);
        // 删除关键词模板关联表
        List<BKeywordTemplatePO> poList = cKeywordTemplateMapper.getKeywordTemplateByTemplateId(templateId);
        List<Integer> keyWordIds = new ArrayList<>();
        for (BKeywordTemplatePO po : poList) {
            keyWordIds.add(po.getKeyWordId());
            cKeywordTemplateMapper.deleteById(po.getId());
        }
        // 删除关键词模板信息
        for (Integer keyWordId : keyWordIds) {
            cMsgKeywordMapper.deleteById(keyWordId);
        }
        return "删除成功";
    }

    @Override
    public List<MsgTemplateTagGroupDTO> getTagList() {
        List<MsgTemplateTagGroupDTO> msgTemplateTagGroupDTOList = new ArrayList<>();
        List<String> tagTypes = msgTemplateTagMapper.getTagGroup();
        for (String tagType : tagTypes) {
            MsgTemplateTagGroupDTO msgTemplateTagGroupDTO = new MsgTemplateTagGroupDTO();
            List<MsgTemplateTagDTO> msgTemplateContentDTOList = new ArrayList<>();
            List<MsgTemplateTagPO> lists = msgTemplateTagMapper.getTagList(tagType);
            for (MsgTemplateTagPO msgTemplateContentPO : lists) {
                MsgTemplateTagDTO msgTemplateContentDTO = new MsgTemplateTagDTO();
                msgTemplateContentDTO.setId(msgTemplateContentPO.getId());
                msgTemplateContentDTO.setTagName(msgTemplateContentPO.getTagName());
                msgTemplateContentDTO.setTagValue(msgTemplateContentPO.getTagValue());
                msgTemplateContentDTOList.add(msgTemplateContentDTO);
            }
            msgTemplateTagGroupDTO.setTagType(tagType);
            msgTemplateTagGroupDTO.setMsgTemplateTagDTO(msgTemplateContentDTOList);
            msgTemplateTagGroupDTOList.add(msgTemplateTagGroupDTO);
        }
        return msgTemplateTagGroupDTOList;
    }

    @Override
    public List<MsgTemplateButtonDTO> getButtonList(CMsgUserPO user, String buttonType) {
        List<MsgTemplateButtonDTO> msgTemplateButtonDTOList = new ArrayList<>();
        EntityWrapper<MsgTemplateButtonPO> wrapper = new EntityWrapper();
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        wrapper.eq("BUTTON_TYPE", buttonType);
        List<MsgTemplateButtonPO> lists = msgTemplateButtonMapper.selectList(wrapper);
        for (MsgTemplateButtonPO msgTemplateButtonPO : lists) {
            MsgTemplateButtonDTO msgTemplateButtonDTO = new MsgTemplateButtonDTO();
            msgTemplateButtonDTO.setId(msgTemplateButtonPO.getId());
            msgTemplateButtonDTO.setButtonName(msgTemplateButtonPO.getButtonName());
            msgTemplateButtonDTO.setButtonContent(msgTemplateButtonPO.getButtonContent());
            msgTemplateButtonDTOList.add(msgTemplateButtonDTO);
        }
        return msgTemplateButtonDTOList;
    }

    @Override
    public String createButton(CMsgUserPO user, MsgTemplateButtonVO msgTemplateButtonVO) {
        MsgTemplateButtonPO msgTemplateButtonPO = new MsgTemplateButtonPO();
        msgTemplateButtonPO.setButtonType(msgTemplateButtonVO.getButtonType());
        msgTemplateButtonPO.setButtonName(msgTemplateButtonVO.getButtonName());
        msgTemplateButtonPO.setButtonContent(msgTemplateButtonVO.getButtonContent());
        if (Objects.isNull(msgTemplateButtonVO.getId()) || msgTemplateButtonVO.getId().equals("")) {
            String buttonId = UUID.randomUUID().toString().replace("-", "");
            msgTemplateButtonPO.setId(buttonId);
            msgTemplateButtonPO.setCreatedOrg(String.valueOf(user.getOrganId()));
            msgTemplateButtonPO.setCreatedBy(String.valueOf(user.getId()));
            msgTemplateButtonPO.setCreatedTime(new Date());
            msgTemplateButtonMapper.insert(msgTemplateButtonPO);
        } else {
            msgTemplateButtonPO.setId(msgTemplateButtonVO.getId());
            msgTemplateButtonPO.setUpdatedBy(String.valueOf(user.getId()));
            msgTemplateButtonPO.setUpdatedTime(new Date());
            msgTemplateButtonPO.setCreatedOrg(msgTemplateButtonVO.getCreatedOrg());
            msgTemplateButtonPO.setCreatedBy(msgTemplateButtonVO.getCreatedBy());
            msgTemplateButtonPO.setCreatedTime(msgTemplateButtonVO.getCreatedTime());
            msgTemplateButtonMapper.updateById(msgTemplateButtonPO);
        }
        return "保存成功";
    }


    @Override
    public String createTemplate(CMsgUserPO user, MsgTemplateVO msgTemplateVO) {
        // 按钮的存储处理
        List<String> buttonIds = new ArrayList<>();
        List<MsgTemplateButtonVO> msgTemplateButtonVOList = msgTemplateVO.getMsgTemplateButtonVO();
        for (MsgTemplateButtonVO msgTemplateButtonVO : msgTemplateButtonVOList) {
            MsgTemplateButtonPO msgTemplateButtonPO = new MsgTemplateButtonPO();
            msgTemplateButtonPO.setButtonType(msgTemplateButtonVO.getButtonType());
            msgTemplateButtonPO.setButtonName(msgTemplateButtonVO.getButtonName());
            msgTemplateButtonPO.setButtonContent(msgTemplateButtonVO.getButtonContent());
            if (StringUtils.isEmpty(msgTemplateButtonVO.getId())) {// 新增按钮
                String buttonId = UUID.randomUUID().toString().replace("-", "");
                msgTemplateButtonPO.setId(buttonId);
                msgTemplateButtonPO.setCreatedOrg(String.valueOf(user.getOrganId()));
                msgTemplateButtonPO.setCreatedBy(String.valueOf(user.getId()));
                msgTemplateButtonPO.setCreatedTime(new Date());
                msgTemplateButtonMapper.insert(msgTemplateButtonPO);
                buttonIds.add(buttonId);
            } else {// 更新按钮
                msgTemplateButtonPO.setId(msgTemplateButtonVO.getId());
                msgTemplateButtonPO.setUpdatedBy(String.valueOf(user.getId()));
                msgTemplateButtonPO.setUpdatedTime(new Date());
                msgTemplateButtonPO.setCreatedOrg(msgTemplateButtonVO.getCreatedOrg());
                msgTemplateButtonPO.setCreatedBy(msgTemplateButtonVO.getCreatedBy());
                msgTemplateButtonPO.setCreatedTime(msgTemplateButtonVO.getCreatedTime());
                msgTemplateButtonMapper.updateById(msgTemplateButtonPO);
                buttonIds.add(msgTemplateButtonVO.getId());
            }
        }
        MsgTemplatePO msgTemplatePO = new MsgTemplatePO();
        // 素材id，标题，模板类型，，模板内容存储到模板表
        msgTemplatePO.setTemplateStrategy(MsgTemplateStrategyConstant.DynamicTagTemplateStrategy);
        msgTemplatePO.setModuleId(msgTemplateVO.getModuleId());
        msgTemplatePO.setTemplateName(msgTemplateVO.getTemplateName());
        msgTemplatePO.setTemplateType(msgTemplateVO.getTemplateType());
        msgTemplatePO.setTemplateImageId(msgTemplateVO.getTemplateImageId());
        msgTemplatePO.setTemplateTitle(msgTemplateVO.getTemplateTitle());
        msgTemplatePO.setTemplateContent(msgTemplateVO.getTemplateContent());
        //设置动态模板ID
        msgTemplatePO.setDynamicTemplateId(msgTemplateVO.getDynamicTemplateId());

        String templateId;
        if (Objects.isNull(msgTemplateVO.getId()) || msgTemplateVO.getId().equals("")) {// 新增
            templateId = UUID.randomUUID().toString().replace("-", "");
            msgTemplatePO.setId(templateId);
            msgTemplatePO.setCreatedOrg(String.valueOf(user.getOrganId()));
            msgTemplatePO.setCreatedBy(String.valueOf(user.getId()));
            msgTemplatePO.setCreatedTime(new Date());
            msgTemplateMapper.insert(msgTemplatePO);
        } else {// 更新
            templateId = msgTemplateVO.getId();
            msgTemplatePO.setId(templateId);
            msgTemplatePO.setUpdatedBy(String.valueOf(user.getId()));
            msgTemplatePO.setUpdatedTime(new Date());
            msgTemplatePO.setCreatedOrg(msgTemplateVO.getCreatedOrg());
            msgTemplatePO.setCreatedBy(msgTemplateVO.getCreatedBy());
            msgTemplatePO.setCreatedTime(msgTemplateVO.getCreatedTime());
            msgTemplateMapper.updateById(msgTemplatePO);
            // 删除旧的模板按钮关系表中相关内容
            msgTemplateToButtonMapper.deleteByTemplateId(templateId);
        }
        // 将新的关系插入模板按钮关系表
        int buttonSort = 1;
        for (String buttonId : buttonIds) {
            MsgTemplateToButtonPO msgTemplateToButtonPO = new MsgTemplateToButtonPO();
            msgTemplateToButtonPO.setId(UUID.randomUUID().toString().replace("-", ""));
            msgTemplateToButtonPO.setTemplateId(templateId);
            msgTemplateToButtonPO.setButtonId(buttonId);
            msgTemplateToButtonPO.setSort(buttonSort);
            buttonSort += 1;
            msgTemplateToButtonMapper.insert(msgTemplateToButtonPO);
        }
        return "保存成功";
    }

    @Override
    public MsgTemplatePageDTO getTemplateByModuleId(CMsgUserPO user, Integer currentPage, Integer pageSize, String title, String type) {
        List<MsgTemplateDTO> msgTemplateDTOList = new ArrayList<>();
        EntityWrapper<MsgTemplatePO> wrapper = new EntityWrapper();
        if (StringUtils.isNotEmpty(type)) {
            wrapper.eq("TEMPLATE_TYPE", type);
        }
        if (StringUtils.isNotEmpty(title)) {
            wrapper.like("TEMPLATE_TITLE", title);
        }
        wrapper.in("CREATED_ORG", user.getOrgs());
        wrapper.orderBy("CREATED_TIME", false);
        int totalSize = msgTemplateMapper.selectCount(wrapper);
        Page<MsgTemplatePO> page = new Page<>(currentPage, pageSize);
        List<MsgTemplatePO> lists = msgTemplateMapper.selectPage(page, wrapper);
        for (MsgTemplatePO msgTemplatePO : lists) {
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
            msgTemplateDTO.setCreatedBy(cMsgUserMapper.selectById(msgTemplatePO.getCreatedBy()).getUserName());
            msgTemplateDTO.setCreatedTime(msgTemplatePO.getCreatedTime());
            msgTemplateDTOList.add(msgTemplateDTO);
        }
        MsgTemplatePageDTO pageDTO = new MsgTemplatePageDTO();
        pageDTO.setTotolSize(totalSize);
        pageDTO.setData(msgTemplateDTOList);
        return pageDTO;
    }

    @Override
    public List<Map> getLinkList(CMsgUserPO user, Integer moduleId) {
        List<Map> list = new ArrayList<>();
        if (moduleId == 4) {
            EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
            wrapper.orderBy("CREATED_TIME", false);
            wrapper.in("ORGAN_ID", user.getOrgs());
            wrapper.eq("MODULAR", moduleId);
            wrapper.eq("TYPE", 2);
            wrapper.eq("STATUS", 3);
            List<MsgH5ProductPO> lists = msgH5ProductMapper.selectList(wrapper);
            for (MsgH5ProductPO msgH5ProductPO : lists) {
                Map map = new HashMap();
                if (msgH5ProductPO.getTagId() == null) {
                    continue;
                }
                // 发布单位名称获取
                String organName = cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName();
                map.put("name", msgH5ProductPO.getTitle());
                map.put("tag", organName);
                String url = "http://tj.msgcloud.80php.com/#/healthDetail?id=" + msgH5ProductPO.getId();
                map.put("value", url);
                list.add(map);
            }
        } else if (moduleId == 8) {
            EntityWrapper<BH5ProductTourPO> wrapper = new EntityWrapper();
            wrapper.orderBy("CREATED_TIME", false);
            wrapper.eq("STATUS", 3);
            List<BH5ProductTourPO> lists = bh5ProductTourMapper.selectList(wrapper);
            for (BH5ProductTourPO po : lists) {
                MsgH5ProductPO msgH5ProductPO = msgH5ProductMapper.selectById(po.getRelatedArticle());
                List orgs = user.getOrgs();
                if (!orgs.contains(msgH5ProductPO.getOrganId())) {
                    continue;
                }
                Map map = new HashMap();
                // 发布单位名称获取
                String organName = cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName();
                map.put("name", po.getName());
                map.put("tag", organName);
                String url = "http://tj.msgcloud.80php.com/#/travelDetail?id=" + po.getId();
                map.put("value", url);
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public List<Map> getCustomLinkList(CMsgUserPO user) {
        List<Map> list = new ArrayList<>();
        // 非旅游文章获取
        EntityWrapper<MsgH5ProductPO> wrapper = new EntityWrapper();
        wrapper.orderBy("CREATED_TIME", false);
        wrapper.in("ORGAN_ID", user.getOrgs());
        wrapper.eq("TYPE", 2);
        wrapper.eq("STATUS", 3);
        List<MsgH5ProductPO> lists = msgH5ProductMapper.selectList(wrapper);
        for (MsgH5ProductPO msgH5ProductPO : lists) {
            Map map = new HashMap();
            if (msgH5ProductPO.getTagId() == null) {
                continue;
            }
            // 发布单位名称获取
            String organName = cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName();
            map.put("name", msgH5ProductPO.getTitle());
            map.put("tag", organName);
            String url = "http://tj.msgcloud.80php.com/#/healthDetail?id=" + msgH5ProductPO.getId();
            map.put("value", url);
            list.add(map);
        }
        // 旅游文章获取
        EntityWrapper<BH5ProductTourPO> wrapperTour = new EntityWrapper();
        wrapperTour.orderBy("CREATED_TIME", false);
        wrapperTour.eq("STATUS", 3);
        List<BH5ProductTourPO> tourLists = bh5ProductTourMapper.selectList(wrapperTour);
        for (BH5ProductTourPO po : tourLists) {
            MsgH5ProductPO msgH5ProductPO = msgH5ProductMapper.selectById(po.getRelatedArticle());
            List orgs = user.getOrgs();
            if (!orgs.contains(msgH5ProductPO.getOrganId())) {
                continue;
            }
            Map map = new HashMap();
            // 发布单位名称获取
            String organName = cMsgOrganMapper.selectById(msgH5ProductPO.getOrganId()).getOrganName();
            map.put("name", po.getName());
            map.put("tag", organName);
            String url = "http://tj.msgcloud.80php.com/#/travelDetail?id=" + po.getId();
            map.put("value", url);
            list.add(map);
        }
        return list;
    }

}
