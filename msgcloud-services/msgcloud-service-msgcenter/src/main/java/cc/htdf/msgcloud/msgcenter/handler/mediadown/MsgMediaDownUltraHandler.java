package cc.htdf.msgcloud.msgcenter.handler.mediadown;

import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.msgcenter.handler.MsgMediaDownHandler;
import cn.hutool.http.HttpUtil;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/10/9
 * title:
 */
@Component
public class MsgMediaDownUltraHandler implements MsgMediaDownHandler {

    @Value("${path.tmp}")
    private String tmpPath;


    @Override
    public String channelBusiness() {
        return ChannelBusiness.CHANNLE_ULTRA;
    }

    @Override
    public File downloadMedia(Msg msg) {
        MsgUpInfo msgUpInfo = msg.getMsgUpInfo();
        Media media = msgUpInfo.getMedia();
        if (Objects.isNull(media)) {
            throw new BusinessException(500, "未找到需要下载的媒体文件URL!");
        }
        String url = media.getUrl();
        String filename = FilenameUtils.getName(url);
        if (Objects.isNull(url)) {
            throw new BusinessException(500, "未找到需要下载的媒体文件URL!");
        }
        if (Objects.isNull(filename)) {
            throw new BusinessException(500, "未找到需要下载的媒体文件名[" + url + "]");
        }

        File destFile = new File(this.getTmpPath() + filename);
        HttpUtil.downloadFile(url, destFile);
        return destFile;
    }

    public String getTmpPath() {
        return tmpPath.endsWith("/") ? tmpPath : tmpPath + "/";
    }
}
