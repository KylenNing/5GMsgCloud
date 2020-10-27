package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.domain.Msg;
import com.feinno.msgctenter.sdk.respon.ResponseData;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 */
public interface MsgSender {


    ResponseData sendMsg(Msg msg);

    //单文本消息发送
    ResponseData sendTextMsg(Msg msg);

    //单卡片消息
    ResponseData sendCardMsg(Msg msg);

    // 带返回建议的单卡片消息
    ResponseData sendCardMsgWithReceive(Msg msg);

    // 带返回建议的多卡片消息
    ResponseData sendManyCardsMsgWithReceive(Msg msg);

    // 单图片
    ResponseData sendSinglePictureMsg(Msg msg);

    //带悬浮菜单的文本消息
    ResponseData sendMsgWithHoverMenu(Msg msg);

}
