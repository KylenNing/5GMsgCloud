package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.CMsgMenuPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Mapper
public interface CMsgMenuMapper extends BaseMapper<CMsgMenuPO> {

    @Select("SELECT  " +
            "\tcMsgMenu.ID AS ID, " +
            "  cMsgMenu.MENU_CODE AS MENU_CODE, " +
            "  cMsgMenu.MENU_NAME AS MENU_NAME, " +
            "  cMsgMenu.MENU_LCON AS MENU_LCON, " +
            "  cMsgMenu.MENU_URL AS MENU_URL, " +
            "  cMsgMenu.REQUEST_METHOD AS REQUEST_METHOD, " +
            "  cMsgMenu.PARENT_ID AS PARENT_ID, " +
            "  cMsgMenu.LEVEL AS LEVEL, " +
            "  cMsgMenu.SORT AS SORT, " +
            "  cMsgMenu.MENU_TYPE AS MENU_TYPE, " +
            "  cMsgMenu.BUTTON_TYPE AS BUTTON_TYPE, " +
            "  cMsgMenu.IS_AVAILABLE AS IS_AVAILABLE, " +
            "  cMsgMenu.CREATED_ORG AS CREATED_ORG, " +
            "  cMsgMenu.CREATED_BY AS CREATED_BY, " +
            "  cMsgMenu.CREATED_TIME AS CREATED_TIME, " +
            "  cMsgMenu.UPDATED_BY AS UPDATED_BY, " +
            "  cMsgMenu.UPDATED_TIME AS UPDATED_TIME " +
            "FROM c_msg_user cMsgUser  " +
            "LEFT JOIN c_msg_role cMsgRole ON cMsgRole.ID = cMsgUser.ROLE_ID " +
            "LEFT JOIN c_msg_role_menu cMsgRoleMenu ON cMsgRoleMenu.ROLE_ID = cMsgRole.ID " +
            "LEFT JOIN c_msg_menu cMsgMenu ON cMsgMenu.ID = cMsgRoleMenu.MENU_ID " +
            "WHERE cMsgUser.ID = #{userId} " +
//            "AND cMsgMenu.MENU_TYPE = 1 " +
            "ORDER BY cMsgRoleMenu.MENU_ID")
    List<CMsgMenuPO> getAllMenu(@Param("userId") int userId);

    @Select("SELECT  " +
            "\tcMsgMenu.ID AS ID, " +
            "  cMsgMenu.MENU_CODE AS MENU_CODE, " +
            "  cMsgMenu.MENU_NAME AS MENU_NAME, " +
            "  cMsgMenu.MENU_LCON AS MENU_LCON, " +
            "  cMsgMenu.MENU_URL AS MENU_URL, " +
            "  cMsgMenu.REQUEST_METHOD AS REQUEST_METHOD, " +
            "  cMsgMenu.PARENT_ID AS PARENT_ID, " +
            "  cMsgMenu.LEVEL AS LEVEL, " +
            "  cMsgMenu.SORT AS SORT, " +
            "  cMsgMenu.MENU_TYPE AS MENU_TYPE, " +
            "  cMsgMenu.BUTTON_TYPE AS BUTTON_TYPE, " +
            "  cMsgMenu.IS_AVAILABLE AS IS_AVAILABLE, " +
            "  cMsgMenu.CREATED_ORG AS CREATED_ORG, " +
            "  cMsgMenu.CREATED_BY AS CREATED_BY, " +
            "  cMsgMenu.CREATED_TIME AS CREATED_TIME, " +
            "  cMsgMenu.UPDATED_BY AS UPDATED_BY, " +
            "  cMsgMenu.UPDATED_TIME AS UPDATED_TIME " +
            "FROM c_msg_user cMsgUser  " +
            "LEFT JOIN c_msg_role cMsgRole ON cMsgRole.ID = cMsgUser.ROLE_ID " +
            "LEFT JOIN c_msg_role_menu cMsgRoleMenu ON cMsgRoleMenu.ROLE_ID = cMsgRole.ID " +
            "LEFT JOIN c_msg_menu cMsgMenu ON cMsgMenu.ID = cMsgRoleMenu.MENU_ID " +
            "WHERE cMsgUser.ID = #{userId} " +
            "AND cMsgRoleMenu.IS_AVAILABLE = 1 " +
            "AND cMsgMenu.MENU_TYPE = 1 " +
            "ORDER BY cMsgRoleMenu.MENU_ID")
    List<CMsgMenuPO> getMenusByUserId(@Param("userId") int userId);

    @Select("SELECT cMsgMenu.BUTTON_TYPE " +
            "FROM c_msg_user cMsgUser  " +
            "LEFT JOIN c_msg_role cMsgRole ON cMsgRole.ID = cMsgUser.ROLE_ID " +
            "LEFT JOIN c_msg_role_menu cMsgRoleMenu ON cMsgRoleMenu.ROLE_ID = cMsgRole.ID " +
            "LEFT JOIN c_msg_menu cMsgMenu ON cMsgMenu.ID = cMsgRoleMenu.MENU_ID " +
            "WHERE cMsgUser.ID = #{userId} " +
            "AND cMsgMenu.PARENT_ID = #{menuId} " +
            "AND cMsgRoleMenu.IS_AVAILABLE = 1")
    List<String> selectButtonByMenuId(@Param("userId") int userId, @Param("menuId") String menuId);
}
