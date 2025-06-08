package com.github.kevin.learning.learning_17;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisOperationService redisOperationService;
    private final RedisDistributedLock redisDistributedLock;
    private final RedisCounterService redisCounterService;

    public RedisController(RedisOperationService redisOperationService,
                           RedisDistributedLock redisDistributedLock,
                           RedisCounterService redisCounterService) {
        this.redisOperationService = redisOperationService;
        this.redisDistributedLock = redisDistributedLock;
        this.redisCounterService = redisCounterService;
    }

    // ==================== 基本操作示例 ====================

    @PostMapping("/string")
    public void setString(@RequestParam String key, @RequestParam String value) {
        redisOperationService.setString(key, value);
    }

    @GetMapping("/string")
    public String getString(@RequestParam String key) {
        return redisOperationService.getString(key);
    }

    @PostMapping("/list")
    public void addToList(@RequestParam String key, @RequestBody List<String> values) {
        redisOperationService.addToList(key, values.toArray());
    }

    @GetMapping("/list")
    public List<Object> getList(@RequestParam String key) {
        return redisOperationService.getList(key);
    }

    @GetMapping("/rightPop")
    public Object rightPop(@RequestParam String key) {
        return redisOperationService.rightPop(key);
    }

    @GetMapping("/leftPush")
    public Object leftPush(@RequestParam String key, @RequestParam String value) {
        return redisOperationService.leftPush(key, value);
    }

    @PostMapping("/hash")
    public void hash(@RequestParam String key, @RequestBody Map<String, String> user) {
        redisOperationService.setHashAll(key, user);
    }

    @GetMapping("/getHash")
    public Object getHash(@RequestParam String key, @RequestParam String field) {
        return redisOperationService.getHash(key, field);
    }

    // ==================== 分布式锁示例 ====================

    @GetMapping("/lock")
    public String testLock() {
        String lockKey = "product_123";
        String requestId = UUID.randomUUID().toString();

        try {
            // 尝试获取锁，等待5秒，锁有效期10秒
            boolean locked = redisDistributedLock.tryLockWithRetry(lockKey, requestId, 10, 5, 500);
            if (locked) {
                // 执行业务逻辑
                Thread.sleep(3000); // 模拟业务处理
                return "Lock acquired and business processed";
            } else {
                return "Failed to acquire lock";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Interrupted";
        } finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, requestId);
        }
    }

    // ==================== 计数器示例 ====================

    @PostMapping("/counter/increment")
    public Long increment(@RequestParam String key, @RequestParam long delta) {
        return redisCounterService.increment(key, delta);
    }

    @PostMapping("/counter/decrement")
    public Long decrement(@RequestParam String key, @RequestParam long delta) {
        return redisCounterService.decrement(key, delta);
    }

    @GetMapping("/counter/value")
    public Long getCounterValue(@RequestParam String key) {
        return redisCounterService.getCurrentValue(key);
    }
}