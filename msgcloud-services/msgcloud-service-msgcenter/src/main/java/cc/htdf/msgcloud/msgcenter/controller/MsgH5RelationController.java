package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.msgcenter.service.MsgH5RelationService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: guozx
 * @Date: 2020/10/21
 * @Description: TODO
 */
@Validated
@RestController
@RequestMapping("/productTour")
@Api("旅游H5文章类型关系对应API")
public class MsgH5RelationController {

    @Resource
    private MsgH5RelationService msgH5RelationService;
}
