package com.whl.cornerstone.zookeeper.client;

import com.whl.cornerstone.zookeeper.listener.AbstractStateListener;
import com.whl.cornerstone.zookeeper.listener.DefaultStateListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.api.BackgroundPathAndBytesable;
import org.apache.curator.framework.api.BackgroundPathable;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by whling on 2018/4/12.
 */
public class CuratorClient {
    private Logger logger = LoggerFactory.getLogger(CuratorClient.class);
    private String zkAddress;
    private String namespace;
    private int retryNum = 5;
    private int timeout = 10000;
    private AbstractStateListener stateListener;
    private CuratorFramework curator;

    public void init()
            throws InterruptedException {
        this.curator = CuratorFrameworkFactory.builder().connectString(this.zkAddress).namespace(this.namespace).retryPolicy(new RetryNTimes(this.retryNum, 1000)).connectionTimeoutMs(this.timeout).build();
        CuratorFrameworkState curState = this.curator.getState();
        if (curState == CuratorFrameworkState.LATENT) {
            if (this.stateListener == null) {
                this.stateListener = new DefaultStateListener(this);
            }
            addListener(this.stateListener);
            this.curator.start();
            while (!this.curator.getZookeeperClient().isConnected()) {
                Thread.sleep(200L);
            }
        }
        this.logger.warn("!!!ERROR STATE!!!");
    }

    public void init(String namespace, String address, int timeout, AbstractStateListener stateListener)
            throws Exception {
        this.namespace = namespace;
        this.zkAddress = address;
        this.timeout = timeout;
        this.stateListener = stateListener;
        init();
    }

    public Stat stat(String path)
            throws Exception {
        return (Stat) this.curator.checkExists().forPath(path);
    }

    public boolean exists(String path)
            throws Exception {
        Stat serverStat = stat(path);
        if (serverStat == null) {
            return false;
        }
        return true;
    }

    public void create(String path, CreateMode mode)
            throws Exception {
        create(path, mode, "");
    }

    public void create(String path, CreateMode mode, String value)
            throws Exception {
        ((BackgroundPathAndBytesable) ((ACLBackgroundPathAndBytesable) this.curator.create().creatingParentsIfNeeded().withMode(mode)).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)).forPath(path, value.getBytes(Charset.forName("utf-8")));
    }

    public void delete(String path)
            throws Exception {
        this.curator.delete().forPath(path);
    }

    public void set(String path, String value)
            throws Exception {
        this.curator.setData().forPath(path, value.getBytes(Charset.forName("utf-8")));
    }

    public String get(String path)
            throws Exception {
        byte[] buffer = (byte[]) this.curator.getData().forPath(path);
        return new String(buffer);
    }

    public String watch(String path, CuratorWatcher watcher)
            throws Exception {
        byte[] buffer = (byte[]) ((BackgroundPathable) this.curator.getData().usingWatcher(watcher)).forPath(path);
        return new String(buffer);
    }

    public PathChildrenCache createPathChildrenCache(String registryPath)
            throws Exception {
        return createPathChildrenCache(registryPath, true);
    }

    public PathChildrenCache createPathChildrenCache(String registryPath, boolean isCacheData)
            throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(this.curator, registryPath, isCacheData);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        return childrenCache;
    }

    public List<String> children(String path)
            throws Exception {
        return (List) this.curator.getChildren().forPath(path);
    }

    public CuratorFramework curator() {
        return this.curator;
    }

    public void addListener(ConnectionStateListener stateListener) {
        this.curator.getConnectionStateListenable().addListener(stateListener);
    }

    public void removeListener(ConnectionStateListener stateListener) {
        this.curator.getConnectionStateListenable().removeListener(stateListener);
    }

    private void close()
            throws Exception {
        if (this.curator != null) {
            removeListener(this.stateListener);
            CuratorFrameworkState curState = this.curator.getState();
            if (curState != CuratorFrameworkState.STOPPED) {
                this.curator.close();
            }
            this.curator = null;
        }
    }

    public void destroy() {
        try {
            synchronized (this) {
                close();
            }
        } catch (Exception localException) {
        }
    }

    public void initCurator()
            throws Exception {
        init();
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public CuratorFramework getCurator() {
        return this.curator;
    }

    public AbstractStateListener getStateListener() {
        return this.stateListener;
    }

    public void setStateListener(AbstractStateListener stateListener) {
        this.stateListener = stateListener;
    }
}