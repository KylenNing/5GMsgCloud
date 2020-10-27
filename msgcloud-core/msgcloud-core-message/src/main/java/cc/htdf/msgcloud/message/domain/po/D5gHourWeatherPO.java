package cc.htdf.msgcloud.message.domain.po;

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
@TableName("d_5g_hour_weather")
public class D5gHourWeatherPO {

    private int id;

    private Integer areacode;

    private String areaname;

    private String phenomena;

    private Double temp;

    private String windDir;

    private String windSpeed;

    private Double airQuality;

    private Double humidity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;

    private Integer dataSource;

}