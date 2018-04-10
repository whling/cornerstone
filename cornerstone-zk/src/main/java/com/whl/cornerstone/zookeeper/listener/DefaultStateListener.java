package com.whl.cornerstone.zookeeper.listener;

import com.whl.cornerstone.zookeeper.client.CuratorClient;

/**
 * Created by whling on 2018/4/12.
 */
public class DefaultStateListener
        extends AbstractStateListener {
    protected CuratorClient curatorClient;

    public DefaultStateListener(CuratorClient curatorClient) {
        this.curatorClient = curatorClient;
    }

    protected void doLost() {
        this.logger.info(" connection lost, waiting for reconnect");
        try {
            this.logger.info(" reiniting");
            this.curatorClient.destroy();
            this.curatorClient.init();
            this.logger.info(" reinited");
        } catch (Exception e) {
            this.logger.error("reinit Exception", e);
        }
    }
}
