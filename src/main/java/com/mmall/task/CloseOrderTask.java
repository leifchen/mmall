package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.OrderService;
import com.mmall.util.PropertiesUtils;
import com.mmall.util.RedisShardedPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 关闭订单任务
 * <p>
 * @Author LeifChen
 * @Date 2019-03-27
 */
@Slf4j
@Component
public class CloseOrderTask {

    public static final int WAIT_TIME = 0;
    public static final int LEASE_TIME = 5;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedissonManager redissonManager;

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTask() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtils.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtils.setnx(Const.RedisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            // 返回值是1，代表设置成功，获取锁
            closeOrder();
        } else {
            // 未获取到锁，继续判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtils.get(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisShardedPoolUtils.getSet(Const.RedisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
                if (getSetResult == null || (StringUtils.equals(lockValueStr, getSetResult))) {
                    // 获取到锁
                    closeOrder();
                } else {
                    log.info("没有获得分布式锁:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获得分布式锁:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskByRedisson() {
        RLock lock = redissonManager.getRedisson().getLock(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
                log.info("Redisson获取到分布式锁:{},ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtils.getProperty("task.order.close.time.hour", "2"));
                orderService.closeOrder(hour);
            } else {
                log.info("Redisson没有获取到分布式锁:{},ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson分布式锁获取异常", e);
        } finally {
            if (getLock) {
                lock.unlock();
                log.info("Redisson分布式锁释放");
            }
        }
    }

    private void closeOrder() {
        // 有效期5秒，防止死锁
        RedisShardedPoolUtils.expire(Const.RedisLock.CLOSE_ORDER_TASK_LOCK, 5);
        log.info("获取{},ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtils.getProperty("task.order.close.time.hour", "2"));
        orderService.closeOrder(hour);
        RedisShardedPoolUtils.del(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
    }
}
