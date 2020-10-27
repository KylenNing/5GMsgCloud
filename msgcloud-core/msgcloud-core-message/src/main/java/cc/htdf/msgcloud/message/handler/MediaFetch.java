package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import com.feinno.msgctenter.sdk.dto.Media;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
public interface MediaFetch {

    String BASE_URL = "http://tj.msgcloud.80php.com/api/msgcenter/resource/view/";

    Media fetchMedia(MsgMaterialPO msgMaterialPO);

}
