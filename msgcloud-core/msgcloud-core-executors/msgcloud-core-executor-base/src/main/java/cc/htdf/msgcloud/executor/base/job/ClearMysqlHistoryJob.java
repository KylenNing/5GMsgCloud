package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.executor.base.service.ImportTJDataService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * author: JT
 * date: 2020/8/6
 * title:
 */
@Slf4j
@Component
public class ClearMysqlHistoryJob extends IJobHandler {

    @Resource
    private ImportTJDataService importTJDataService;

    @Resource
    private RedisTemplate redisTemplate;

    @XxlJob("CLEAR_MYSQL_HISTORY")
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        XxlJobLogger.log("开始清理历史数据！");
        importTJDataService.clearHistoryData();
        XxlJobLogger.log("成功清理历史数据！");
        return ReturnT.SUCCESS;
    }
}
