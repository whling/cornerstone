package com.whl.cornerstone.zookeeper.leader;

import com.whl.cornerstone.util.GuidUtils;
import com.whl.cornerstone.util.StringUtils;
import com.whl.cornerstone.zookeeper.client.CuratorClient;
import com.whl.cornerstone.zookeeper.drm.DrmZookeeprClient;
import com.whl.cornerstone.zookeeper.leader.event.AbstractOnceEvent;
import com.whl.cornerstone.zookeeper.leader.event.LeaderBecomeEvent;
import com.whl.cornerstone.zookeeper.leader.event.LeaderEvent;
import com.whl.cornerstone.zookeeper.leader.proxy.OnceEventJDKProxy;
import com.whl.cornerstone.zookeeper.listener.LeaderManagerStateEventListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by whling on 2018/4/12.
 */
public class LeaderManager
        extends Thread
        implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderManager.class);
    private volatile boolean running = false;
    private volatile boolean isLeader = false;
    private BlockingDeque<LeaderEvent> leaderEventQueue = new LinkedBlockingDeque();
    private static LeaderManager leaderManager;
    private PathChildrenCache pathChildrenCache;
    private LeaderShipSelector leaderShipSelector;
    private String zkAddress = DrmZookeeprClient.getDrmZookeeprClient()
            .getZkAddress();
    private final String NAMESPACE = "leadermanager";
    private String appName = DrmZookeeprClient.getDrmZookeeprClient()
            .getAppName();
    private String leaderPath;
    private String registryPath;
    public static final String CURATOR_UUID = GuidUtils.getNextUid("Leader");
    private CuratorClient curatorClient;

    private void startManager()
            throws Exception {
        LOGGER.info("Leader manager 启动");
        leaderManager = this;

        initZkConnect();
        LOGGER.info("Leader manager 与zookeeper建立连接,[zkaddress={}]", this.zkAddress);

        createPathChildrenCache();

        createPath();

        initLeaderShipSelector();

        setRunning(true);
        super.setDaemon(true);
        super.start();
    }

    private String getLeaderPath() {
        if (StringUtils.isBlank(this.leaderPath)) {
            this.leaderPath = ("/" + this.appName + "/leader");
        }
        return this.leaderPath;
    }

    private String getRegistryPath() {
        if (StringUtils.isBlank(this.registryPath)) {
            this.registryPath = ("/" + this.appName + "/application");
        }
        return this.registryPath;
    }

    public void createPath() {
        try {
            this.curatorClient.create(getRegistryPath() + "/" + CURATOR_UUID, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void createPathChildrenCache()
            throws Exception {
        try {
            this.pathChildrenCache = this.curatorClient.createPathChildrenCache(getRegistryPath(), true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void initZkConnect() {
        try {
            this.curatorClient = new CuratorClient();
            this.curatorClient.init("leadermanager", this.zkAddress, 3000, new LeaderManagerStateEventListener());
        } catch (Exception e) {
            LOGGER.info("与zookeeper建立连接失败", e);
        }
    }

    public void initLeaderShipSelector() {
        this.leaderShipSelector = new LeaderShipSelector(this.curatorClient.getCurator(), getLeaderPath(), CURATOR_UUID, this);
        try {
            this.leaderShipSelector.start();
        } catch (Exception e) {
            LOGGER.error("leadershipselector初始化异常！");
        }
    }

    public void run() {
        while (isRunning()) {
            try {
                LeaderEvent currentEvent = (LeaderEvent) this.leaderEventQueue.take();
                if (null == currentEvent) {
                    TimeUnit.SECONDS.sleep(1L);
                } else if (((currentEvent instanceof LeaderBecomeEvent)) || ((this.isLeader) && (!(currentEvent instanceof AbstractOnceEvent)))) {
                    currentEvent.execute();
                } else {
                    OnceEventJDKProxy proxy = new OnceEventJDKProxy();
                    LeaderEvent eventProxy = (LeaderEvent) proxy.bind(currentEvent);
                    eventProxy.execute();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static LeaderManager getInstance() {
        return leaderManager == null ? new LeaderManager() : leaderManager;
    }

    private LeaderManager() {
        try {
            startManager();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteAllTask() {
    }

    public boolean isLeader() {
        return this.isLeader;
    }

    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void addEvent(LeaderEvent event) {
        if ((this.isLeader) || ((event instanceof LeaderBecomeEvent))) {
            try {
                this.leaderEventQueue.put(event);
            } catch (InterruptedException localInterruptedException) {
            }
        }
    }

    public CuratorClient getTradeCuratorClient() {
        return this.curatorClient;
    }

    public void close() {
        LOGGER.info("关闭leader管理器");
        stopThread();
        try {
            if (null != this.pathChildrenCache) {
                this.pathChildrenCache.close();
            }
            if (null != this.leaderShipSelector) {
                this.leaderShipSelector.close();
            }
            if (null != this.curatorClient) {
                this.curatorClient.destroy();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void stopThread() {
        setRunning(false);
        while (super.isAlive()) {
            try {
                Thread.sleep(5L);
            } catch (Exception localException) {
            }
        }
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public PathChildrenCache getPathChildrenCache() {
        return this.pathChildrenCache;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void clearDrmData(String nodeName) {
        DrmZookeeprClient.getDrmZookeeprClient().clearNodeData(nodeName);
    }
}