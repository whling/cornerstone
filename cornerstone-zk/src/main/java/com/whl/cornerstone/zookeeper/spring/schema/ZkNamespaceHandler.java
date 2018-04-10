package com.whl.cornerstone.zookeeper.spring.schema;

import com.whl.cornerstone.zookeeper.client.CuratorClient;
import com.whl.cornerstone.zookeeper.drm.DrmZookeeprClient;
import com.whl.cornerstone.zookeeper.leader.LeaderManager;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by whling on 2018/4/12.
 */
public class ZkNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("client", new ZkBeanDefinitionParser(CuratorClient.class));
        registerBeanDefinitionParser("drm", new ZkBeanDefinitionParser(DrmZookeeprClient.class));
        registerBeanDefinitionParser("leader", new ZkBeanDefinitionParser(LeaderManager.class));
    }
}
