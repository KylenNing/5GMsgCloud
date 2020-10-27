package cc.htdf.msgcloud.executor.base.domain.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
@Data
@Document(collection = "b_area_station_forecast")
public class BAreaStationForecastPO {
    private String _id;
    private String areaname;
    private String areacode;
    private String stationcode;
    private String forecastrank;

}