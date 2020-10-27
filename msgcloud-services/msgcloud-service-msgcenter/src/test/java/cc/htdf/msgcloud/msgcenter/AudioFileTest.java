package cc.htdf.msgcloud.msgcenter;

import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.utils.HttpRequestUtils;
import cc.htdf.msgcloud.msgcenter.coder.CommandExec;
import cc.htdf.msgcloud.msgcenter.coder.WavCoderCommand;
import cc.htdf.msgcloud.msgcenter.handler.MsgMediaDownHandler;
import cc.htdf.msgcloud.msgcenter.utils.Wav2TextUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.speech.AipSpeech;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * author: JT
 * date: 2020/10/6
 * title:
 */
@Slf4j
public class AudioFileTest extends BaseTest {

    @Resource
    private List<MsgMediaDownHandler> mediaDownHandlerList = Collections.synchronizedList(new ArrayList<>(2));

    @Value("${path.ffmpeg}")
    private String ffmpegPath;

    @Test
    public void analysisUrlTest() throws DocumentException {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<file xmlns=\"urn:gsma:params:xml:ns:rcs:rcs:fthttp\" xmlns:am=\"urn:gsma:params:xml:ns:rcs:rcs:rram\">\n" +
                "  <file-info type=\"file\" file-disposition=\"render\">\n" +
                "    <file-size>3206</file-size>\n" +
                "    <file-hash-algorithm>md5</file-hash-algorithm>\n" +
                "    <file-hash-value>30:0C:E7:9D:B9:88:57:0F:50:CF:12:26:52:EF:6F:CF</file-hash-value>\n" +
                "    <file-name>827312427327119209.amr</file-name>\n" +
                "    <content-type>audio/amr</content-type>\n" +
                "    <data url=\"https://hdn.ftcontentserver.rcs.mnc000.mcc460.pub.3gppnetwork.org:10011/s/10061125182311610091100096FD.amr\" until=\"2020-10-09T19:25:18+08:00\" />\n" +
                "    <am:playing-length>2000</am:playing-length>\n" +
                "  </file-info>\n" +
                "</file>";
        Document document = DocumentHelper.parseText(text);
        Element rootEl = document.getRootElement();
        Element fileinfoEl = rootEl.element("file-info");
        Element dataEl = fileinfoEl.element("data");
        System.out.println(dataEl.attributeValue("url"));
    }

    @Test
    public void receiveUltraAudioTest() throws IOException {
        String content = "{\"appId\":\"1065051121231\",\"channelId\":\"504973341409411072\",\"contentType\":\"audio\",\"contributionId\":\"bb24bc87-ec2b-1038-b054-433d945fb2d0\",\"conversationId\":\"882beb77-1a6c-4cfa-acdd-4acba2d2078c\",\"destination\":\"17857992797\",\"media\":{\"name\":\"7496634029107691605.amr\",\"originUrl\":\"http://5gmc.fetiononline.com/fastdfs/group1/M00/00/90/CgAB41-AC7eAOlGrAAAORvEId_Q555.amr\",\"size\":\"3654\",\"type\":5,\"url\":\"http://5gmc.fetiononline.com/fastdfs/group1/M00/00/90/CgAB41-AC7eAOlGrAAAORvEId_Q555.amr\"}}";
        MsgUpInfo msgUpInfo = (MsgUpInfo) JSON.parseObject(content, MsgUpInfo.class);
        Msg msg = new Msg();
        msg.setMsgUpInfo(msgUpInfo);

        if ("audio".equals(msgUpInfo.getContentType())) {
            MsgMediaDownHandler msgMediaDownHandler = null;
            for (MsgMediaDownHandler temp : mediaDownHandlerList) {
                if (ChannelBusiness.CHANNLE_ULTRA.equals(temp.channelBusiness())) {
                    msgMediaDownHandler = temp;
                }
            }
            if (Objects.isNull(msgMediaDownHandler)) {
                return;
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
            System.out.println(text);
            Files.deleteIfExists(destPath);
            Files.deleteIfExists(Paths.get(amrFilepath));
        }

    }

    @Test
    public void ultraAudioDownloadTest() throws IOException {
        String url = "http://5gmc.fetiononline.com/fastdfs/group1/M00/00/90/CgAB419__NSAfWIUAAAOhmvLvaI257.amr";
        String dir = "D:\\data\\5g\\";
        String filename = "CgAB419__NSAfWIUAAAOhmvLvaI257.amr";
        File armFile = new File(dir + filename);
        HttpUtil.downloadFile(url, armFile);
        System.out.println("文件下载成功: " + armFile);
        String destName = filename.replace(".amr", ".wav");
        File destFile = new File(dir + destName);
        WavCoderCommand wavCoderCommand = new WavCoderCommand(
                "D:\\soft\\utils\\ffmpeg\\bin\\ffmpeg.exe",
                armFile.getPath(),
                destFile.getPath()
        );
        CommandExec.run(wavCoderCommand);
        System.out.println("文件转码成功：" + destFile.getPath());

        byte[] bytes = Files.readAllBytes(Paths.get(destFile.getPath()));
        String destBase64 = Base64.getEncoder().encodeToString(bytes);
        long destfileLength = destFile.length();
        System.out.println("文件流进行Base64处理成功");

        String au2textStart = "http://60.247.77.213:8081/chat/start?stype=dsr&aus=16000&auf=pcm";
        HttpResponse response = HttpRequestUtils.get(au2textStart);
        com.alibaba.fastjson.JSONObject bodyObj = com.alibaba.fastjson.JSONObject.parseObject(response.body());
        com.alibaba.fastjson.JSONObject resultObj = bodyObj.getJSONObject("result");
        String sid = resultObj.getString("sid");
        System.out.println("获取语音识别会话ID：" + sid);

        String au2texting = "http://60.247.77.213:8081/chat/audio";
        Map<String, Object> param = new HashMap<>();
        param.put("stype", "dsr");
        param.put("sid", sid);
        param.put("audio_status", 4);
        param.put("wav_len", destfileLength);
        param.put("wav_data", destBase64);
        String au2textingResponse = HttpUtil.post(au2texting, param);
        com.alibaba.fastjson.JSONObject au2textResponseObj = com.alibaba.fastjson.JSONObject.parseObject(au2textingResponse);
        com.alibaba.fastjson.JSONObject au2textResutObj = au2textResponseObj.getJSONObject("result");
        String kw = au2textResutObj.getString("kw");
        System.out.println("语音识别完成，识别结果：" + kw);


        String au2textEnd = "http://60.247.77.213:8081/chat/end?stype=dsr&sid=" + sid;
        HttpUtil.get(au2textEnd);
        System.out.println("结束语音识别会话ID：" + sid);

    }

    @Test
    public void fileTest() throws IOException {
        File file = new File("D:\\soft\\utils\\ffmpeg\\bin\\test.wav");
        byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
        String base64 =Base64.getEncoder().encodeToString(bytes);
//        System.out.println(URLEncoder.encode(base64, "UTF-8"));
        System.out.println(base64);
//        System.out.println(file.length());
    }

    @Test
    public void audioCodeTest() {
        WavCoderCommand wavCoderCommand = new WavCoderCommand(
                "D:\\soft\\utils\\ffmpeg\\bin\\ffmpeg.exe",
                "D:\\soft\\utils\\ffmpeg\\bin\\CgAB419wPzuAdTpBAAANZvEFAUk830.amr",
                "D:\\soft\\utils\\ffmpeg\\bin\\a.wav"
        );
        CommandExec.run(wavCoderCommand);
    }

    @Test
    public void armTest() {
        String APP_ID = "22792773";
        String API_KEY = "ZhGv6qVNhgic2nXbvIl7R169";
        String SECRET_KEY = "DDxUazrBwtl4iC6yNleoWfyqY4iNNw1Q";

        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("dev_pid", 1537);

        // 调用接口
        JSONObject res = client.asr(
                "D:\\soft\\utils\\ffmpeg\\bin\\10081517152311870092400105FD.amr",
                "amr",
                8000,
                options
        );
        System.out.println(res.toString(2));
    }

    /**
     * 1, 2, 3, 4, 5, n
     * n, n-1, n-2,
     * @param s
     */
    public void reverseString(char[] s) {
        for (int left = 0; ; left++) {
            int right = s.length - (1 + left);
            if (right <= left) {
                break;
            }
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
        }
    }

    public String getFfmpegPath() {
        return ffmpegPath;
    }
}
