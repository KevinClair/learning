package com.github.kevin.learning.learning_8;

import com.google.common.util.concurrent.RateLimiter;

public class GuavaTokenBucket {
    public static void main(String[] args) {
        RateLimiter limiter = RateLimiter.create(1.0); // 每秒1个令牌
        for (int i = 0; i < 5; i++) {
            System.out.println("请求" + i + ": " + 
                (limiter.tryAcquire() ? "通过" : "被限流"));
        }
    }
}