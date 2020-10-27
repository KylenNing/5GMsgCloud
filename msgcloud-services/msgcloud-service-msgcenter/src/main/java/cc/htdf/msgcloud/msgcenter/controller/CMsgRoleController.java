package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgRoleVO;
import cc.htdf.msgcloud.msgcenter.service.CMsgRoleService;
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
@RequestMapping("/sys/role")
public class CMsgRoleController {

    @Resource
    private CMsgRoleService cMsgRoleService;

    /**
     * 新建角色
     *
     * @param user
     * @param cMsgRoleVO
     * @return
     */
    @PostMapping(value = "/createRole")
    @ApiOperation(value = "新建角色", notes = "新建角色")
    public WebResponse createRole(@LoginUser CMsgUserPO user, @RequestBody CMsgRoleVO cMsgRoleVO) {
        return WebResponseBuilder.ok().setMsg("成功新建角色！")
                .data(cMsgRoleService.createRole(user, cMsgRoleVO));
    }

    /**
     * 更新角色
     *
     * @param user
     * @param cMsgRoleVO
     * @return
     */
    @PostMapping(value = "/updateRole")
    @ApiOperation(value = "更新角色", notes = "更新角色")
    public WebResponse updateRole(@LoginUser CMsgUserPO user, @RequestBody CMsgRoleVO cMsgRoleVO) {
        return WebResponseBuilder.ok().setMsg("成功更新角色！")
                .data(cMsgRoleService.updateRole(user, cMsgRoleVO));
    }

    /**
     * 根据组织ID获取角色列表带分页
     *
     * @param organId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getRoleListByOrganId")
    @ApiOperation(value = "根据组织ID获取角色列表带分页", notes = "根据组织ID获取角色列表带分页")
    public WebResponse getRoleListByOrganId(@RequestParam(value = "organId", required = false) String organId,
                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return WebResponseBuilder.ok().setMsg("成功获取角色列表！")
                .data(cMsgRoleService.getRoleListByOrganId(organId, pageNum, pageSize));
    }

    /**
     * 根据组织ID获取角色列表不带分页
     *
     * @param organId
     * @return
     */
    @GetMapping(value = "/getRoleList")
    @ApiOperation(value = "根据组织ID获取角色列表不带分页", notes = "根据组织ID获取角色列表不带分页")
    public WebResponse getRoleList(@RequestParam(value = "organId") String organId) {
        return WebResponseBuilder.ok().setMsg("成功获取角色列表！")
                .data(cMsgRoleService.getRoleList(organId));
    }

    /**
     * 根据角色ID获取角色对象
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/getRoleByRoleId")
    @ApiOperation(value = "根据角色ID获取角色对象", notes = "根据角色ID获取角色对象")
    public WebResponse getRoleByRoleId(@RequestParam(value = "roleId") String roleId) {
        return WebResponseBuilder.ok().setMsg("成功获取角色对象！")
                .data(cMsgRoleService.getRoleByRoleId(roleId));
    }
}
