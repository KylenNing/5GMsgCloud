package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.BH5ProductTourPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.service.H5ProductTourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: ningyq
 * @Date: 2020/8/11
 * @Description: TODO
 */
@Validated
@RestController
@RequestMapping("/productTour")
@Api("旅游H5产品对应API")
public class H5ProductTourController {

    @Resource
    private H5ProductTourService h5ProductTourService;


    /**
     * @Description: 添加旅游H5产品
     * @param  po
     * @author ningyq
     * @date 2020/8/11
     */
    @PostMapping(value = "/addProductTour")
    @ApiOperation(value = "添加旅游H5产品表", notes = "添加旅游H5产品")
    @ResponseBody
    public WebResponse addProductTour(@LoginUser CMsgUserPO user,@RequestBody BH5ProductTourPO po) {

       return WebResponseBuilder.ok().setMsg("成功返回！")
               .data(h5ProductTourService.createTourProduct(user,po));

    }

    /**
     * @Description: 删除旅游H5产品
     * @param  tourId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "删除旅游H5产品", notes = "删除旅游H5产品")
    @PostMapping(value = "/deleteProductTour")
    public WebResponse deleteProductTour(@RequestParam("tourId") Integer tourId) {
        h5ProductTourService.deleteTourProduct(tourId);
        return WebResponseBuilder.ok().setMsg("成功删除旅游H5产品！");

    }

    /**
     * @Description: 更新旅游H5产品
     * @param  po
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "更新旅游H5产品", notes = "更新旅游H5产品")
    @PostMapping(value = "/updateProductTour")
    public WebResponse updateProductTour(@LoginUser CMsgUserPO user,@RequestBody BH5ProductTourPO po) {
        return WebResponseBuilder.ok().setMsg("成功返回!")
                .data(h5ProductTourService.updateTourProduct(user,po));

    }

    /**
     * @Description: 获取所有旅游H5产品
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "获取所有旅游H5产品", notes = "获取所有旅游H5产品")
    @GetMapping(value = "/getAllProductTour")
    public WebResponse getAllProductTour(@LoginUser CMsgUserPO user,
                                         @RequestParam(value = "currentPage",required = false) Integer currentPage,
                                         @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                         @RequestParam(value = "tourName", required = false) String tourName,
                                         @RequestParam(value = "isRecommend",required = false) Integer isRecommend) {
        return WebResponseBuilder.ok().setMsg("成功获取旅游H5产品！").data(
                h5ProductTourService.getAllTourProduct(user,currentPage,pageSize,tourName,isRecommend));

    }

    /**
     * @Description: 获取所有旅游H5产品（移动端）
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "获取所有旅游H5产品（移动端）", notes = "获取所有旅游H5产品（移动端）")
    @GetMapping(value = "/mobile/getAllProductTour")
    public WebResponse getAllProductTourMobile(
                                         @RequestParam(value = "organId", required = false) Integer organId,
                                         @RequestParam(value = "currentPage",required = false) Integer currentPage,
                                         @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                         @RequestParam(value = "tourName", required = false) String tourName,
                                               @RequestParam(value = "isRecommend",required = false) Integer isRecommend,
                                         @RequestParam(value = "tagId", required = false) Integer tagId) {
        return WebResponseBuilder.ok().setMsg("成功获取旅游H5产品！").data(
                h5ProductTourService.getAllTourProductMobile(organId,currentPage,pageSize,tourName,isRecommend,tagId));

    }

    /**
     * @Description: 根据ID旅游H5产品(移动端)
     * @param  tourId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "根据ID旅游H5产品(移动端)", notes = "根据ID旅游H5产品(移动端)")
    @GetMapping(value = "/mobile/getProductTourById")
    public WebResponse getProductTourByIdMobile(@RequestParam("tourId") Integer tourId) {
        return WebResponseBuilder.ok().setMsg("成功获取旅游H5产品！")
                .data(h5ProductTourService.getTourByIdMobile(tourId));

    }

    /**
     * @Description: 根据ID旅游H5产品
     * @param  tourId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "根据ID旅游H5产品", notes = "根据ID旅游H5产品")
    @GetMapping(value = "/getProductTourById")
    public WebResponse getProductTourById(@RequestParam("tourId") Integer tourId) {
        return WebResponseBuilder.ok().setMsg("成功获取旅游H5产品！")
                .data(h5ProductTourService.getTourById(tourId));

    }

    /**
     * @Description: 根据ID更新推荐景点
     * @param  tourId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "根据ID更新推荐景点", notes = "根据ID更新推荐景点")
    @PostMapping(value = "/setRecommandProduct")
    public WebResponse setRecommandProduct(@RequestParam("tourId") Integer tourId, @RequestParam("isRecommend") Integer isRecommend) {
        return WebResponseBuilder.ok().setMsg("成功更新推荐景点！")
                .data(h5ProductTourService.setRecommandProduct(tourId,isRecommend));

    }

    /**
     * @Description: 根据ID下架景点
     * @param  tourId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "根据ID下架景点", notes = "根据ID下架景点")
    @PostMapping(value = "/downProduct")
    public WebResponse downProduct(@RequestParam("tourId") Integer tourId) {
        return WebResponseBuilder.ok().setMsg("成功返回！")
                .data(h5ProductTourService.downProduct(tourId));

    }
}