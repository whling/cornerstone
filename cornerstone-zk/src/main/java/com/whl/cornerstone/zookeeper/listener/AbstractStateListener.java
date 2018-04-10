package com.whl.cornerstone.zookeeper.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by whling on 2018/4/12.
 */
public abstract class AbstractStateListener
        implements ConnectionStateListener {

    protected Logger logger = LoggerFactory.getLogger("zookeeper-event");

    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        switch (connectionState.ordinal()) {
            case 1:
                doConnected();
                break;
            case 2:
                doLost();
                break;
            case 3:
                doReconected();
                break;
            case 4:
                doSuspended();
                break;
            case 5:
                doReadOnly();
        }
    }

    protected abstract void doLost();

    protected void doConnected() {
        this.logger.info("connection established");
    }

    protected void doSuspended() {
        this.logger.info("suspended event");
    }

    protected void doReconected() {
        this.logger.info("reconected event");
    }

    protected void doReadOnly() {
        this.logger.info("readonly event");
    }
}