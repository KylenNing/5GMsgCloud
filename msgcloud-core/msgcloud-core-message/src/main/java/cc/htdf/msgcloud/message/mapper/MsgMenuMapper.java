package cc.htdf.msgcloud.message.mapper;

import cc.htdf.msgcloud.message.domain.po.BMsgMenuPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplateButtonPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * author: JT
 * date: 2020/8/17
 * title:
 */
@Mapper
public interface MsgMenuMapper extends BaseMapper {

    @Select("SELECT " +
            "  msgMenu.ID AS ID, " +
            "  msgMenu.MENU_NAME AS MENU_NAME, " +
            "  msgMenu.MENU_TYPE AS MENU_TYPE, " +
            "  msgMenu.MENU_CONTENT AS MENU_CONTENT " +
            "FROM " +
            "  b_msg_keyword msgKeyword " +
            "LEFT JOIN b_keyword_menu keywordMenu ON keywordMenu.KEY_WORD_ID = msgKeyword.ID " +
            "LEFT JOIN b_msg_menu msgMenu ON msgMenu.ID = keywordMenu.MENU_ID " +
            "WHERE " +
            "msgKeyword.KEY_WORD = #{keyword} and msgKeyword.SERVICE_ID = #{serviceId} ORDER BY keywordMenu.MENU_SORT ASC ")
    List<BMsgMenuPO> findSuspensionMenusByKeyword(@Param("keyword") String keyword, @Param("serviceId") String serviceId);

    @Select("SELECT " +
            " msgMenu.ID AS ID," +
            " msgMenu.MENU_NAME AS MENU_NAME," +
            " msgMenu.MENU_TYPE AS MENU_TYPE," +
            " msgMenu.MENU_CONTENT AS MENU_CONTENT " +
            " FROM" +
            " b_group_menu keywordMenu " +
            " LEFT JOIN b_msg_menu msgMenu ON msgMenu.ID = keywordMenu.MENU_ID " +
            " WHERE " +
            " keywordMenu.GROUP_ID = #{groupId}  " +
            " ORDER BY " +
            " keywordMenu.MENU_SORT ASC")
    List<BMsgMenuPO> findSuspensionMenusByGroupId(@Param("groupId") Integer groupId);

    @Select("SELECT " +
            "  msgButton.ID AS BUTTON_ID, " +
            "  msgButton.BUTTON_NAME AS BUTTON_NAME, " +
            "  msgButton.BUTTON_TYPE AS BUTTON_TYPE, " +
            "  msgButton.BUTTON_CONTENT AS BUTTON_CONTENT " +
            "FROM " +
            "  b_msg_template msgTemplate " +
            "  LEFT JOIN b_msg_template_to_button msgTemplateButton ON msgTemplateButton.TEMPLATE_ID = msgTemplate.ID " +
            "  LEFT JOIN b_msg_template_button msgButton ON msgButton.ID = msgTemplateButton.BUTTON_ID " +
            "WHERE " +
            "  msgTemplate.ID = #{templateId} ORDER BY msgTemplateButton.SORT ASC ")
    List<MsgTemplateButtonPO> findTemplateButtonsByTemplateId(@Param("templateId") String templateId);

}
