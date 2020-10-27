package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgTemplatePO;
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
public interface MsgTemplateMapper extends BaseMapper<MsgTemplatePO> {

    @Select("select * from b_msg_template where ID = #{templateId} order by UPDATED_TIME desc")
    MsgTemplatePO getTemplateById(@Param("templateId") String templateId);

    @Select("select * from b_msg_template where TEMPLATE_IMAGE_ID = #{materialId}")
    List<MsgTemplatePO> getTemplateByMaterialId(@Param("materialId") String materialId);

    @Select("SELECT temp.* FROM b_msg_template AS temp LEFT JOIN b_message_template AS message ON temp.ID = message.TEMPLATE_ID WHERE message.MESSAGE_ID = #{msgId}")
    List<MsgTemplatePO> getTemplateByMsgId(@Param("msgId") Integer msgId);

    @Select("SELECT temp.* FROM b_msg_template AS temp LEFT JOIN b_group_template AS gro ON temp.ID = gro.TEMPLATE_ID  WHERE gro.group_id = #{groupId} ORDER BY gro.TEMPLATE_SORT")
    List<MsgTemplatePO> getTemplateIdByGroupId(@Param("groupId") Integer groupId);

    @Select("SELECT temp.* FROM b_msg_template AS temp LEFT JOIN b_keyword_template AS keyword ON temp.ID = keyword.TEMPLATE_ID  WHERE keyword.key_word_id = #{keywordId}")
    List<MsgTemplatePO> getKeywordTemplateByKeyworId(@Param("keywordId") Integer keywordId);

}
