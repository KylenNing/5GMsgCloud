package cc.htdf.msgcloud.common.config.log;

import cc.htdf.msgcloud.common.config.LogConfig;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import java.util.Objects;

/**
 * author: JT
 * date: 2020/8/6
 * title:
 */
public class LogMessageFilterConfig extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        Marker marker = iLoggingEvent.getMarker();
        if (Objects.isNull(marker) || Objects.equals("", marker.getName())) {
            return FilterReply.DENY;
        }
        if (Objects.equals(LogConfig.LOG_MESSAGE, marker.getName())) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }
}
