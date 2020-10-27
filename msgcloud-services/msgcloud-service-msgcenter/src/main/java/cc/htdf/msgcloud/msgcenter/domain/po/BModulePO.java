package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @Author: ningyq
 * @Date: 2020/10/6
 * @Description: TODO
 */
@Data
@TableName("b_module")
public class BModulePO {

    @TableId(type = IdType.AUTO)
    private int id;

    private String name;

    private String disc;

}