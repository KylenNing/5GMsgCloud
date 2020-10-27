package cc.htdf.msgcloud.msgcenter.handler;

import cc.htdf.msgcloud.common.domain.Msg;

import java.io.File;

/**
 * author: JT
 * date: 2020/10/9
 * title:
 */
public interface MsgMediaDownHandler {

    String channelBusiness();

    File downloadMedia(Msg msg);

}
