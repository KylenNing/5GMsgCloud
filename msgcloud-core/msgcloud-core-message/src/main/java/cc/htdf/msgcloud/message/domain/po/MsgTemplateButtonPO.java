package cc.htdf.msgcloud.message.domain.po;

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
@TableName("b_msg_template_button")
public class MsgTemplateButtonPO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("BUTTON_TYPE")
    private String buttonType;

    @TableField("BUTTON_NAME")
    private String buttonName;

    @TableField("BUTTON_CONTENT")
    private String buttonContent;

    @TableField("CREATED_ORG")
    private String createdOrg;

    @TableField("CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField("CREATED_BY")
    private String createdUser;

    @TableField("UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @TableField("UPDATED_BY")
    private String updatedUser;
}
