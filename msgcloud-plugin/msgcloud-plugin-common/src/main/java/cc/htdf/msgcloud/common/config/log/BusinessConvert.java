package cc.htdf.msgcloud.common.config.log;

import cc.htdf.msgcloud.common.config.LogConfig;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Objects;

/**
 * author: JT
 * date: 2020/4/22
 * title:
 */
public class BusinessConvert extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        if (Objects.isNull(iLoggingEvent.getMarker())
                || Objects.equals("", iLoggingEvent.getMarker().getName())) {
            return LogConfig.LOG_SYSTEM;
        }
        return iLoggingEvent.getMarker().getName();
    }
}
