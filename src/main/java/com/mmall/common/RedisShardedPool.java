package com.mmall.common;

import com.mmall.util.PropertiesUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis连接池
 * <p>
 * @Author LeifChen
 * @Date 2019-03-19
 */
public class RedisShardedPool {

    private static ShardedJedisPool pool;
    private static Integer maxTotal = Integer.parseInt(PropertiesUtils.getProperty("redis.max.total", "20"));
    private static Integer maxIdle = Integer.parseInt(PropertiesUtils.getProperty("redis.max.idle", "10"));
    private static Integer minIdle = Integer.parseInt(PropertiesUtils.getProperty("redis.min.idle", "2"));
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtils.getProperty("redis.test.borrow", "true"));
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtils.getProperty("redis.test.return", "true"));
    private static String redis1Ip = PropertiesUtils.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtils.getProperty("redis1.port", "6379"));
    private static String redis2Ip = PropertiesUtils.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtils.getProperty("redis2.port", "6380"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        // 连接耗尽时，是否阻塞，false会抛出异常，true阻塞直到超时。默认true
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }
}
