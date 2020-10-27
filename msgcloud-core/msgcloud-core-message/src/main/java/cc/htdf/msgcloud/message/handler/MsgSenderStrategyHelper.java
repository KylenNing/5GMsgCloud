package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.message.handler.sender.MsgSenderMobile;
import cc.htdf.msgcloud.message.handler.sender.MsgSenderUltra;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 */
@Component
public class MsgSenderStrategyHelper {

    @Resource
    private MsgSenderMobile msgSenderMobile;

    @Resource
    private MsgSenderUltra msgSenderUltra;

    private Map<String, MsgSender> senderStrategyMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        this.senderStrategyMap.put(ChannelBusiness.CHANNEL_MOBILE, msgSenderMobile);
        this.senderStrategyMap.put(ChannelBusiness.CHANNLE_ULTRA, msgSenderUltra);
    }

    public MsgSender fetchStrategy(String serviceProvider) {
        MsgSender sender = this.senderStrategyMap.get(serviceProvider);
        if (Objects.isNull(sender)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到发送者策略[{}]", serviceProvider);
        }
        return sender;
    }

}
