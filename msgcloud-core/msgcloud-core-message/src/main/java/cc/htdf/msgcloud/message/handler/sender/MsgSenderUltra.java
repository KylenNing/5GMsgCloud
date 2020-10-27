package cc.htdf.msgcloud.message.handler.sender;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.message.handler.MsgSender;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 *
 *      神州泰岳消息发送
 */
@Component
public class MsgSenderUltra implements MsgSender {

    @Override
    public ResponseData sendMsg(Msg msg) {
        ResponseData responseData = Api.point2pointSend(msg.getMsgInfo());
        responseData.setData(
                ImmutableMap.of("body", msg.getMsgInfo())
        );
        return responseData;
    }

    @Override
    public ResponseData sendTextMsg(Msg msg) {
        return sendMsg(msg);
    }

    @Override
    public ResponseData sendCardMsg(Msg msg) {
        return sendMsg(msg);
    }

    @Override
    public ResponseData sendCardMsgWithReceive(Msg msg){
        return sendMsg(msg);
    }

    @Override
    public ResponseData sendManyCardsMsgWithReceive(Msg msg){
        return sendMsg(msg);
    }

    @Override
    public ResponseData sendSinglePictureMsg(Msg msg){
        return sendMsg(msg);
    }

    @Override
    public ResponseData sendMsgWithHoverMenu(Msg msg){
        return sendMsg(msg);
    }

}
