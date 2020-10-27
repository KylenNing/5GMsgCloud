package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.dto.GroupPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.service.MsgGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: 获取所有模块
 * @param  @param
 * @author ningyq
 * @date 2020/10/6
 */

@Validated
@RestController
@RequestMapping("/group")
public class MsgGroupController {

    @Resource
    private MsgGroupService msgGroupService;

    /**
     * 获取所有模块
     *
     * @return
     */
    @GetMapping(value = "/getAllModules")
    @ApiOperation(value = "获取所有模块", notes = "获取所有模块")
    public WebResponse getAllModules() {
        return WebResponseBuilder.ok().setMsg("成功获取所有模块！")
                .data(msgGroupService.getAllModules());
    }


    /**
     * @Description: 添加卡片组
     * @param  dto
     * @author ningyq
     * @date 2020/8/11
     */
    @PostMapping(value = "/addGroup")
    @ApiOperation(value = "添加卡片组", notes = "添加卡片组")
    @ResponseBody
    public WebResponse addGroup(@LoginUser CMsgUserPO user, @RequestBody GroupPageDTO dto) {

        return WebResponseBuilder.ok().setMsg("成功返回！")
                .data(msgGroupService.createGroup(user,dto));

    }

    /**
     * @Description: 删除卡片组
     * @param  groupId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "删除卡片组", notes = "删除卡片组")
    @PostMapping(value = "/deleteGroup")
    public WebResponse deleteGroup(@RequestParam("groupId") Integer groupId) {
        msgGroupService.deleteGroupById(groupId);
        return WebResponseBuilder.ok().setMsg("成功删除卡片组！");

    }

    /**
     * @Description: 获取所有卡片组
     * @param  @param
     * @author ningyq
     * @date 2020/10/6
     */
    @GetMapping(value = "/getAllGroups")
    @ApiOperation(value = "获取所有卡片组", notes = "获取所有卡片组")
    public WebResponse getAllGroups(@LoginUser CMsgUserPO user,
                                    @RequestParam("currentPage") Integer currentPage,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestParam(value = "moduleId",required = false) Integer moduleId,
                                    @RequestParam(value = "groupName", required = false) String groupName
                                    ){
        return WebResponseBuilder.ok().setMsg("成功获取所有卡片组！")
                .data(msgGroupService.getAllGroup(user,currentPage,pageSize,moduleId,groupName));
    }

    /**
     * @Description: 获取该卡片组相关信息
     * @param  @param
     * @author ningyq
     * @date 2020/10/6
     */
    @GetMapping(value = "/getGroupInfoById")
    @ApiOperation(value = "获取该卡片组相关信息", notes = "获取该卡片组相关信息")
    public WebResponse getAllGroups(@LoginUser CMsgUserPO user,
                                    @RequestParam("groupId") Integer groupId
    ){
        return WebResponseBuilder.ok().setMsg("成功获取该卡片组相关信息！")
                .data(msgGroupService.getGroupInfoById(user,groupId));
    }
}
