package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
@TableName("c_msg_user_role")
public class CMsgUserRolePO {

    private Integer userId;

    private Integer roleId;
}
