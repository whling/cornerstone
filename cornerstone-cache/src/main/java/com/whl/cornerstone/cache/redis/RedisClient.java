package com.whl.cornerstone.cache.redis;

import com.whl.cornerstone.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by whling on 2018/4/10.
 */
public class RedisClient implements InitializingBean, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger("redis-event");

    private JedisPool pool = null;
    private JedisPoolConfig config = null;
    private String ip = "127.0.0.1";
    private int port = 6379;
    private String passwd = null;
    private int timeOut = 3000;
    private int retryNum = 0;

    public synchronized void init() {
        try {
            logger.info("------------- redis pool init start------------- ");
            if (StringUtils.isNotBlank(this.passwd)) {
                this.pool = new JedisPool(this.config, this.ip, this.port, this.timeOut, this.passwd);
            } else {
                this.pool = new JedisPool(this.config, this.ip, this.port, this.timeOut);
            }
            boolean connected = isConnected();
            if (!connected) {
                logger.error("redis 初始化出错 缓存服务器连接不上！");
                throw new Exception("IP:" + this.ip + ", redis服务器无法连接，请检查 redis配置 与 redis服务器");
            }
            logger.info("------------- redis pool init end------------- ");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Error("IP:" + this.ip + ", 设置redis服务器出错", e);
        }
    }

    public boolean isConnected() {
        return getJedis().isConnected();
    }

    public Jedis getJedis() {
        Jedis jedis = null;
        int count = 0;
        do {
            try {
                jedis = this.pool.getResource();
            } catch (Exception e) {
                logger.error("get redis failed!", e);


                jedis.close();
            }
            count++;
        } while ((jedis == null) && (count < this.retryNum));
        return jedis;
    }

    public void releaseJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }

    public void setConfig(JedisPoolConfig config) {
        this.config = config;
    }

    public void afterPropertiesSet()
            throws Exception {
        init();
    }

    public void destroy()
            throws Exception {
        this.pool.destroy();
    }
}
