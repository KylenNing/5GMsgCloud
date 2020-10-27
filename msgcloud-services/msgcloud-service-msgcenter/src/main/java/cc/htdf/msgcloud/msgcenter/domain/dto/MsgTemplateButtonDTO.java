package cc.htdf.msgcloud.msgcenter.domain.dto;

import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgTemplateButtonDTO {

    private String id;
    private String buttonType;
    private String buttonName;
    private String buttonContent;
}
