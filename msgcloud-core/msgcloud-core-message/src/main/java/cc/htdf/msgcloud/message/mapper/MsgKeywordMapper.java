package cc.htdf.msgcloud.message.mapper;

import cc.htdf.msgcloud.message.domain.po.BMsgKeywordPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Mapper
public interface MsgKeywordMapper extends BaseMapper<BMsgKeywordPO> {

    @Select("select * from b_msg_keyword keyword LEFT JOIN b_seveice_num seveice ON keyword.SERVICE_ID = seveice.ID " +
            "where seveice.CHANNEL_ID = #{channelId} and keyword.key_word = #{keyword}")
    BMsgKeywordPO selectByNameAndServiceId(@Param("channelId") Long channelId, @Param("keyword") String action);
}
