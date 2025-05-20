package com.github.kevin.learning.learning_5;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 时间轮（Timing Wheel）实现
 */
public class TimingWheel {
    private final long tickDuration;  // 每个时间格的时长（毫秒）
    private final int wheelSize;      // 时间轮的大小（格数）
    private final LinkedList<Runnable>[] wheel;  // 时间轮数组
    private volatile int currentTick = 0;        // 当前指针位置
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ExecutorService executor;      // 任务执行线程池

    /**
     * 初始化时间轮
     * @param tickDuration 每个时间格的时长（毫秒）
     * @param wheelSize    时间轮大小（格数）
     */
    public TimingWheel(long tickDuration, int wheelSize) {
        this.tickDuration = tickDuration;
        this.wheelSize = wheelSize;
        this.wheel = new LinkedList[wheelSize];
        for (int i = 0; i < wheelSize; i++) {
            wheel[i] = new LinkedList<>();
        }
        this.executor = Executors.newFixedThreadPool(4); // 任务执行线程池
        start(); // 启动时间轮
    }

    /**
     * 添加延迟任务
     * @param task   任务
     * @param delay  延迟时间（毫秒）
     */
    public void addTask(Runnable task, long delay) {
        if (!running.get()) {
            throw new IllegalStateException("TimingWheel is stopped");
        }
        synchronized (this) {
            // 计算任务所在的格子位置
            int ticks = (int) (delay / tickDuration);
            int targetTick = (currentTick + ticks) % wheelSize;
            wheel[targetTick].add(task);
        }
    }

    /**
     * 启动时间轮（内部线程推进指针）
     */
    private void start() {
        Thread worker = new Thread(() -> {
            while (running.get()) {
                try {
                    Thread.sleep(tickDuration); // 等待一个时间格
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                advanceClock();
            }
        });
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * 推进时间轮指针，执行到期任务
     */
    private void advanceClock() {
        synchronized (this) {
            LinkedList<Runnable> tasks = wheel[currentTick];
            if (!tasks.isEmpty()) {
                // 提交任务到线程池执行
                for (Runnable task : tasks) {
                    executor.submit(task);
                }
                tasks.clear(); // 清空当前格
            }
            currentTick = (currentTick + 1) % wheelSize; // 指针移动到下一格
        }
    }

    /**
     * 停止时间轮
     */
    public void stop() {
        running.set(false);
        executor.shutdown();
    }

    // 测试用例
    public static void main(String[] args) {
        TimingWheel wheel = new TimingWheel(1000, 10); // 1秒一格，共10格

        // 添加延迟任务
        wheel.addTask(() -> System.out.println("Task 1 executed at " + new Date()), 3000); // 3秒后执行
        wheel.addTask(() -> System.out.println("Task 2 executed at " + new Date()), 5000); // 5秒后执行

        // 主线程等待，防止JVM退出
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wheel.stop();
    }
}