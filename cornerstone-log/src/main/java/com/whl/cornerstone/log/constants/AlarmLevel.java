package com.whl.cornerstone.log.constants;

/**
 * Created by whling on 2018/4/10.
 */
public enum AlarmLevel {

    WARNNING("1"), ERROR("2"), FATAL("3");

    private String level;

    private AlarmLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }

    public String toString() {
        return "AlarmLevel{level='" + this.level + '\'' + '}';
    }
}
