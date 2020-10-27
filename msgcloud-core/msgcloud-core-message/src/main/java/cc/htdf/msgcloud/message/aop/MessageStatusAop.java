package cc.htdf.msgcloud.message.aop;

import cc.htdf.msgcloud.common.constants.MessageStatus;
import cc.htdf.msgcloud.common.constants.MsgOperation;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgDownInfo;
import cc.htdf.msgcloud.message.service.BMessageService;
import cc.htdf.msgcloud.message.service.MsgProductSendService;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/22
 * title:
 */
@Component
@Aspect
public class MessageStatusAop {

    @Resource
    private BMessageService messageService;

    @Resource
    private MsgProductSendService msgProductSendService;

    @Pointcut(value = "execution(* cc.htdf.msgcloud.message.listener.Msg5GListener.onMessage(..)) && args(msg)")
    public void message(Msg msg) {}

    @Before(value = "message(msg)")
    public void beforeMessage(Msg msg) {
        if (Objects.isNull(msg)) {
            return;
        }
        String msgOp = msg.getOperation();
        if (Objects.isNull(msgOp) || !Objects.equals(msgOp, MsgOperation.MSG_DOWN)) {
            return;
        }
        MsgDownInfo msgDownInfo = msg.getMsgDownInfo();
        if (Objects.isNull(msgDownInfo) || Objects.isNull(msgDownInfo.getMessageId())) {
            return;
        }
        messageService.updateMessageStatus(msgDownInfo.getMessageId(), MessageStatus.SENDING);
        msgProductSendService.updateMessageStatus(msgDownInfo.getMessageId(), MessageStatus.SENDING);
    }

    @Pointcut(value = "execution(* cc.htdf.msgcloud.message.handler.MsgSender.sendMsg(..)) && args(msg)")
    public void sendMessage(Msg msg) {}

    @AfterReturning(value = "sendMessage(msg)")
    public void sendMessageSuccess(Msg msg) {
        if (Objects.isNull(msg)) {
            return;
        }
        String msgOp = msg.getOperation();
        if (Objects.isNull(msgOp) || !Objects.equals(msgOp, MsgOperation.MSG_DOWN)) {
            return;
        }
        MsgDownInfo msgDownInfo = msg.getMsgDownInfo();
        if (Objects.isNull(msgDownInfo) || Objects.isNull(msgDownInfo.getMessageId())) {
            return;
        }
        messageService.updateMessageStatus(msgDownInfo.getMessageId(), MessageStatus.SEND_SUCCESS);
        msgProductSendService.updateMessageStatus(msgDownInfo.getMessageId(), MessageStatus.SEND_SUCCESS);


    }

    @AfterThrowing(value = "sendMessage(msg)")
    public void sendMessageFailed(Msg msg) {
        if (Objects.isNull(msg)) {
            return;
        }
        String msgOp = msg.getOperation();
        if (Objects.isNull(msgOp) || !Objects.equals(msgOp, MsgOperation.MSG_DOWN)) {
            return;
        }
        MsgDownInfo msgDownInfo = msg.getMsgDownInfo();
        if (Objects.isNull(msgDownInfo) || Objects.isNull(msgDownInfo.getMessageId())) {
            return;
        }
        messageService.updateMessageStatus(msgDownInfo.getMessageId(), MessageStatus.SEND_FAILED);
        msgProductSendService.updateMessageStatus(msgDownInfo.getMessageId(), MessageStatus.SEND_FAILED);
    }

}
