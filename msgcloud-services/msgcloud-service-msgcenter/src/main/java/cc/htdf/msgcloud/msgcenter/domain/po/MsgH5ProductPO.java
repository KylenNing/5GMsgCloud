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
@TableName("b_msg_h5product")
public class MsgH5ProductPO {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("TITLE")
    private String title;

    @TableField("ORGAN_ID")
    private Integer organId;

    @TableField("MODULAR")
    private Integer modular;

    @TableField("TAG_ID")
    private String tagId;

    @TableField("TYPE")
    private Integer type;

    @TableField("MEDIA_URL")
    private String mediaUrl;

    @TableField("MEDIA_SL_URL")
    private String mediaSlUrl;

    @TableField("MEDIA_RC_URL")
    private String mediaRcUrl;

    @TableField("MEDIA_RC_SL_URL")
    private String mediaRcSlUrl;

    @TableField("DESCRIBE")
    private String describe;

    @TableField("SUITABLE")
    private String suitable;

    @TableField("AVOID")
    private String avoid;

    @TableField("STATUS")
    private Integer status;

    @TableField("REASON")
    private String reason;

    @TableField("VIEW_COUNT")
    private Integer viewCount;

    @TableField("UP_COUNT")
    private Integer upCount;

    @TableField("BROWSE_COUNT")
    private Integer browseCount;

    @TableField("RELEASE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date releaseTime;

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
    
    @TableField(exist = false) 
    private String name;
}
