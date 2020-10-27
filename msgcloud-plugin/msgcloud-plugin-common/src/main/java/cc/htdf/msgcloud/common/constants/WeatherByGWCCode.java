package cc.htdf.msgcloud.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengxn on 2020/2/3
 **/
public class WeatherByGWCCode {

    public static final String WEATHER_SNOW_CODE = "1";//雪


    public static final String WEATHER_SNOW_OTHER_CODE = "2";

    public static final String WEATHER_RAIN_CODE = "2";//雨

    public static final String WEATHER_LRAIN_CODE = "3";//小雨

    public static final String WEATHER_HRAIN_CODE = "4";//大雨

    public static final String WEATHER_MIX_CODE = "5";//雨夹雪

    public static final String WEATHER_MIX_OTHER_CODE = "3";


    public static final String WEATHER_SLEET_CODE = "6";//雨加雪

    public static final String WEATHER_SLEET_OTHER_CODE = "4";

    public static final String WEATHER_RAINSTORM_CODE = "8";//暴雨

    public static final String WEATHER_BRAINSTORM_CODE = "9";//大暴雨

    public static final String WEATHER_HEAVY_RAINFALL_CODE = "10";//特大暴雨

    public static final String WEATHER_FRAIN_CODE = "7";//冻雨

    public static final String WEATHER_FRAIN_OTHER_CODE = "5";


    public static final String WEATHER_STORM_CODE = "11";//雷阵雨

    public static final String WEATHER_SUNNY_CODE = "21";//晴

    public static final String WEATHER_CLOUDY_CODE = "22";//多云

    public static final String WEATHER_OVERCAST = "23";

    public static final String WEATHER_LSNOW_CODE = "12";//小雪
    public static final String WEATHER_MSNOW_CODE = "13";//中雪
    public static final String WEATHER_HSNOW_CODE = "14";//大雪
    public static final String WEATHER_SNOWSTORM_CODE = "15";//暴雪




    public static final Map mapValuePreHour = new HashMap() {{
        put(WEATHER_SNOW_CODE, "雪");
        put(WEATHER_RAIN_CODE, "中雨");
        put(WEATHER_LRAIN_CODE, "小雨");
        put(WEATHER_HRAIN_CODE, "大雨");
        put(WEATHER_RAINSTORM_CODE, "暴雨");
        put(WEATHER_BRAINSTORM_CODE, "大暴雨");
        put(WEATHER_HEAVY_RAINFALL_CODE, "特大暴雨");
        put(WEATHER_MIX_CODE, "雨夹雪");
        put(WEATHER_SLEET_CODE, "雨加雪");
        put(WEATHER_FRAIN_CODE, "冻雨");
        put(WEATHER_STORM_CODE, "雷阵雨");

        put(WEATHER_LSNOW_CODE,"小雪");
        put(WEATHER_MSNOW_CODE,"中雪");
        put(WEATHER_HSNOW_CODE,"大雪");
        put(WEATHER_SNOWSTORM_CODE,"暴雪");
    }};



    public static final Map mapValuePreHourOther = new HashMap() {{
        put(WEATHER_SNOW_OTHER_CODE, WEATHER_SNOW_CODE);
        put(WEATHER_MIX_OTHER_CODE, WEATHER_MIX_CODE);
        put(WEATHER_SLEET_OTHER_CODE,WEATHER_SLEET_CODE);
        put(WEATHER_FRAIN_OTHER_CODE, WEATHER_FRAIN_CODE);
    }};



    public static final Map mapValueCoverage = new HashMap() {{

        put(WEATHER_SUNNY_CODE, "晴");
        put(WEATHER_CLOUDY_CODE, "多云");
        put(WEATHER_OVERCAST,"阴");

    }};


}
