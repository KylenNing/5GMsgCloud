package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.common.utils.HtmlToImageUtils;
import cc.htdf.msgcloud.executor.base.BaseTest;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
public class ImportToRedisTest extends BaseTest {

    @Resource
    private ImportObserveDataToRedisJob importObserveDataToRedisJob;

    @Resource
    private ImportForecastDataToRedisJob importForecastDataToRedisJob;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void jobTest() throws Exception {
        importObserveDataToRedisJob.execute("");
    }

    @Test
    public void forecastJobTest() throws Exception {
        importForecastDataToRedisJob.execute("");
    }

    @Test
    public void redisTest(){
        String key = "test:20200824";
        Map map = new HashMap<>();
        map.put("7","c");
        map.put("8","d");
        //redisTemplate.delete(key);
        redisTemplate.opsForHash().putAll(key,map);
        redisTemplate.expire(key,20 , TimeUnit.SECONDS);
    }

    @Test
    public void imageTest() throws InterruptedException, AWTException, IOException {
        HtmlToImageUtils.htmlToImageByUrl("","");
    }

}