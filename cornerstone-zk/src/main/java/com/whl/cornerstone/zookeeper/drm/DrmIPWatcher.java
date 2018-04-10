package com.whl.cornerstone.zookeeper.drm;

import com.whl.cornerstone.util.ReflectionUtils;
import com.whl.cornerstone.util.StringUtils;
import com.whl.cornerstone.util.IPUtils;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by whling on 2018/4/12.
 */
public class DrmIPWatcher
        implements CuratorWatcher {
    private static Logger logger = LoggerFactory.getLogger("zookeeper-event");
    private DrmZookeeprClient client;
    private AppDrmNode drmNode;
    private boolean isroot;

    public DrmIPWatcher(DrmZookeeprClient client, AppDrmNode drmNode, boolean isroot) {
        this.client = client;
        this.drmNode = drmNode;
        this.isroot = isroot;
    }

    public void process(WatchedEvent event)
            throws Exception {
        logger.info(event.toString());
        if ((event.getState() == Watcher.Event.KeeperState.Disconnected) ||
                (event.getState() == Watcher.Event.KeeperState.Expired)) {
            return;
        }
        if (this.client == null) {
            return;
        }
        if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
            try {
                String path = event.getPath();
                String value = this.client.getCuratorClient().get(path);
                if (this.isroot) {
                    String ip = IPUtils.getRealIp();
                    String ippath = path + "/" + ip;
                    this.client.getCuratorClient().set(ippath, value);
                } else if (StringUtils.isNotBlank(value)) {
                    Object drmobj = this.drmNode.getObj();
                    String parmname = this.drmNode.getParmname();
                    ReflectionUtils.writeFieldWithSet(parmname, drmobj, value);
                    logger.info("path:{}, data:{}", path, value);
                }
            } finally {
                this.client.getCuratorClient().watch(event.getPath(), this);
            }
        }
    }
}
