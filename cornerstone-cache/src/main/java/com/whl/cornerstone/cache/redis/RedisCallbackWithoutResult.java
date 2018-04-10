package com.whl.cornerstone.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by whling on 2018/4/10.
 */
public abstract class RedisCallbackWithoutResult implements RedisCallback<Object> {

    public Object execute(Jedis jedis) {
        executeWithoutResult(jedis);
        return null;
    }

    public abstract void executeWithoutResult(Jedis paramJedis);
}
