package com.github.kevin.learning.learning_17;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLock {

    private final StringRedisTemplate redisTemplate;

    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取分布式锁
     *
     * @param lockKey    锁的key
     * @param requestId  请求标识(用于解锁验证)
     * @param expireTime 过期时间(秒)
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁的key
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
        return Objects.equals(result, 1L);
    }

    /**
     * 尝试获取锁，带有重试机制
     *
     * @param lockKey     锁的key
     * @param requestId   请求标识
     * @param expireTime  过期时间(秒)
     * @param retryTimes  重试次数
     * @param sleepMillis 每次重试间隔(毫秒)
     * @return 是否获取成功
     */
    public boolean tryLockWithRetry(String lockKey, String requestId, long expireTime,
                                    int retryTimes, long sleepMillis) throws InterruptedException {
        for (int i = 0; i < retryTimes; i++) {
            if (tryLock(lockKey, requestId, expireTime)) {
                return true;
            }
            Thread.sleep(sleepMillis);
        }
        return false;
    }
}