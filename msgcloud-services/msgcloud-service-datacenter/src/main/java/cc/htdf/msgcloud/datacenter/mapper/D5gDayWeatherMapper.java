package cc.htdf.msgcloud.datacenter.mapper;

import cc.htdf.msgcloud.datacenter.domain.po.D5gDayWeatherPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Mapper
public interface D5gDayWeatherMapper extends BaseMapper<D5gDayWeatherPO> {

    @Select("select max(publish_date) from d_5g_day_weather")
    Date getLastPublishTime();

    @Select("select * from d_5g_day_weather where publish_date = (select max(publish_date) from d_5g_day_weather where areacode = #{areacode})" +
            "and data_source = #{dataSourceArea} and areacode = #{areacode} order by valid_date asc")
    List<D5gDayWeatherPO> getDataByDataSource(
            @Param("dataSourceArea") Integer dataSourceArea,
            @Param("areacode") Integer areacode);
}
