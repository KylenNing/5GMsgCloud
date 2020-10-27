package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordDictPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.service.KeywordDictService;
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
@RequestMapping("/keywordDict")
@Api("关键字码表相关api")
public class KeywordDictController {

    @Resource
    private KeywordDictService keywordDictService;


    /**
     * @Description: 添加关键字码表
     * @param  po
     * @author ningyq
     * @date 2020/8/11
     */
    @PostMapping(value = "/addKeyWordDict")
    @ApiOperation(value = "添加关键字码表", notes = "添加关键字码表")
    @ResponseBody
    public WebResponse addKeyWordDict(@LoginUser CMsgUserPO user,@RequestBody BKeywordDictPO po) {

       return WebResponseBuilder.ok().setMsg("成功返回！")
               .data(keywordDictService.createKeywordDict(user,po));

    }

    /**
     * @Description: 删除关键字码表
     * @param  dictId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "删除关键字码表", notes = "删除关键字码表")
    @PostMapping(value = "/deleteKeyWordDict")
    public WebResponse deleteKeyWordDict(@RequestParam("dictId") Integer dictId) {
        keywordDictService.deleteKeywordDict(dictId);
        return WebResponseBuilder.ok().setMsg("成功删除关键字！");

    }

    /**
     * @Description: 更新关键字码表
     * @param  po
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "更新关键字码表", notes = "更新关键字码表")
    @PostMapping(value = "/updateKeyWordDict")
    public WebResponse updateKeyWordDict(@LoginUser CMsgUserPO user,@RequestBody BKeywordDictPO po) {
        return WebResponseBuilder.ok().setMsg("成功返回!")
                .data(keywordDictService.updateKeywordDict(user,po));

    }

    /**
     * @Description: 获取所有关键字码表
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "获取所有关键字码表", notes = "获取所有关键字码表")
    @GetMapping(value = "/getAllKeyWordDict")
    public WebResponse getAllKeyWordDict(@LoginUser CMsgUserPO user,
                                         @RequestParam(value = "currentPage",required = false) Integer currentPage,
                                         @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                         @RequestParam(value = "dictName", required = false) String dictName) {
        return WebResponseBuilder.ok().setMsg("成功获取关键字码表！").data(
                keywordDictService.getAllKeyWordDict(user,currentPage,pageSize,dictName));

    }

}