package com.whl.cornerstone.zookeeper.leader.event;

import com.whl.cornerstone.zookeeper.leader.LeaderManager;

/**
 * Created by whling on 2018/4/12.
 */
public class LeaderQuitEvent
        implements LeaderEvent {
    public void execute()
            throws Exception {
        LeaderManager.getInstance().getPathChildrenCache().getListenable().clear();
        LeaderManager.getInstance().deleteAllTask();
        LeaderManager.getInstance().setLeader(false);
    }
}