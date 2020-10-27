package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgTemplateButtonPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Mapper
public interface MsgTemplateButtonMapper extends BaseMapper<MsgTemplateButtonPO> {

    @Select("select * from b_msg_template_button where ID = #{buttonId}")
    MsgTemplateButtonPO getButtonById(@Param("buttonId") String buttonId);
}
