package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMsgKeywordPO;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/8/11
 * @Description: TODO
 */
@Mapper
public interface BMsgKeywordMapper extends BaseMapper<BMsgKeywordPO> {


    @Select("select * from b_msg_keyword where id = #{keyWordId}")
    BMsgKeywordPO getKeywordById(@Param("keyWordId") Integer keyWordId);

    @Select("select id from b_msg_keyword where key_word = #{keyWord}")
    Integer getKeywordId(@Param("keyWord") String keyWord);

    @Select("delete from b_msg_keyword where id = #{id}")
    void deleteKeywordById(@Param("id") Integer id);

    @Select("select * from b_msg_keyword where service_id = #{serviceId}" +
            "and key_word like CONCAT('%',#{keyWord},'%')")
    List<BMsgKeywordPO> selectKeywordByNameAndServiceId(
            @Param("serviceId") String serviceId,@Param("keyWord") String keyWord);

    @Select("select * from b_msg_keyword where SERVICE_ID = #{serviceId}")
    List<BMsgKeywordPO> getKeywordByServiceId(String serviceId);
    
    @Select("select * from b_msg_keyword where key_word = #{name}"+" and service_id = #{serviceId}")
	List<BMsgKeywordPO> getKeywordByName(@Param("name") String name, @Param("serviceId") String serviceId);
    
	
}
