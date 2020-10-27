package cc.htdf.msgcloud.msgcenter.domain.dto;

import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordTemplatePO;
import cc.htdf.msgcloud.msgcenter.domain.po.BMsgKeywordPO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/11
 * @Description: TODO
 */
@Data
public class KeyWordPageDTO {


    private String templateType;

    private Integer groupId;

    private BMsgKeywordPO keyword;

    private List<BKeywordTemplatePO> ktList;

    private List<MsgTemplateDTO> templateList;

    private List<MsgMenuDTO> menuList;
}