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
 * @Date: 2020/8/31
 * @Description:
 */
@Data
@TableName("b_msg_product")
public class MsgProductPO {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("PRODUCT_TITLE")
    private String productTitle;

    @TableField("PRODUCT_STYLE")
    private String productStyle;

    @TableField("PRODUCT_AREA")
    private Integer productArea;

    @TableField("PRODUCT_DESCRIBE")
    private String productDescribe;

    @TableField("MESSAGE_STATUS")
    private Integer messageStatus;

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
