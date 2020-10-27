package cc.htdf.msgcloud.msgcenter.handler.msgup;

import cc.htdf.msgcloud.common.constants.QueueName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.msgcenter.constant.MsgUpStrategyConstant;
import cc.htdf.msgcloud.msgcenter.domain.po.BMsgKeywordPO;
import cc.htdf.msgcloud.msgcenter.handler.MsgHandler;
import cc.htdf.msgcloud.msgcenter.handler.MsgNlpStrategyHelper;
import cc.htdf.msgcloud.msgcenter.service.MsgKeywordService;
import cc.htdf.msgcloud.msgcenter.utils.ActionConvertUtil;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.dto.AuditNotify;
import com.feinno.msgctenter.sdk.dto.MsgUpInfo;
import com.feinno.msgctenter.sdk.dto.StatusNotify;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/12
 * title:
 */

@Slf4j
@Component(MsgUpStrategyConstant.MSGUP_HANDLER_ULTRA)
public class MsgUpHandlerUltra implements MsgHandler {

    @Resource
    private MsgNlpStrategyHelper msgNlpStrategyHelper;

    @Resource
    private MsgKeywordService msgKeywordService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessageStateCallback(StatusNotify statusNotify) {

    }

    @Override
    public void onMessageUpNotify(Msg msg) {
        MsgUpInfo msgUpInfo = msg.getMsgUpInfo();
        MsgChatbot msgChatbot = msg.getMsgChatbot();

        String text = msgUpInfo.getText().trim();
        String contentType = msgUpInfo.getContentType();
        boolean isUseNlp = true;
        if (contentType.contains("text")) {
            List<BMsgKeywordPO> msgKeywordList = msgKeywordService.findByKeyworkAndServiceId(text, msgChatbot.getServiceId());
            if (!Objects.isNull(msgKeywordList) && !msgKeywordList.isEmpty()) {
                isUseNlp = false;
            }
        }
        if (isUseNlp) {
            try {
                msg = msgNlpStrategyHelper.nlpAnalysisMsg(msg);
            } catch (Exception e) {
                log.error("使用NLP解析上行信息失败, 信息：{}, 异常：{}", msgUpInfo.getText(), e.getMessage());
                msg.setAction("未匹配到真实意图");
            }
        } else {
            msg.setAction(text);
            msg.setMsgUpInfo(msgUpInfo);
        }
        if (Objects.isNull(msg) || Objects.isNull(msg.getAction())) {
            log.info("NLP未解析到当前上行消息的真实意图！[{}]", JSONObject.toJSONString(msgUpInfo));
            msg.setAction("未匹配到真实意图");
            msg.setMsgUpInfo(msg.getMsgUpInfo());
        }

        msg = ActionConvertUtil.convert(msg);
        log.info("意图转换，转换后意图： {}", msg.getAction());
        List<BMsgKeywordPO> msgKeywordList = msgKeywordService.findByKeyworkAndServiceId(msg.getAction(), msgChatbot.getServiceId());
        if (Objects.isNull(msgKeywordList) || msgKeywordList.isEmpty()) {
            msg.setAction("未匹配到真实意图");
            msg.setMsgUpInfo(msg.getMsgUpInfo());
        }
        rocketMQTemplate.convertAndSend(QueueName.MSGCLOUD_MESSAGE, msg);
    }

    @Override
    public void onTemplateAuditResultNotify(AuditNotify auditNotify) {

    }

    @Override
    public void onMaterialAuditResultNotify(AuditNotify auditNotify) {

    }

}
