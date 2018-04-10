package com.whl.cornerstone.zookeeper.leader;

import com.whl.cornerstone.zookeeper.leader.event.LeaderBecomeEvent;
import com.whl.cornerstone.zookeeper.leader.event.LeaderQuitEvent;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by whling on 2018/4/12.
 */
public class LeaderShipSelector
        extends LeaderSelectorListenerAdapter
        implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderShipSelector.class);
    private final LeaderSelector leaderSelector;
    private volatile boolean isLeader = false;
    private volatile boolean isClosed = false;
    LeaderManager leaderManager;
    String selectorPath;
    String appUUID;

    public LeaderShipSelector(CuratorFramework client, String path, String appUUID, LeaderManager leaderManager) {
        this.leaderSelector = new LeaderSelector(client, path, this);
        this.leaderManager = leaderManager;
        this.leaderSelector.autoRequeue();
        this.selectorPath = path;
        this.appUUID = appUUID;
    }

    public void start()
            throws IOException {
        this.leaderSelector.start();
    }

    public void close()
            throws IOException {
        this.isClosed = true;
        this.isLeader = false;
        if (null != this.leaderSelector) {
            this.leaderSelector.close();
        }
    }

    public void takeLeadership(CuratorFramework client)
            throws Exception {
        if (this.isClosed) {
            return;
        }
        LOGGER.info("Leader is :" + this.appUUID);
        this.isLeader = true;
        this.leaderManager.addEvent(new LeaderBecomeEvent());
        while (this.isLeader) {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        LOGGER.info("释放领导权");
    }

    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        LOGGER.info("Connection State :" + newState);
        if (((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) &&
                (this.isLeader)) {
            this.leaderManager.addEvent(new LeaderQuitEvent());
        }
    }
}