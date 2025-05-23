package com.github.kevin.learning.learning_8;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TokenBucketRateLimiter {
    private final long capacity;          // 桶容量
    private final long refillRate;       // 每秒填充的令牌数
    private AtomicLong tokens;           // 当前令牌数
    private volatile long lastRefillTime; // 上次填充时间（毫秒）

    public TokenBucketRateLimiter(long capacity, long refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = new AtomicLong(capacity);
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        // 1. 计算新增令牌数（基于时间差）
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastRefillTime;
        long newTokens = elapsedTime * refillRate / 1000; // 毫秒转秒
        if (newTokens > 0) {
            // 2. 填充令牌（不超过容量）
            tokens.set(Math.min(capacity, tokens.get() + newTokens));
            lastRefillTime = now;
        }
        // 3. 尝试消费一个令牌
        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }

    // 测试
    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1); // 容量5，每秒1个令牌
        for (int i = 0; i < 10; i++) {
            System.out.println("请求" + i + ": " + (limiter.tryAcquire() ? "通过" : "被限流"));
            Thread.sleep(300); // 模拟请求间隔
        }
    }
}