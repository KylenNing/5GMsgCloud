package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.BMessagePO;
import cc.htdf.msgcloud.msgcenter.domain.po.BMessageTemplatePO;
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
public interface BMessageTemplateMapper extends BaseMapper<BMessageTemplatePO> {

    @Select("select * from b_message_template where message_id = #{msgId}")
    List<BMessageTemplatePO> getTemplateByMsgId(@Param("msgId") Integer msgId);
}
