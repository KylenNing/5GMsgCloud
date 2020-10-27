package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordMenuPO;
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
public interface BKeywordMenuMapper extends BaseMapper<BKeywordMenuPO> {

    @Select("select menu_id from b_keyword_menu where key_word_id = #{keywordId}")
    List<Integer> selectMenuIdByKeywordId(@Param("keywordId") Integer keywordId);

    @Select("select * from b_keyword_menu where key_word_id = #{keywordId}")
    List<BKeywordMenuPO> selecByKeywordId(@Param("keywordId") Integer keywordId);

    @Select("delete from b_keyword_menu where key_word_id = #{keywordId}")
    void deleteByKeywordId(@Param("keywordId") Integer keywordId);

}
