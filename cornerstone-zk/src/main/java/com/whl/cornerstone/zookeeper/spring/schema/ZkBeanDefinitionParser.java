package com.whl.cornerstone.zookeeper.spring.schema;

import com.whl.cornerstone.util.StringUtils;
import com.whl.cornerstone.zookeeper.client.CuratorClient;
import com.whl.cornerstone.zookeeper.drm.DrmZookeeprClient;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by whling on 2018/4/12.
 */
public class ZkBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private final Class<?> beanClass;

    public ZkBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(this.beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");
        if (StringUtils.isBlank(id)) {
            String generatedBeanName = element.getAttribute("name");
            if (StringUtils.isBlank(generatedBeanName)) {
                generatedBeanName = this.beanClass.getName();
            }
            id = generatedBeanName;
            int counter = 2;
            while (parserContext.getRegistry().containsBeanDefinition(id)) {
                id = generatedBeanName + counter++;
            }
        }
        if ((id != null) && (id.length() > 0)) {
            if (parserContext.getRegistry().containsBeanDefinition(id)) {
                throw new IllegalStateException("Duplicate spring bean id " + id);
            }
            parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        }
        if (CuratorClient.class.equals(this.beanClass)) {
            String zkAddress = element.getAttribute("zkAddress");
            String namespace = element.getAttribute("namespace");
            String retryNum = element.getAttribute("retryNum");
            String timeout = element.getAttribute("timeout");
            beanDefinition.getPropertyValues().addPropertyValue("zkAddress", zkAddress);
            beanDefinition.getPropertyValues().addPropertyValue("namespace", namespace);
            beanDefinition.getPropertyValues().addPropertyValue("retryNum", retryNum);
            beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
            String stateListener = element.getAttribute("stateListener");
            if (StringUtils.isNotBlank(stateListener)) {
                beanDefinition.getPropertyValues().addPropertyValue("stateListener", new RuntimeBeanReference(stateListener));
            }
            beanDefinition.setInitMethodName("initCurator");
            beanDefinition.setDestroyMethodName("destroy");
            beanDefinition.isEnforceInitMethod();
            beanDefinition.isEnforceDestroyMethod();
        } else if (DrmZookeeprClient.class.equals(this.beanClass)) {
            String zkAddress = element.getAttribute("zkAddress");
            String timeout = element.getAttribute("timeout");
            String appName = element.getAttribute("appName");
            String stateListener = element.getAttribute("stateListener");
            beanDefinition.getPropertyValues().addPropertyValue("zkAddress", zkAddress);
            beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
            beanDefinition.getPropertyValues().addPropertyValue("appName", appName);
            if (StringUtils.isNotBlank(stateListener)) {
                beanDefinition.getPropertyValues().addPropertyValue("stateListener", new RuntimeBeanReference(stateListener));
            }
        }
        return beanDefinition;
    }
}