package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgTemplateTagPO;
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
public interface MsgTemplateTagMapper extends BaseMapper<MsgTemplateTagPO> {

    @Select("select * from b_msg_template_tag where TAG_TYPE = #{tagType} order by UPDATED_TIME desc")
    List<MsgTemplateTagPO> getTagList(@Param("tagType") String tagType);

    @Select("select tag_type from b_msg_template_tag  GROUP BY TAG_TYPE")
    List<String> getTagGroup();

}
