package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgH5ProductVO;
import cc.htdf.msgcloud.msgcenter.service.MsgH5ProductService;
import io.minio.errors.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: guozx
 * @Date: 2020/9/10
 * @Description:
 */
@Validated
@RestController
@RequestMapping("/h5product")
public class MsgH5ProductController {

    @Resource
    private MsgH5ProductService msgH5ProductService;

    /**
     * 获取发布单位下拉
     *
     * @return
     */
    @GetMapping(value = "/getOrganList")
    @ApiOperation(value = "获取发布单位下拉", notes = "获取发布单位下拉")
    public WebResponse getOrganList(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取发布单位下拉！")
                .data(msgH5ProductService.getOrganList(user));
    }

    /**
     * 获取H5产品列表(配置列表)
     *
     * @param title
     * @param organId
     * @param modular
     * @param type
     * @param status
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getH5ProductListByPage")
    @ApiOperation(value = "获取H5产品列表(配置列表)", notes = "获取H5产品列表(配置列表)")
    public WebResponse getH5ProductListByPage(@LoginUser CMsgUserPO user,
                                              @RequestParam(value = "title", required = false) String title,
                                              @RequestParam(value = "organId", required = false) Integer organId,
                                              @RequestParam(value = "modular", required = false) Integer modular,
                                              @RequestParam(value = "type", required = false) Integer type,
                                              @RequestParam(value = "status", required = false) Integer status,
                                              @RequestParam(value = "startTime", required = false) String startTime,
                                              @RequestParam(value = "endTime", required = false) String endTime,
                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取H5产品列表(配置列表)！")
                .data(msgH5ProductService.getH5ProductListByPage(user, title, organId, modular, type, status, startTime, endTime, pageNum, pageSize));
    }

    /**
     * 新建H5产品
     *
     * @param user
     * @param msgH5ProductVO
     * @return
     */
    @PostMapping(value = "/createH5Product")
    @ApiOperation(value = "新建H5产品", notes = "新建H5产品")
    public WebResponse createH5Product(@LoginUser CMsgUserPO user, @RequestBody MsgH5ProductVO msgH5ProductVO) {
        return WebResponseBuilder.ok().setMsg("成功新建H5产品！")
                .data(msgH5ProductService.createH5Product(user, msgH5ProductVO));
    }

    /**
     * 更新H5产品
     *
     * @param user
     * @param msgH5ProductVO
     * @return
     */
    @PostMapping(value = "/updateH5Product")
    @ApiOperation(value = "更新H5产品", notes = "更新H5产品")
    public WebResponse updateH5Product(@LoginUser CMsgUserPO user, @RequestBody MsgH5ProductVO msgH5ProductVO) {
        return WebResponseBuilder.ok().setMsg("成功更新H5产品！")
                .data(msgH5ProductService.updateH5Product(user, msgH5ProductVO));
    }

    /**
     * 删除H5产品
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/deleteH5ProductById")
    @ApiOperation(value = "删除H5产品", notes = "删除H5产品")
    public WebResponse deleteH5ProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功删除H5产品！")
                .data(msgH5ProductService.deleteH5ProductById(productId));
    }

    /**
     * 下架H5产品
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/offH5ProductById")
    @ApiOperation(value = "下架H5产品", notes = "下架H5产品")
    public WebResponse offH5ProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功下架H5产品！")
                .data(msgH5ProductService.offH5ProductById(productId));
    }

    /**
     * 上传H5素材
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadH5Material", method = RequestMethod.POST)
    public WebResponse uploadMaterial(@RequestParam("file") MultipartFile file) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        return WebResponseBuilder.ok().setMsg("成功上传H5素材！")
                .data(msgH5ProductService.uploadH5Material(file));
    }

    /**
     * 上传H5素材(推荐图片)
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadH5MaterialRe", method = RequestMethod.POST)
    public WebResponse uploadH5MaterialRe(@RequestParam("file") MultipartFile file) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        return WebResponseBuilder.ok().setMsg("成功上传H5素材(推荐图片)！")
                .data(msgH5ProductService.uploadH5MaterialRe(file));
    }

    /**
     * 根据ID获取H5产品对象
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/getH5ProductById")
    @ApiOperation(value = "根据ID获取H5产品对象", notes = "根据ID获取H5产品对象")
    public WebResponse getH5ProductById(@RequestParam(value = "productId") String productId) {
        return WebResponseBuilder.ok().setMsg("成功根据ID获取H5产品对象！")
                .data(msgH5ProductService.getH5ProductById(productId));
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
                .data(msgH5ProductService.examine(productId, reason));
    }

    /**
     * 获取H5产品列表(审核列表)
     *
     * @param title
     * @param modular
     * @param type
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getH5Product")
    @ApiOperation(value = "获取H5产品列表(审核列表)", notes = "获取H5产品列表(审核列表)")
    public WebResponse getH5Product(@LoginUser CMsgUserPO user,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "organId", required = false) Integer organId,
                                    @RequestParam(value = "modular", required = false) Integer modular,
                                    @RequestParam(value = "type", required = false) Integer type,
                                    @RequestParam(value = "startTime", required = false) String startTime,
                                    @RequestParam(value = "endTime", required = false) String endTime,
                                    @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取H5产品列表(审核列表)！")
                .data(msgH5ProductService.getH5Product(user, title, organId, modular, type, startTime, endTime, pageNum, pageSize));
    }

    /**
     * 获取温馨提示选择
     *
     * @return
     */
    @GetMapping(value = "/getChoiceList")
    @ApiOperation(value = "获取温馨提示选择", notes = "获取温馨提示选择")
    public WebResponse getChoiceList() {
        return WebResponseBuilder.ok().setMsg("成功获取温馨提示选择！")
                .data(msgH5ProductService.getChoiceList());
    }

    /**
     * 获取标签树
     *
     * @param moduleId
     * @return
     */
    @GetMapping(value = "/getTagTree")
    @ApiOperation(value = "获取标签树", notes = "获取标签树")
    public WebResponse getTagTree(@RequestParam(value = "moduleId", required = false) Integer moduleId) {
        return WebResponseBuilder.ok().setMsg("成功获取标签树！")
                .data(msgH5ProductService.getTagTree(moduleId));
    }

    /**
     * 获取已发布的旅游文章
     *
     * @return
     */
    @GetMapping(value = "/getTourProduct")
    @ApiOperation(value = "获取已发布的旅游文章", notes = "获取已发布的旅游文章")
    public WebResponse getTourProduct(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取旅游文章！")
                .data(msgH5ProductService.getTourProduct(user));
    }

}
