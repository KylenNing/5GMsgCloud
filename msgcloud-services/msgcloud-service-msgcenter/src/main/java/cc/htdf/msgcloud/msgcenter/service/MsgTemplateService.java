package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgTemplateButtonDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgTemplateDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgTemplatePageDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgTemplateTagGroupDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgTemplateButtonVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgTemplateVO;

import java.util.List;
import java.util.Map;


/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface MsgTemplateService {

    MsgTemplatePageDTO getTemplateList(CMsgUserPO user, String templateName, Integer organId, Integer modularId, Integer pageNum, Integer pageSize);

    List<MsgTemplateDTO> getTemplateLists(CMsgUserPO user, String templateType);

    MsgTemplateDTO getTemplateById(String templateId);

    String deleteTemplateById(String templateId);

    List<MsgTemplateTagGroupDTO> getTagList();

    List<MsgTemplateButtonDTO> getButtonList(CMsgUserPO user, String buttonType);

    String createButton(CMsgUserPO user, MsgTemplateButtonVO msgTemplateButtonVO);

    String createTemplate(CMsgUserPO user, MsgTemplateVO msgTemplateVO);

    MsgTemplatePageDTO getTemplateByModuleId(CMsgUserPO user, Integer currentPage, Integer pageSize, String title, String type);

    List<Map> getLinkList(CMsgUserPO user, Integer moduleId);

    List<Map> getCustomLinkList(CMsgUserPO user);
}
