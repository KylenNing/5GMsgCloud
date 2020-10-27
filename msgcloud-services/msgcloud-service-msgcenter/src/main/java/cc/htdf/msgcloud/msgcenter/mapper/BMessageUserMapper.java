package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMessageUserPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/12
 * @Description: TODO
 */
@Mapper
public interface BMessageUserMapper extends BaseMapper<BMessageUserPO> {

    @Select("select user_id from b_message_user where message_id = #{msgId}")
    List<String> getUsersByMsgId(@Param("msgId") Integer msgId);


}
