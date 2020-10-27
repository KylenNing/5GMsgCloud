package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMsgGroupPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: ningyq
 * @Date: 2020/8/12
 * @Description: TODO
 */
@Mapper
public interface BMsgGroupMapper extends BaseMapper<BMsgGroupPO> {

    @Select("delete from b_msg_group where id = #{id}")
    void deleteGroupById(@Param("id") Integer id);

    @Select("select * from b_msg_group where id = #{groupId}")
    BMsgGroupPO getGroupById(@Param("groupId") Integer groupId);

}
