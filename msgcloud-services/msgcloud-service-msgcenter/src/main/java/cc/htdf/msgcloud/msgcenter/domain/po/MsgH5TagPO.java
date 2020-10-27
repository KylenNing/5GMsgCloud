package cc.htdf.msgcloud.msgcenter.domain.po;

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
@TableName("b_msg_h5tag")
public class MsgH5TagPO {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String name;

    private Integer parentId;

    private Integer level;

    private Integer type;

    private Integer display;

    private Integer color;

}
