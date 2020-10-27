package handlerTest;


import cc.htdf.msgcloud.message.handler.DynamicTemplateImageHelper;
import cc.htdf.msgcloud.message.handler.msgtemp.dynamictag.OperaMysqlIndexDataHandler;
import cc.htdf.msgcloud.message.handler.msgtemp.dynamictag.OperaMysqlObserveDataHandler;
import cc.htdf.msgcloud.message.handler.msgtemp.dynamictag.OperaRedisDataHandler;
import cc.htdf.msgcloud.message.handler.msgtemp.strategy.DynamicTagTemplateStrategy;
import io.minio.errors.*;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/8/10
 * @Description: TODO
 */
public class handlerTest extends BaseTest{

    @Resource
    private OperaRedisDataHandler operaRedisDataHandler;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DynamicTagTemplateStrategy dynamicTagTemplateStrategy;

    @Resource
    private OperaMysqlIndexDataHandler operaMysqlIndexDataHandler;

    @Resource
    private DynamicTemplateImageHelper dynamicTemplateImageHelper;

    @Resource
    private OperaMysqlObserveDataHandler operaMysqlObserveDataHandler;


    @Test
    public void getListTest() throws ParseException {
//        Map<String,Object> param = new HashMap<>();
//        param.put("observe_area","大连");
//        param.put("observe_interval",3);
//        operaRedisDataHandler.execute(param);
    }

    @Test
    public void redisTest(){
        redisTemplate.opsForValue().set("1","hello");
    }

    @Test
    public void forecastTest() throws ParseException {
        Map map = new HashMap();
        dynamicTagTemplateStrategy.handlerText(
                "{forecast_day_time},{forecast_day_temp},{forecast_day_wind},{forecast_day_wins},{forecast_day_pehno}",map);

    }

    @Test
    public void forecastHourTest() throws ParseException {
        Map map = new HashMap();
        dynamicTagTemplateStrategy.handlerText(
                "{forecast_hour_time},{forecast_hour_temp},{forecast_hour_rh},{forecast_hour_wind},{forecast_hour_wins},{forecast_hour_pehno}",map);

    }

    @Test
    public void observeIndexTest() throws ParseException {
        Map map = new HashMap();
        String result = dynamicTagTemplateStrategy.handlerText(
                "{index_fishing},{index_wearing},{index_wash_car},{index_ultraviolet_rays},{index_fire},{index_air_pollution}",map);
        System.out.println(result);
    }

    @Test
    public void indexTagTest() throws ParseException {
        Map map = new HashMap();
        operaMysqlIndexDataHandler.execute(map);
    }

    @Test
    public void getImage() throws IOException, ParseException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {

        dynamicTemplateImageHelper.getDynamicMedia("6190a4edb449413a80c0250bcf959a4e",new HashMap<>());
    }

    @Test
    public void observeWeatherTest() throws Exception {
        Map map = new HashMap();
        String result = dynamicTagTemplateStrategy.handlerText(
                "{observe-tj-temp},{observe-tj-pehno},{observe-tj-sunrise},{observe-tj-sunset},{observe-tj-maxtemp},{observe-tj-mintemp},"+
                        "{observe-tj-humdity},{observe-tj-wind},{observe-tj-wins}"
                        ,map);
        System.out.println(result);
    }

    @Test
    public void observeWeatherDataTest() throws Exception {
        operaMysqlObserveDataHandler.execute(new HashMap<>());
    }
}