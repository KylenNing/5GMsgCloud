package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
@TableName("c_msg_role_menu")
public class CMsgRoleMenuPO {

    private Integer roleId;

    private Integer menuId;

    private Integer isAvailable;
}
