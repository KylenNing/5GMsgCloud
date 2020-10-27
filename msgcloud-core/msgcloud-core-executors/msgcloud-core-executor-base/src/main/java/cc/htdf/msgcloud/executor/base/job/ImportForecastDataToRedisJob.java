package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.executor.base.service.ImportDataToRedisService;
import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * author: JT
 * date: 2020/8/6
 * title:
 */
@Slf4j
@Component
public class ImportForecastDataToRedisJob extends IJobHandler {

    @Resource
    private ImportDataToRedisService importDataToRedisService;

    @Resource
    private RedisTemplate redisTemplate;

    @XxlJob("COLLECT_FORECAST_TO_REDIS")
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        String dataLastTime = String.valueOf(redisTemplate.opsForValue().get("msgcloud:001:forecast:lasttime"));
        String currentTime = DateUtil.format(new Date(),"yyyyMMddHHmmss").substring(0,8);
        Integer currentHour = Integer.valueOf(DateUtil.format(new Date(),"yyyyMMddHHmmss").substring(8,10));
        String lastDateHour = null;
        if(currentHour >= 8 && currentHour < 20){
            lastDateHour = ".00";
        }else {
            lastDateHour = ".12";
            if(currentHour<8){
                currentTime = DateUtil.format(DateUtils.addDays(new Date(),-1),"yyyyMMddHHmmss").substring(8,10);
            }
        }
        currentTime = currentTime+lastDateHour;
        if(!currentTime.equals(dataLastTime)){
            XxlJobLogger.log("开始导入预报天气数据！");
            importDataToRedisService.importForcastDataToRedis();
            XxlJobLogger.log("成功导入预报天气数据！");
        }else {
            XxlJobLogger.log("数据为最新，currentTime为{},dataLastTime为{}",currentTime,dataLastTime);
        }
//        XxlJobLogger.log("开始导入实况天气数据！");
//        importDataToRedisService.importForcastDataToRedis();
//        XxlJobLogger.log("成功导入实况天气数据！");
        return ReturnT.SUCCESS;
    }
}
