package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.dto.KeyWordPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.service.MsgKeywordService;
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
@RequestMapping("/keyword")
@Api("关键字相关api")
public class MsgKeywordController {

    @Resource
    private MsgKeywordService msgKeywordService;

   /**
    * @Description: 获取所有关键字
    * @param  @param
    * @author ningyq
    * @date 2020/8/11
    */
    @GetMapping(value = "/getAllKeyword")
    @ApiOperation(value = "获取所有关键字", notes = "获取所有关键字")
    public WebResponse getAllKeyWord(@LoginUser CMsgUserPO user,
            @RequestParam("keyWord") String keyWord,@RequestParam("currentPage") Integer currentPage,
                                     @RequestParam("pageSize") Integer pageSize, @RequestParam("serviceId") String serviceId                    ) {
        return WebResponseBuilder.ok().setMsg("成功获取关键字列表！")
                .data(msgKeywordService.getAllKeyWord(user,keyWord,currentPage,pageSize,serviceId));
    }
    
  

    /**
     * @Description: 添加关键字
     * @param  dto
     * @author ningyq
     * @date 2020/8/11
     */
    @PostMapping(value = "/addKeyWord")
    @ApiOperation(value = "添加关键字", notes = "添加关键字")
    @ResponseBody
    public WebResponse addKeyWord(@LoginUser CMsgUserPO user,@RequestBody KeyWordPageDTO dto) {

       return WebResponseBuilder.ok().setMsg("成功返回！")
               .data(msgKeywordService.addKeyWord(user,dto));

    }

    /**
     * @Description: 删除关键字
     * @param  keyWordId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "删除关键字", notes = "删除关键字")
    @PostMapping(value = "/deleteKeyWord")
    public WebResponse deleteKeyWord(@RequestParam("keyWordId") Integer keyWordId) {
        msgKeywordService.deleteKeyWordById(keyWordId);
        return WebResponseBuilder.ok().setMsg("成功删除关键字！");

    }

    /**
     * @Description: 获取该关键字关联信息！
     * @param  keyWordId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "获取该关键字关联信息", notes = "获取该关键字关联信息")
    @GetMapping(value = "/getKeyWordRelatedInfo")
    public WebResponse getKeyWordRelatedInfo(@RequestParam("keyWordId") Integer keyWordId) {
        return WebResponseBuilder.ok().setMsg("成功查询到该关键字关联信息！").
                data(msgKeywordService.getKeyWordInfoById(keyWordId));

    }
    
    /**
     * 根据服务号过滤查询所有关键字
     * @param user
     * @param keyWord
     * @param currentPage
     * @param pageSize
     * @param serviceId
     * @return
     */
     @GetMapping(value = "/getAllKeyWordByServiceId")
     @ApiOperation(value = "获取所有关键字", notes = "根据服务号过滤查询所有关键字")
     public WebResponse getAllKeyWordByServiceId(@RequestParam("serviceId") String serviceId                    ) {
         return WebResponseBuilder.ok().setMsg("成功获取关键字列表！")
                 .data(msgKeywordService.getAllKeyWordByServiceId(serviceId));
     }
     
     
}