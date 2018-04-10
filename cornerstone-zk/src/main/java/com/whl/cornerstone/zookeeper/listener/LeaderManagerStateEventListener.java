package com.whl.cornerstone.zookeeper.listener;

import com.whl.cornerstone.zookeeper.leader.LeaderManager;

/**
 * Created by whling on 2018/4/12.
 */
public class LeaderManagerStateEventListener
        extends AbstractStateListener {
    protected void doLost() {
        LeaderManager.getInstance().initZkConnect();
        LeaderManager.getInstance().initLeaderShipSelector();
    }
}