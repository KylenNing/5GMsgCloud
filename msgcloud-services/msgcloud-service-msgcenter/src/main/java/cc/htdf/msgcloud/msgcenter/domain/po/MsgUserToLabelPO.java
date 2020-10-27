package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
@TableName("b_msg_user_to_label")
public class MsgUserToLabelPO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("USER_ID")
    private String userId;

    @TableField("LABEL_ID")
    private String labelId;

    @TableField("SORT")
    private Integer sort;

}
