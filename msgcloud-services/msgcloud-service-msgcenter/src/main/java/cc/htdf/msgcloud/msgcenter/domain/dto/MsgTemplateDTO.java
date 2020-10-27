package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgTemplateDTO {

    private String id;
    private Integer moduleId;
    private String templateName;
    private String templateType;
    private MsgMaterialDTO msgTemplateMaterialDTO;
    private String templateTitle;
    private String templateContent;
    private List<MsgTemplateButtonDTO> msgTemplateButtonDTO;
    private String dynamicTemplateId;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
