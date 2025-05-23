package com.github.kevin.learning.learning_8;

import java.util.concurrent.atomic.AtomicLong;

public class SlidingWindowRateLimiter {
    private final int subWindowSize;    // 子窗口数量（如10个）
    private final long subWindowTime;   // 子窗口时长（毫秒，如100ms）
    private final long windowTime;      // 总窗口时长（毫秒，= subWindowSize * subWindowTime）
    private final int limit;           // 窗口内请求上限
    private final AtomicLong[] counters; // 子窗口计数器
    private volatile long windowStart; // 当前窗口起始时间

    public SlidingWindowRateLimiter(int subWindowSize, long subWindowTime, int limit) {
        this.subWindowSize = subWindowSize;
        this.subWindowTime = subWindowTime;
        this.windowTime = subWindowSize * subWindowTime;
        this.limit = limit;
        this.counters = new AtomicLong[subWindowSize];
        for (int i = 0; i < subWindowSize; i++) {
            counters[i] = new AtomicLong(0);
        }
        this.windowStart = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        // 计算需要滑动的子窗口数
        long elapsedTime = currentTime - windowStart;
        int subWindowsToShift = (int) (elapsedTime / subWindowTime);

        // 滑动窗口：重置过期的子窗口
        if (subWindowsToShift > 0) {
            int startIdx = (int) (windowStart % subWindowSize);
            for (int i = 0; i < Math.min(subWindowsToShift, subWindowSize); i++) {
                int idx = (startIdx + i) % subWindowSize;
                counters[idx].set(0);
            }
            windowStart += subWindowsToShift * subWindowTime;
        }

        // 计算当前窗口总请求数
        long total = 0;
        for (AtomicLong counter : counters) {
            total += counter.get();
        }

        // 超过限流阈值则拒绝
        if (total >= limit) {
            return false;
        }

        // 当前请求落入的子窗口
        int currentSubWindow = (int) ((currentTime % windowTime) / subWindowTime);
        counters[currentSubWindow].incrementAndGet();
        return true;
    }

    // 测试
    public static void main(String[] args) throws InterruptedException {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(10, 100, 5); // 1秒内5次请求（10个子窗口）
        for (int i = 0; i < 10; i++) {
            System.out.println("请求" + i + ": " + (limiter.tryAcquire() ? "通过" : "被限流"));
            Thread.sleep(150); // 模拟请求间隔
        }
    }
}