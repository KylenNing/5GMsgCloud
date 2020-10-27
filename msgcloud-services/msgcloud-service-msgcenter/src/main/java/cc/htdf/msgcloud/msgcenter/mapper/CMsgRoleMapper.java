package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRolePO;
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
public interface CMsgRoleMapper extends BaseMapper<CMsgRolePO> {

    @Select("select count(*) from c_msg_role where ORGAN_ID = #{organId} and IS_AVAILABLE = 1")
    int getRoleCountByOrganId(@Param("organId") String organId);

    @Select("select * from c_msg_role where ORGAN_ID = #{organId} and IS_AVAILABLE = 1")
    List<CMsgRolePO> getRoleByOrganId(@Param("organId") String organId);

}
