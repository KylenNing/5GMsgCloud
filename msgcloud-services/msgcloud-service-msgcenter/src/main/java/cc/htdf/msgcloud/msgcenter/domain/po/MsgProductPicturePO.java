package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Data
@TableName("b_msg_product_picture")
public class MsgProductPicturePO {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("PICTURE_NAME")
    private String pictureName;

    @TableField("PICTURE_LOCAL_URL")
    private String pictureLocalUrl;

    @TableField("PICTURE_SIZE")
    private Long pictureSize;

    @TableField("PICTURE_WEB_ID")
    private String pictureWebId;

    @TableField("PICTURE_WEB_URL")
    private String pictureWebUrl;

    @TableField("PICTURE_WEB_SL_URL")
    private String pictureWebSlUrl;
}
