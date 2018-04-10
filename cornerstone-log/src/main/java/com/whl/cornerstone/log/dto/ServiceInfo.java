package com.whl.cornerstone.log.dto;

/**
 * Created by whling on 2018/4/10.
 */
public class ServiceInfo {
    private String userid;
    private String retcode;
    private String errorcode;
    private String requestbody;
    private String logmsg;
    private String result;
    private String remark;

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRetcode() {
        return this.retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getErrorcode() {
        return this.errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getRequestbody() {
        return this.requestbody;
    }

    public void setRequestbody(String requestbody) {
        this.requestbody = requestbody;
    }

    public String getLogmsg() {
        return this.logmsg;
    }

    public void setLogmsg(String logmsg) {
        this.logmsg = logmsg;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

