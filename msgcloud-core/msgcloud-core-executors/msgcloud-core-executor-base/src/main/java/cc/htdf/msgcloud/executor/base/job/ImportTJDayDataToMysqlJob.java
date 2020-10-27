package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.executor.base.mapper.D5gDayWeatherMapper;
import cc.htdf.msgcloud.executor.base.service.ImportTJDataService;
import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/6
 * title:
 */
@Slf4j
@Component
public class ImportTJDayDataToMysqlJob extends IJobHandler {

    @Resource
    private ImportTJDataService importTJDataService;

    @Resource
    private D5gDayWeatherMapper d5gDayWeatherMapper;

    @XxlJob("COLLECT_TJ_DAY_DATA")
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        String testUrl = "http://60.29.105.39:8061/PewckDS/json_origswaquery?dtype=forecast15d&areaid=101030100&ud=fklk3ifjpo2ks";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(testUrl.toString(),String.class);
        Map<String,Object> obj = JSON.parseObject(responseEntity.getBody(), Map.class);
        Map<String,Object> dataMap = (Map<String, Object>) obj.get("f");
        Date currentDateTime = DateUtils.parseDate(dataMap.get("f0")+"00","yyyyMMddHHmmss");
        Date lastDateTime = d5gDayWeatherMapper.getLastPublishTime();
        if(Objects.isNull(lastDateTime) || currentDateTime.getTime() != lastDateTime.getTime()){
            XxlJobLogger.log("任务开始");
            importTJDataService.importDayData();
            XxlJobLogger.log("任务结束");
        }else {
            XxlJobLogger.log("数据已经为最新，最新时次为{}",lastDateTime);
        }
        return ReturnT.SUCCESS;
    }
}
