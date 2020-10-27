package cc.htdf.msgcloud.executor.base.service.impl;

import cc.htdf.msgcloud.common.utils.DateUtils;
import cc.htdf.msgcloud.common.utils.WeatherUtils;
import cc.htdf.msgcloud.executor.base.domain.po.*;
import cc.htdf.msgcloud.executor.base.mapper.*;
import cc.htdf.msgcloud.executor.base.service.ImportTJDataService;
import cc.htdf.msgcloud.executor.base.utils.AreaCodeTransformUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Slf4j
@Service
public class ImportTJDataServiceImpl implements ImportTJDataService {

    @Value("${web-url.tj-forecast-data}")
    private String tjForecastDataAddress;

    @Value("${web-url.tj-observe-data}")
    private String tjObserveDataAddress;

    @Value("${web-url.tj-index-data}")
    private String tjIndexDataAddress;

    @Resource
    private D5gHourWeatherMapper d5gHourWeatherMapper;

    @Resource
    private D5gDayWeatherMapper d5gDayWeatherMapper;

    @Resource
    private D5gObserveWeatherMapper d5gObserveWeatherMapper;

    @Resource
    private BAreaMapper bAreaMapper;

    @Resource
    private BAreaNineMapper bAreaNineMapper;

    @Resource
    private D5gAirMapper d5gAirMapper;

    @Resource
    private D5gLifeIndexMapper d5gLifeIndexMapper;

    private final Integer dataSorceArea = 120100;


    @Override
    public void importHourData() throws ParseException {
        EntityWrapper<BAreaNinePO> wrapper = new EntityWrapper<>();
        List<BAreaNinePO> areaNinePOList = bAreaNineMapper.selectList(wrapper);
        EntityWrapper<BAreaPO> areaWrapper = new EntityWrapper<>();
        List<BAreaPO> areaList = bAreaMapper.selectList(areaWrapper);
        Map<Integer,String> areaMap = areaList.stream().collect(Collectors.toMap(BAreaPO::getId,BAreaPO::getAreaname));
        Date nowHourDate = DateUtils.parseDate(DateUtils.formatDateToStr(new Date(),"yyyyMMddHHmmss").substring(0,10) + "0000","yyyyMMddHHmmss");
        for(BAreaNinePO areaNinePO : areaNinePOList){
            if(Objects.isNull(areaNinePO.getAreaidsix())){
                XxlJobLogger.log("[{}]Areaidsix为空",areaNinePO.getNamecn());
                continue;
            }
            StringBuilder url = new StringBuilder();
            url.append(tjForecastDataAddress);
            url.append("&dtype=hourfc");
            url.append("&areaid=" + String.valueOf(areaNinePO.getAreaid()));
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(),String.class);
            Map<String,Object> obj = null;
            try{
              obj  = JSON.parseObject(responseEntity.getBody(), Map.class);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
            List<Map<String,String>> dataList = (List)obj.get("jh");
            Date publishDate = null;
            try{
                 publishDate = DateUtils.parseDate(dataList.get(0).get("jf")+"00","yyyyMMddHHmmss");
                 if(publishDate.getTime() != nowHourDate.getTime()){
                     XxlJobLogger.log("[{}]数据非最新时次数据",areaNinePO.getNamecn());
                     continue;
                 }
            }catch (Exception e){
                XxlJobLogger.log("问题数据[{}]",areaNinePO.getNamecn());
                continue;
            }
            int areaCode = areaNinePO.getAreaidsix();
            //String areaname = bAreaMapper.getAreanameById(areaCode);
            XxlJobLogger.log("开始入库{}数据",areaNinePO.getNamecn());
            for(Map<String,String> map : dataList){
                D5gHourWeatherPO hourDataPO = new D5gHourWeatherPO();
                hourDataPO.setAreacode(areaCode);
                hourDataPO.setAreaname(areaMap.get(areaCode));
                hourDataPO.setDataSource(dataSorceArea);
                hourDataPO.setPhenomena(WeatherUtils.getPhenomenaByCode(map.get("ja")));
                hourDataPO.setTemp(Double.valueOf(map.get("jb")));
                hourDataPO.setWindDir(WeatherUtils.getWindDirByCode(map.get("jc")));
                hourDataPO.setWindSpeed(WeatherUtils.getWindSpeedByCode(map.get("jd")));
                hourDataPO.setHumidity(Double.valueOf(map.get("je")));
                hourDataPO.setPublishDate(publishDate);
                hourDataPO.setValidDate(DateUtils.parseDate(map.get("jf")+"00","yyyyMMddHHmmss"));
                //hourDataPO.setAirQuality();
                //XxlJobLogger.log("开始入库{}时次数据",map.get("jf")+"00");
                d5gHourWeatherMapper.insert(hourDataPO);
            }
            XxlJobLogger.log("成功入库{}时次发布的数据",publishDate);
        }

    }

    @Override
    public void importDayData() throws ParseException {
        EntityWrapper<BAreaNinePO> wrapper = new EntityWrapper<>();
        List<BAreaNinePO> areaNinePOList = bAreaNineMapper.selectList(wrapper);
        EntityWrapper<BAreaPO> areaWrapper = new EntityWrapper<>();
        List<BAreaPO> areaList = bAreaMapper.selectList(areaWrapper);
        Map<Integer,String> areaMap = areaList.stream().collect(Collectors.toMap(BAreaPO::getId,BAreaPO::getAreaname));
        for(BAreaNinePO areaNinePO : areaNinePOList){
            if(Objects.isNull(areaNinePO.getAreaidsix())){
                continue;
            }
            StringBuilder url = new StringBuilder();
            url.append(tjForecastDataAddress);
            url.append("&dtype=forecast15d");
            url.append("&areaid=" + String.valueOf(areaNinePO.getAreaid()));
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(),String.class);
            Map<String,Object> obj = null;
            try{
                obj = JSON.parseObject(responseEntity.getBody(), Map.class);
            }catch (Exception e){
                XxlJobLogger.log("问题数据{}",areaNinePO.getNamecn());
            }
            Map<String,Object> dataMap = (Map<String, Object>) obj.get("f");
            Date publishDate = null;
            try{
                publishDate = DateUtils.parseDate(dataMap.get("f0")+"00","yyyyMMddHHmmss");
            }catch (Exception e){
                XxlJobLogger.log("问题数据[{}]",areaNinePO.getNamecn());
                continue;
            }
            Date firstValidDate = DateUtils.parseDate(dataMap.get("f0").toString().substring(0,8)+"000000","yyyyMMddHHmmss");
            List<Map<String,String>> dataList = (List<Map<String, String>>) dataMap.get("f1");
            int areaCode = areaNinePO.getAreaidsix();
            String areaname = areaMap.get(areaCode);
            //String areaname = bAreaMapper.getAreanameById(areaCode);
            XxlJobLogger.log("开始入库{}数据",areaname);
            int count = 0;
            for(Map<String,String> map : dataList){
                D5gDayWeatherPO dayDataPO = new D5gDayWeatherPO();
                dayDataPO.setAreacode(areaCode);
                dayDataPO.setAreaname(areaname);
                dayDataPO.setDataSource(dataSorceArea);
                dayDataPO.setPublishDate(publishDate);
                dayDataPO.setValidDate(DateUtils.addDays(firstValidDate,count));
                dayDataPO.setDayPhenomena(WeatherUtils.getPhenomenaByCode(map.get("fa")));
                dayDataPO.setNightPhenomena(WeatherUtils.getPhenomenaByCode(map.get("fb")));
                dayDataPO.setDayTemp(Double.valueOf(map.get("fc")));
                dayDataPO.setNightTemp(Double.valueOf(map.get("fd")));
                dayDataPO.setDayWindDir(WeatherUtils.getWindDirByCode(map.get("fe")));
                dayDataPO.setNightWindDir(WeatherUtils.getWindDirByCode(map.get("ff")));
                dayDataPO.setDayWindSpeed(WeatherUtils.getWindSpeedByCode(map.get("fg")));
                dayDataPO.setNightWindSpeed(WeatherUtils.getWindSpeedByCode(map.get("fh")));
                String sunTime = map.get("fi");
                dayDataPO.setSunRise(sunTime.substring(0,sunTime.indexOf("|")));
                dayDataPO.setSunSet(sunTime.substring(sunTime.indexOf("|")+1,sunTime.length()));
                //hourDataPO.setAirQuality();
                //XxlJobLogger.log("开始入库{}时次数据",map.get("jf")+"00");
                d5gDayWeatherMapper.insert(dayDataPO);
                count++;
            }
            XxlJobLogger.log("成功入库{}时次发布的数据",publishDate);
        }

    }

    @Override
    public void importObserveData() throws ParseException {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(tjObserveDataAddress.toString(),String.class);
        Map<String,Object> obj = JSON.parseObject(responseEntity.getBody(), Map.class);
        Date publishDate = DateUtils.parseDate((String) obj.get("datemarkcr"),"yyyy-MM-dd HH:mm:ss");
        List<Map<String,String>> dataList = (List<Map<String, String>>) obj.get("data");
        for(int tjArea : AreaCodeTransformUtils.commonTjAreaList){
            for(Map<String,String> map : dataList){
                D5gObserveWeatherPO observeDataPO = new D5gObserveWeatherPO();
                String stationCode = getAreacodeByStation(tjArea);
                if(stationCode.equals(map.get("stationCode"))){
                    observeDataPO.setAreacode(tjArea);
                    //observeDataPO.setArea(map.get("AREA"));
                    observeDataPO.setArea(bAreaMapper.getAreanameById(tjArea));
                    observeDataPO.setLon(Double.valueOf(map.get("LON")));
                    observeDataPO.setLat(Double.valueOf(map.get("LAT")));
                    observeDataPO.setWindDirExMax(map.get("windDirectExMax"));
                    observeDataPO.setWindGrade(map.get("windGrade"));
                    observeDataPO.setWindGradeExMax(map.get("windGradeExMax"));
                    observeDataPO.setStationcode(map.get("stationCode"));
                    observeDataPO.setVisibility(map.get("visibility"));
                    observeDataPO.setRainfall(Double.valueOf(map.get("rainfall")));
                    observeDataPO.setTemp(Double.valueOf(map.get("temperature")));
                    observeDataPO.setMaxTemp(Double.valueOf(map.get("maxTem")));
                    observeDataPO.setMinTemp(Double.valueOf(map.get("minTem")));
                    observeDataPO.setMaxTempTime(map.get("maxTemTime"));
                    observeDataPO.setMinTempTime(map.get("minTemTime"));
                    observeDataPO.setWindDir(map.get("windDirect"));
                    observeDataPO.setWindSpeed(map.get("windSpeed"));
                    observeDataPO.setWindExMaxTime(map.get("windExMaxTime"));
                    observeDataPO.setHumdity(Double.valueOf(map.get("relHumdity")));
                    observeDataPO.setStationPress(map.get("stationPress"));
                    observeDataPO.setWindSpeedExMax(map.get("windSpeedExMax"));
                    observeDataPO.setStationName(map.get("stationName"));
                    observeDataPO.setPublishDate(publishDate);
                    observeDataPO.setDataSource(dataSorceArea);
                    String tempTime = DateUtils.formatDateToStr(publishDate,"yyyy-MM-dd HH:mm:ss");
                    if(d5gObserveWeatherMapper.getListByAreacodeAndPublishtime(tjArea,tempTime).size() == 0){
                        XxlJobLogger.log("开始入库{}数据",map.get("AREA"));
                        d5gObserveWeatherMapper.insert(observeDataPO);
                    }else {
                        XxlJobLogger.log("{}数据已存在",map.get("AREA"));
                    }
                    break;
                }
            }
        }
        XxlJobLogger.log("成功入库{}时次发布的数据",publishDate);
    }

    @Override
    public void importAirData() throws ParseException {
        EntityWrapper<BAreaNinePO> wrapper = new EntityWrapper<>();
        List<BAreaNinePO> areaNinePOList = bAreaNineMapper.selectList(wrapper);
        Date publishDate = null;
        for(BAreaNinePO areaNinePO : areaNinePOList){
            if(Objects.isNull(areaNinePO.getAreaidsix())){
                continue;
            }
            StringBuilder url = new StringBuilder();
            url.append(tjForecastDataAddress);
            url.append("&dtype=air");
            url.append("&areaid=" + String.valueOf(areaNinePO.getAreaid()));
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(),String.class);
            Map<String,Object> obj = null;
            try{
                obj = JSON.parseObject(responseEntity.getBody(), Map.class);
            }catch (Exception e){
                XxlJobLogger.log("问题数据[{}]",areaNinePO.getNamecn());
                continue;
            }
            Map<String,String> dataMap = (Map<String, String>) obj.get("p");
            try{
                publishDate = DateUtils.parseDate(dataMap.get("p9")+"00","yyyyMMddHHmmss");
            }catch (Exception e){
                XxlJobLogger.log("问题数据[{}]",areaNinePO.getNamecn());
                continue;
            }
            int areaCode = areaNinePO.getAreaidsix();
            String areaname = bAreaMapper.getAreanameById(areaCode);
            D5gAirPO airPO = new D5gAirPO();
            airPO.setAreacode(areaCode);
            airPO.setAreaname(areaname);
            airPO.setPm25(dataMap.get("p1"));
            airPO.setAqi(dataMap.get("p2"));
            airPO.setPm10(dataMap.get("p5"));
            airPO.setPublishDate(publishDate);
            airPO.setDataSource(dataSorceArea);
            XxlJobLogger.log("开始入库{}数据",areaname);
            d5gAirMapper.insert(airPO);
            XxlJobLogger.log("成功入库{}数据",areaname);

        }
        XxlJobLogger.log("成功入库{}时次发布的空气质量数据",publishDate);


    }

    @Override
    public void importLifeIndexData() throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(tjIndexDataAddress.toString(),String.class);
        Map<String,Object> obj = JSON.parseObject(responseEntity.getBody(), Map.class);
        Date publishDate = DateUtils.parseDate((String) obj.get("pubtimeCrStr"),"yyyy-MM-dd HH:mm:ss");
        List<Map<String,String>> dataList = (List<Map<String, String>>) obj.get("data");
        XxlJobLogger.log("开始入库");
        for(Map<String,String> map : dataList){
            D5gLifeIndexPO lifeIndexPO = new D5gLifeIndexPO();
            lifeIndexPO.setAreacode(dataSorceArea);
            lifeIndexPO.setAreaname("天津市");
            lifeIndexPO.setPublishDate(publishDate);
            lifeIndexPO.setIndexName(map.get("i_name"));
            lifeIndexPO.setIndexLevel(map.get("i_level"));
            lifeIndexPO.setIndexContent(map.get("i_prompt"));
            lifeIndexPO.setIndexType(map.get("i_type"));
            lifeIndexPO.setDataSource(dataSorceArea);
            d5gLifeIndexMapper.insert(lifeIndexPO);
        }
        XxlJobLogger.log("成功入库{}时次发布的数据",publishDate);
    }



//    public void updateAreaTable(){
//        EntityWrapper<BAreaPO> wrapper = new EntityWrapper<>();
//        List<BAreaPO> areaList = bAreaMapper.selectList(wrapper);
//        Map<String,BAreaPO> areaMap = new HashMap<>();
//        List list = new ArrayList();
//        for(BAreaPO areaPO : areaList){
//            Set<String> keySet = areaMap.keySet();
//            if(!keySet.contains(areaPO.getShortname())){
//                areaMap.put(areaPO.getShortname(),areaPO);
//            }else {
//                list.add(areaPO);
//            }
//        }
//        EntityWrapper<BAreaNinePO> nineWrapper = new EntityWrapper<>();
//        List<BAreaNinePO> nineAreaList = bAreaNineMapper.selectList(nineWrapper);
//        List tl = new ArrayList();
//        for(BAreaNinePO ninePO : nineAreaList){
//            Integer areacode = null;
//            try{
//                areacode = areaMap.get(ninePO.getNamecn()).getId();
//                bAreaNineMapper.updateSixAreacodeByShortName(areacode,ninePO.getAreaid());
//            }catch (Exception e){
//                tl.add(ninePO);
//            }
//        }
//    }

    @Override
    public void clearHistoryData() {

        String lastThreeDayDate = DateUtils.formatDateToStr(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd HH:mm:ss");
        String lastSevenDayDate = DateUtils.formatDateToStr(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd HH:mm:ss");

        XxlJobLogger.log("开始清除{}之前天次历史数据！", lastThreeDayDate);
        d5gDayWeatherMapper.clearHistoryDate(lastThreeDayDate);
        XxlJobLogger.log("成功清除天次历史数据！");

        XxlJobLogger.log("开始清除{}之前小时历史数据！", lastThreeDayDate);
        d5gHourWeatherMapper.clearHistoryDate(lastThreeDayDate);
        XxlJobLogger.log("成功清除小时历史数据！");

        XxlJobLogger.log("开始清除{}之前观测历史数据！", lastSevenDayDate);
        d5gObserveWeatherMapper.clearHistoryDate(lastSevenDayDate);
        XxlJobLogger.log("成功清除观测历史数据！");

        XxlJobLogger.log("开始清除{}之前空气质量历史数据！", lastSevenDayDate);
        d5gAirMapper.clearHistoryDate(lastSevenDayDate);
        XxlJobLogger.log("成功清除空气质量历史数据！");

        XxlJobLogger.log("开始清除{}之前生活指数历史数据！", lastSevenDayDate);
        d5gLifeIndexMapper.clearHistoryDate(lastSevenDayDate);
        XxlJobLogger.log("成功清除生活指数历史数据！");
    }

    public String getAreacodeByStation(Integer code){
        Map<Integer,String> map = new HashMap<Integer, String>(){{
            put(120000,"54528");
            put(120100,"54528");
            put(120101,"54526");
            put(120102,"54526");
            put(120103,"54517");
            put(120104,"54527");
            put(120105,"54528");
            put(120106,"54528");
            put(120110,"54526");
            put(120111,"54527");
            put(120112,"54622");
            put(120113,"54528");
            put(120114,"54523");
            put(120115,"54525");
            put(120116,"54623");
            put(120221,"54529");
            put(120223,"54619");
            put(120225,"54428");

        }};
        return map.get(code);
    }
}