package cc.htdf.msgcloud.msgcenter.utils;

import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/10/9
 * title:
 */
public class Wav2TextUtil {


    private static final String au2textStartUrl = "http://60.247.77.213:8081/chat/start";
    private static final String au2textingUrl = "http://60.247.77.213:8081/chat/audio";
    private static final String au2textEndUrl = "http://60.247.77.213:8081/chat/end";



    public static String trans2Text(String descFileBase64, long descFileSize) {
        String sid = createAu2TextChat();
        String text = au2texting(descFileBase64, descFileSize, sid);
        endAu2TextChat(sid);
        return text;
    }


    public static String createAu2TextChat() {
        Map<String, Object> param = new HashMap<>();
        param.put("stype", "dsr");
        param.put("aus", "16000");
        param.put("auf", "pcm");

        String responseStr = HttpUtil.get(au2textStartUrl, param);
        if (JSONUtil.isJson(responseStr)) {
            JSONObject responseObj = JSONObject.parseObject(responseStr);
            JSONObject resultObj = responseObj.getJSONObject("result");
            if (Objects.isNull(resultObj)) {
                throw new BusinessException(500, "[语音识别工具] 创建语音识别会话失败, 获取[result]JSON节点失败！");
            }
            String sid = resultObj.getString("sid");
            if (Objects.isNull(sid) || Objects.equals("", sid)) {
                throw new BusinessException(500, "[语音识别工具] 创建语音识别会话失败, 获取[sid]会话ID失败！");
            }
            return sid;
        }
        throw new BusinessException(500, "[语音识别工具] 创建语音识别会话失败。");
    }

    private static String au2texting(String descFileBase64, long descFileSize, String sid) {
        Map<String, Object> param = new HashMap<>();
        param.put("stype", "dsr");
        param.put("sid", sid);
        param.put("audio_status", 4);
        param.put("wav_len", descFileSize);
        param.put("wav_data", descFileBase64);

        String responseStr = HttpUtil.post(au2textingUrl, param);
        if (JSONUtil.isJson(responseStr)) {
            JSONObject responseObj = JSONObject.parseObject(responseStr);
            JSONObject resultObj = responseObj.getJSONObject("result");
            if (Objects.isNull(resultObj)) {
                endAu2TextChat(sid);
                throw new BusinessException(500, "[语音识别工具] 语音识别失败, 获取[result]JSON节点失败！");
            }
            String text = resultObj.getString("kw");
            if (Objects.isNull(text) || Objects.equals("", text)) {
                endAu2TextChat(sid);
                throw new BusinessException(500, "[语音识别工具] 语音识别失败, 获取[result]JSON节点失败！");
            }
            return text;
        }
        endAu2TextChat(sid);
        throw new BusinessException(500, "[语音识别工具] 语音识别失败！");
    }

    private static void endAu2TextChat(String sid) {
        Map<String, Object> param = new HashMap<>();
        param.put("stype", "dsr");
        param.put("sid", sid);

        HttpUtil.get(au2textEndUrl, param);
    }



}
