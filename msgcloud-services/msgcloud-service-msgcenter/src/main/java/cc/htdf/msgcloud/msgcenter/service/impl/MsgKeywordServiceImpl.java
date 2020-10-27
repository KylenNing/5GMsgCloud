package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.domain.dto.*;
import cc.htdf.msgcloud.msgcenter.domain.po.*;
import cc.htdf.msgcloud.msgcenter.domain.vo.AllKeyWordVO;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.MsgKeywordService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

;

/**
 * @Author: ningyq
 * @Date: 2020/8/11
 * @Description: TODO
 */
@Service
public class MsgKeywordServiceImpl implements MsgKeywordService {

    @Resource
    private BMsgKeywordMapper bMsgKeywordMapper;

    @Resource
    private BKeywordTemplateMapper bKeywordTemplateMapper;

    @Resource
    private BMsgMenuMapper bMsgMenuMapper;

    @Resource
    private BKeywordMenuMapper bKeywordMenuMapper;

    @Resource
    private SeveiceNumMapper seveiceNumMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Resource
    private BGroupTemplateMapper bGroupTemplateMapper;

    @Resource
    private BGroupMenuMapper bGroupMenuMapper;


    @Resource
    private BKeywordDictMapper bKeywordDictMapper;

    @Resource
    private MsgMaterialMapper msgMaterialMapper;

    @Resource
    private MsgTemplateToButtonMapper msgTemplateToButtonMapper;

    @Resource
    private MsgTemplateButtonMapper msgTemplateButtonMapper;

    @Override
    public Map getAllKeyWord(CMsgUserPO user, String keyWord, Integer currentPage, Integer pageSize, String serviceId) {
        Map resMap = new HashMap();
        EntityWrapper<SeveiceNumPO> serviceNumWrapper = new EntityWrapper();
        List<SeveiceNumPO> serviceNumList = seveiceNumMapper.selectList(serviceNumWrapper);
        Map<String, SeveiceNumPO> serviceNumMap = serviceNumList.stream().
                collect(Collectors.toMap(SeveiceNumPO::getId, data -> data));
        EntityWrapper<BMsgKeywordPO> wrapper = new EntityWrapper();
        if (!serviceId.equals("") && Objects.nonNull(serviceId)) {
            wrapper.eq("service_id", serviceId);
        }
        wrapper.in("created_org", user.getOrgs());
        int totalSize = bMsgKeywordMapper.selectCount(wrapper);
        if (Objects.nonNull(keyWord) && !keyWord.equals("")) {
            wrapper.like("key_word", keyWord);
        }
        wrapper.orderBy("created_time", false);
        if (Objects.isNull(currentPage) && Objects.isNull(pageSize)) {
            List<BMsgKeywordPO> dataList = bMsgKeywordMapper.selectList(wrapper);
            dataList = replaceServiceIdToName(dataList, serviceNumMap);
            List<AllKeyWordVO> resList = addCreatedUserNameToKeywordList(dataList, user);
            resMap.put("DATA", resList);
        } else {
            Page<BMsgKeywordPO> page = new Page<>(currentPage, pageSize);
            List<BMsgKeywordPO> pageList = bMsgKeywordMapper.selectPage(page, wrapper);
            pageList = replaceServiceIdToName(pageList, serviceNumMap);
            List<AllKeyWordVO> resList = addCreatedUserNameToKeywordList(pageList, user);
            resMap.put("DATA", resList);
        }
        resMap.put("totalSize", totalSize);
        return resMap;
    }

    @Override
    public String addKeyWord(CMsgUserPO user, KeyWordPageDTO dto) {
        String keyWords = dto.getKeyword().getKeyWord();
        String message = "";
        String[] spKeyWords = keyWords.split(",");
        BMsgKeywordPO bMsgKeywordPO = dto.getKeyword();
        for (String keyWord : spKeyWords) {
            bMsgKeywordPO.setKeyWord(keyWord);
            int createdBy = user.getId();
            int createdOrg = user.getOrganId();
            Integer groupId = dto.getGroupId();
            bMsgKeywordPO.setCreatedBy(createdBy);
            bMsgKeywordPO.setCreatedOrg(createdOrg);
            bMsgKeywordPO.setGroupId(groupId);
            Map paramMap = new HashMap();
            paramMap.put("key_word", bMsgKeywordPO.getKeyWord());
            paramMap.put("service_id", bMsgKeywordPO.getServiceId());
            int tempSize = bMsgKeywordMapper.selectByMap(paramMap).size();
            if (tempSize == 0) {
                if (Objects.nonNull(bMsgKeywordPO.getId()) && bMsgKeywordPO.getId() != 0) {
                    bMsgKeywordPO.setId(0);
                    bMsgKeywordPO.setUpdatedTime(new Date());
                    for (BKeywordTemplatePO kt : dto.getKtList()) {
                        kt.setUpdatedTime(new Date());
                    }
                    for (MsgMenuDTO menuDto : dto.getMenuList()) {
                        menuDto.setUpdatedTime(new Date());
                    }
                    //deleteKeyWordById(bMsgKeywordPO.getId());
                } else {
                    bMsgKeywordPO.setCreatedTime(new Date());
                    for (BKeywordTemplatePO kt : dto.getKtList()) {
                        kt.setCreatedTime(new Date());
                    }
                    for (MsgMenuDTO menuDto : dto.getMenuList()) {
                        menuDto.setCreatedTime(new Date());
                    }
                }
                bMsgKeywordMapper.insert(bMsgKeywordPO);
                int keyWordId = bMsgKeywordPO.getId();
                if (Objects.isNull(groupId) || groupId == 0) {
                    for (BKeywordTemplatePO keywordTemplate : dto.getKtList()) {
                        keywordTemplate.setKeyWordId(keyWordId);
                        keywordTemplate.setCreatedBy(createdBy);
                        keywordTemplate.setCreatedOrg(createdOrg);
                        bKeywordTemplateMapper.insert(keywordTemplate);
                    }
                    for (MsgMenuDTO menu : dto.getMenuList()) {
                        BMsgMenuPO msgMenuPO = new BMsgMenuPO();
                        msgMenuPO.setMenuName(menu.getMenuName());
                        msgMenuPO.setMenuType(menu.getMenuType());
                        msgMenuPO.setMenuContent(menu.getMenuContent());
                        msgMenuPO.setCreatedBy(createdBy);
                        msgMenuPO.setCreatedTime(menu.getCreatedTime());
                        msgMenuPO.setCreatedOrg(createdOrg);
                        bMsgMenuMapper.insert(msgMenuPO);
                        int menuId = msgMenuPO.getId();
                        BKeywordMenuPO keywordMenuPO = new BKeywordMenuPO();
                        keywordMenuPO.setKeyWordId(keyWordId);
                        keywordMenuPO.setMenuId(menuId);
                        keywordMenuPO.setMenuSort(menu.getMenuSort());
                        keywordMenuPO.setCreatedBy(createdBy);
                        keywordMenuPO.setCreatedTime(menu.getCreatedTime());
                        keywordMenuPO.setCreatedOrg(createdOrg);
                        bKeywordMenuMapper.insert(keywordMenuPO);
                    }
                }
                message = "成功添加关键字！";
            } else {
                message = "当前服务号下的该关键字已经存在，请更改关键字名称";
            }
        }
        return message;

    }

    @Override
    public void deleteKeyWordById(Integer keyWordId) {
        bMsgKeywordMapper.deleteKeywordById(keyWordId);
        bKeywordTemplateMapper.deleteByKeywordId(keyWordId);
        List<Integer> menuIdList = bKeywordMenuMapper.selectMenuIdByKeywordId(keyWordId);
        bKeywordMenuMapper.deleteByKeywordId(keyWordId);
        for (int menuId : menuIdList) {
            bMsgMenuMapper.deleteByMenuId(menuId);
        }
    }

    @Override
    public KeyWordPageDTO getKeyWordInfoById(Integer keyWordId) {
        KeyWordPageDTO dto = new KeyWordPageDTO();
        BMsgKeywordPO keyword = bMsgKeywordMapper.getKeywordById(keyWordId);
        dto.setKeyword(keyword);
        Integer groupId = keyword.getGroupId();
        dto.setGroupId(groupId);
        if (Objects.isNull(groupId) || groupId == 0) {
//            List<BKeywordTemplatePO> keywordTemplateList = bKeywordTemplateMapper.getKeywordTemplateByKeyworId(keyWordId);
            List<BKeywordMenuPO> keywordMenuList = bKeywordMenuMapper.selecByKeywordId(keyWordId);
            List<MsgMenuDTO> menuDTOList = new ArrayList<>();
            for (BKeywordMenuPO keywordMenu : keywordMenuList) {
                MsgMenuDTO msgMenuDto = new MsgMenuDTO();
                BMsgMenuPO bMsgMenuPO = bMsgMenuMapper.selectMenuById(keywordMenu.getMenuId());
                msgMenuDto.setMenuName(bMsgMenuPO.getMenuName());
                msgMenuDto.setMenuType(bMsgMenuPO.getMenuType());
                msgMenuDto.setMenuContent(bMsgMenuPO.getMenuContent());
                msgMenuDto.setMenuSort(keywordMenu.getMenuSort());
                msgMenuDto.setCreatedOrg(bMsgMenuPO.getCreatedOrg());
                msgMenuDto.setCreatedBy(bMsgMenuPO.getCreatedBy());
                msgMenuDto.setCreatedTime(bMsgMenuPO.getCreatedTime());
                msgMenuDto.setUpdatedBy(bMsgMenuPO.getUpdatedBy());
                msgMenuDto.setUpdatedTime(bMsgMenuPO.getUpdatedTime());
                menuDTOList.add(msgMenuDto);
            }
            List<MsgTemplatePO> keywordTemplateList = msgTemplateMapper.getKeywordTemplateByKeyworId(keyWordId);
            List<MsgTemplateDTO> templateDTOList = makeTemplateList(keywordTemplateList);
            dto.setTemplateList(templateDTOList);
            if (keywordTemplateList != null && !keywordTemplateList.isEmpty()) {
                String templateId = keywordTemplateList.get(0).getId();
                String templateType = msgTemplateMapper.selectById(templateId).getTemplateType();
                dto.setTemplateType(templateType);
            }
//            dto.setKtList(keywordTemplateList);
            dto.setMenuList(menuDTOList);
        } else {
            List<BKeywordTemplatePO> keywordTemplateList = new ArrayList<>();
//            List<String> templateIdList = bGroupTemplateMapper.getTemplateIdByGroupId(groupId);
//            for(String tId : templateIdList){
//                BKeywordTemplatePO kt = new BKeywordTemplatePO();
//                kt.setTemplateId(tId);
//                keywordTemplateList.add(kt);
//            }
//            dto.setKtList(keywordTemplateList);
            List<MsgTemplatePO> templateIdList = msgTemplateMapper.getTemplateIdByGroupId(groupId);
            List<MsgTemplateDTO> templateDTOList = makeTemplateList(templateIdList);
            dto.setTemplateList(templateDTOList);
            List<MsgMenuDTO> menuDTOList = new ArrayList<>();
            List<Integer> menuIdList = bGroupMenuMapper.selectMenuIdByGroupId(groupId);
            EntityWrapper<BMsgMenuPO> entityWrapper = new EntityWrapper<>();
            entityWrapper.in("id", menuIdList);
            List<BMsgMenuPO> menuList = new ArrayList<>();
            if (menuIdList.size() != 0) {
                menuList = bMsgMenuMapper.selectList(entityWrapper);
            }
            for (BMsgMenuPO menu : menuList) {
                MsgMenuDTO msgMenuDTO = new MsgMenuDTO();
                BeanUtils.copyProperties(menu, msgMenuDTO);
                msgMenuDTO.setMenuSort(bGroupMenuMapper.selectMenuSortByGroupAndMenu(groupId, menu.getId()));
                menuDTOList.add(msgMenuDTO);
            }
            dto.setMenuList(menuDTOList);

        }
        return dto;
    }

    List<MsgTemplateDTO> makeTemplateList(List<MsgTemplatePO> templateList) {
        List<MsgTemplateDTO> msgTemplateDTOList = new ArrayList<>();
        for (MsgTemplatePO msgTemplatePO : templateList) {
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
            msgTemplateDTOList.add(msgTemplateDTO);
        }
        return msgTemplateDTOList;
    }

    @Override
    public Set<BKeywordDictPO> getAllKeyWordByServiceId(String serviceId) {
        // TODO Auto-generated method stub
        Set<BKeywordDictPO> set = new HashSet<BKeywordDictPO>();
        List<BMsgKeywordPO> keyword = bMsgKeywordMapper.getKeywordByServiceId(serviceId);
        List<BKeywordDictPO> dirctList = new ArrayList<>();
        for (BMsgKeywordPO bMsgKeywordPO : keyword) {
            List<BKeywordDictPO> keyWordDirct = bKeywordDictMapper.getNameByKeyWord(bMsgKeywordPO.getKeyWord());
            for (BKeywordDictPO bKeywordDictPO : keyWordDirct) {
                String name = bKeywordDictPO.getName();
                List<BMsgKeywordPO> keywo = bMsgKeywordMapper.getKeywordByName(name, serviceId);
                if (keywo == null || keywo.size() == 0) {
                    dirctList.add(bKeywordDictPO);
                }
            }
        }

        if (keyword == null || keyword.size() == 0) {
            set = bKeywordDictMapper.getNameAllKeyWord();
        }
        for (BKeywordDictPO bKeywordDictPO : dirctList) {
            set.add(bKeywordDictPO);
        }

        return set;
    }


    @Override
    public List<BMsgKeywordPO> findByKeyworkAndServiceId(String keyword, String serviceId) {
        Map<String, Object> param = new HashMap<>();
        param.put("SERVICE_ID", serviceId);
        param.put("KEY_WORD", keyword);
        return bMsgKeywordMapper.selectByMap(param);
    }

    List<BMsgKeywordPO> replaceServiceIdToName(List<BMsgKeywordPO> keyWordList, Map<String, SeveiceNumPO> numMap) {
        for (BMsgKeywordPO po : keyWordList) {
            po.setServiceId(numMap.get(po.getServiceId()).getChatbotName());
        }
        return keyWordList;
    }

    List<AllKeyWordVO> addCreatedUserNameToKeywordList(List<BMsgKeywordPO> keywordList, CMsgUserPO user) {
        EntityWrapper<CMsgUserPO> wrapper = new EntityWrapper<>();
        Map<Integer, CMsgUserPO> keywordMap = cMsgUserMapper.selectList(wrapper).
                stream().collect(Collectors.toMap(CMsgUserPO::getId, data -> data));
        List<AllKeyWordVO> resList = new ArrayList<>();
        for (BMsgKeywordPO keyword : keywordList) {
            AllKeyWordVO allKeyWordVO = new AllKeyWordVO();
            BeanUtils.copyProperties(keyword, allKeyWordVO);
            String createdUserName = keywordMap.get(keyword.getCreatedBy()).getUserName();
            allKeyWordVO.setCreatedUserName(createdUserName);
            resList.add(allKeyWordVO);
        }
        return resList;
    }


}