package cc.htdf.msgcloud.executor.base.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Data
@TableName("d_5g_day_weather")
public class D5gDayWeatherPO {

    private int id;

    private Integer areacode;

    private String areaname;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;

    private String sunRise;

    private String sunSet;

    private String dayPhenomena;

    private String nightPhenomena;

    private Double dayTemp;

    private Double nightTemp;

    private String dayWindDir;

    private String nightWindDir;

    private String dayWindSpeed;

    private String nightWindSpeed;

    private Double airQuality;

    private Integer dataSource;

}