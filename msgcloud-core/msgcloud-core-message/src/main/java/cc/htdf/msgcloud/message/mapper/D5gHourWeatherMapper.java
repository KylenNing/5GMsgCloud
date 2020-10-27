package cc.htdf.msgcloud.message.mapper;

import cc.htdf.msgcloud.message.domain.po.D5gHourWeatherPO;
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
public interface D5gHourWeatherMapper extends BaseMapper<D5gHourWeatherPO> {

    @Select("select max(publish_date) from d_5g_hour_weather")
    Date getLastPublishTime();

    @Select("select * from d_5g_hour_weather where publish_date = (select max(publish_date) from d_5g_hour_weather where areacode = #{areacode})" +
            "and data_source = #{dataSourceArea} and areacode = #{areacode} order by valid_date asc")
    List<D5gHourWeatherPO> getDataByDataSource(
            @Param("dataSourceArea") Integer dataSourceArea,
            @Param("areacode") Integer areacode);

    @Select("select * from d_5g_hour_weather where publish_date = #{publishDate}" +
            "and data_source = #{dataSourceArea} and areacode = #{areacode} order by valid_date asc")
    List<D5gHourWeatherPO> getDataByDataSourceAndPublishDate(
            @Param("publishDate") String publishDate,
            @Param("dataSourceArea") Integer dataSourceArea,
            @Param("areacode") Integer areacode);

    @Select("select max(publish_date) from d_5g_hour_weather")
    String getLastDate();

    @Select("select * from d_5g_hour_weather where areaname = #{areaname} and publish_date = (select max(publish_date) from d_5g_hour_weather)" +
            "and valid_date = (select min(valid_date) from d_5g_hour_weather where publish_date = (select max(publish_date) from d_5g_hour_weather))")
    D5gHourWeatherPO getLastDataByAreaname(@Param("areaname")  String areaname);
}
