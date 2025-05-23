package com.github.kevin.learning.learning_8;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter {
    private final int limit;         // 窗口内请求上限
    private final long windowSize;  // 窗口大小（毫秒）
    private final AtomicInteger counter;
    private volatile long windowStart; // 窗口开始时间

    public FixedWindowRateLimiter(int limit, long windowSize) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.counter = new AtomicInteger(0);
        this.windowStart = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        // 检查是否进入新窗口
        if (currentTime - windowStart >= windowSize) {
            counter.set(0);      // 重置计数器
            windowStart = currentTime; // 更新窗口起始时间
        }
        // 判断是否超过限流阈值
        return counter.incrementAndGet() <= limit;
    }

    // 测试
    public static void main(String[] args) throws InterruptedException {
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(5, 1000); // 1秒内最多5次请求
        for (int i = 0; i < 10; i++) {
            System.out.println("请求" + i + ": " + (limiter.tryAcquire() ? "通过" : "被限流"));
            Thread.sleep(200); // 模拟请求间隔
        }
    }
}