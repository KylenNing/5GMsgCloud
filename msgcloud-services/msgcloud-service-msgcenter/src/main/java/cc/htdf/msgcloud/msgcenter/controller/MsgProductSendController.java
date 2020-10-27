package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.service.MsgProductSendService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Validated
@RestController
@RequestMapping("/productSend")
public class MsgProductSendController {

    @Resource
    private MsgProductSendService msgProductSendService;

    /**
     * 获取产品发送列表
     *
     * @param productName
     * @param serviceId
     * @param msgStatus
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getProductSendList")
    @ApiOperation(value = "获取产品发送列表", notes = "获取产品发送列表 ")
    public WebResponse getProductSendList(@LoginUser CMsgUserPO user,
                                          @RequestParam(value = "productName", required = false) String productName,
                                          @RequestParam(value = "serviceId", required = false) String serviceId,
                                          @RequestParam(value = "msgStatus", required = false) Integer msgStatus,
                                          @RequestParam(value = "startTime", required = false) String startTime,
                                          @RequestParam(value = "endTime", required = false) String endTime,
                                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取产品发送列表！")
                .data(msgProductSendService.getProductSendList(user, productName, serviceId, msgStatus, startTime, endTime, pageNum, pageSize));
    }

    /**
     * 根据ID获取产品发送对象
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/getProductSendById")
    @ApiOperation(value = "根据ID获取产品发送对象", notes = "根据ID获取产品发送对象")
    public WebResponse getProductSendById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功根据ID获取产品发送对象！")
                .data(msgProductSendService.getProductSendById(productId));
    }

    /**
     * 发送产品
     *
     * @param productId
     * @param serviceId
     * @param userLabelId
     * @return
     */
    @GetMapping(value = "/sendProduct")
    @ApiOperation(value = "发送产品", notes = "发送产品")
    public WebResponse sendProduct(@LoginUser CMsgUserPO user,
                                   @RequestParam(value = "productId") String productId,
                                   @RequestParam(value = "serviceId") String serviceId,
                                   @RequestParam(value = "isAll") boolean isAll,
                                   @RequestParam(value = "userLabelId") String userLabelId) {
        return WebResponseBuilder.ok().setMsg("成功发送产品！")
                .data(msgProductSendService.sendProduct(user, productId, serviceId, isAll, userLabelId));
    }

    /**
     * 审核
     *
     * @param productId
     * @param reason
     * @return
     */
    @GetMapping(value = "/examine")
    @ApiOperation(value = "审核", notes = "审核")
    public WebResponse examine(@RequestParam(value = "productId") String productId,
                               @RequestParam(value = "reason", required = false) String reason) {
        return WebResponseBuilder.ok().setMsg("成功审核！")
                .data(msgProductSendService.examine(productId, reason));
    }
}
