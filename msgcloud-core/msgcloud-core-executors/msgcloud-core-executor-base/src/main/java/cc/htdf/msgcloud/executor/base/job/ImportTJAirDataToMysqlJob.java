package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.executor.base.mapper.D5gAirMapper;
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
public class ImportTJAirDataToMysqlJob extends IJobHandler {

    @Resource
    private ImportTJDataService importTJDataService;

    @Resource
    private D5gAirMapper d5gAirMapper;

    @XxlJob("COLLECT_TJ_AIR_DATA")
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        String testUrl = "http://60.29.105.39:8061/PewckDS/json_origswaquery?dtype=air&areaid=101030100&ud=fklk3ifjpo2ks";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(testUrl,String.class);
        Map<String,Object> obj = JSON.parseObject(responseEntity.getBody(), Map.class);
        Map<String,String> dataMap = (Map<String, String>) obj.get("p");
        Date publishDate  = DateUtils.parseDate(dataMap.get("p9")+"00","yyyyMMddHHmmss");
        Date lastDateTime = d5gAirMapper.getLastPublishTime();
        if(Objects.isNull(lastDateTime) || publishDate.getTime() != lastDateTime.getTime()){
            XxlJobLogger.log("任务开始");
            importTJDataService.importAirData();
            XxlJobLogger.log("任务结束");
        }else {
            XxlJobLogger.log("数据已经为最新，最新时次为{}",lastDateTime);
        }
        return ReturnT.SUCCESS;
    }
}
