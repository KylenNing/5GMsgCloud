package cc.htdf.msgcloud.msgcenter.domain.dto;

import cc.htdf.msgcloud.msgcenter.domain.po.BMessagePO;
import cc.htdf.msgcloud.msgcenter.domain.po.BMessageTemplatePO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/17
 * @Description: TODO
 */
@Data
public class MessagePageDTO {

    private String templateType;

    private BMessagePO message;

    private List<BMessageTemplatePO> mtList;

    private List<MsgTemplateDTO> templateList;

    private List<MsgMenuDTO> menuList;

    private List<String> lableList;
}