package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BGroupTemplatePO;
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
public interface BGroupTemplateMapper extends BaseMapper<BGroupTemplatePO> {

    @Select("delete from b_group_template where group_id = #{groupId}")
    void deleteByGroupId(@Param("groupId") Integer groupId);

    @Select("select * from b_group_template where group_id = #{groupId}")
    List<BGroupTemplatePO> getGroupTemplateByGroupId(@Param("groupId") Integer groupId);

    @Select("select template_id from b_group_template where group_id = #{groupId} ORDER BY TEMPLATE_SORT")
    List<String> getTemplateIdByGroupId(@Param("groupId") Integer groupId);

    @Select("select count(*) from b_group_template where TEMPLATE_ID = #{templateId}")
    int getCountByTemplateId(@Param("templateId") String templateId);

}
