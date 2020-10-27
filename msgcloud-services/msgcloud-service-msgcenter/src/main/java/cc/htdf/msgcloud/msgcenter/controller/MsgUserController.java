package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgUserLabelVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgUserVO;
import cc.htdf.msgcloud.msgcenter.service.MsgUserService;
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
@RequestMapping("/user")
public class MsgUserController {

    @Resource
    private MsgUserService msgUserService;

    /**
     * 5G气象服务云平台获取用户组列表
     *
     * @param userLabelName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getUserLabelList")
    @ApiOperation(value = "5G气象服务云平台获取用户组列表", notes = "5G气象服务云平台获取用户组列表")
    public WebResponse getUserLabelList(@LoginUser CMsgUserPO user,
                                        @RequestParam(value = "userLabelName", required = false) String userLabelName,
                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取用户组列表！")
                .data(msgUserService.getUserLabelList(user, userLabelName, pageNum, pageSize));
    }

    /**
     * 5G气象服务云平台获取用户列表
     *
     * @param userLabelId
     * @param userTel
     * @param userName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getUserList")
    @ApiOperation(value = "5G气象服务云平台获取用户列表", notes = "5G气象服务云平台获取用户列表")
    public WebResponse getUserList(@LoginUser CMsgUserPO user,
                                   @RequestParam(value = "userLabelId", required = false) String userLabelId,
                                   @RequestParam(value = "userTel", required = false) String userTel,
                                   @RequestParam(value = "userName", required = false) String userName,
                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取用户列表！")
                .data(msgUserService.getUserList(userLabelId, userTel, user, userName, pageNum, pageSize));
    }

    /**
     * 5G气象服务云平台新建用户组
     *
     * @param user
     * @param msgUserLabelVO
     * @return
     */
    @PostMapping(value = "/createUserLabel")
    @ApiOperation(value = "5G气象服务云平台新建用户组", notes = "5G气象服务云平台新建用户组")
    public WebResponse createUserLabel(@LoginUser CMsgUserPO user, @RequestBody MsgUserLabelVO msgUserLabelVO) {
        return WebResponseBuilder.ok().setMsg("成功新建用户组！")
                .data(msgUserService.createUserLabel(user, msgUserLabelVO));
    }

    /**
     * 5G气象服务云平台新建用户
     *
     * @param user
     * @param msgUserVO
     * @return
     */
    @PostMapping(value = "/createUser")
    @ApiOperation(value = "5G气象服务云平台新建用户", notes = "5G气象服务云平台新建用户")
    public WebResponse createUser(@LoginUser CMsgUserPO user, @RequestBody MsgUserVO msgUserVO) {
        return WebResponseBuilder.ok().setMsg("成功新建用户！")
                .data(msgUserService.createUser(user, msgUserVO));
    }

    /**
     * 5G气象服务云平台删除用用户组
     *
     * @param userLabelIds
     * @return
     */
    @GetMapping(value = "/deleteUserLabelByIds")
    @ApiOperation(value = "5G气象服务云平台删除用用户组", notes = "5G气象服务云平台删除用用户组")
    public WebResponse deleteUserLabelByIds(@RequestParam(value = "userLabelIds") String userLabelIds) {
        return WebResponseBuilder.ok().setMsg("成功删除用户组！")
                .data(msgUserService.deleteUserLabelByIds(userLabelIds));
    }

    /**
     * 5G气象服务云平台删除用户
     *
     * @param userIds
     * @return
     */
    @GetMapping(value = "/deleteUserByIds")
    @ApiOperation(value = "5G气象服务云平台删除用户", notes = "5G气象服务云平台删除用户")
    public WebResponse deleteUserByIds(@RequestParam(value = "userIds") String userIds) {
        return WebResponseBuilder.ok().setMsg("成功删除用户！")
                .data(msgUserService.deleteUserByIds(userIds));
    }

    /**
     * 5G气象服务云平台获取用户组
     *
     * @param userLabelId
     * @return
     */
    @GetMapping(value = "/getUserLabelById")
    @ApiOperation(value = "5G气象服务云平台获取用户组", notes = "5G气象服务云平台获取用户组")
    public WebResponse getUserLabelById(@RequestParam(value = "userLabelId") String userLabelId) {
        return WebResponseBuilder.ok().setMsg("成功获取用户组！")
                .data(msgUserService.getUserLabelById(userLabelId));
    }

    /**
     * 5G气象服务云平台获取用户
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/getUserById")
    @ApiOperation(value = "5G气象服务云平台获取用户", notes = "5G气象服务云平台获取用户")
    public WebResponse getUserById(@RequestParam(value = "userId") String userId) {
        return WebResponseBuilder.ok().setMsg("成功获取用户！")
                .data(msgUserService.getUserById(userId));
    }

    /**
     * 5G气象服务云平台全部用户组
     *
     * @param user
     * @return
     */
    @GetMapping(value = "/getUserLabel")
    @ApiOperation(value = "5G气象服务云平台全部用户组", notes = "5G气象服务云平台全部用户组")
    public WebResponse getUserLabel(@LoginUser CMsgUserPO user) {
        return WebResponseBuilder.ok().setMsg("成功获取全部用户组！")
                .data(msgUserService.getUserLabel(user));
    }

    /**
     * 获取关注区域
     *
     * @return
     */
    @GetMapping(value = "/getAreaList")
    @ApiOperation(value = "获取关注区域", notes = "获取关注区域")
    public WebResponse getAreaList() {
        return WebResponseBuilder.ok().setMsg("成功获取关注区域！")
                .data(msgUserService.getAreaList());
    }

    /**
     * 获取用户性别
     *
     * @return
     */
    @GetMapping(value = "/getSexList")
    @ApiOperation(value = "获取用户性别", notes = "获取用户性别")
    public WebResponse getSexList() {
        return WebResponseBuilder.ok().setMsg("成功获取用户性别！")
                .data(msgUserService.getSexList());
    }

    /**
     * 获取用户职业
     *
     * @return
     */
    @GetMapping(value = "/getWorkList")
    @ApiOperation(value = "获取用户职业", notes = "获取用户职业")
    public WebResponse getWorkList() {
        return WebResponseBuilder.ok().setMsg("成功获取用户职业！")
                .data(msgUserService.getWorkList());
    }

    /**
     * 获取用户出行
     *
     * @return
     */
    @GetMapping(value = "/getTravelList")
    @ApiOperation(value = "获取用户出行", notes = "获取用户出行")
    public WebResponse getTravelList() {
        return WebResponseBuilder.ok().setMsg("成功获取用户出行！")
                .data(msgUserService.getTravelList());
    }

    /**
     * 获取用户活跃度
     *
     * @return
     */
    @GetMapping(value = "/getActiveList")
    @ApiOperation(value = "获取用户活跃度", notes = "获取用户活跃度")
    public WebResponse getActiveList() {
        return WebResponseBuilder.ok().setMsg("成功获取用户活跃度！")
                .data(msgUserService.getActiveList());
    }
}
