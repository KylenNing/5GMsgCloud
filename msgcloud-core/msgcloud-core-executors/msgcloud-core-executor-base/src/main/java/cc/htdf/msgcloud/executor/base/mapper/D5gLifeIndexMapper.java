package cc.htdf.msgcloud.executor.base.mapper;

import cc.htdf.msgcloud.executor.base.domain.po.D5gLifeIndexPO;
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
public interface D5gLifeIndexMapper extends BaseMapper<D5gLifeIndexPO> {

    @Select("select max(publish_date) from d_5g_life_index")
    Date getLastPublishTime();

    @Select("delete from d_5g_life_index where publish_date < #{invalidDate}")
    void clearHistoryDate(@Param("invalidDate") String invalidDate);
}
