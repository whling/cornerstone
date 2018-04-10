package com.whl.cornerstone.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by whling on 2018/4/10.
 */
public interface RedisCallback<T> {

    T execute(Jedis paramJedis);
}
