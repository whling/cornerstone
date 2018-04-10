package com.whl.cornerstone.cache.spring.schema;

import com.whl.cornerstone.cache.redis.RedisClient;
import com.whl.cornerstone.cache.redis.RedisTemplate;
import com.whl.cornerstone.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by whling on 2018/4/10.
 */
public class CacheBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private final Class<?> beanClass;

    public CacheBeanDefinitionParser(Class<?> beanClass) {
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
        if (RedisClient.class.equals(this.beanClass)) {
            String type = element.getAttribute("type");
            if ((StringUtils.isBlank(type)) || (type.equals("redis"))) {
                RootBeanDefinition config = new RootBeanDefinition();
                config.setBeanClass(JedisPoolConfig.class);
                config.setLazyInit(false);
                String configId = JedisPoolConfig.class.getName();
                int counter = 2;
                while (parserContext.getRegistry().containsBeanDefinition(configId)) {
                    configId = configId + counter++;
                }
                parserContext.getRegistry().registerBeanDefinition(configId, config);
                String maxTotal = element.getAttribute("maxTotal");
                String maxIdle = element.getAttribute("maxIdle");
                String minIdle = element.getAttribute("minIdle");
                String maxWaitMillis = element.getAttribute("maxWaitMillis");
                String testOnBorrow = element.getAttribute("testOnBorrow");
                String testOnReturn = element.getAttribute("testOnReturn");
                String testWhileIdle = element.getAttribute("testWhileIdle");
                config.getPropertyValues().addPropertyValue("maxTotal", maxTotal);
                config.getPropertyValues().addPropertyValue("maxIdle", maxIdle);
                config.getPropertyValues().addPropertyValue("minIdle", minIdle);
                config.getPropertyValues().addPropertyValue("maxWaitMillis", maxWaitMillis);
                config.getPropertyValues().addPropertyValue("testOnBorrow", testOnBorrow);
                config.getPropertyValues().addPropertyValue("testOnReturn", testOnReturn);
                config.getPropertyValues().addPropertyValue("testWhileIdle", testWhileIdle);

                String ip = element.getAttribute("ip");
                String port = element.getAttribute("port");
                String passwd = element.getAttribute("passwd");
                String timeOut = element.getAttribute("timeOut");
                String retryNum = element.getAttribute("retryNum");
                beanDefinition.getPropertyValues().addPropertyValue("ip", ip);
                beanDefinition.getPropertyValues().addPropertyValue("port", port);
                beanDefinition.getPropertyValues().addPropertyValue("passwd", passwd);
                beanDefinition.getPropertyValues().addPropertyValue("timeOut", timeOut);
                beanDefinition.getPropertyValues().addPropertyValue("retryNum", retryNum);
                beanDefinition.getPropertyValues().addPropertyValue("config", new BeanDefinitionHolder(config, configId));
            }
        } else if (RedisTemplate.class.equals(this.beanClass)) {
            String config = element.getAttribute("config");
            BeanDefinition configDefinition = parserContext.getRegistry().getBeanDefinition(config);
            beanDefinition.getPropertyValues().addPropertyValue("redisConfig", new BeanDefinitionHolder(configDefinition, config));
        }
        return beanDefinition;
    }
}