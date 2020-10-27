package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgAreaPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Mapper
public interface MsgAreaMapper extends BaseMapper<MsgAreaPO> {

    @Select("SELECT id,areaname,parentid,level FROM b_area")
    List<MsgAreaPO> getAreaList();

    @Select("SELECT id,areaname,parentid,level FROM b_area where parentid = #{parentId} OR id = #{parentId}")
    List<MsgAreaPO> selectByparentId(@Param("parentId") String parentId);
}
