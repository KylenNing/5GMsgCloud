package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgProductSendPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Mapper
public interface MsgProductSendMapper extends BaseMapper<MsgProductSendPO> {

    @Update("update b_msg_product_send set MESSAGE_STATUS = 1 where ID = #{productId}")
    void examine(@Param("productId") String productId);

    @Update("update b_msg_product_send set MESSAGE_STATUS = -1,REASON = #{reason} where ID = #{productId}")
    void examineNg(@Param("productId") String productId, @Param("reason") String reason);
}
