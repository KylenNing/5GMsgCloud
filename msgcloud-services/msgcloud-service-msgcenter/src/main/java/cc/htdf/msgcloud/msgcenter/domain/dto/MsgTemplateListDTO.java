package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgTemplateListDTO {

    private String id;

    private String moduleName;

    private String templateTitle;

    private String templateType;

    private String createdOrg;

    private String createdBy;

    private String templateContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
