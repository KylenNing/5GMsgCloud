package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgUserToLabelPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
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
public interface MsgUserToLabelMapper extends BaseMapper<MsgUserToLabelPO> {

    @Delete("delete from b_msg_user_to_label where USER_ID = #{userId}")
    void deleteByUserId(@Param("userId") String userId);

    @Select("select count(*) from b_msg_user_to_label where LABEL_ID = #{userLabelIds}")
    int getUserCountByLabelId(@Param("userLabelIds") String userLabelIds);

    @Select("select * from b_msg_user_to_label where USER_ID = #{userId}")
    MsgUserToLabelPO getLabelByUserId(@Param("userId") String userId);

    @Select("select user_id from b_msg_user_to_label where label_id = #{labelId}")
    List<String> getUserIdListByLabelId(@Param("labelId") String labelId);
}
