package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgMenuVO;
import cc.htdf.msgcloud.msgcenter.service.CMsgMenuService;
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
@RequestMapping("/sys/menu")
public class CMsgMenuController {

    @Resource
    private CMsgMenuService cMsgMenuService;

    /**
     * 新建菜单
     *
     * @param user
     * @param cMsgMenuVO
     * @return
     */
    @PostMapping(value = "/createMenu")
    @ApiOperation(value = "新建菜单", notes = "新建菜单")
    public WebResponse createMenu(@LoginUser CMsgUserPO user, @RequestBody CMsgMenuVO cMsgMenuVO) {
        return WebResponseBuilder.ok().setMsg("成功新建菜单！")
                .data(cMsgMenuService.createMenu(user, cMsgMenuVO));
    }

    /**
     * 更新菜单
     *
     * @param user
     * @param cMsgMenuVO
     * @return
     */
    @PostMapping(value = "/updateMenu")
    @ApiOperation(value = "更新菜单", notes = "更新菜单")
    public WebResponse updateMenu(@LoginUser CMsgUserPO user, @RequestBody CMsgMenuVO cMsgMenuVO) {
        return WebResponseBuilder.ok().setMsg("成功更新菜单！")
                .data(cMsgMenuService.updateMenu(user, cMsgMenuVO));
    }

    /**
     * 获取更新菜单树
     *
     * @return
     */
    @GetMapping(value = "/getMenuTree")
    @ApiOperation(value = "获取更新菜单树", notes = "获取更新菜单树")
    public WebResponse getMenuTree(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取菜单树！")
                .data(cMsgMenuService.getMenuTree(user));
    }

    /**
     * 获取按钮类型
     *
     * @return
     */
    @GetMapping(value = "/getButtonTypeList")
    @ApiOperation(value = "获取按钮类型", notes = "获取按钮类型")
    public WebResponse getButtonTypeList() {
        return WebResponseBuilder.ok().setMsg("成功获取按钮类型！")
                .data(cMsgMenuService.getButtonTypeList());
    }

    /**
     * 获取按钮
     *
     * @return
     */
    @GetMapping(value = "/getButtonDisPlayList")
    @ApiOperation(value = "获取按钮", notes = "获取按钮")
    public WebResponse getButtonDisPlayList(@LoginUser CMsgUserPO user, @RequestParam(value = "menuId") String menuId) {
        return WebResponseBuilder.ok().setMsg("成功获取按钮！")
                .data(cMsgMenuService.getButtonDisPlayList(user, menuId));
    }
}
