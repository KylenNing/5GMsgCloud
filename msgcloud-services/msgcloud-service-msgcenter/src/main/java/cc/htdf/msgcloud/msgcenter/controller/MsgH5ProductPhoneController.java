package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.service.MsgH5ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: guozx
 * @Date: 2020/9/10
 * @Description:
 */
@Validated
@RestController
@RequestMapping("/h5productPhone")
@Api("H5手机端接口")
public class MsgH5ProductPhoneController {

    @Resource
    private MsgH5ProductService msgH5ProductService;

    /**
     * 获取H5产品列表
     *
     * @param organId
     * @param title
     * @param isMain
     * @param modular
     * @param tag
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getH5ProductList")
    @ApiOperation(value = "获取H5产品列表", notes = "获取H5产品列表")
    public WebResponse getH5ProductList(@RequestParam(value = "organId", required = false) Integer organId,
                                        @RequestParam(value = "title", required = false) String title,
                                        @RequestParam(value = "isMain", required = false) boolean isMain,
                                        @RequestParam(value = "modular", required = false) Integer[] modular,
                                        @RequestParam(value = "tag", required = false) String tag,
                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取H5产品列表！")
                .data(msgH5ProductService.getH5ProductList(organId, title, isMain, modular, tag, pageNum, pageSize));
    }

    /**
     * 根据ID获取H5产品对象
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/getH5PhoneProductById")
    @ApiOperation(value = "根据ID获取H5产品对象", notes = "根据ID获取H5产品对象")
    public WebResponse getH5PhoneProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功根据ID获取H5产品对象！")
                .data(msgH5ProductService.getH5PhoneProductById(productId));
    }

    /**
     * 获取H5查天气信息(手机端)
     *
     * @param organId
     * @param modular
     * @return
     */
    @GetMapping(value = "/getH5WeatherDescribe")
    @ApiOperation(value = "获取H5查天气信息", notes = "获取H5查天气信息")
    public WebResponse getH5WeatherDescribe(@RequestParam(value = "organId", required = false) Integer organId,
                                            @RequestParam(value = "modular", required = false) Integer modular) {
        return WebResponseBuilder.ok().setMsg("成功获取H5查天气描述！")
                .data(msgH5ProductService.getH5WeatherDescribe(organId, modular));
    }

    /**
     * 有效浏览数加一
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/addBrowseCountProductById")
    @ApiOperation(value = "有效浏览数加一", notes = "有效浏览数加一")
    public WebResponse addBrowseCountProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功有效浏览数加一！")
                .data(msgH5ProductService.addBrowseCountProductById(productId));
    }

    /**
     * 获取手机端展示的标签
     *
     * @param modular
     * @return
     */
    @GetMapping(value = "/getTagByModular")
    @ApiOperation(value = "获取手机端展示的标签", notes = "获取手机端展示的标签")
    public WebResponse getTagByModular(@RequestParam(value = "modular", required = false) Integer modular) {
        return WebResponseBuilder.ok().setMsg("获取手机端展示的标签！")
                .data(msgH5ProductService.getTagByModular(modular));
    }

    /**
     * 获取养生科普主页面推荐,精选数据列表
     *
     * @param organId
     * @return
     */
    @GetMapping(value = "/getH5MainList")
    @ApiOperation(value = "获取养生科普主页面推荐,精选数据列表", notes = "获取养生科普主页面推荐,精选数据列表")
    public WebResponse getH5MainList(@RequestParam(value = "organId", required = false) Integer organId) {
        return WebResponseBuilder.ok().setMsg("成功获取养生科普主页面推荐,精选数据列表！")
                .data(msgH5ProductService.getH5MainList(organId));
    }
    
    /**
     * 获取养生科普，气象科普主页面推荐,精选数据列表
     *
     * @param organId
     * @return
     */
    @GetMapping(value = "/getH5ScienceMainList")
    @ApiOperation(value = "获取养生科普，气象科普主页面推荐,精选数据列表", notes = "获取养生科普，气象科普主页面推荐,精选数据列表")
    public WebResponse getH5ScienceMainList(@RequestParam(value = "modularId", required = false) String modularId,
    		@RequestParam(value = "type", required = false) String type,
    		@RequestParam(value = "pageNum", required = false) Integer pageNum,
    		@RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@RequestParam(value = "title", required = false) String title) {
        return WebResponseBuilder.ok().setMsg("获取养生科普，气象科普主页面推荐,精选数据列表！")
                .data(msgH5ProductService.getH5ScienceMainList(modularId,type,pageNum,pageSize,title));
    }
    
    
}
