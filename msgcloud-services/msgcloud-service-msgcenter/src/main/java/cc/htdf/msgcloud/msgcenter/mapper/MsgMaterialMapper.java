package cc.htdf.msgcloud.msgcenter.mapper;

import cc.htdf.msgcloud.msgcenter.domain.po.MsgMaterialPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Mapper
public interface MsgMaterialMapper extends BaseMapper<MsgMaterialPO> {

    @Update("update b_msg_material set MATERIAL_LABEL_ID = null where MATERIAL_LABEL_ID = #{materialLabelId}")
    void updateByMaterialLabelId(@Param("materialLabelId") String materialLabelId);

    @Select("select * from b_msg_material where MATERIAL_TYPE = #{materialType} and MATERIAL_LABEL_ID = #{materialLabelId} order by UPDATED_TIME desc")
    List<MsgMaterialPO> getMaterialListBylabelId(@Param("materialType") String materialType, @Param("materialLabelId") String materialLabelId);

    @Select("select * from b_msg_material where MATERIAL_TYPE = #{materialType} and MATERIAL_NAME like #{materialName} and MATERIAL_LABEL_ID = #{materialLabelId} order by UPDATED_TIME desc")
    List<MsgMaterialPO> getMaterialListByNameAndlabelId(@Param("materialType") String materialType, @Param("materialName") String materialName, @Param("materialLabelId") String materialLabelId);
}
