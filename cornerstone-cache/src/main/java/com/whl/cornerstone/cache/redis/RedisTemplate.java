package com.whl.cornerstone.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.util.SafeEncoder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by whling on 2018/4/10.
 */
public class RedisTemplate {

    private static Logger logger = LoggerFactory.getLogger("redis-event");

    private RedisClient redisConfig;

    public String ping() {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.ping();
            }
        });
    }

    public String get(final String key) {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public void set(final String key, final String value) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public void set(final String key, final byte[] value) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.set(SafeEncoder.encode(key), value);
            }
        });
    }

    public void set(final String key, final String value, final int seconds) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    public void set(final String key, final byte[] value, final int seconds) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.setex(SafeEncoder.encode(key), seconds, value);
            }
        });
    }

    public String getSet(final String key, final String value) {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.getSet(key, value);
            }
        });
    }

    public Long incr(final String key) {
        return (Long) execute(new RedisCallback() {
            public Long execute(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long incrBy(final String key, final long value) {
        return (Long) execute(new RedisCallback() {
            public Long execute(Jedis jedis) {
                return jedis.incrBy(key, value);
            }
        });
    }

    public Double incrByFloat(final String key, final double value) {
        return (Double) execute(new RedisCallback() {
            public Double execute(Jedis jedis) {
                return jedis.incrByFloat(key, value);
            }
        });
    }

    public Long del(final String key) {
        return (Long) execute(new RedisCallback() {
            public Long execute(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    public Boolean exists(final String key) {
        return (Boolean) execute(new RedisCallback() {
            public Boolean execute(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    public Boolean hexists(final String mapKey, final String attributeKey) {
        return (Boolean) execute(new RedisCallback() {
            public Boolean execute(Jedis jedis) {
                return jedis.hexists(mapKey, attributeKey);
            }
        });
    }

    public String hget(final String key, final String field) {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.hget(key, field);
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return (Map) execute(new RedisCallback() {
            public Map<String, String> execute(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public Long hdel(final String mapKey, final String attributeKey) {
        return (Long) execute(new RedisCallback() {
            public Long execute(Jedis jedis) {
                return jedis.hdel(mapKey, new String[]{attributeKey});
            }
        });
    }

    public void hset(final String key, final String field, final String value) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.hset(key, field, value);
            }
        });
    }

    public byte[] getByte(final String key) {
        return (byte[]) execute(new RedisCallback() {
            public byte[] execute(Jedis jedis) {
                return jedis.get(SafeEncoder.encode(key));
            }
        });
    }

    public String llen(final String key) {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.llen(key) + "";
            }
        });
    }

    public void lpush(final String key, final String value) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.lpush(key, new String[]{value});
            }
        });
    }

    public void rpush(final String key, final String value) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                jedis.rpush(key, new String[]{value});
            }
        });
    }

    public void lpushPipeLine(final String key, final List<String> values) {
        execute(new RedisCallbackWithoutResult() {
            public void executeWithoutResult(Jedis jedis) {
                Pipeline p = jedis.pipelined();
                for (String value : values) {
                    p.lpush(key, new String[]{value});
                }
                p.sync();
            }
        });
    }

    public String lpop(final String key) {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.lpop(key);
            }
        });
    }

    public String rpop(final String key) {
        return (String) execute(new RedisCallback() {
            public String execute(Jedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    public List<String> brpop(final String key) {
        return (List) execute(new RedisCallback() {
            public List<String> execute(Jedis jedis) {
                return jedis.brpop(0, key);
            }
        });
    }

    public List<String> lrange(final String key, final long start, long end) {
        return (List) execute(new RedisCallback() {
            public List<String> execute(Jedis jedis) {
                return jedis.lrange(key, start, end);
            }
        });
    }

    public Long lrem(final String key, final String value) {
        return (Long) execute(new RedisCallback() {
            public Long execute(Jedis jedis) {
                return jedis.lrem(key, 1L, value);
            }
        });
    }

    public Long sadd(final String key, final String value) {
        return (Long) execute(new RedisCallback() {
            public Long execute(Jedis jedis) {
                return jedis.sadd(key, new String[]{value});
            }
        });
    }

    public Set<String> smembers(final String key) {
        return (Set) execute(new RedisCallback() {
            public Set<String> execute(Jedis jedis) {
                return jedis.smembers(key);
            }
        });
    }

    public Boolean isConnected() {
        return (Boolean) execute(new RedisCallback() {
            public Boolean execute(Jedis jedis) {
                return Boolean.valueOf(jedis.isConnected());
            }
        });
    }

    protected <T> T execute(RedisCallback<T> callback) {
        Jedis jedis = null;
        try {
            jedis = this.redisConfig.getJedis();
            return callback.execute(jedis);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                this.redisConfig.releaseJedis(jedis);
            }
        }
        return null;
    }

    public void setRedisConfig(RedisClient redisConfig) {
        this.redisConfig = redisConfig;
    }
}