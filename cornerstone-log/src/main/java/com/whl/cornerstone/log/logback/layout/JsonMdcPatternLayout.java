package com.whl.cornerstone.log.logback.layout;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.google.gson.JsonObject;
import com.whl.cornerstone.log.constants.AppIdLoader;
import com.whl.cornerstone.log.constants.LogConstants;
import com.whl.cornerstone.log.constants.LogType;
import com.whl.cornerstone.util.DateUtils;
import com.whl.cornerstone.util.IPUtils;
import com.whl.cornerstone.util.MapUtils;
import com.whl.cornerstone.util.StringUtils;
import com.whl.cornerstone.util.json.JsonUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by whling on 2018/4/10.
 */
public class JsonMdcPatternLayout
        extends LayoutBase<ILoggingEvent> {
    private static final String CURR_VERSION = "1.0";
    private int lengthOption = 10;

    public String doLayout(ILoggingEvent event) {
        JsonObject logJson = new JsonObject();
        StringBuilder exceptionlines = new StringBuilder(32);

        String projectName = AppIdLoader.getProjectName();
        addEventData(logJson, "timestamp",
                DateUtils.formatDateSSS(event.getTimeStamp()));
        addEventData(logJson, "level", event.getLevel().toString());
        addEventData(logJson, "logger", convertLogger(event.getLoggerName(), 2));
        String formattedMessage = event.getFormattedMessage();

        formattedMessage = formattedMessage.replace("\n", "\t");
        formattedMessage = formattedMessage.replace("\r", "\t");

        Map<String, String> mdcMap = event.getMDCPropertyMap();
        Object jsonToMap;
        if (MapUtils.isNotEmpty(mdcMap)) {
            for (String mdcKey :
                    LogConstants.MDC_KEY_ARR) {
                String mdcVal = (String) mdcMap.get(mdcKey);
                if ("logtype".equals(mdcKey)) {
                    addLogType(logJson, mdcVal, event.getLevel());
                } else if (StringUtils.isNotBlank(mdcVal)) {
                    addEventData(logJson, mdcKey, mdcVal);
                }
            }
            String ext = (String) mdcMap.get("_EXT_");
            if (StringUtils.isNotBlank(ext)) {
                jsonToMap = JsonUtils.jsonToMap(ext);
                Object keySet = ((Map) jsonToMap).keySet();
                for (String key : (Set<String>) keySet) {
                    String value = (String) ((Map) jsonToMap).get(key);
                    if (value != null) {
                        addEventData(logJson, key, value);
                    }
                }
            }
        } else {
            addLogType(logJson, null, event.getLevel());
        }
        addEventData(logJson, "logmsg", formattedMessage);

        IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            StackTraceElementProxy[] elementArray = tp.getStackTraceElementProxyArray();
            StringBuilder exceptionStr = new StringBuilder();
            ThrowableProxyUtil.subjoinFirstLine(exceptionStr, tp);
            if (elementArray != null) {
                exceptionStr.append("    ").append(elementArray[0].toString());
            }
            addEventData(logJson, "desc", exceptionStr.toString());
            subjoinThrowableProxy(exceptionlines, tp);
        }
        addEventData(logJson, "localip", IPUtils.LOCAL_IP);
        addEventData(logJson, "appid", projectName);
        addEventData(logJson, "version", "1.0");
        if (StringUtils.isNotBlank(exceptionlines.toString())) {
            return logJson.toString() + CoreConstants.LINE_SEPARATOR + exceptionlines.toString();
        }
        return logJson.toString() + CoreConstants.LINE_SEPARATOR;
    }

    private void addEventData(JsonObject jsonObject, String keyname, String keyval) {
        if (null != keyval) {
            jsonObject.addProperty(keyname, keyval);
        }
    }

    private void addLogType(JsonObject logJson, String keyval, Level level) {
        if (StringUtils.isNotBlank(keyval)) {
            addEventData(logJson, "logtype", keyval);
            return;
        }
        switch (level.toInt()) {
            case 40000:
                addEventData(logJson, "logtype", LogType.ERROR.getType());
                break;
            case 10000:
                addEventData(logJson, "logtype", LogType.DEBUG.getType());
                break;
            default:
                addEventData(logJson, "logtype", LogType.COMMON.getType());
        }
    }

    private String convertLogger(String fullClassName, int precision) {
        if (precision <= 0) {
            return fullClassName;
        }
        int len = fullClassName.length();
        int end = len - 1;
        for (int i = precision; i > 0; i--) {
            end = fullClassName.lastIndexOf('.', end - 1);
            if (end == -1) {
                return fullClassName;
            }
        }
        return fullClassName.substring(end + 1, len);
    }

    private void subjoinThrowableProxy(StringBuilder buf, IThrowableProxy tp) {
        ThrowableProxyUtil.subjoinFirstLine(buf, tp);
        buf.append(CoreConstants.LINE_SEPARATOR);
        StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        int commonFrames = tp.getCommonFrames();
        boolean unrestrictedPrinting = this.lengthOption > stepArray.length;
        int maxIndex = unrestrictedPrinting ? stepArray.length : this.lengthOption;
        if ((commonFrames > 0) && (unrestrictedPrinting)) {
            maxIndex -= commonFrames;
        }
        for (int i = 0; i < maxIndex; i++) {
            String string = stepArray[i].toString();
            buf.append('\t');
            buf.append(string);
            ThrowableProxyUtil.subjoinPackagingData(buf, stepArray[i]);
            buf.append(CoreConstants.LINE_SEPARATOR);
        }
        if ((commonFrames > 0) && (unrestrictedPrinting)) {
            buf.append("\t... ").append(tp.getCommonFrames()).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
        }
    }
}