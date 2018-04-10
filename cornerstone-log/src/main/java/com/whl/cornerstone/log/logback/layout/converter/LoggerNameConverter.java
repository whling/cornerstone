package com.whl.cornerstone.log.logback.layout.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by whling on 2018/4/10.
 */
public class LoggerNameConverter
        extends ClassicConverter {
    public String convert(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        String optStr = getFirstOption();
        if (optStr != null) {
            int precision = Integer.parseInt(optStr);
            if (precision <= 0) {
                return loggerName;
            }
            int len = loggerName.length();
            int end = len - 1;
            for (int i = precision; i > 0; i--) {
                end = loggerName.lastIndexOf('.', end - 1);
                if (end == -1) {
                    return loggerName;
                }
            }
            return loggerName.substring(end + 1, len);
        }
        return loggerName;
    }
}