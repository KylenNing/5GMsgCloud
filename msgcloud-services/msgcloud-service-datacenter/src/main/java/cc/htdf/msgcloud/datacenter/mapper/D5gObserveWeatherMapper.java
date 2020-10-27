package cc.htdf.msgcloud.datacenter.mapper;

import cc.htdf.msgcloud.datacenter.domain.po.D5gObserveWeatherPO;
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
public interface D5gObserveWeatherMapper extends BaseMapper<D5gObserveWeatherPO> {

    @Select("select max(publish_date) from d_5g_observe_weather")
    String getLastPublishTime();

    @Select("select * from d_5g_observe_weather where publish_date = (select max(publish_date) from d_5g_observe_weather)" +
            "and data_source = #{dataSourceArea} and areacode = #{areacode}")
    D5gObserveWeatherPO getDataByDataSource(
            @Param("dataSourceArea") Integer dataSourceArea,
            @Param("areacode") Integer areacode);
}
