package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgUserPO;
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
public interface MsgUserMapper extends BaseMapper<MsgUserPO> {

    @Select("select * from b_msg_user order by UPDATED_TIME desc")
    List<MsgUserPO> getUserList();

    @Select("select * from b_msg_user where id = #{userId}")
    MsgUserPO getUserById(@Param("userId") String userId);
}
