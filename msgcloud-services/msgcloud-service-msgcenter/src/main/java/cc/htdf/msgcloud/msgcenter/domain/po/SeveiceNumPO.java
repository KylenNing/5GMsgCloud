package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: renxh
 * @Date: 2020/8/12
 * @Description:
 */
@Data
@TableName("b_seveice_num")
public class SeveiceNumPO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("CHATBOT_ID")
    private String chatbotId;

    @TableField("CHANNEL_ID")
    private String channelId;

    @TableField("CHATBOT_NAME")
    private String chatbotName;

    @TableField("APP_ID")
    private String appId;

    @TableField("APP_SECRET")
    private String appSecret;

    @TableField("CSP_CODE")
    private String cspCode;

    @TableField("LOGO_URL")
    private String logoUrl;

    @TableField("CREATED_ORG")
    private String createdOrg;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private String updatedBy;

    @TableField("UPDATED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

}
