package cc.htdf.msgcloud.executor.base.service.impl;

import cc.htdf.msgcloud.common.constants.WeatherByGWCCode;
import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.common.utils.HttpRequestUtils;
import cc.htdf.msgcloud.common.utils.NumberUtils;
import cc.htdf.msgcloud.common.utils.WeatherUtils;
import cc.htdf.msgcloud.executor.base.domain.dto.GwcWeatherValueDTO;
import cc.htdf.msgcloud.executor.base.domain.po.BAreaStationForecastPO;
import cc.htdf.msgcloud.executor.base.domain.po.BAreaStationPO;
import cc.htdf.msgcloud.executor.base.repository.BAreaStationForecastRepository;
import cc.htdf.msgcloud.executor.base.repository.BAreaStationRepository;
import cc.htdf.msgcloud.executor.base.service.ImportDataToRedisService;
import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
@Slf4j
@Service
public class ImportDataToRedisServiceImpl implements ImportDataToRedisService {

    @Resource
    private BAreaStationRepository bAreaStationRepository;

    @Resource
    private BAreaStationForecastRepository bAreaStationForecastRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${web-url.nation-station-data}")
    private String nationDataAddress;

    @Value("${dir.gwc-forecast-filepath}")
    private String gwcForecastFilepath;

    @Override
    public void importObserveDataToRedis() throws ParseException {
        //查询城市-台站号字典表
        List<BAreaStationPO> areaStationList = bAreaStationRepository.getAllAreaStation();
        //获取当前时次台站数据文件
        String currentTime = DateUtil.format(new Date(),"yyyyMMddHHmmss").substring(0,10)+"0000";
        //currentTime = "20200818150000";
        String observeTime = null;
        String url = nationDataAddress + currentTime + ".csv";
        InputStream inputStream = null;
        try {
            inputStream = HttpRequestUtils.getFileByUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = new BufferedReader(new InputStreamReader(inputStream))
                .lines().parallel().collect(Collectors.joining("\n"));
        List<String> lines = Arrays.asList(result.split("\n"));
        if(lines.size() < 1000){
            XxlJobLogger.log("当前时次数据文件未入库！");
            return;
        }
        for(BAreaStationPO areaStation : areaStationList){
            for(int i = 1; i < lines.size(); i++){
                String line = lines.get(i);
                String[] fields = line.split(",");
                String stationCode = fields[0];
                observeTime = DateUtils.formatDateToStr(DateUtils.parseDate(fields[1],"yyyy-MM-dd HH:mm:ss"),"yyyyMMddHHmmss");
                if(areaStation.getStationcode().equals(stationCode)){
                    Map<String,String> valueMap = new HashMap<>();
                    StringBuilder key = new StringBuilder();
                    key.append("msgcloud:001:observe:");
                    key.append(areaStation.getAreaname());
                    StringBuilder weatherValues = new StringBuilder();
//                    weatherValues.append(areaStation.getAreaname());
//                    weatherValues.append(",");
                    for(int j = 2; j< fields.length; j++){
                        weatherValues.append(fields[j]);
                        if(j < fields.length-1){
                            weatherValues.append(",");
                        }
                    }
                    valueMap.put(currentTime,weatherValues.toString());
                    redisTemplate.opsForHash().put(key.toString(), currentTime,weatherValues);
//                    Map<String,String> innermap = redisTemplate.opsForHash().entries(key);
//                    for(String keyTime : innermap.keySet()){
//                        Date tempTime = DateUtils.parseDate(keyTime,"yyyyMMddHHmmss");
//                        Date deleteTime = DateUtils.addDays(tempTime,-1);
//                        if(tempTime.getTime() < deleteTime.getTime()){
//                            innermap.remove(keyTime);
//                        }
//                    }
//                    redisTemplate.delete(key);
//                    redisTemplate.opsForHash().putAll(key,innermap);
                    break;
                }
            }
        }
        //测试
        Map<String,String> testMap = redisTemplate.opsForHash().entries("msgcloud:001:observe:北京");
        for(String k : testMap.keySet()){
            XxlJobLogger.log("测试数据-北京key:{}value:{}",k,testMap.get(k));
        }
        if(Objects.nonNull(observeTime)){

            XxlJobLogger.log("开始更新最新数据时间");
            redisTemplate.opsForValue().set("msgcloud:001:observe:lasttime",observeTime);
            XxlJobLogger.log("最新数据时间更新为{}",observeTime);

        }
    }

    @Override
    public void importForcastDataToRedis() throws ParseException, IOException {
        //查询城市-台站号字典表
        List<BAreaStationForecastPO> areaStationForecastList = bAreaStationForecastRepository.getAllAreaStation();
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
        String dataLastTime = currentTime+lastDateHour;
        String filename =  "defeng_point_" + currentTime + lastDateHour + ".txt";
        String  filePath = gwcForecastFilepath + filename;
        //获取当前时次台站数据文件
        String observeTime = null;
        XxlJobLogger.log("文件路径为{}",filePath);
        Map<String, List<String[]>> lineMap = Files.readAllLines(Paths.get(filePath)).stream().skip(1)
                .map(str -> str.split(",")).collect(Collectors.groupingBy(data -> data[0]));
        for(BAreaStationForecastPO areaStation : areaStationForecastList){
            //inkedHashMap<String,String> valueMap = new LinkedHashMap<>();
            List<String[]> values = lineMap.get(areaStation.getForecastrank());
            observeTime = values.get(0)[3];
            StringBuilder key = null;
            key = new StringBuilder();
            key.append("msgcloud:001:forecast:");
            key.append(areaStation.getAreaname());
            redisTemplate.delete(key.toString());
            int i = 0;
            List<GwcWeatherValueDTO> dtoList = new ArrayList<>();
            for(String[] value : values){
                i++;
                GwcWeatherValueDTO dto = new GwcWeatherValueDTO();
                StringBuilder weatherValues = new StringBuilder();
                for(int j = 5; j< value.length; j++){
                    weatherValues.append(value[j]);
                    if(j < value.length-1){
                        weatherValues.append(",");
                    }
                }
                String validTime = value[4].substring(0,19);
                validTime = DateUtils.formatDateToStr(DateUtils.addHour
                                (DateUtils.parseDate(validTime,"yyyy-MM-dd HH:mm:ss"),8),"yyyy-MM-dd HH:mm:ss");
//                valueMap.put(validTime,weatherValues.toString());
                dto.setDATETIME(validTime);
                dto.setTEMP(value[5]);
                dto.setWIND(WeatherUtils.getWindRect(Float.valueOf(value[6])));
                dto.setWINS(value[7]);
                dto.setWINDP(String.valueOf(WeatherUtils.countWindPowerByWindSpeed(Double.parseDouble(value[7]))));
                dto.setRH(value[9]);
                dto.setCLOUDCOVER(value[10]);
                dto.setPROBPRECIP(value[11]);
                dto.setRAIN(value[12]);
                dto.setVISIBILITY(value[15]);
                dto.setKUCHERASNOWRATE(value[17]);
                String[] conditions = NumberUtils.parseConditionCode(value[19]);
                String[] weather = WeatherUtils.getGwcWeatherTextByCode(conditions,
                        value[10],value[13],value[12]);
                dto.setCONDITIONSCODE(weather[0]);
                if(weather[1].equals("雪")){
                    dto.setCONDITIONSTEXT(getSnowLevel(Double.valueOf(value[12])));
                }else {
                    dto.setCONDITIONSTEXT(weather[1]);
                }
//                dto.setCONDITIONSCODE(value[19]);
//                dto.setCONDITIONSTEXT(value[20]);
                dtoList.add(dto);
                String dayTime = null;
                if(dtoList.size() == 24){
                    if(lastDateHour.equals(".00")){
                        dayTime = dtoList.get(0).getDATETIME().substring(0,10);
                    }else {
                        dayTime = DateUtils.formatDateToStr(DateUtils.addDays(
                                DateUtils.parseDate(dtoList.get(0).getDATETIME().substring(0,10),"yyyy-MM-dd"),1),"yyyy-MM-dd");
                    }
                    GwcWeatherValueDTO resValue = getDayWeatherDataByHourData(dayTime,dtoList);
                    redisTemplate.opsForHash().put(key.toString(), dayTime,resValue);
                    dtoList.clear();
                }

            }

        }
        //测试
        Map<String,String> testMap = redisTemplate.opsForHash().entries("msgcloud:001:forecast:北京");
        for(String k : testMap.keySet()){
            XxlJobLogger.log("测试数据-北京key:{}value:{}",k,testMap.get(k));
            log.info("测试数据-北京key:{}value:{}",k,testMap.get(k));
        }
        if(Objects.nonNull(dataLastTime)){

            XxlJobLogger.log("开始更新最新数据时间");
            redisTemplate.opsForValue().set("msgcloud:001:forecast:lasttime",dataLastTime);
            XxlJobLogger.log("最新数据时间更新为{}",dataLastTime);
        }
    }

    GwcWeatherValueDTO getDayWeatherDataByHourData(String datetime, List<GwcWeatherValueDTO> list){

        GwcWeatherValueDTO gwv = new GwcWeatherValueDTO();
        String maxtemp = list.get(0).getTEMP();//最高温
        String mintemp = list.get(0).getTEMP();//最低温
        double rain = 0;//降雨
        double precipitation = 0; //降水;
        double rh = 0;//相对湿度
        double snow = 0;
        String wind = list.get(0).getWIND();//风向
        String wins = list.get(0).getWINS();//风速
        String weather = "";//天气描述
        String code = "";
        String cloud = list.get(0).getCLOUDCOVER();
        String visibility = list.get(0).getVISIBILITY();
//        List listSnow = new ArrayList<>();
//        List listRain = new ArrayList<>();
//        List listFrain = new ArrayList<>();
//        List listMix = new ArrayList<>();
//        List listSleet = new ArrayList<>();
        boolean rainbool = false;
        boolean frainbool = false;
        boolean mixbool = false;
        boolean sleetbool = false;
        boolean snowbool = false;
        for(int i = 0 ; i<list.size();i++){
            if(Double.parseDouble(list.get(i).getTEMP().toString())>Double.parseDouble(maxtemp))
                maxtemp = list.get(i).getTEMP().toString();
            if(Double.parseDouble(list.get(i).getTEMP().toString())<Double.parseDouble(mintemp))
                mintemp = list.get(i).getTEMP().toString();
            if(Double.parseDouble(list.get(i).getWINS().toString())>Double.parseDouble(wins)){
                wins = list.get(i).getWINS().toString();
                wind = list.get(i).getWIND().toString();
            }
            if(Double.parseDouble(list.get(i).getVISIBILITY().toString())<Double.parseDouble(visibility)){
                visibility = list.get(i).getVISIBILITY().toString();
            }
            if(Double.parseDouble(list.get(i).getCLOUDCOVER().toString())>Double.parseDouble(cloud))
                cloud = list.get(i).getCLOUDCOVER();
            precipitation = precipitation + Double.parseDouble(list.get(i).getRAIN());
            rh = rh + Double.parseDouble(list.get(i).getRH());
            String ptype = list.get(i).getCONDITIONSCODE();
//            String[] conditions = NumberUtils.parseConditionCode(conditionscode);
//            String ptype = Integer.parseInt(conditions[2],2)+"";
            if(StringUtils.equals(ptype, WeatherByGWCCode.WEATHER_SNOW_CODE)){
//                listSnow.add(list.get(i));
                snowbool = true;
                snow = snow + Double.parseDouble(list.get(i).getRAIN());
            }
            if(StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_FRAIN_CODE)||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_LRAIN_CODE) ||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_RAIN_CODE) ||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_RAINSTORM_CODE) ||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_BRAINSTORM_CODE) ||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_HEAVY_RAINFALL_CODE) ||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_HRAIN_CODE)||
                    StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_STORM_CODE)){
//                listRain.add(list.get(i));
                rainbool = true ;
                rain = rain + Double.parseDouble(list.get(i).getRAIN());
            }
            if(StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_FRAIN_CODE)){
//                listFrain.add(list.get(i));
                frainbool = true;
            }

            if(StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_MIX_CODE) ){
//                listMix.add(list.get(i));
                mixbool = true;
            }
            if(StringUtils.equals(ptype,WeatherByGWCCode.WEATHER_SLEET_CODE)){
//                listSleet.add(list.get(i));
                sleetbool = true;
            }
        }
        String rs[]=  this.getWeatherCodeByDay(snowbool,rainbool,frainbool,mixbool,sleetbool,cloud,snow,precipitation);
        weather = rs[1];
        code = rs[0];
        rh = rh/list.size();
        gwv.setRAIN(String.format("%.2f", precipitation));
        gwv.setRH(String.format("%.2f", rh));
        gwv.setMAXTEMP(maxtemp);
        gwv.setMINTEMP(mintemp);
        gwv.setWIND(wind);
        gwv.setWINS(wins);
        gwv.setWINDP(String.valueOf(WeatherUtils.countWindPowerByWindSpeed(Double.parseDouble(wins))));
        gwv.setRAIN24(String.format("%.2f", rain));
        gwv.setSNOW24(String.format("%.2f",snow));
        gwv.setDATETIME(datetime.split(" ")[0]);
        gwv.setCONDITIONSTEXT(weather);
        gwv.setCONDITIONSCODE(code);
        gwv.setCLOUDCOVER(cloud);
        gwv.setVISIBILITY(visibility);
        gwv.setEDATETIME(list.get(list.size()-1).getDATETIME());
        gwv.setSDATETIME(list.get(0).getDATETIME());
        gwv.setSUNRISE(list.get(0).getSUNRISE());
        gwv.setSUNSET(list.get(0).getSUNSET());
        return gwv;

    }

    public String[] getWeatherCodeByDay(
            boolean snowbool,
            boolean rainbool,
            boolean frainbool,
            boolean mixbool,
            boolean sleetbool ,
            String cloud,double snow,double rain){
        String rs[] = new String[2];
        if(snowbool){
            if(rainbool||frainbool||mixbool||sleetbool){
                rs[0] = WeatherByGWCCode.WEATHER_SLEET_CODE;
                rs[1] = WeatherByGWCCode.mapValuePreHour.get(WeatherByGWCCode.WEATHER_SLEET_CODE).toString();
                return rs;
            }else {
                String code = WeatherUtils.transformSnowText24(snow);
                rs[0] = code;
                rs[1] = WeatherByGWCCode.mapValuePreHour.get(code).toString();
                return rs;
            }
        }
        if(mixbool){
            rs[0] = WeatherByGWCCode.WEATHER_MIX_CODE;
            rs[1] = WeatherByGWCCode.mapValuePreHour.get(WeatherByGWCCode.WEATHER_MIX_CODE).toString();
            return rs;
        }
        if(frainbool){
            rs[0] = WeatherByGWCCode.WEATHER_FRAIN_CODE;
            rs[1] = WeatherByGWCCode.mapValuePreHour.get(WeatherByGWCCode.WEATHER_FRAIN_CODE).toString();
            return rs;
        }
        if(rainbool){
            String code = WeatherUtils.transformRainText24(rain);
            rs[0] = code;
            if(!Objects.isNull(code) && !StringUtils.equals(code,"0")){
                rs[1] = WeatherByGWCCode.mapValuePreHour.get(code).toString();
                return rs;
            }
        }
        rs[0] = WeatherUtils.getCloudValue(cloud);
        rs[1] = WeatherByGWCCode.mapValueCoverage.get(WeatherUtils.getCloudValue(cloud)).toString();
        return rs;
    }

    private String getSnowLevel(Double value){

        String level = "";
        Map<Double[], String> levelMap = new LinkedHashMap<>();
        levelMap.put(new Double[]{0.0, 0.9}, "小雪");
        levelMap.put(new Double[]{1D, 4.9}, "中雪");
        levelMap.put(new Double[]{5D, 100D}, "大雪");
        log.info("雨量为{}",value);
        for (Map.Entry<Double[], String> entry : levelMap.entrySet()) {
            if (value >= entry.getKey()[0] && value < entry.getKey()[1]) {
                level = entry.getValue();
                log.info("降雪级别为{}",level);
            }
        }
        log.info("输出降雪级别为{}",level);
        return level;
    }
}