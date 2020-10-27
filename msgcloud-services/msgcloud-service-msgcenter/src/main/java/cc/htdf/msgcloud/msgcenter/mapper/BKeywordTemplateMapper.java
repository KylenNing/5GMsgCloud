package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordTemplatePO;
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
public interface BKeywordTemplateMapper extends BaseMapper<BKeywordTemplatePO> {

    @Select("delete from b_keyword_template where key_word_id = #{keywordId}")
    void deleteByKeywordId(@Param("keywordId") Integer keywordId);

    @Select("select * from b_keyword_template where key_word_id = #{keywordId}")
    List<BKeywordTemplatePO> getKeywordTemplateByKeyworId(@Param("keywordId") Integer keywordId);

    @Select("select * from b_keyword_template where TEMPLATE_ID = #{templateId}")
    List<BKeywordTemplatePO> getKeywordTemplateByTemplateId(@Param("templateId") String templateId);
}
