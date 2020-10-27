package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRoleMenuPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Mapper
public interface CMsgRoleMenuMapper extends BaseMapper<CMsgRoleMenuPO> {

    @Update("update c_msg_role_menu set IS_AVAILABLE = 0 where ROLE_ID = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);

    @Delete("delete from  c_msg_role_menu where ROLE_ID = #{roleId} and MENU_ID = #{menuId}")
    void deleteByRoleIdAndMenuId(@Param("roleId") String roleId, @Param("menuId") String menuId);

    @Update("update c_msg_role_menu set IS_AVAILABLE = #{isAvailable} where ROLE_ID = #{roleId} and MENU_ID = #{menuId}")
    void updateByRoleIdAndMenuId(@Param("isAvailable") String isAvailable, @Param("roleId") String roleId, @Param("menuId") String menuId);

    @Select("select * from c_msg_role_menu where ROLE_ID = #{roleId} and IS_AVAILABLE = 1")
    List<CMsgRoleMenuPO> getMenuByRoleId(@Param("roleId") String roleId);

    @Select("select * from c_msg_role_menu where ROLE_ID = #{roleId}")
    List<CMsgRoleMenuPO> getMenu(@Param("roleId") String roleId);
}
