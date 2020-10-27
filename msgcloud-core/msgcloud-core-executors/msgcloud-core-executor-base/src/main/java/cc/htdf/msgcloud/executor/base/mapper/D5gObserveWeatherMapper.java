package cc.htdf.msgcloud.executor.base.mapper;

import cc.htdf.msgcloud.executor.base.domain.po.D5gObserveWeatherPO;
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
public interface D5gObserveWeatherMapper extends BaseMapper<D5gObserveWeatherPO> {

    @Select("select max(publish_date) from d_5g_observe_weather")
    String getLastPublishTime();

    @Select("delete from d_5g_observe_weather where publish_date < #{invalidDate}")
    void clearHistoryDate(@Param("invalidDate") String invalidDate);

    @Select("select * from d_5g_observe_weather where areacode = #{areacode} and publish_date = #{publish_date}")
    List<D5gObserveWeatherPO> getListByAreacodeAndPublishtime(@Param("areacode") Integer areacode,
                                                              @Param("publish_date") String publish_date);
}
