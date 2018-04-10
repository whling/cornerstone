package com.whl.cornerstone.zookeeper.leader.event;

/**
 * Created by whling on 2018/4/12.
 */
public abstract interface LeaderEvent {
    public abstract void execute()
            throws Exception;
}
