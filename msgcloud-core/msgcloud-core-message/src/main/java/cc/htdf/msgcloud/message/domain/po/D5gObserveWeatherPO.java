package cc.htdf.msgcloud.message.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/9/11
 * @Description: TODO
 */
@Data
@TableName("d_5g_observe_weather")
public class D5gObserveWeatherPO {

    private int id;

    private Integer areacode;

    private String area;

    private Double lon;

    private Double lat;

    private String windDirExMax;

    private String windGrade;

    private String windGradeExMax;

    private String stationcode;

    private String visibility;

    private Double rainfall;

    private Double temp;

    private Double maxTemp;

    private Double minTemp;

    private String maxTempTime;

    private String minTempTime;

    private String windDir;

    private String windSpeed;

    private String windExMaxTime;

    private Double humdity;

    private String stationPress;

    private String windSpeedExMax;

    private String stationName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;

    private Integer dataSource;

}