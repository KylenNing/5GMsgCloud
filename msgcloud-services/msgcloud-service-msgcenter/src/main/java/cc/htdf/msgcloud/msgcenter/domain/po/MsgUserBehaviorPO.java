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
@TableName("b_msg_user_behavior")
public class MsgUserBehaviorPO {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("AREA_ID")
    private String areaId;

    @TableField("ACTIVE")
    private String active;

    @TableField("ADDRESS")
    private String address;

    @TableField("CHANGE_ADDRESS")
    private String changeAddress;
}
