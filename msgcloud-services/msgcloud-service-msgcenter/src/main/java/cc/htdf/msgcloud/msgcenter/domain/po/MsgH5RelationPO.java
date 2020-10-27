package cc.htdf.msgcloud.msgcenter.domain.po;

import lombok.Data;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
/**
 * 
 * @author 刘向阳
 *
 */
@Data
@TableName("b_msg_h5_relation")
public class MsgH5RelationPO {

	 	@TableId(value = "id", type = IdType.AUTO)
	    private int id;

	    @TableField("MODULAR")
	    private Integer modular;

	    @TableField("TYPE")
	    private Integer type;

	    @TableField("PRODUCT_ID")
	    private Integer productId;
	    
}
