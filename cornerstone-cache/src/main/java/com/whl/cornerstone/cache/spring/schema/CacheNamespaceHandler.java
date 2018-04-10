package com.whl.cornerstone.cache.spring.schema;

import com.whl.cornerstone.cache.redis.RedisClient;
import com.whl.cornerstone.cache.redis.RedisTemplate;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by whling on 2018/4/10.
 */
public class CacheNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("config", new CacheBeanDefinitionParser(RedisClient.class));
        registerBeanDefinitionParser("template", new CacheBeanDefinitionParser(RedisTemplate.class));
    }
}