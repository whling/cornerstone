package com.whl.cornerstone.log.constants;

/**
 * Created by whling on 2018/4/10.
 */
public enum LogType {
    COMMON("0"), BIZ("1"), ERROR("2"), DEBUG("3"), IN_OUT("4"), STATISTICS("5"), ALARM("6");

    private String type;

    private LogType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String toString() {
        return "LogType{type='" + this.type + '\'' + '}';
    }
}