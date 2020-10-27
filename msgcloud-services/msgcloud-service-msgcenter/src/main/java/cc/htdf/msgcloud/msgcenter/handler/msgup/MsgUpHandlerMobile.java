package cc.htdf.msgcloud.msgcenter.handler.msgup;

import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.constants.MsgOperation;
import cc.htdf.msgcloud.common.constants.MsgType;
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
@Component(MsgUpStrategyConstant.MSGUP_HANDLER_MOBILE)
public class MsgUpHandlerMobile implements MsgHandler {

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
        msg.setChannelBusiness(ChannelBusiness.CHANNEL_MOBILE);
        msg.setOperation(MsgOperation.MSG_UP);
        msg.setType(MsgType.AUTO_REPLAY);
        MsgChatbot msgChatbot = msg.getMsgChatbot();

        MsgUpInfo msgUpInfo = msg.getMsgUpInfo();
        String text = msgUpInfo.getText().trim();
        String contentType = msgUpInfo.getContentType();
        msg.getMsgUpInfo().setChannelId(Long.valueOf(msgChatbot.getChannelId()));

        boolean isUseNlp = true;
        if (contentType.contains("text") || contentType.contains("json")) {
            List<BMsgKeywordPO> msgKeywordList = msgKeywordService.findByKeyworkAndServiceId(text, msgChatbot.getServiceId());
            if (!Objects.isNull(msgKeywordList) && !msgKeywordList.isEmpty()) {
                isUseNlp = false;
            }
        }
        if (isUseNlp) {
            try {
                msg = msgNlpStrategyHelper.nlpAnalysisMsg(msg);
            } catch (Exception e) {
                log.error("使用NLP解析上行信息失败, 信息：{}, 异常：{}", msg.getMsgUpInfo().getText(), e.getMessage());
                msg.setAction("未匹配到真实意图");
                msg.setMsgUpInfo(msg.getMsgUpInfo());
            }
        } else {
            msg.setAction(text);
            msg.setMsgUpInfo(msg.getMsgUpInfo());
        }
        if (Objects.isNull(msg) || Objects.isNull(msg.getAction())) {
            log.info("NLP未解析到当前上行消息的真实意图！[{}]", JSONObject.toJSONString(msgUpInfo));
            msg.setAction("未匹配到真实意图");
            msg.setMsgUpInfo(msg.getMsgUpInfo());
        }
        msg = ActionConvertUtil.convert(msg);
        log.info("意图转换，转换后意图： {}", msg.getAction());
        rocketMQTemplate.convertAndSend(QueueName.MSGCLOUD_MESSAGE, msg);
    }

    @Override
    public void onTemplateAuditResultNotify(AuditNotify auditNotify) {

    }

    @Override
    public void onMaterialAuditResultNotify(AuditNotify auditNotify) {

    }
}
