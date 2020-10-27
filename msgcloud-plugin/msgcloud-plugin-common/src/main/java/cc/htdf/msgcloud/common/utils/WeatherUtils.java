package cc.htdf.msgcloud.common.utils;

import cc.htdf.msgcloud.common.constants.WeatherByGWCCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class WeatherUtils {

    private static Map<Integer, Integer> phenomenonCodeMap = new HashMap<Integer, Integer>() {{

            put(0, 10100);
            put(1, 10100);
            put(2, 10100);
            put(3, 10100);
            put(4, 10200);
            put(5, 10201);
            put(6, 10202);
            put(7, 10202);
            put(8, 10202);
            put(9, 10203);
            put(10, 10300);
            put(11, 10300);
            put(12, 10300);
            put(13, 10200);
            put(14, 10200);
            put(15, 10200);
            put(16, 10200);
            put(17, 10200);
            put(18, 10200);
            put(19, 10100);
            put(20, 10200);
            put(21, 10200);
            put(22, 10200);
            put(23, 10200);
            put(24, 10200);
            put(25, 10200);
            put(26, 10200);
            put(27, 10200);
            put(28, 10200);
            put(29, 10200);
            put(30, 10203);
            put(31, 10203);
            put(32, 10203);
            put(33, 10203);
            put(34, 10203);
            put(35, 10203);
            put(36, 10701);
            put(37, 10701);
            put(38, 10701);
            put(39, 10701);
            put(40, 10301);
            put(41, 10301);
            put(42, 10301);
            put(43, 10301);
            put(44, 10301);
            put(45, 10301);
            put(46, 10301);
            put(47, 10301);
            put(48, 10301);
            put(49, 10301);
            put(50, 10602);
            put(51, 10602);
            put(52, 10602);
            put(53, 10602);
            put(54, 10602);
            put(55, 10602);
            put(56, 10606);
            put(57, 10606);
            put(58, 10650);
            put(59, 10650);
            put(60, 10602);
            put(61, 10602);
            put(62, 10603);
            put(63, 10603);
            put(64, 10604);
            put(65, 10604);
            put(66, 10606);
            put(67, 10606);
            put(68, 10650);
            put(69, 10650);
            put(70, 10702);
            put(71, 10702);
            put(72, 10703);
            put(73, 10703);
            put(74, 10704);
            put(75, 10704);
            put(76, 10700);
            put(77, 10700);
            put(78, 10700);
            put(79, 10700);
            put(80, 10605);
            put(81, 10605);
            put(82, 10605);
            put(83, 10650);
            put(84, 10650);
            put(85, 10705);
            put(86, 10705);
            put(87, 10705);
            put(88, 10705);
            put(89, 10607);
            put(90, 10607);
            put(91, 10602);
            put(92, 10603);
            put(93, 10702);
            put(94, 10703);
            put(95, 1100);
            put(96, 10401);
            put(97, 1500);
            put(98, 10401);
            put(99, 1500);
            //处理后天气现象
            put(2100,2100);
            put(2200,2200);
            put(2200,2200);
            put(100, 100);
            put(200, 200);
            put(300, 300);
            put(400, 400);
            put(800, 800);
            put(900, 900);
            put(1000, 1000);
            put(500, 500);
            put(600, 600);
            put(700, 700);
            put(1100, 1100);
            put(1200,1200);
            put(1300,1300);
            put(1400,1400);
            put(1500,1500);
    }};

    private static Map<Integer, String> phenomenonDistMap = new HashMap<Integer, String>(){{
        put(10100, "晴");
        put(10101, "多云");

        put(10200, "烟");
        put(10201, "霾");
        put(10202, "尘");
        put(10203, "沙尘暴");

        put(10300, "轻雾");
        put(10301, "雾");

        put(10400, "闪电");
        put(10401, "雷暴");

        put(10500, "飑");
        put(10501, "漏斗云");

        put(10600, "雨");
        put(10601, "毛毛雨");
        put(10602, "小雨");
        put(10603, "中雨");
        put(10604, "大雨");
        put(10605, "阵雨");
        put(10606, "冻雨");
        put(10607, "冰雹");
        put(10650, "雨夹雪");

        put(10700, "雪");
        put(10701, "吹雪");
        put(10702, "小雪");
        put(10703, "中雪");
        put(10704, "大雪");
        put(10705, "阵雪");

        put(2100,"晴");
        put(2200,"多云");
        put(2200,"多云");
        put(100, "雪");
        put(200, "雨");
        put(300, "小雨");
        put(400, "大雨");
        put(800, "暴雨");
        put(900, "大暴雨");
        put(1000, "特大暴雨");
        put(500, "雨夹雪");
        put(600, "雨加雪");
        put(700, "冻雨");
        put(1100, "雷阵雨");
        put(1200,"小雪");
        put(1300,"中雪");
        put(1400,"大雪");
        put(1500,"暴雪");
    }};

    public static Integer getWeatherPhenomenonCode(Integer code) {
        return phenomenonCodeMap.get(code);
    }

    /**
     * 获取天气现象描述
     * @param code
     * @return
     */
    public static String getWeatherPhenomenonByCode(Integer code) {
        return phenomenonDistMap.get(phenomenonCodeMap.get(code));
    }


    public static Integer countWindPowerByWindSpeed(double windspeed) {
        double[] valueRange = new double[] {
                0, 0.2, 1.5, 3.3, 5.4, 7.9, 10.7, 13.8, 17.1, 20.7,
                24.4, 28.4, 32.6, 36.9, 41.4, 46.4, 50.9, 56.0, 61.2
        };
        int[] powerLevels = new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17
        };
        for (int i = 0; i < valueRange.length - 1; i++) {
            if (windspeed > valueRange[i] && windspeed <= valueRange[i+1]) {
                return powerLevels[i];
            }
        }
        return 0;
    }


    /**
     * 施工指数生成
     * @param maxTemplate
     * @param precipSum
     * @param maxWindSpeed
     * @return
     *      0: 指数值
     *      1: 指数码
     *      2: 指数描述
     *      3: 建议
     */
    public static String[] productConstructionIndex(double maxTemplate, double precipSum, double maxWindSpeed) {
        String[] constructionIndex = new String[4];
        Integer maxWindPower = WeatherUtils.countWindPowerByWindSpeed(maxWindSpeed);
        constructionIndex[0] = "90";
        constructionIndex[1] = "A";
        constructionIndex[2] = "优";
        constructionIndex[3] = "最佳施工期";
//        if (maxTemplate > 10 && maxTemplate < 25 && precipSum < 0.01 && maxWindPower < 4) {
//            constructionIndex[1] = "最佳施工期";
//        }
        if (
                ((maxTemplate > 25 && maxTemplate < 35) || (maxTemplate >= 5 && maxTemplate < 10))
                        && precipSum < 0.01 && maxWindPower < 4
        ) {
            constructionIndex[0] = "70";
            constructionIndex[1] = "B";
            constructionIndex[2] = "良";
            constructionIndex[3] = "对建筑施工基本无影响，要控制混凝土凝结速度";
        }
        if (maxTemplate >= 5 && maxTemplate <= 35 && precipSum < 10 && maxWindPower < 4) {
            constructionIndex[0] = "50";
            constructionIndex[1] = "C";
            constructionIndex[2] = "差";
            constructionIndex[3] = "对建筑施工影响不大，但要做好建筑材料的防雨";
        }
        if (maxTemplate >=0 && maxTemplate < 5 && precipSum < 10 && maxWindPower >= 4 && maxWindPower <= 5) {
            constructionIndex[0] = "30";
            constructionIndex[1] = "D";
            constructionIndex[2] = "较差";
            constructionIndex[3] = "要采取相应措施可继续施工";
        }
        if (maxTemplate < 0 || precipSum >= 10 || maxWindPower > 5) {
            constructionIndex[0] = "10";
            constructionIndex[1] = "E";
            constructionIndex[2] = "极差";
            constructionIndex[3] = "应停止施工";
        }
        return constructionIndex;
    }

    public static String getWindRect(float rect) {
        return WeatherUtils.getWindRect((double) rect);
    }

    /**
     * 根据度数，得到风向值 88方位
     *
     * @param rect
     * @return
     */
    public static String getWindRect(double rect) {
        String r ;
        if (rect >= 0.0 && rect <= 22.5) {
            r = "北风";
        } else if (rect > 22.5 && rect <= 67.5) {
            r = "东北风";
        } else if (rect > 67.5 && rect <= 112.5) {
            r = "东风";
        } else if (rect > 112.5 && rect <= 157.5) {
            r = "东南风";
        } else if (rect > 157.5 && rect <= 202.5) {
            r = "南风";
        } else if (rect > 202.5 && rect <= 247.5) {
            r = "西南风";
        } else if (rect > 247.5 && rect <= 292.5) {
            r = "西风";
        } else if (rect > 292.5 && rect <= 337.5) {
            r = "西北风";
        } else if (rect > 337.5 && rect < 360.0) {
            r = "北风";
        } else {
            r = "-";
        }
        return r;
    }

    //A - 云层覆盖
    public static String getCloudValue(String cloud) {

        if (Double.parseDouble(cloud) < 65 ){
            return WeatherByGWCCode.WEATHER_SUNNY_CODE;
        }else if (Double.parseDouble(cloud)>=65 && Double.parseDouble(cloud) < 80){
            return WeatherByGWCCode.WEATHER_CLOUDY_CODE;
        }else {
            return WeatherByGWCCode.WEATHER_OVERCAST;
        }
    }

    /**
     * 根据降水量获得降水类型
     * @param qop1Hour
     * @return
     */
    public static String getPreTypeByQop1Hour(double qop1Hour) {
        String value = "0" ;
        if (qop1Hour >= 1.6&&qop1Hour <3.6) {
            value = WeatherByGWCCode.WEATHER_RAIN_CODE;
        }else if (qop1Hour>=3.6&&qop1Hour <8.0) {
            value = WeatherByGWCCode.WEATHER_HRAIN_CODE;
        }else if (qop1Hour >= 8.0&&qop1Hour <20) {
            value = WeatherByGWCCode.WEATHER_RAINSTORM_CODE;
        }else if(qop1Hour >= 20&&qop1Hour <50.0){
            value = WeatherByGWCCode.WEATHER_BRAINSTORM_CODE;
        }else if(qop1Hour>=50.0 ){
            value = WeatherByGWCCode.WEATHER_HEAVY_RAINFALL_CODE;;
        }else if(qop1Hour>0 && qop1Hour<1.6){
            value = WeatherByGWCCode.WEATHER_LRAIN_CODE;
        }else {
            value = "0";
        }
        return value;
    }

    /**
     * 获取降雨大小
     * @param rain
     * @return
     */
    public static String transformRainText24(Double rain) {
        if (rain >= 0.01 && rain <= 9.9) {
            return WeatherByGWCCode.WEATHER_LRAIN_CODE;
        } else if (rain > 9.9 && rain <= 24.9) {
            return WeatherByGWCCode.WEATHER_RAIN_CODE;
        } else if (rain > 24.9 && rain <= 49.9) {
            return WeatherByGWCCode.WEATHER_HRAIN_CODE;
        } else if (rain > 49.9 && rain <= 99.9) {
            return WeatherByGWCCode.WEATHER_RAINSTORM_CODE;
        } else if (rain > 99.9 && rain <= 249.9 ) {
            return WeatherByGWCCode.WEATHER_BRAINSTORM_CODE;
        } else if (rain > 249.9) {
            return WeatherByGWCCode.WEATHER_HEAVY_RAINFALL_CODE;
        }
        return null;
    }

    /**
     * 雪
     * @param snow
     * @return
     */
    public static String transformSnowText24(Double snow) {
        if (snow >= 0.0 && snow < 2.5) {
            return WeatherByGWCCode.WEATHER_LSNOW_CODE;
        } else if (snow >= 2.5 && snow < 4.9){
            return WeatherByGWCCode.WEATHER_MSNOW_CODE;
        } else if (snow >= 4.9 && snow <= 9.9) {
            return WeatherByGWCCode.WEATHER_HSNOW_CODE;
        } else if (snow > 9.9) {
            return WeatherByGWCCode.WEATHER_SNOWSTORM_CODE;
        }
        return WeatherByGWCCode.WEATHER_LSNOW_CODE;
    }


    public static String[] getGwcWeatherTextByCode(String[] conditions, String cloudcover, String precipitationType, String precipitation ){
        String[] rs = new String[2] ;
        String pretype = Integer.parseInt(conditions[2],2)+""; //获得降水类型
        if(StringUtils.equals("0",pretype)){//无降水
            if(!StringUtils.equals("0.00",precipitation)){//如果降水量不等于0，根据降水类型返回具体天气现象
                if(StringUtils.equals("1",precipitationType)){
                    String code = getPreTypeByQop1Hour(Double.parseDouble(precipitation));
                    rs[0] = code ;
                    rs[1] = WeatherByGWCCode.mapValuePreHour.get(code).toString();
                    return rs;
                }else{
                    rs[0] =  WeatherByGWCCode.mapValuePreHourOther.get(precipitationType).toString();
                    rs[1] = WeatherByGWCCode.mapValuePreHour.get(rs[0]).toString();
//                    rs[0] = transformSnowText24(Double.valueOf(precipitation));
//                    rs[1] = String.valueOf(WeatherByGWCCode.mapValuePreHour.get(transformSnowText24(Double.valueOf(precipitation))));
                    return rs;
                }
            }
            String code = getCloudValue(cloudcover);
            rs[0] = code ;
            rs[1] = WeatherByGWCCode.mapValueCoverage.get(code).toString();
            return rs;
        }else if (StringUtils.equals(WeatherByGWCCode.WEATHER_RAIN_CODE,pretype)||StringUtils.equals(WeatherByGWCCode.WEATHER_HRAIN_CODE,pretype)||StringUtils.equals(WeatherByGWCCode.WEATHER_LRAIN_CODE,pretype)){
            String storms =  Integer.parseInt(conditions[0],2)+""; //获取雷暴类型
            if(StringUtils.equals("1",storms) || StringUtils.equals("2",storms)){
                rs[0] = WeatherByGWCCode.WEATHER_STORM_CODE;
                rs[1] = WeatherByGWCCode.mapValuePreHour.get(WeatherByGWCCode.WEATHER_STORM_CODE).toString();
                return rs;
            }else {
                String code = getPreTypeByQop1Hour(Double.parseDouble(precipitation));
                if(StringUtils.equals(code,"0")){
                    rs[0] = pretype;
                    rs[1] = WeatherByGWCCode.mapValuePreHour.get(pretype).toString();
                    return rs;
                }else {
                    rs[0] = code;
                    rs[1] = WeatherByGWCCode.mapValuePreHour.get(code).toString();
                    return rs;
                }
            }
        }else{
            rs[0] = pretype;
            rs[1] = WeatherByGWCCode.mapValuePreHour.get(pretype).toString();
            return rs;
        }
    }

    /**
     * 获取空气质量状态
     * @param airQuality24
     * @return
     */
    public static String transformAirQuality24(int airQuality24) {
        if(airQuality24>=0 && airQuality24<50){
            return "优";
        }else if(airQuality24>=50 && airQuality24<70){
            return "良";
        }else if(airQuality24>=70){
            return "差";
        }
        return null;
    }

    /**
     * 获取天气状态
     * @param code
     * @return
     */
    public static String getSIGNALTYPENAME(String code){
        HashMap<String, String> map = new HashMap<String, String>() {
            {
                put("11B01","台风");
                put("11B02","龙卷风");
                put("11B03","暴雨");
                put("11B04","暴雪");
                put("11B05","寒潮");
                put("11B06","大风");
                put("11B07","沙尘暴");
                put("11B08","低温冻害");
                put("11B09","巾高温");
                put("11B10","热浪");
                put("11B11","干热风");
                put("11B12","下击暴流");
                put("11B13","雪崩");
                put("11B14","雷电");
                put("11B15","冰雹");
                put("11B16","霜冻");
                put("11B17","大雾");
                put("11B18","低空风切变");
                put("11B19","霾");
                put("11B20","雷雨大风");
                put("11B21","道路结冰");
                put("11B22","干旱");
                put("11B23","海上大风");
                put("11B24","高温中暑");
                put("11B25","森林火险");
                put("11B26","草原火险");
                put("11B27","冰冻");
                put("11B28","空间天气");
                put("11B29","重污染");
                put("11B30","低温雨雪冰冻");
                put("11B31","强对流");
                put("11B32","臭氧");
                put("11B33","大雪");
                put("11B34","寒冷");
                put("11B35","连阴雨");
                put("11B36","渍涝风险");
                put("11B37","地质灾害气象风险");
                put("11B38","强降雨");
                put("11B39","强降温");
                put("11B40","雪灾");
                put("11B41","森林（草原）火险");
                put("11B42","医疗气象");
                put("11B43","雷暴");
                put("11B44","停课信号");
                put("11B45","停工信号");
                put("11B46","海上风险");
                put("11B47","春季沙尘天气趋势预警");
                put("11B48","降温");
            }
        };
        return map.get(code);
    }
    /**
     * @Description: 根据code返回天气现象（天津）
     * @param  @param
     * @author ningyq
     * @date 2020/9/10
     */
    public static String getPhenomenaByCode(String code){

        Map<String, String> phenomenaMap = new HashMap<String, String>() {
            {
                put("00","晴");
                put("01","多云");
                put("02","阴");
                put("03","阵雨");
                put("04","雷阵雨");
                put("05","雷阵雨而伴有冰雹");
                put("06","雨夹雪");
                put("07","小雨");
                put("08","中雨");
                put("09","大雨");
                put("10","暴雨");
                put("11","大暴雨");
                put("12","特大暴雨");
                put("13","阵雪");
                put("14","小雪");
                put("15","中雪");
                put("16","大雪");
                put("17","暴雪");
                put("18","雾");
                put("19","冻雨");
                put("20","沙尘暴");
                put("21","小到中雨");
                put("22","中到大雨");
                put("23","大到暴雨");
                put("24","暴雨到大暴雨");
                put("25","大暴雨到特大暴雨");
                put("26","小到中雪");
                put("27","中到大雪");
                put("28","大到暴雪");
                put("29","浮尘");
                put("30","扬沙");
                put("31","强沙尘暴");
                put("32","浓雾");
                put("33","雪");
                put("49","强浓雾");
                put("53","霾");
                put("54","中度霾");
                put("55","重度霾");
                put("56","严重霾");
                put("57","大雾");
                put("58","特强浓雾");
                put("99","无");
            }
        };
        return phenomenaMap.get(code);
    }

    /**
     * @Description: 根据code返回风向（天津）
     * @param  @param
     * @author ningyq
     * @date 2020/9/10
     */
    public static String getWindDirByCode(String code){

        Map<String, String> windDirMap = new HashMap<String, String>() {
            {
                put("0","无持续风向");
                put("1","东北风");
                put("2","东风");
                put("3","东南风");
                put("4","南风");
                put("5","西南风");
                put("6","西风");
                put("7","西北风");
                put("8","北风");
                put("9","旋转风");
            }
        };
        return windDirMap.get(code);
    }

    /**
     * @Description: 根据code返回风向（天津）
     * @param  @param
     * @author ningyq
     * @date 2020/9/10
     */
    public static String getWindSpeedByCode(String code){

        Map<String, String> windSpeedMap = new HashMap<String, String>() {
            {
                put("0","微风");
                put("1","3-4");
                put("2","4-5");
                put("3","5-6");
                put("4","6-7");
                put("5","7-8");
                put("6","8-9");
                put("7","9-10");
                put("8","10-11");
                put("9","11-12");
            }
        };
        return windSpeedMap.get(code);
    }


    public static void main(String[] args) {
        System.out.println(WeatherUtils.transformRainText24(50d));
    }
}
