package cc.htdf.msgcloud.msgcenter.handler.mediadown;

import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.utils.SHA256Util;
import cc.htdf.msgcloud.msgcenter.handler.MsgMediaDownHandler;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import com.feinno.msgctenter.sdk.util.AESEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * author: JT
 * date: 2020/10/9
 * title:
 */
@Slf4j
@Component
public class MsgMediaDownMobileHandler implements MsgMediaDownHandler {

    @Value("${path.tmp}")
    private String tmpPath;

    @Override
    public String channelBusiness() {
        return ChannelBusiness.CHANNEL_MOBILE;
    }

    @Override
    public File downloadMedia(Msg msg) {
        MsgUpInfo msgUpInfo = msg.getMsgUpInfo();
        MsgChatbot msgChatbot = msg.getMsgChatbot();
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
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String dateFormat = sdf.format(cd.getTime());
        HttpResponse httpResponse = HttpRequest.get(url)
                .header("Terminal-type", "Chatbot")
                .header("User-Agent", String.format("SP/sip:%s@botplatform.rcs.chinamobile.com", msgChatbot.getChatbotId()))
                .header("X-3GPP-Intended-Identity", String.format("%s@botplatform.rcs.chinamobile.com", msgChatbot.getChatbotId()))
                .header("Date", dateFormat)
                .header("Authorization", "Basic " + AESEncryptUtil.base64Encode(
                        String.format(
                                "%s:%s",
                                msgChatbot.getAppId(),
                                SHA256Util.getSHA256("4f95d15126615625250926f3747dd5861b745927ce58051295989de43deeb3d2" + dateFormat)
                        ).getBytes()
                ))
                .execute();
        try {
            FileUtils.copyInputStreamToFile(httpResponse.bodyStream(), destFile);
        } catch (IOException e) {
            throw new BusinessException(500, "下载文件[" + url + "]失败！");
        }
        return destFile;
    }

    public String getTmpPath() {
        return tmpPath.endsWith("/") ? tmpPath : tmpPath + "/";
    }
}
