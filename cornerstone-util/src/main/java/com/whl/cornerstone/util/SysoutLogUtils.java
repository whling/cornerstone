package com.whl.cornerstone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by whling on 2018/4/10.
 */
public class SysoutLogUtils {

    private static Logger logger = LoggerFactory.getLogger(SysoutLogUtils.class);

    public static void debug(String msg) {
        if (logger.isDebugEnabled())
            colorMsg("CORNERSTONE:DEBUG " + msg, COLOR.WHITE);
    }

    public static void info(String msg) {
        if (logger.isInfoEnabled())
            colorMsg("CORNERSTONE:INFO  " + msg, COLOR.BLACK);
    }

    public static void warn(String msg) {
        if (logger.isWarnEnabled())
            colorMsg("CORNERSTONE:WARN  " + msg, COLOR.BOLD_YELLOW);
    }

    public static void error(String msg, Throwable e) {
        if (logger.isErrorEnabled()) {
            colorMsg("CORNERSTONE:ERROR " + msg, COLOR.BOLD_RED);
            e.printStackTrace();
        }
    }

    public static void colorMsg(String msg, COLOR color) {
        StringBuilder sb = new StringBuilder();
        sb.append("\033[");
        sb.append(color.colorNum);
        sb.append("m");
        sb.append(msg);
        sb.append("\033[0;39m");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BLACK);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_BLACK);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.RED);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_RED);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.GREEN);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_GREEN);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.YELLOW);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_YELLOW);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BLUE);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_BLUE);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.MAGENTA);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_MAGENTA);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.CYAN);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_CYAN);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.WHITE);
            colorMsg("CORNERSTONE:ERROR vgyfguuhvhvhjv", COLOR.BOLD_WHITE);
        }
    }

    static enum COLOR {
        BLACK("30"),
        BOLD_BLACK("1;30"),
        RED("31"),
        BOLD_RED("1;31"),
        GREEN("32"),
        BOLD_GREEN("1;32"),
        YELLOW("33"),
        BOLD_YELLOW("1;33"),
        BLUE("34"),
        BOLD_BLUE("1;34"),
        MAGENTA("35"),
        BOLD_MAGENTA("1;35"),
        CYAN("36"),
        BOLD_CYAN("1;36"),
        WHITE("37"),
        BOLD_WHITE("1;37");

        private String colorNum;

        private COLOR(String colorNum) {
            this.colorNum = colorNum;
        }
    }
}
