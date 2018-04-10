package com.whl.cornerstone.log.constants;

/**
 * Created by whling on 2018/4/10.
 */
public class LogConstants {
    public static final String COMMON_PERF = "PERF";
    public static final String RPC_EVENT = "rpc-event";
    public static final String METAQ_EVENT = "metaq-event";
    public static final String METAQ_RECEIVE = "metaq-receiver-digest";
    public static final String METAQ_FINISH = "metaq-finish-digest";
    public static final String REDIS_EVENT = "redis-event";
    public static final String ZK_EVENT = "zookeeper-event";
    public static final String REFLECTION_EVENT = "reflection-event";
    public static final String NETTY_EVENT = "netty-event";
    public static final String SOCKETCLI_EVENT = "socketcli-event";
    public static final String WEB_EVENT = "web-event";
    public static final String[] MDC_KEY_ARR = {"traceid", "remoteip", "upstramip", "interface", "begintime", "endtime", "costtime", "logmsg", "remark", "requestbody", "result", "retcode", "errorcode", "logtype", "userid"};
}