package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Data
@TableName("b_area")
public class MsgAreaPO {

    private int id;

    @TableField("areaname")
    private String areaName;

    @TableField("parentid")
    private int parentId;

    @TableField("level")
    private int level;
}
