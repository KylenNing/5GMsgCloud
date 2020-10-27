package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgOrganVO;
import cc.htdf.msgcloud.msgcenter.service.CMsgOrganService;
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
@RequestMapping("/sys/organ")
public class CMsgOrganController {

    @Resource
    private CMsgOrganService cMsgOrganService;

    /**
     * 新建组织结构
     *
     * @param user
     * @param cMsgOrganVO
     * @return
     */
    @PostMapping(value = "/createOrgan")
    @ApiOperation(value = "新建组织结构", notes = "新建组织结构")
    public WebResponse createOrgan(@LoginUser CMsgUserPO user, @RequestBody CMsgOrganVO cMsgOrganVO) {
        return WebResponseBuilder.ok().setMsg("成功新建组织结构！")
                .data(cMsgOrganService.createOrgan(user, cMsgOrganVO));
    }

    /**
     * 更新组织结构
     *
     * @param user
     * @param cMsgOrganVO
     * @return
     */
    @PostMapping(value = "/updateOrgan")
    @ApiOperation(value = "更新组织结构", notes = "更新组织结构")
    public WebResponse updateOrgan(@LoginUser CMsgUserPO user, @RequestBody CMsgOrganVO cMsgOrganVO) {
        return WebResponseBuilder.ok().setMsg("成功更新组织结构！")
                .data(cMsgOrganService.updateOrgan(user, cMsgOrganVO));
    }

    /**
     * 获取组织机构树
     *
     * @param user
     * @return
     */
    @GetMapping(value = "/getOrganTree")
    @ApiOperation(value = "获取组织机构树", notes = "获取组织机构树")
    public WebResponse getOrganTree(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取组织机构树！")
                .data(cMsgOrganService.getOrganTree(user));
    }

    /**
     * 获取组织机类型
     *
     * @param user
     * @return
     */
    @GetMapping(value = "/getOrganType")
    @ApiOperation(value = "获取组织机类型", notes = "获取组织机类型")
    public WebResponse getOrganType(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取组织机类型！")
                .data(cMsgOrganService.getOrganType(user));
    }

}
