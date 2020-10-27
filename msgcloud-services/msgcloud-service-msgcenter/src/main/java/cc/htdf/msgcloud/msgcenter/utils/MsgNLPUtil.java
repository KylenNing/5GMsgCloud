package cc.htdf.msgcloud.msgcenter.utils;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.utils.HttpRequestUtils;
import cc.htdf.msgcloud.msgcenter.domain.dto.TextMsgNlpDTO;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/13
 * title:
 */
public class MsgNLPUtil {


    private static final String NLP_TEXT_URL = "http://117.107.241.79:7792/atmosphere/nlp/parser";

    public static Msg nlpAnalysisText(Msg msg) {
        MsgUpInfo msgUpInfo = msg.getMsgUpInfo();
        TextMsgNlpDTO textMsgNlpDTO = new TextMsgNlpDTO();
        textMsgNlpDTO.setMessageId(msgUpInfo.getMsgId());
        textMsgNlpDTO.setText(Arrays.asList(msgUpInfo.getText()));
        textMsgNlpDTO.setClientId("htdf-msgclient-1");
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("User-Agent", "Mozilla/5.0");
        header.put("Accept", "*/*");
        HttpResponse response = HttpRequestUtils.post(NLP_TEXT_URL, textMsgNlpDTO, header);
        if (response.getStatus() == HttpStatus.HTTP_OK) {
            //TextMsgNlpResponseDTO textMsgNlpResponse = JSONObject.parseObject(response.body(), TextMsgNlpResponseDTO.class);
            JSONArray actions = JSONArray.parseArray(response.body());
            JSONObject actionObj = actions.getJSONObject(0);
            Map<String, Object> param = new HashMap<>();
            JSONObject locationObj = actionObj.getJSONObject("location");
            JSONArray timesArr = actionObj.getJSONArray("times");
            JSONArray elementArr = actionObj.getJSONArray("element");

            if (!Objects.isNull(locationObj)) {
                param.put(NlpParamName.LOCATION_PROVINCE, locationObj.getString("province"));
                param.put(NlpParamName.LOCATION_CITY, locationObj.getString("city"));
                param.put(NlpParamName.LOCATION_COUNTY, locationObj.getString("county"));
                param.put(NlpParamName.LOCATION_DETAIL, locationObj.getString("detail"));
            }

            // 设置Action
            String action = actionObj.getString("action");
            param.put(NlpParamName.ACTION, action);
            msg.setAction(action);

            // 设置时间段
            param.put(NlpParamName.TIMES_START, timesArr.getString(0));
            param.put(NlpParamName.TIMES_END, timesArr.getString(1));

            // 设置要素
            String[] elements = new String[elementArr.size()];
            elementArr.toArray(elements);
            param.put(NlpParamName.ELEMENTS, elements);

            msg.setParam(param);
        }
        return msg;
    }

}
