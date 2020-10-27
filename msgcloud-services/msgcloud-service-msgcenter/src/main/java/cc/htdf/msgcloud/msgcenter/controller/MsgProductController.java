package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.common.utils.HtmlToImageUtils;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgProductVO;
import cc.htdf.msgcloud.msgcenter.service.MsgProductService;
import io.minio.errors.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/product")
public class MsgProductController {

    @Resource
    private MsgProductService msgProductService;

    /**
     * 获取产品列表
     *
     * @param productName
     * @param msgStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getProductList")
    @ApiOperation(value = "获取产品列表", notes = "获取产品列表 ")
    public WebResponse getProductList(@LoginUser CMsgUserPO user,
                                      @RequestParam(value = "productName", required = false) String productName,
                                      @RequestParam(value = "msgStatus", required = false) Integer msgStatus,
                                      @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取产品列表！")
                .data(msgProductService.getProductList(user, productName, msgStatus, pageNum, pageSize));
    }

    /**
     * 获取产品所属区域下拉列表
     *
     * @return
     */
    @GetMapping(value = "/getAreaList")
    @ApiOperation(value = "获取产品所属区域下拉列表", notes = "获取产品所属区域下拉列表 ")
    public WebResponse getAreaList(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取产品所属区域下拉列表！")
                .data(msgProductService.getAreaList(user));
    }

    /**
     * 新建产品
     *
     * @param user
     * @param msgProductVO
     * @return
     */
    @PostMapping(value = "/createProduct")
    @ApiOperation(value = "新建产品", notes = "新建产品")
    public WebResponse createProduct(@LoginUser CMsgUserPO user, @RequestBody MsgProductVO msgProductVO) {
        return WebResponseBuilder.ok().setMsg("成功新建产品！")
                .data(msgProductService.createProduct(user, msgProductVO));
    }

    /**
     * 更新产品
     *
     * @param user
     * @param msgProductVO
     * @return
     */
    @PostMapping(value = "/updateProduct")
    @ApiOperation(value = "更新产品", notes = "更新产品")
    public WebResponse updateProduct(@LoginUser CMsgUserPO user, @RequestBody MsgProductVO msgProductVO) {
        return WebResponseBuilder.ok().setMsg("成功更新产品！")
                .data(msgProductService.updateProduct(user, msgProductVO));
    }

    /**
     * 删除产品
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/deleteProductById")
    @ApiOperation(value = "删除产品", notes = "删除产品")
    public WebResponse deleteProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功删除产品！")
                .data(msgProductService.deleteProductById(productId));
    }

    /**
     * 根据ID获取产品对象
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/getProductById")
    @ApiOperation(value = "根据ID获取产品对象", notes = "根据ID获取产品对象")
    public WebResponse getProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功根据ID获取产品对象！")
                .data(msgProductService.getProductById(productId));
    }

    /**
     * 生成产品
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/makeProduct")
    @ApiOperation(value = "生成产品", notes = "生成产品")
    public WebResponse makeProduct(@LoginUser CMsgUserPO user, @RequestParam(value = "productId") String productId) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException, InterruptedException, AWTException {

        return WebResponseBuilder.ok().setMsg("成功生成产品！")
                .data(msgProductService.makeProduct(user, productId));
    }

    @GetMapping(value = "/test/callable")
    @ResponseBody
    public String test () throws AWTException, InterruptedException, IOException {
        String name = Math.random()+"";
        log.info("{}开始",name);
        HtmlToImageUtils.htmlToImageByUrl("http://172.17.247.250/template/template_1/index.html?code=210200&id=28","/usr/bin/chromedriver");
        log.info("{}结束",name);
        return "successful";
    }
}
