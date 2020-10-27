package cc.htdf.msgcloud.datacenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/9/15
 * @Description: TODO
 */
@Data
@TableName("d_5g_air")
public class D5gAirPO {

    private int id;

    private Integer areacode;

    private String  areaname;

    private String pm25;

    private String aqi;

    private String pm10;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;

    private Integer dataSource;
}