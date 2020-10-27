package cc.htdf.msgcloud.datacenter.mapper;

import cc.htdf.msgcloud.datacenter.domain.po.D5gAirPO;
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
public interface D5gAirMapper extends BaseMapper<D5gAirPO> {

    @Select("select max(publish_date) from d_5g_air")
    String getLastPublishTime();

    @Select("select * from d_5g_air where publish_date = (select max(publish_date) from d_5g_air)" +
            "and data_source = #{dataSourceArea} and areacode = #{areacode}")
    D5gAirPO getDataByDataSource(
            @Param("dataSourceArea") Integer dataSourceArea,
            @Param("areacode") Integer areacode);
}
