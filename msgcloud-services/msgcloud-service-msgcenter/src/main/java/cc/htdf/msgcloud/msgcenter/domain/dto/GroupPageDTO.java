package cc.htdf.msgcloud.msgcenter.domain.dto;


import cc.htdf.msgcloud.msgcenter.domain.po.BGroupTemplatePO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/10/6
 * @Description: TODO
 */
@Data
public class GroupPageDTO {

    private Integer groupId;

    private Integer moduleId;

    private String groupName;

    private List<BGroupTemplatePO> gtList;

    private List<MsgTemplateDTO> templateList;

    private List<MsgMenuDTO> menuList;
}