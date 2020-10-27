package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMessageMenuPO;
import cc.htdf.msgcloud.msgcenter.domain.po.BMessagePO;
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
public interface BMessageMenuMapper extends BaseMapper<BMessageMenuPO> {

    @Select("select * from b_message_menu where message_id = #{msgId}")
    List<BMessageMenuPO> getMenuByMsgId(@Param("msgId") Integer msgId);
}
