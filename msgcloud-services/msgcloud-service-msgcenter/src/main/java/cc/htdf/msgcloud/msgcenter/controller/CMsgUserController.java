package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgUserVO;
import cc.htdf.msgcloud.msgcenter.service.CMsgUserService;
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
@RequestMapping("/sys/user")
public class CMsgUserController {

    @Resource
    private CMsgUserService cMsgUserService;

    /**
     * 新建用户
     *
     * @param user
     * @param cMsgUserVO
     * @return
     */
    @PostMapping(value = "/createUser")
    @ApiOperation(value = "新建用户", notes = "新建用户")
    public WebResponse createUser(@LoginUser CMsgUserPO user, @RequestBody CMsgUserVO cMsgUserVO) {
        return WebResponseBuilder.ok().setMsg("成功新建用户！")
                .data(cMsgUserService.createUser(user, cMsgUserVO));
    }

    /**
     * 更新用户
     *
     * @param user
     * @param cMsgUserVO
     * @return
     */
    @PostMapping(value = "/updateUser")
    @ApiOperation(value = "更新用户", notes = "更新用户")
    public WebResponse updateUser(@LoginUser CMsgUserPO user, @RequestBody CMsgUserVO cMsgUserVO) {
        return WebResponseBuilder.ok().setMsg("成功更新用户！")
                .data(cMsgUserService.updateUser(user, cMsgUserVO));
    }

    /**
     * 根据组织ID获取用户列表
     *
     * @param organId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getUserListByOrganId")
    @ApiOperation(value = "根据组织ID获取用户列表", notes = "根据组织ID获取用户列表")
    public WebResponse getUserListByOrganId(@RequestParam(value = "organId") String organId,
                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取用户列表！")
                .data(cMsgUserService.getUserListByOrganId(organId, pageNum, pageSize));
    }

    /**
     * 根据用户ID获取用户对象
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/getUserByUserId")
    @ApiOperation(value = "根据用户ID获取用户对象", notes = "根据用户ID获取用户对象")
    public WebResponse getUserByUserId(@RequestParam(value = "userId") String userId) {
        return WebResponseBuilder.ok().setMsg("成功获取用户对象！")
                .data(cMsgUserService.getUserByUserId(userId));
    }

}
