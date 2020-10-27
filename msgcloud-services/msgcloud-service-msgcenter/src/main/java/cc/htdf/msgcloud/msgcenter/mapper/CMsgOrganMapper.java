package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.CMsgOrganPO;
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
public interface CMsgOrganMapper extends BaseMapper<CMsgOrganPO> {

    @Select("SELECT * FROM c_msg_organ WHERE id = #{organId} OR PARENT_ID = #{organId} OR PARENT_ID IN ( SELECT id FROM c_msg_organ WHERE id = #{organId} OR PARENT_ID = #{organId} ) OR PARENT_ID IN (SELECT id FROM c_msg_organ WHERE PARENT_ID IN ( SELECT id FROM c_msg_organ WHERE id = #{organId} OR PARENT_ID = #{organId} ))")
    List<CMsgOrganPO> getOrganTree(@Param("organId") String id);

    @Select("SELECT COUNT(*) FROM c_msg_organ WHERE ORGAN_NAME = #{organName}")
    int getOrganNameCount(@Param("organName") String organName);

    @Select("SELECT COUNT(*) FROM c_msg_organ WHERE ORGAN_NAME = #{organName} and ID != #{organId}")
    int getOrganNameCountAndId(@Param("organName") String organName, @Param("organId") String organId);

    @Select("SELECT organ_name FROM c_msg_organ where id = (select organ_id from b_msg_h5product where id = " +
            "(select related_article from b_h5product_tour where id = #{tourId}))")
    String getOrganNameByTourId(@Param("tourId") Integer tourId);

}
