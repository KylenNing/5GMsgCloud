package cc.htdf.msgcloud.msgcenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgH5RelationPO;

import com.baomidou.mybatisplus.mapper.BaseMapper;
/**
 * 
 * @author 刘向阳
 * 
 */
@Mapper
public interface MsgH5RelationMapper  extends BaseMapper<MsgH5RelationPO> {

    @Select("SELECT * FROM b_msg_h5_relation where PRODUCT_ID = #{productId}")
    MsgH5RelationPO selectByProductId(@Param("productId") int productId);

}
