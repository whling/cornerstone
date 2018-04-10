package com.whl.cornerstone.log.logback.layout;

import ch.qos.logback.classic.pattern.*;
import ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import ch.qos.logback.core.pattern.color.RedCompositeConverter;
import ch.qos.logback.core.pattern.parser.Parser;
import com.whl.cornerstone.log.logback.layout.converter.DefaultMDCConverter;
import com.whl.cornerstone.log.logback.layout.converter.LoggerNameConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by whling on 2018/4/10.
 */
public class DefaultPatternLayout
        extends PatternLayoutBase<ILoggingEvent> {

    private static final String DEFAULT_PATTERN = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%p]%X[%c{2}] [%m]%n";
    private static final String HEADER_PREFIX = "#logback.classic pattern: ";
    private static final Map<String, String> defaultConverterMap = new HashMap();

    public DefaultPatternLayout() {
        setPattern("[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%p]%X[%c{2}] [%m]%n");
        this.postCompileProcessor = new EnsureExceptionHandling();
    }

    public Map<String, String> getDefaultConverterMap() {
        return defaultConverterMap;
    }

    public String doLayout(ILoggingEvent event) {
        return !isStarted() ? "" : writeLoopOnConverters(event);
    }

    protected String getPresentationHeaderPrefix() {
        return "#logback.classic pattern: ";
    }

    static {
        defaultConverterMap.putAll(Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);
        defaultConverterMap.put("d", DateConverter.class.getName());
        defaultConverterMap.put("p", LevelConverter.class.getName());
        defaultConverterMap.put("t", ThreadConverter.class.getName());
        defaultConverterMap.put("c", LoggerNameConverter.class.getName());
        defaultConverterMap.put("m", MessageConverter.class.getName());
        defaultConverterMap.put("L", LineOfCallerConverter.class.getName());
        defaultConverterMap.put("F", FileOfCallerConverter.class.getName());
        defaultConverterMap.put("X", DefaultMDCConverter.class.getName());
        defaultConverterMap.put("ex", ThrowableProxyConverter.class.getName());
        defaultConverterMap.put("n", LineSeparatorConverter.class.getName());
        defaultConverterMap.put("black", BlackCompositeConverter.class.getName());
        defaultConverterMap.put("red", RedCompositeConverter.class.getName());
        defaultConverterMap.put("highlight", HighlightingCompositeConverter.class.getName());
    }
}