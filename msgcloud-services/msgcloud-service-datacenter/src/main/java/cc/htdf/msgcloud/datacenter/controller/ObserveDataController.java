package cc.htdf.msgcloud.datacenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.datacenter.service.ObserveDataService;
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
@RequestMapping("/observe")
public class ObserveDataController {

    @Resource
    private ObserveDataService observeDataService;

    /**
     * @Description: 获取最新天气观测数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/9
     */
    @GetMapping(value = "/getLastObserveData")
    //@ApiOperation(value = "获取最新天气观测数据", notes = "获取最新天气观测数据")
    public WebResponse getLastObserveData(@RequestParam("dataSourceArea") Integer dataSourceArea,
                                        @RequestParam("areacode") Integer areacode) {
        return WebResponseBuilder.ok().setMsg("成功获取最新小时预报！")
                .data(observeDataService.getObserveDataByAreacode(dataSourceArea,areacode));
    }

    /**
     * @Description: 获取最新空气质量数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/9
     */
    @GetMapping(value = "/getLastAirData")
    //@ApiOperation(value = "获取最新空气质量数据", notes = "获取最新空气质量数据")
    public WebResponse getLastAirData(@RequestParam("dataSourceArea") Integer dataSourceArea,
                                          @RequestParam("areacode") Integer areacode) {
        return WebResponseBuilder.ok().setMsg("成功获取最新小时预报！")
                .data(observeDataService.getAirDataByAreacode(dataSourceArea,areacode));
    }

    /**
     * @Description: 获取最新生活指数数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/9
     */
    @GetMapping(value = "/getLastIndexData")
    //@ApiOperation(value = "获取最新生活指数数据", notes = "获取最新生活指数数据")
    public WebResponse getLastIndexData(@RequestParam("dataSourceArea") Integer dataSourceArea,
                                      @RequestParam("areacode") Integer areacode) {
        return WebResponseBuilder.ok().setMsg("成功获取最新小时预报！")
                .data(observeDataService.getIndexDataByAreacode(dataSourceArea,areacode));
    }

}