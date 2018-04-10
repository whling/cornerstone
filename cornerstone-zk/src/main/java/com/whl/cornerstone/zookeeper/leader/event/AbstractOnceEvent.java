package com.whl.cornerstone.zookeeper.leader.event;

/**
 * Created by whling on 2018/4/12.
 */
public abstract class AbstractOnceEvent
        implements LeaderEvent {
    protected String nodeName;
    protected Object object;

    public AbstractOnceEvent(String nodeName, Object object) {
        this.nodeName = nodeName;
        this.object = object;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}