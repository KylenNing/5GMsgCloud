package cc.htdf.msgcloud.msgcenter.mapper;

import java.util.List;
import java.util.Map;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgH5ProductPO;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Mapper
public interface MsgH5ProductMapper extends BaseMapper<MsgH5ProductPO> {

    @Update("update b_msg_h5product set STATUS = 3,REASON = NULL where ID = #{productId}")
    void examine(@Param("productId") String productId);

    @Update("update b_msg_h5product set VIEW_COUNT = VIEW_COUNT+ 1 where ID = #{productId}")
    void updateViewCount(@Param("productId") String productId);

    @Update("update b_msg_h5product set BROWSE_COUNT = BROWSE_COUNT+ 1 where ID = #{productId}")
    void updateBrowseCount(@Param("productId") String productId);

    @Update("update b_msg_h5product set STATUS = 2,REASON = #{reason} where ID = #{productId}")
    void examineNg(@Param("productId") String productId, @Param("reason") String reason);

    @Update("update b_msg_h5product set STATUS = 2 where ID = #{productId}")
    void offH5ProductById(@Param("productId") String productId);

    
    @Select("<script> select IFNULL(u.ORGAN_NAME,'') As organName,"+" IFNULL(p.TITLE,'') as title,IFNULL(p.MEDIA_RC_SL_URL,p.MEDIA_SL_URL) as mediaSlUrl,IFNULL(p.VIEW_COUNT,'') as viewCount,IFNULL(p.id,'') as id,IFNULL(p.type,'') as type,IFNULL(p.DESCRIBE,'') as descri"
    		+",IFNULL(DATE_FORMAT(p.RELEASE_TIME,'%Y-%m-%d %H:%i:%s'),'') releaseTime,IFNULL(m.disc,'') as disc,IFNULL(p.TAG_ID,'') as tagId,IFNULL(p.MEDIA_URL,'') as mediaUrl from b_module m left join b_msg_h5_relation r "+"on m.id=r.modular left join b_msg_h5product p on r.product_id = p.id "
    		+" left join c_msg_organ u on p.ORGAN_ID = u.id where p.RELEASE_TIME &lt;= str_to_date(#{date}, '%Y-%m-%d %H:%i:%s')"
	        +" and p.ORGAN_ID=12 and p.STATUS = 3 "
    		+" <if test='modularId != null '> "
    		+ " and m.id=#{modularId} "
    		+"</if>"
    		+" <if test='type != null'> "
    		+ " and r.type =#{type} "
    		+"</if>"
    		+" <if test='title != null'> "
    		+ " and INSTR(p.title,#{title}) &gt; 0 "
    		+"</if> </script>")
    List<Map<String,Object>> getProductList(@Param("modularId") String modularId,@Param("type")  String type,@Param("title")  String title,@Param("date")  String date);
    
    @Select("<script> select IFNULL(u.ORGAN_NAME,'') As organName,IFNULL(p.TITLE,'') as title,IFNULL(p.MEDIA_RC_SL_URL,'') as mediaSlUrl,"
			+" IFNULL(p.VIEW_COUNT,'') as viewCount,IFNULL(p.id,'') as id,IFNULL(p.type,'') as type,"
			+" IFNULL(p.DESCRIBE,'') as descri,IFNULL(ur.id,'') as urid,"
			+" IFNULL(DATE_FORMAT(p.RELEASE_TIME,'%Y-%m-%d %H:%i:%s'),'') releaseTime,IFNULL(m.disc,'') as disc,IFNULL(p.TAG_ID,'') as tagId,IFNULL(p.MEDIA_URL,'') as mediaUrl "
			+" from b_module m left join b_msg_h5_relation r "
			+" on m.id=r.modular left join b_h5product_tour ur "
			+" on r.product_id = ur.id left join b_msg_h5product p "
			+" on ur.RELATED_ARTICLE = p.id left join c_msg_organ u "
			+" on p.ORGAN_ID = u.id where r.modular = 8 and ur.STATUS=3 and p.ORGAN_ID=12 "
			+" <if test='modularId != null '> "
    		+" and m.id=#{modularId} "
    		+" </if>"
    		+" <if test='type != null'> "
    		+" and r.type =#{type} "
    		+"</if>"
    		+" <if test='title != null'> "
    		+ "and INSTR(p.title,#{title}) &gt; 0 "
    		+"</if> </script>")
	List<Map<String, Object>> getProductListTravel(@Param("modularId") String modularId,@Param("type")  String type,@Param("title")  String title,@Param("date")  String date);
    
	
}
