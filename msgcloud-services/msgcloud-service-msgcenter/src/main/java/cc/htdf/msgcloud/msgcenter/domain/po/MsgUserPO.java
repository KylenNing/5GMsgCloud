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
@TableName("b_msg_user")
public class MsgUserPO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("USER_NAME")
    private String userName;

    @TableField("USER_TEL")
    private String userTel;

    @TableField("USER_ATTRIBUTE_ID")
    private int userAttributeId;

    @TableField("USER_BEHAVIOR_ID")
    private int userBehaviorId;

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
