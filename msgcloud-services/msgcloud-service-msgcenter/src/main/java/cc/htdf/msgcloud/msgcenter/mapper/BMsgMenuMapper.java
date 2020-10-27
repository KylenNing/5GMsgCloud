package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMsgMenuPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/8/12
 * @Description: TODO
 */
@Mapper
public interface BMsgMenuMapper extends BaseMapper<BMsgMenuPO> {

    @Select("select id from b_msg_menu where menu_type = " +
            "#{menuType} and menu_name = #{menuName} and menu_content = #{menuContent}" +
            "and created_time = #{createdTime}")
    Integer getMenuId(@Param("menuType") String menuType,@Param("menuName") String menuName,
    @Param("menuContent") String menuContent,@Param("createdTime") Date createdTime);

    @Select("delete from b_msg_menu where id = #{menuId}")
    void deleteByMenuId(@Param("menuId") Integer menuId);

    @Select("select * from b_msg_menu where id = #{menuId}")
    BMsgMenuPO selectMenuById(@Param("menuId") Integer menuId);
}
