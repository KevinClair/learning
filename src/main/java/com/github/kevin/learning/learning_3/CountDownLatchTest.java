package com.github.kevin.learning.learning_3;

public class CountDownLatchTest {

    public static void main(String[] args) {
        // CountDownLatch的使用示例
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(3);

        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " is doing some work...");
            try {
                Thread.sleep(1000); // 模拟工作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown(); // 任务完成，计数器减一
                System.out.println(Thread.currentThread().getName() + " finished work.");
            }
        };

        for (int i = 0; i < 3; i++) {
            new Thread(task).start();
        }

        try {
            latch.await(); // 等待所有任务完成
            System.out.println("All tasks are completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
