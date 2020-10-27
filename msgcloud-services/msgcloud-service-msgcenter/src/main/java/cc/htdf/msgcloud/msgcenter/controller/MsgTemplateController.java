package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgTemplateButtonVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgTemplateVO;
import cc.htdf.msgcloud.msgcenter.service.MsgTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Validated
@RestController
@RequestMapping("/template")
public class MsgTemplateController {

    @Resource
    private MsgTemplateService msgTemplateService;

    /**
     * 5G气象服务云平台获取模板列表
     *
     * @param templateName
     * @param organId
     * @param modularId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getTemplateList")
    public WebResponse getTemplateList(@LoginUser CMsgUserPO user,
                                       @RequestParam(value = "templateName", required = false) String templateName,
                                       @RequestParam(value = "organId", required = false) Integer organId,
                                       @RequestParam(value = "modularId", required = false) Integer modularId,
                                       @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取模板列表！")
                .data(msgTemplateService.getTemplateList(user, templateName, organId, modularId, pageNum, pageSize));
    }

    /**
     * 5G气象服务云平台获取全部模板
     *
     * @param templateType
     * @return
     */
    @GetMapping(value = "/getTemplateLists")
    public WebResponse getTemplateLists(@LoginUser CMsgUserPO user,
                                        @RequestParam(value = "templateType", required = false) String templateType) {
        return WebResponseBuilder.ok().setMsg("成功获取全部模板！")
                .data(msgTemplateService.getTemplateLists(user, templateType));
    }

    /**
     * 5G气象服务云平台根据模板ID获取模板
     *
     * @param templateId
     * @return
     */
    @GetMapping(value = "/getTemplateById")
    public WebResponse getTemplateById(@RequestParam(value = "templateId") String templateId) {
        return WebResponseBuilder.ok().setMsg("成功获取模板！")
                .data(msgTemplateService.getTemplateById(templateId));
    }

    /**
     * 5G气象服务云平台根据模板ID删除模板
     *
     * @param templateId
     * @return
     */
    @GetMapping(value = "/deleteTemplateById")
    public WebResponse deleteTemplateById(@RequestParam(value = "templateId") String templateId) {
        return WebResponseBuilder.ok().setMsg("成功删除模板！")
                .data(msgTemplateService.deleteTemplateById(templateId));
    }

    /**
     * 5G气象服务云平台获取标签列表
     *
     * @return
     */
    @GetMapping(value = "/getTagList")
    public WebResponse getTagList() {
        return WebResponseBuilder.ok().setMsg("成功获取内容列表！")
                .data(msgTemplateService.getTagList());
    }

    /**
     * 5G气象服务云平台获取按钮列表
     *
     * @param buttonType
     * @return
     */
    @GetMapping(value = "/getButtonList")
    public WebResponse getButtonList(@LoginUser CMsgUserPO user,
                                     @RequestParam(value = "buttonType") String buttonType) {
        return WebResponseBuilder.ok().setMsg("成功获取按钮列表！")
                .data(msgTemplateService.getButtonList(user, buttonType));
    }

    /**
     * 5G气象服务云平台新建按钮
     *
     * @param msgTemplateButtonVO
     * @return
     */
    @PostMapping(value = "/createButton")
    public WebResponse createButton(@LoginUser CMsgUserPO user, @RequestBody MsgTemplateButtonVO msgTemplateButtonVO) {
        return WebResponseBuilder.ok().setMsg("成功创建按钮！")
                .data(msgTemplateService.createButton(user, msgTemplateButtonVO));
    }

    /**
     * 5G气象服务云平台创建新模板
     *
     * @param msgTemplateVO
     * @return
     */
    @PostMapping(value = "/createTemplate")
    public WebResponse createTemplate(@LoginUser CMsgUserPO user, @RequestBody MsgTemplateVO msgTemplateVO) {
        return WebResponseBuilder.ok().setMsg("成功创建模板！")
                .data(msgTemplateService.createTemplate(user, msgTemplateVO));
    }

    /**
     * 5G气象服务云平台根据模块ID获取模板
     *
     * @param user
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getTemplateByModuleId")
    @ApiOperation(value = "根据模块ID获取模板", notes = "根据模块ID获取模板")
    public WebResponse getTemplateByModuleId(
            @LoginUser CMsgUserPO user,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "type", required = false) String type) {
        return WebResponseBuilder.ok().setMsg("成功获取模板！")
                .data(msgTemplateService.getTemplateByModuleId(user, currentPage, pageSize, title, type));
    }

    /**
     * 获取链接列表
     *
     * @param moduleId
     * @return
     */
    @GetMapping(value = "/getLinkList")
    @ApiOperation(value = "获取链接列表", notes = "获取链接列表")
    public WebResponse getLinkList(@LoginUser CMsgUserPO user, @RequestParam(value = "moduleId", required = false) Integer moduleId) {
        return WebResponseBuilder.ok().setMsg("成功获取链接列表！")
                .data(msgTemplateService.getLinkList(user, moduleId));
    }

    /**
     * 获取链接列表(自定义卡片)
     *
     * @return
     */
    @GetMapping(value = "/getCustomLinkList")
    @ApiOperation(value = "获取链接列表(自定义卡片)", notes = "获取链接列表(自定义卡片)")
    public WebResponse getCustomLinkList(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取链接列表(自定义卡片)！")
                .data(msgTemplateService.getCustomLinkList(user));
    }
}
