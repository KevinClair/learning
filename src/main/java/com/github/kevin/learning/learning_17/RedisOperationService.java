package com.github.kevin.learning.learning_17;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisOperationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisOperationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ==================== String 操作 ====================

    public void setString(String key, String value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    public void setStringWithExpire(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, unit);
    }

    public String getString(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (String) ops.get(key);
    }

    // ==================== List 操作 ====================

    public void addToList(String key, Object... values) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        ops.rightPushAll(key, values);
    }

    public List<Object> getList(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return ops.range(key, 0, -1);
    }

    public Object getFirstElement(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return ops.index(key, 0);
    }

    public Object rightPop(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return ops.rightPop(key);
    }

    public Object leftPush(String key, String value) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return ops.leftPush(key, value);
    }

    // ==================== Hash 操作 ====================

    public void setHash(String key, String field, Object value) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.put(key, field, value);
    }

    public void setHashAll(String key, Map<String, String> entries) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.putAll(key, entries);
    }

    public Object getHash(String key, String field) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.get(key, field);
    }

    public Map<String, Object> getHashAll(String key) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.entries(key);
    }

    // ==================== Set 操作 ====================

    public void addToSet(String key, Object... values) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        ops.add(key, values);
    }

    public Set<Object> getSet(String key) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        return ops.members(key);
    }

    public boolean isSetMember(String key, Object value) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        return ops.isMember(key, value);
    }

    // ==================== Sorted Set 操作 ====================

    public void addToSortedSet(String key, Object value, double score) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        ops.add(key, value, score);
    }

    public Set<Object> getSortedSetRange(String key, long start, long end) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        return ops.range(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getSortedSetWithScores(String key, long start, long end) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        return ops.rangeWithScores(key, start, end);
    }
}