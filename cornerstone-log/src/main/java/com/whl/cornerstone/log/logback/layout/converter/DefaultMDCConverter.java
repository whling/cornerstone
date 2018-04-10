package com.whl.cornerstone.log.logback.layout.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.OptionHelper;
import com.whl.cornerstone.log.constants.LogConstants;

import java.util.Map;

/**
 * Created by whling on 2018/4/10.
 */
public class DefaultMDCConverter
        extends ClassicConverter {
    private String key;
    private String defaultValue = " ";

    public void start() {
        String[] keyInfo = OptionHelper.extractDefaultReplacement(getFirstOption());
        this.key = keyInfo[0];
        if (keyInfo[1] != null) {
            this.defaultValue = keyInfo[1];
        }
        super.start();
    }

    public void stop() {
        this.key = null;
        super.stop();
    }

    public String convert(ILoggingEvent event) {
        Map mdcPropertyMap = event.getMDCPropertyMap();
        if ((mdcPropertyMap == null) || (mdcPropertyMap.size() == 0)) {
            return this.defaultValue;
        }
        if (this.key == null) {
            return outputMDCForAllKeys(mdcPropertyMap);
        }
        String value = (String) event.getMDCPropertyMap().get(this.key);
        return value != null ? value : this.defaultValue;
    }

    private String outputMDCForAllKeys(Map<String, String> mdcPropertyMap) {
        StringBuilder buf = new StringBuilder(" ");
        for (String mdcKey : LogConstants.MDC_KEY_ARR) {
            Object mdcVal = mdcPropertyMap.get(mdcKey);
            if (mdcVal != null) {
                buf.append("[");
                buf.append(mdcKey);
                buf.append(":");
                buf.append(mdcVal.toString());
                buf.append("] ");
            }
        }
        return buf.toString();
    }
}