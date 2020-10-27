package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.executor.base.mapper.D5gObserveWeatherMapper;
import cc.htdf.msgcloud.executor.base.service.ImportTJDataService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
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
public class ImportTJObserveDataToMysqlJob extends IJobHandler {

    @Resource
    private ImportTJDataService importTJDataService;

    @Resource
    private D5gObserveWeatherMapper d5gObserveWeatherMapper;

    @XxlJob("COLLECT_TJ_OBSERVE_DATA")
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        String currentTime = DateUtils.formatDateToStr(
                new Date(),"yyyy-MM-dd HH:mm:ss").substring(0,14)+"00:00";
        String lastDateTime = d5gObserveWeatherMapper.getLastPublishTime();
        if(!currentTime.equals(lastDateTime)){
            XxlJobLogger.log("任务开始");
            importTJDataService.importObserveData();
            XxlJobLogger.log("任务结束");
        }else {
            XxlJobLogger.log("数据已经为最新，最新时次为{}",lastDateTime);
        }
        return ReturnT.SUCCESS;
    }
}
