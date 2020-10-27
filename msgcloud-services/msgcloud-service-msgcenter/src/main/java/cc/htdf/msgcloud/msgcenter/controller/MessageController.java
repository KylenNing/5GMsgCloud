package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.domain.WebResponse;
import cc.htdf.msgcloud.common.domain.WebResponseBuilder;
import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgMessageVO;
import cc.htdf.msgcloud.msgcenter.service.MessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @Author: ningyq
 * @Date: 2020/8/14
 * @Description: TODO
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService msgMessageService;

    /**
     * @Description: 新建消息
     * @param  vo
     * @author ningyq
     * @date 2020/8/11
     */
    @PostMapping(value = "/createMsg")
    @ApiOperation(value = "新建消息", notes = "新建消息")
    @ResponseBody
    public WebResponse createMsg(@LoginUser CMsgUserPO user,@RequestBody MsgMessageVO vo) throws InterruptedException {

        msgMessageService.insertNewMessageToDatabase(user,vo);
        return WebResponseBuilder.ok().setMsg("成功新增消息！");

    }

    /**
     * @Description: 获取所有消息
     * @param  msgStatus,serviceId,currentPage,pageSize
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "获取所有消息", notes = "获取所有消息")
    @GetMapping(value = "/getAllMessage")
    public WebResponse getAllMessage(@LoginUser CMsgUserPO user,@RequestParam("msgStatus") Integer msgStatus,@RequestParam("serviceId") String serviceId,
    @RequestParam("currentPage") Integer currentPage,@RequestParam("pageSize") Integer pageSize
            ,@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) throws ParseException {


        return WebResponseBuilder.ok().setMsg("成功获取所有消息！")
                .data(msgMessageService.getAllMessage(user,msgStatus,serviceId,currentPage,pageSize, startTime, endTime));

    }

    /**
     * @Description: 根据id获取消息相关信息
     * @param  msgId
     * @author ningyq
     * @date 2020/8/11
     */
    @ApiOperation(value = "根据id获取消息相关信息", notes = "根据id获取消息相关信息")
    @GetMapping(value = "/getMessageRelatedInfo")
    public WebResponse getMessageRelatedInfo(@RequestParam("msgId") Integer msgId) {


        return WebResponseBuilder.ok().setMsg("成功获取该消息相关信息！")
                .data(msgMessageService.getMessageRelatedInfoById(msgId));

    }
}