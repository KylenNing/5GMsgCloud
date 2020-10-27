package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
@TableName("b_msg_template")
public class MsgTemplatePO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("TEMPLATE_STRATEGY")
    private String templateStrategy;

    @TableField("MODULE_ID")
    private Integer moduleId;

    @TableField("TEMPLATE_NAME")
    private String templateName;

    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @TableField("TEMPLATE_IMAGE_ID")
    private String templateImageId;

    @TableField("TEMPLATE_TITLE")
    private String templateTitle;

    @TableField("TEMPLATE_CONTENT")
    private String templateContent;

    @TableField("TEMPLATE_CONFIG")
    private String templateConfig;

    @TableField("DYNAMIC_TEMPLATE_ID")
    private String dynamicTemplateId;

    @TableField("CREATED_ORG")
    private String createdOrg;

    @TableField("CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    @TableField("UPDATED_BY")
    private String updatedBy;
}
