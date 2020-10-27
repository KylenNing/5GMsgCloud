package cc.htdf.msgcloud.executor.base.mapper;

import cc.htdf.msgcloud.executor.base.domain.po.BAreaNinePO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Mapper
public interface BAreaNineMapper extends BaseMapper<BAreaNinePO> {

    @Select("update b_area_nine set areaidsix = #{areaIdSix} where areaid = #{areaId}")
    void updateSixAreacodeByShortName(@Param("areaIdSix") int areaIdSix,
                                      @Param("areaId") int areaId);

    @Select("select * from b_area_nine")
    List<BAreaNinePO> getAllAreaCode();
}
