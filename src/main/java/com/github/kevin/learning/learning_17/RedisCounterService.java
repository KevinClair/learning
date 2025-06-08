package com.github.kevin.learning.learning_17;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCounterService {

    private final StringRedisTemplate redisTemplate;

    public RedisCounterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 递增操作
     *
     * @param key   键
     * @param delta 增量(可以为负)
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减操作
     *
     * @param key   键
     * @param delta 减量(可以为负)
     * @return 递减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 递增操作并设置过期时间
     *
     * @param key     键
     * @param delta   增量
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 递增后的值
     */
    public Long incrementWithExpire(String key, long delta, long timeout, TimeUnit unit) {
        Long value = redisTemplate.opsForValue().increment(key, delta);
        redisTemplate.expire(key, timeout, unit);
        return value;
    }

    /**
     * 获取当前值
     *
     * @param key 键
     * @return 当前值，如果不存在返回0
     */
    public Long getCurrentValue(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? 0L : Long.parseLong(value);
    }

    /**
     * 重置计数器
     *
     * @param key 键
     */
    public void resetCounter(String key) {
        redisTemplate.delete(key);
    }
}