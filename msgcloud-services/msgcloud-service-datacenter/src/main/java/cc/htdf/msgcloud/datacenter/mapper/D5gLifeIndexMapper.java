package cc.htdf.msgcloud.datacenter.mapper;

import cc.htdf.msgcloud.datacenter.domain.po.D5gAirPO;
import cc.htdf.msgcloud.datacenter.domain.po.D5gLifeIndexPO;
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
public interface D5gLifeIndexMapper extends BaseMapper<D5gAirPO> {

    @Select("select max(publish_date) from d_5g_life_index")
    String getLastPublishTime();

    @Select("select * from d_5g_life_index where publish_date = (select max(publish_date) from d_5g_life_index where areacode = #{areacode})" +
            "and data_source = #{dataSourceArea} and areacode = #{areacode} and index_name " +
            "in ('晨练指数','紫外线指数','穿衣指数','感冒指数')")
    List<D5gLifeIndexPO> getDataByDataSource(
            @Param("dataSourceArea") Integer dataSourceArea,
            @Param("areacode") Integer areacode);

    @Select("select * from d_5g_life_index a " +
            "inner join (select max(id) max_id from d_5g_life_index group by index_name) b on b.max_id = a.id" +
            " where a.index_name in ('晨练指数','紫外线指数','穿衣指数','感冒指数') and data_source = #{dataSourceArea} and areacode = #{areacode}")
    List<D5gLifeIndexPO> getLastIndexList(@Param("dataSourceArea") Integer dataSourceArea,
                                          @Param("areacode") Integer areacode);
}
