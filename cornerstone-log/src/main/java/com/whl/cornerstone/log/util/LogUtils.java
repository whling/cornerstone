package com.whl.cornerstone.log.util;

import com.whl.cornerstone.util.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by whling on 2018/4/10.
 */
public class LogUtils {

    private static final Logger perfLogger = LoggerFactory.getLogger("PERF");

    public static void dumpPerf(String dumpValue) {
        if (perfLogger.isInfoEnabled()) {
            perfLogger.info(dumpValue);
        }
    }

    public static void debug(Logger logger, String msg, Object... params) {
        if (logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(msg, params);
            logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }

    public static void info(Logger logger, String msg, Object... params) {
        if (logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(msg, params);
            logger.info(ft.getMessage(), ft.getThrowable());
        }
    }

    public static void warn(Logger logger, String msg, Object... params) {
        if (logger.isWarnEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(msg, params);
            logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }

    public static void error(Logger logger, String msg, Object... params) {
        if (logger.isErrorEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(msg, params);
            logger.error(ft.getMessage(), ft.getThrowable());
        }
    }

    public static void info(String metric, String errorcode, Logger logger, String msg, Object... params) {
        try {
            MDC.put("interface", metric);
            MDC.put("errorcode", errorcode);
            info(logger, msg, params);
        } finally {
            MDC.remove("interface");
            MDC.remove("errorcode");
        }
    }

    public static void warn(String metric, String errorcode, Logger logger, String msg, Object... params) {
        try {
            MDC.put("interface", metric);
            MDC.put("errorcode", errorcode);
            warn(logger, msg, params);
        } finally {
            MDC.remove("interface");
            MDC.remove("errorcode");
        }
    }

    public static void error(String metric, String errorcode, Logger logger, String msg, Object... params) {
        try {
            MDC.put("interface", metric);
            MDC.put("errorcode", errorcode);
            error(logger, msg, params);
        } finally {
            MDC.remove("interface");
            MDC.remove("errorcode");
        }
    }

    public static void debug(Logger logger, String msg, Map<String, String> extMap) {
        try {
            MDC.put("_EXT_", JsonUtils.objectToJsonStr(extMap));
            debug(logger, msg, new Object[0]);

            MDC.remove("_EXT_");
        } finally {
            MDC.remove("_EXT_");
        }
    }

    public static void info(Logger logger, String msg, Map<String, String> extMap) {
        try {
            MDC.put("_EXT_", JsonUtils.objectToJsonStr(extMap));
            info(logger, msg, new Object[0]);

            MDC.remove("_EXT_");
        } finally {
            MDC.remove("_EXT_");
        }
    }

    public static void warn(Logger logger, String msg, Map<String, String> extMap) {
        try {
            MDC.put("_EXT_", JsonUtils.objectToJsonStr(extMap));
            warn(logger, msg, new Object[0]);

            MDC.remove("_EXT_");
        } finally {
            MDC.remove("_EXT_");
        }
    }

    public static void error(Logger logger, String msg, Map<String, String> extMap) {
        try {
            MDC.put("_EXT_", JsonUtils.objectToJsonStr(extMap));
            error(logger, msg, new Object[0]);

            MDC.remove("_EXT_");
        } finally {
            MDC.remove("_EXT_");
        }
    }

    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        throwable.printStackTrace(pw);
        return writer.toString();
    }
}
