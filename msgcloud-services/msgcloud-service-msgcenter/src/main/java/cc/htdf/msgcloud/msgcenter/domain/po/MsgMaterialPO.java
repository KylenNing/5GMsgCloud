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
@TableName("b_msg_material")
public class MsgMaterialPO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("MATERIAL_LABEL_ID")
    private String materialLabelId;

    @TableField("MATERIAL_TYPE")
    private String materialType;

    @TableField("MATERIAL_NAME")
    private String materialName;

    @TableField("MATERIAL_SUFFIX")
    private String materialSuffix;

    @TableField("MATERIAL_LOCAL_URL")
    private String materialLocalUrl;

    @TableField("MATERIAL_LOCAL_SIZE")
    private long materialLocalSize;

    @TableField("MATERIAL_WEB_URL")
    private String materialWebUrl;

    @TableField("MATERIAL_WEB_SIZE")
    private long materialWebSize;

    @TableField("MATERIAL_LOCAL_SL_URL")
    private String materialLocalSlUrl;

    @TableField("MATERIAL_LOCAL_SL_SIZE")
    private long materialLocalSlSize;

    @TableField("MATERIAL_WEB_SL_URL")
    private String materialWebSlUrl;

    @TableField("MATERIAL_WEB_SL_SIZE")
    private long materialWebSlSize;

    @TableField("MATERIAL_WEB_ID")
    private String materialWebId;

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
