package com.mmall.common;

import com.mmall.util.PropertiesUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis连接池
 * <p>
 * @Author LeifChen
 * @Date 2019-03-19
 */
public class RedisPool {

    private static JedisPool pool;
    private static Integer maxTotal = Integer.parseInt(PropertiesUtils.getProperty("redis.max.total", "20"));
    private static Integer maxIdle = Integer.parseInt(PropertiesUtils.getProperty("redis.max.idle", "10"));
    private static Integer minIdle = Integer.parseInt(PropertiesUtils.getProperty("redis.min.idle", "2"));
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtils.getProperty("redis.test.borrow", "true"));
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtils.getProperty("redis.test.return", "true"));
    private static String redisIp = PropertiesUtils.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtils.getProperty("redis.port","6379"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        // 连接耗尽时，是否阻塞，false会抛出异常，true阻塞直到超时。默认true
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("name", "LeifChen");
        returnResource(jedis);
        pool.destroy();
        System.out.println("end");
    }
}
