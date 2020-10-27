package cc.htdf.msgcloud.executor.base.mapper;

import cc.htdf.msgcloud.executor.base.domain.po.D5gDayWeatherPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Mapper
public interface D5gDayWeatherMapper extends BaseMapper<D5gDayWeatherPO> {

    @Select("select max(publish_date) from d_5g_day_weather")
    Date getLastPublishTime();

    @Select("delete from d_5g_day_weather where publish_date < #{invalidDate}")
    void clearHistoryDate(@Param("invalidDate") String invalidDate);
}
