package cc.htdf.msgcloud.executor.base.mapper;

import cc.htdf.msgcloud.executor.base.domain.po.D5gHourWeatherPO;
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

    @Select("delete from d_5g_hour_weather where publish_date < #{invalidDate}")
    void clearHistoryDate(@Param("invalidDate") String invalidDate);

    @Select("select areacode from d_5g_hour_weather where " +
            "publish_date = #{currentTime} group by areacode")
    List<Integer> getLatestDateArea(@Param("currentTime") String currentTime);
}
