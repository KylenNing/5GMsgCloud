package cc.htdf.msgcloud.message.handler.mediafetch;

import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import cc.htdf.msgcloud.message.handler.MediaFetch;
import com.feinno.msgctenter.sdk.dto.Media;
import org.springframework.stereotype.Component;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
@Component
public class MediaFetchUltra implements MediaFetch {
    @Override
    public Media fetchMedia(MsgMaterialPO msgMaterialPO) {
        Media media = new Media();
        media.setId(msgMaterialPO.getMaterialWebId());
        media.setUrl(msgMaterialPO.getMaterialWebUrl());
        media.setType(Integer.valueOf(msgMaterialPO.getMaterialType()) + 3);
        media.setSize(msgMaterialPO.getMaterialWebSize());
        media.setThumbUrl(msgMaterialPO.getMaterialWebSlUrl());
        media.setName(msgMaterialPO.getMaterialName());
        return media;
    }
}
