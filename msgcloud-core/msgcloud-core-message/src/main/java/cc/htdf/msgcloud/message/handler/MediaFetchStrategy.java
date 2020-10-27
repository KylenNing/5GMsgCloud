package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.constants.ChannelBusiness;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.message.handler.mediafetch.MediaFetchMobile;
import cc.htdf.msgcloud.message.handler.mediafetch.MediaFetchUltra;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
@Component
public class MediaFetchStrategy {

    @Resource
    private MediaFetchMobile mediaFetchMobile;

    @Resource
    private MediaFetchUltra mediaFetchUltra;

    private Map<String, MediaFetch> mediaFetchMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        this.mediaFetchMap.put(ChannelBusiness.CHANNLE_ULTRA, mediaFetchUltra);
        this.mediaFetchMap.put(ChannelBusiness.CHANNEL_MOBILE, mediaFetchMobile);
    }

    public MediaFetch getStrategy(String channelBusiness) {
        MediaFetch mediaFetch = mediaFetchMap.get(channelBusiness);
        if (Objects.isNull(mediaFetch)) {
            throw new BusinessException(ExceptionCode.ERROR, "未找到媒体获取策略[{}]", channelBusiness);
        }
        return mediaFetch;
    }
}
