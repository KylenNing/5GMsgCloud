package cc.htdf.msgcloud.message.mapper;

import cc.htdf.msgcloud.message.domain.po.MsgTemplatePO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedList;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Mapper
public interface MsgTemplateMapper extends BaseMapper<MsgTemplatePO> {

    @Select("SELECT " +
            "  msgTemplate.ID AS ID, " +
            "  msgTemplate.TEMPLATE_STRATEGY AS TEMPLATE_STRATEGY, " +
            "  msgTemplate.TEMPLATE_TYPE AS TEMPLATE_TYPE, " +
            "  msgTemplate.TEMPLATE_IMAGE_ID AS TEMPLATE_IMAGE_ID, " +
            "  msgTemplate.TEMPLATE_TITLE AS TEMPLATE_TITLE, " +
            "  msgTemplate.TEMPLATE_CONTENT AS TEMPLATE_CONTENT, " +
            "  msgTemplate.TEMPLATE_CONFIG AS TEMPLATE_CONFIG, " +
            "  msgTemplate.CREATED_ORG AS CREATED_ORG, " +
            "  msgTemplate.CREATED_TIME AS CREATED_TIME, " +
            "  msgTemplate.CREATED_BY AS CREATED_BY, " +
            "  msgTemplate.UPDATED_TIME AS UPDATED_TIME, " +
            "  msgTemplate.UPDATED_BY AS UPDATED_BY, " +
            "  msgTemplate.DYNAMIC_TEMPLATE_ID AS DYNAMIC_TEMPLATE_ID " +
            "FROM  " +
            "b_seveice_num AS seveiceNum  " +
            "LEFT JOIN b_msg_keyword AS msgKeyword ON msgKeyword.SERVICE_ID = seveiceNum.ID " +
            "LEFT JOIN b_keyword_template AS keywordTemplate ON keywordTemplate.KEY_WORD_ID = msgKeyword.ID " +
            "LEFT JOIN b_msg_template AS msgTemplate ON msgTemplate.ID = keywordTemplate.TEMPLATE_ID " +
            "WHERE seveiceNum.CHANNEL_ID = #{channelId} and msgKeyword.KEY_WORD = #{keyword} ORDER BY keywordTemplate.TEMPLATE_SORT ASC")
    LinkedList<MsgTemplatePO> findByChannelIdAndKeyword(@Param("channelId") Long channelId, @Param("keyword") String action);

    @Select("SELECT  " +
            " msgTemplate.ID AS ID, " +
            " msgTemplate.TEMPLATE_STRATEGY AS TEMPLATE_STRATEGY, " +
            " msgTemplate.TEMPLATE_TYPE AS TEMPLATE_TYPE, " +
            " msgTemplate.TEMPLATE_IMAGE_ID AS TEMPLATE_IMAGE_ID, " +
            " msgTemplate.TEMPLATE_TITLE AS TEMPLATE_TITLE, " +
            " msgTemplate.TEMPLATE_CONTENT AS TEMPLATE_CONTENT, " +
            " msgTemplate.TEMPLATE_CONFIG AS TEMPLATE_CONFIG, " +
            " msgTemplate.CREATED_ORG AS CREATED_ORG, " +
            " msgTemplate.CREATED_TIME AS CREATED_TIME, " +
            " msgTemplate.CREATED_BY AS CREATED_BY, " +
            " msgTemplate.UPDATED_TIME AS UPDATED_TIME, " +
            " msgTemplate.UPDATED_BY AS UPDATED_BY, " +
            "  msgTemplate.DYNAMIC_TEMPLATE_ID AS DYNAMIC_TEMPLATE_ID " +
            " FROM b_group_template AS groupTemplate LEFT JOIN b_msg_template AS" +
            " msgTemplate ON groupTemplate.TEMPLATE_ID = msgTemplate.ID " +
            "where groupTemplate.GROUP_ID = #{groupId} ORDER BY groupTemplate.TEMPLATE_SORT ASC")
    LinkedList<MsgTemplatePO> findByGroupId(@Param("groupId") Integer groupId);


}
