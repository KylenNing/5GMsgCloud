package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BGroupMenuPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/11
 * @Description: TODO
 */
@Mapper
public interface BGroupMenuMapper extends BaseMapper<BGroupMenuPO> {

    @Select("delete from b_group_menu where group_id = #{groupId}")
    void deleteByGroupId(@Param("groupId") Integer groupId);

    @Select("select menu_id from b_group_menu where group_id = #{groupId}")
    List<Integer> selectMenuIdByGroupId(@Param("groupId") Integer groupId);

    @Select("select * from b_group_menu where group_id = #{groupId}")
    List<BGroupMenuPO> selectGroupMenuByGroupId(@Param("groupId") Integer groupId);

    @Select("select menu_sort from b_group_menu where group_id = #{groupId} and menu_id = #{menuId}")
    Integer selectMenuSortByGroupAndMenu(@Param("groupId") Integer groupId,@Param("menuId") Integer menuId);

}
