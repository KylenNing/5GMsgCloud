package cc.htdf.msgcloud.message.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/10/19
 * @Description:
 */
@Data
@TableName("b_msg_material_picture_log")
public class MsgMaterialPictureLogPO {

    @TableId(type = IdType.UUID)
    private int id;

    @TableField("CITY")
    private String city;

    @TableField("START_TIME")
    private String startTime;

    @TableField("END_TIME")
    private String endTime;

    @TableField("DYNAMIC_TEMPLATE_ID")
    private String dynamicTemplateId;

    @TableField("WEB_ID")
    private String webId;

    @TableField("WEB_URL")
    private String webUrl;

    @TableField("WEB_SIZE")
    private long webSize;

    @TableField("WEB_SL_URL")
    private String webSlUrl;

    @TableField("LOCAL_URL")
    private String localUrl;

    @TableField("LOCAL_SIZE")
    private long localSize;

}
