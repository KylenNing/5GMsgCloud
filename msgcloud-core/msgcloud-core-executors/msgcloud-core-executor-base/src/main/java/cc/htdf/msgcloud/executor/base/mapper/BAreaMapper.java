package cc.htdf.msgcloud.executor.base.mapper;

import cc.htdf.msgcloud.executor.base.domain.po.BAreaPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Mapper
public interface BAreaMapper extends BaseMapper<BAreaPO> {

    @Select("select areaname from b_area where id = #{areacode}")
    String getAreanameById(@Param("areacode") Integer areacode);
}
