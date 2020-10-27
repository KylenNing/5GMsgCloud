package cc.htdf.msgcloud.msgcenter.handler;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.msgcenter.coder.CommandExec;
import cc.htdf.msgcloud.msgcenter.coder.WavCoderCommand;
import cc.htdf.msgcloud.msgcenter.utils.Wav2TextUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * author: JT
 * date: 2020/10/9
 * title:
 */
@Component
public class Audio2TextHandler {

    @Resource
    private List<MsgMediaDownHandler> mediaDownHandlerList = Collections.synchronizedList(new ArrayList<>(2));

    @Value("${path.ffmpeg}")
    private String ffmpegPath;


    public String handler(Msg msg) throws IOException {
        MsgMediaDownHandler msgMediaDownHandler = null;
        for (MsgMediaDownHandler temp : mediaDownHandlerList) {
            if (msg.getChannelBusiness().equals(temp.channelBusiness())) {
                msgMediaDownHandler = temp;
                break;
            }
        }
        if (Objects.isNull(msgMediaDownHandler)) {
            throw new BusinessException(500, "未匹配到媒体下载策略！");
        }
        File amrFile = msgMediaDownHandler.downloadMedia(msg);
        String amrFilepath = amrFile.getPath();
        String destFilepath = amrFilepath.replace(".amr", ".wav");
        WavCoderCommand wavCoderCommand = new WavCoderCommand(
                this.getFfmpegPath(),
                amrFilepath,
                destFilepath
        );
        CommandExec.run(wavCoderCommand);
        Path destPath = Paths.get(destFilepath);
        byte[] destFileBytes = Files.readAllBytes(destPath);
        String descFileBase64 = Base64.getEncoder().encodeToString(destFileBytes);
        long descFileSize = Files.size(destPath);
        String text = Wav2TextUtil.trans2Text(descFileBase64, descFileSize);
        Files.deleteIfExists(destPath);
        Files.deleteIfExists(Paths.get(amrFilepath));
        return text;
    }

    public String getFfmpegPath() {
        return ffmpegPath;
    }
}
