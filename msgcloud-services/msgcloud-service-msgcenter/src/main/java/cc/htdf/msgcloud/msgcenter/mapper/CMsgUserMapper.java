package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
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
public interface CMsgUserMapper extends BaseMapper<CMsgUserPO> {

    @Select("select count(*) from c_msg_user where ORGAN_ID = #{organId} and IS_AVAILABLE = 1")
    int getUserCountByOrganId(@Param("organId") String organId);

    @Select("select count(*) from c_msg_user where USER_ACCOUNT = #{userAccount} and IS_AVAILABLE = 1")
    int getUserAccountCount(@Param("userAccount") String userAccount);

    @Select("select * from c_msg_user where ORGAN_ID = #{organId} and IS_AVAILABLE = 1")
    List<CMsgUserPO> getUserByOrganId(@Param("organId") String organId);

    @Select("select * from c_msg_user where ROLE_ID = #{roleId} and IS_AVAILABLE = 1")
    List<CMsgUserPO> getUserByRoleId(@Param("roleId") String roleId);

}
