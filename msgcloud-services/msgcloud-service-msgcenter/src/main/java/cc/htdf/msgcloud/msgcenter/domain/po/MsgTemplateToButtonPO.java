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
@TableName("b_msg_template_to_button")
public class MsgTemplateToButtonPO {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("TEMPLATE_ID")
    private String templateId;

    @TableField("BUTTON_ID")
    private String buttonId;

    @TableField("SORT")
    private Integer sort;

}
