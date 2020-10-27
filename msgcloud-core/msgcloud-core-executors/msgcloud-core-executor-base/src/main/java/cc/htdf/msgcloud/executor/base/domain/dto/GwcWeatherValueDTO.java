package cc.htdf.msgcloud.executor.base.domain.dto;

import lombok.Data;

/**
 * author: Ningy
 * date: 2019/12/10
 * discription:
 */
@Data
public class GwcWeatherValueDTO {

    private String CONDITIONSCODE;
    private String CONDITIONSTEXT;
    private String DATETIME;
    private String PROBPRECIP;
    private String RAIN;
    private String RH;
    private String TEMP;
    private String WIND;
    private String WINS;
    private String WINDP;
    private String SUNRISE;
    private String SUNSET;
    private String CLOUDCOVER;
    private String VISIBILITY;
    private String SURFACEPRESSURE;
    private String KUCHERASNOWRATE;
    private String MAXTEMP;
    private String MINTEMP;
    private String RAIN24;
    private String SNOW24;
    private String SDATETIME;
    private String EDATETIME;

}
