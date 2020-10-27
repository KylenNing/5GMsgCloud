package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgH5TagPO;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Mapper
public interface MsgH5TagMapper extends BaseMapper<MsgH5TagPO> {

    @Select("SELECT * FROM b_msg_h5tag where type = #{moduleId}")
    List<MsgH5TagPO> selectAllByModuleId(@Param("moduleId") Integer moduleId);

    @Select("SELECT * FROM b_msg_h5tag where type = #{moduleId}")
    List<MsgH5TagPO> getTagByModular(@Param("moduleId") Integer moduleId);

    @Select("SELECT count(*) FROM b_msg_h5tag where PARENT_ID = #{id}")
    int selectCountById(@Param("id") int id);
    
    @Select("SELECT IFNULL(NAME,'') As name,IFNULL(COLOR,'') As color"
    +" FROM b_msg_h5tag where ID = #{tag}")
	Map<String, Object> getTagById(@Param("tag") String tag);
    
}
