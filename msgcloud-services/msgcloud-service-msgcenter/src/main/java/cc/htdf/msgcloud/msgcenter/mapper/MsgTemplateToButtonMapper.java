package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgTemplateToButtonPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
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
public interface MsgTemplateToButtonMapper extends BaseMapper<MsgTemplateToButtonPO> {

    @Select("select * from b_msg_template_to_button where TEMPLATE_ID = #{templateId} order by SORT ASC")
    List<MsgTemplateToButtonPO> getMsgTemplateToButtonById(@Param("templateId") String templateId);

    @Delete("delete from b_msg_template_to_button where TEMPLATE_ID = #{templateId}")
    void deleteByTemplateId(@Param("templateId") String templateId);

}
