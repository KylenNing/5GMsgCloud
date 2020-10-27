package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMessagePO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: ningyq
 * @Date: 2020/8/12
 * @Description: TODO
 */
@Mapper
public interface BMessageMapper extends BaseMapper<BMessagePO> {

    @Select("select * from b_message where id = #{msgId}")
    BMessagePO getMsgById(@Param("msgId") Integer msgId);


}
