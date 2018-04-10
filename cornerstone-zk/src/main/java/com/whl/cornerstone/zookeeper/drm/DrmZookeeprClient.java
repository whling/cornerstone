package com.whl.cornerstone.zookeeper.drm;

import com.whl.cornerstone.util.IPUtils;
import com.whl.cornerstone.util.ReflectionUtils;
import com.whl.cornerstone.util.StringUtils;
import com.whl.cornerstone.zookeeper.client.CuratorClient;
import com.whl.cornerstone.zookeeper.drm.annotation.DRM;
import com.whl.cornerstone.zookeeper.listener.AbstractStateListener;
import com.whl.cornerstone.zookeeper.listener.DefaultDRMStateListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Created by whling on 2018/4/12.
 */
public class DrmZookeeprClient
        implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, InitializingBean, DisposableBean {
    private static Logger logger = LoggerFactory.getLogger("zookeeper-event");
    private CuratorClient curatorClient;
    private static final String namespace = "DRM";
    private String ip;
    private String zkAddress;
    private int timeout = 10000;
    private String appName;
    private AbstractStateListener listener;
    private Set<AppDrmNode> confSet = new CopyOnWriteArraySet();
    public List<AppDrmNode> appDrmNodes = new ArrayList();
    private ApplicationContext applicationContext;
    private static DrmZookeeprClient drmZookeeprClient;
    private static boolean inited = false;

    public void init()
            throws Exception {
        this.ip = IPUtils.getRealIp();
        if ("127.0.0.1".equals(this.ip)) {
            throw new RuntimeException("不允许使用本地环回地址");
        }
        this.curatorClient = new CuratorClient();
        if (this.listener == null) {
            this.listener = new DefaultDRMStateListener();
        }
        this.curatorClient.init("DRM", this.zkAddress, this.timeout, this.listener);
        drmZookeeprClient = this;
    }

    public boolean confRegist(AppDrmNode drmNode) {
        return confRegist(drmNode, true);
    }

    private void confRegist() {
        logger.info("register appNode when startup.");
        if ((this.appDrmNodes != null) && (this.appDrmNodes.size() > 0)) {
            for (AppDrmNode drmNode : this.appDrmNodes) {
                confRegist(drmNode, true);
            }
        }
    }

    public void clearNodeData(String nodeName) {
        for (AppDrmNode appDrmNode : this.confSet) {
            if (nodeName.equals(appDrmNode.getParmname())) {
                String path = this.appName + "/" + appDrmNode.getClassname() + "." + appDrmNode.getParmname();
                try {
                    this.curatorClient.set(path, "");
                } catch (Exception e) {
                    logger.error("drm节点值清理", e);
                }
            }
        }
    }

    public boolean confRegist(AppDrmNode drmNode, boolean addset) {
        String configKey = drmNode.getConfigname();
        if (StringUtils.isBlank(configKey)) {
            configKey = drmNode.getClassname() + "." + drmNode.getParmname();
        }
        String path = "/" + this.appName + "/" + configKey;
        String ippath = path + "/" + this.ip;
        if ((addset) && (this.confSet.contains(drmNode))) {
            logger.error("重复注册节点:{}", drmNode);
            return false;
        }
        try {
            if (!this.curatorClient.exists(path)) {
                this.curatorClient.create(path, CreateMode.PERSISTENT);
                this.curatorClient.set(path, drmNode.getValue());
            }
            this.curatorClient.watch(path, new DrmIPWatcher(this, drmNode, true));


            String ippathValue = this.curatorClient.get(path);
            if (this.curatorClient.exists(ippath)) {
                if (!drmNode.isOverwrite()) {
                    ippathValue = this.curatorClient.get(ippath);
                }
                this.curatorClient.delete(ippath);
            }
            if (drmNode.isPersistent()) {
                this.curatorClient.create(ippath, CreateMode.PERSISTENT, ippathValue);
            } else {
                this.curatorClient.create(ippath, CreateMode.EPHEMERAL, ippathValue);
            }
            if (!ippathValue.equals(drmNode.getValue())) {
                ReflectionUtils.writeFieldWithSet(drmNode.getParmname(), drmNode.getObj(), ippathValue);

                drmNode.setValue(ippathValue);
            }
            this.curatorClient.watch(ippath, new DrmIPWatcher(this, drmNode, false));
            if (addset) {
                this.confSet.add(drmNode);
            }
        } catch (Exception ex) {
            logger.error("注册drm异常", ex);
            return false;
        }
        return true;
    }

    public void reinit() {
        try {
            unregister();
            init();
            for (AppDrmNode conf : this.confSet) {
                confRegist(conf, false);
            }
            registerSucceed();
        } catch (Exception e) {
            logger.error("重新初始化异常！", e);
        }
    }

    private void unregister()
            throws Exception {
        try {
            this.curatorClient.removeListener(this.listener);
            this.listener = null;
            this.curatorClient.destroy();
            this.curatorClient = null;
        } catch (Exception e) {
            logger.warn("unregister failed");
            throw e;
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!inited) {
            synchronized (DrmZookeeprClient.class) {
                if (!inited) {
                    Map<String, DynamicResource> beans = this.applicationContext.getBeansOfType(DynamicResource.class);
                    for (DynamicResource dr : beans.values()) {
                        for (Field field : dr.getClass().getDeclaredFields()) {
                            DRM drm = (DRM) field.getAnnotation(DRM.class);
                            if (drm != null) {
                                field.setAccessible(true);
                                String fieldName = field.getName();
                                String value = (String) ReflectionUtils.getFieldValue(dr, fieldName);
                                if (StringUtils.isBlank(value)) {
                                    value = "";
                                }
                                AppDrmNode drmNode = new AppDrmNode(dr, fieldName, value);
                                drmNode.setConfigname(drm.key());
                                drmNode.setPersistent(drm.persistent());
                                drmNode.setOverwrite(drm.overwrite());
                                this.appDrmNodes.add(drmNode);
                            }
                        }
                    }
                    confRegist();
                    registerSucceed();
                    inited = true;
                }
            }
        }
    }

    private void registerSucceed() {
        String path = "/" + this.appName + "/drm.alive";
        try {
            if (!this.curatorClient.exists(path)) {
                this.curatorClient.create(path, CreateMode.PERSISTENT, "drm node is alive");
            }
            String ippath = path + "/" + this.ip;
            if (this.curatorClient.exists(ippath)) {
                this.curatorClient.delete(ippath);
            }
            this.curatorClient.create(ippath, CreateMode.EPHEMERAL, "true");
        } catch (Exception e) {
            logger.error("创建DRM状态节点失败", e);
        }
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public CuratorClient getCuratorClient() {
        return this.curatorClient;
    }

    public void setCuratorClient(CuratorClient curatorClient) {
        this.curatorClient = curatorClient;
    }

    public void afterPropertiesSet()
            throws Exception {
        init();
        try {
            if (!this.curatorClient.exists(this.appName)) {
                this.curatorClient.create(this.appName, CreateMode.PERSISTENT);
                this.curatorClient.set(this.appName, "");
            }
        } catch (Exception localException) {
        }
    }

    public static DrmZookeeprClient getDrmZookeeprClient() {
        return drmZookeeprClient;
    }

    public void destroy()
            throws Exception {
        this.curatorClient.destroy();
    }

    public String getZkAddress() {
        return this.zkAddress;
    }

    public String getAppName() {
        return this.appName;
    }
}