package cc.htdf.msgcloud.datacenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.datacenter.service.ForecastDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Validated
@RestController
@RequestMapping("/forecast")
public class ForecastDataController {

    @Resource
    private ForecastDataService forecastDataService;

    /**
     * @Description: 获取最新小时预报数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/9
     */
    @GetMapping(value = "/getLastHoursData")
    //@ApiOperation(value = "获取最新小时预报数据", notes = "获取最新小时预报数据")
    public WebResponse getLastHoursData(@RequestParam("dataSourceArea") Integer dataSourceArea,
                                        @RequestParam("areacode") Integer areacode) {
        return WebResponseBuilder.ok().setMsg("成功获取最新小时预报！")
                .data(forecastDataService.getHoursForecast(dataSourceArea,areacode));
    }

    /**
     * @Description: 获取最新天次预报数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/10
     */
    @GetMapping(value = "/getLastDaysData")
    //@ApiOperation(value = "获取最新天次预报数据", notes = "获取最新天次预报数据")
    public WebResponse getLastDaysData(@RequestParam("dataSourceArea") Integer dataSourceArea,
                                        @RequestParam("areacode") Integer areacode) {
        return WebResponseBuilder.ok().setMsg("成功获取最新天次预报！")
                .data(forecastDataService.getDaysForecast(dataSourceArea,areacode));
    }

    /**
     * @Description: 根据地区获取未来15天内任意天预报
     * @param  @param
     * @author ningyq
     * @date 2020/9/10
     */
    @GetMapping(value = "/getDaysForecastByAreacodeAndCount")
    //@ApiOperation(value = "根据地区获取未来15天内任意天预报", notes = "根据地区获取未来15天内任意天预报")
    public WebResponse getDaysForecastByAreacodeAndCount(@RequestParam("dataSourceArea") Integer dataSourceArea,
                                       @RequestParam("areacode") Integer areacode,
                                                         @RequestParam("count") Integer count) {
        return WebResponseBuilder.ok().setMsg("根据地区获取未来15天内任意天预报！")
                .data(forecastDataService.getDaysForecastByAreacodeAndCount(dataSourceArea,areacode,count));
    }
}