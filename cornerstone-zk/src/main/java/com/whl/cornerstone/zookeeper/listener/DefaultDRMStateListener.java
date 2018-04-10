package com.whl.cornerstone.zookeeper.listener;

import com.whl.cornerstone.zookeeper.drm.DrmZookeeprClient;

/**
 * Created by whling on 2018/4/12.
 */
public class DefaultDRMStateListener
        extends AbstractStateListener {
    protected void doLost() {
        this.logger.info(" connection lost, waiting for reconnect");
        try {
            this.logger.info(" re-initing");
            DrmZookeeprClient.getDrmZookeeprClient().reinit();
            this.logger.info(" re-inited");
        } catch (Exception e) {
            this.logger.error("re-init Exception", e);
        }
    }
}