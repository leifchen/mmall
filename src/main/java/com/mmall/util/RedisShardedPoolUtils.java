package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * Redis连接池工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-20
 */
@Slf4j
public class RedisShardedPoolUtils {

    /**
     * 获取键的值
     * @param key 键
     * @return
     */
    public static String get(String key) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置键值对
     * @param key   键
     * @param value 值
     * @return
     */
    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置键值对和有效时间
     * @param key    键
     * @param value  值
     * @param exTime 有效时间（单位：秒）
     * @return
     */
    public static String setEx(String key, String value, int exTime) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置键的有效期
     * @param key    键
     * @param exTime 有效时间（单位：秒）
     * @return
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除键
     * @param key 键
     * @return
     */
    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置键值对，事前判断是否已存在
     * @param key   键
     * @param value 值
     * @return
     */
    public static Long setnx(String key, String value) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置键值对，并立即返回旧值
     * @param key   键
     * @param value 值
     * @return
     */
    public static String getSet(String key, String value) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
}
