package com.mmall.common;

import com.mmall.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redisson管理类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-28
 */
@Slf4j
@Component
public class RedissonManager {

    private Config config = new Config();
    private Redisson redisson = null;

    private static String redis1Ip = PropertiesUtils.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtils.getProperty("redis1.port", "6379"));

    @PostConstruct
    private void init() {
        try {
            config.useSingleServer().setAddress(redis1Ip + ":" + redis1Port);
            redisson = (Redisson) Redisson.create(config);
            log.info("Redisson init success");
        } catch (Exception e) {
            log.error("Redisson init error", e);
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }
}
