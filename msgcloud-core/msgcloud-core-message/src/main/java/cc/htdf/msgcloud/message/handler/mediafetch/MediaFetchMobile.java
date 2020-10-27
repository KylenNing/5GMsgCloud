package cc.htdf.msgcloud.message.handler.mediafetch;

import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import cc.htdf.msgcloud.message.handler.MediaFetch;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.dto.Media;
import org.springframework.stereotype.Component;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 *
 *      浙江移动媒体获取策略
 */
@Component
public class MediaFetchMobile implements MediaFetch {
    @Override
    public Media fetchMedia(MsgMaterialPO msgMaterialPO) {
        Media media = new Media();
        media.setUrl(BASE_URL + msgMaterialPO.getMaterialLocalUrl());
        media.setType(Integer.valueOf(msgMaterialPO.getMaterialType()) + 3);
        media.setSize(msgMaterialPO.getMaterialLocalSize());
//        if (Objects.isNull(msgMaterialPO.getMaterialLocalSlUrl())
//                || Objects.equals("", msgMaterialPO.getMaterialLocalSlUrl())
//                || Objects.equals("null", msgMaterialPO.getMaterialLocalSlUrl())
//        ) {
//            media.setThumbUrl(BASE_URL + msgMaterialPO.getMaterialLocalUrl());
//        } else {
//            media.setThumbUrl(BASE_URL + msgMaterialPO.getMaterialLocalSlUrl());
//        }
        media.setThumbUrl(BASE_URL + msgMaterialPO.getMaterialLocalUrl());
        media.setName(msgMaterialPO.getMaterialName());
        System.out.println(JSONObject.toJSONString(media));
        return media;
    }
}
