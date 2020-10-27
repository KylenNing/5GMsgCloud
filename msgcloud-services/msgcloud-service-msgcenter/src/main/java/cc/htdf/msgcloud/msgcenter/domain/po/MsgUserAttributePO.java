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
@TableName("b_msg_user_attribute")
public class MsgUserAttributePO {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("SEX")
    private String sex;

    @TableField("AGE")
    private String age;

    @TableField("WORK")
    private String work;

    @TableField("TRAVEL")
    private String travel;

    @TableField("REMARKS")
    private String remarks;
}
