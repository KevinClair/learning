package com.github.kevin.learning.learning_9;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            1, // 核心线程数
            2, // 最大线程数
            60, // 空闲线程存活时间（秒）
            TimeUnit.SECONDS, // 时间单位
            new LinkedBlockingQueue<Runnable>(5), // 工作队列
            new java.util.concurrent.ThreadPoolExecutor.AbortPolicy() // 拒绝策略
    );

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPoolExecutor.submit(() -> {
                // 模拟任务执行时间
//                    Thread.sleep(10);
                System.out.println("正在执行任务: " + finalI + ",当前队列大小：{}" + threadPoolExecutor.getQueue().size());
                System.out.println("当前活动线程数: " + threadPoolExecutor.getActiveCount());

            });
        }
        Thread.sleep(10 * 60 * 1000);
    }
}
