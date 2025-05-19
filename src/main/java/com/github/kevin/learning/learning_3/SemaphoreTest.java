package com.github.kevin.learning.learning_3;

public class SemaphoreTest {

    public static void main(String[] args) {
        // Semaphore的使用示例
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(2); // 允许2个线程同时访问
        Runnable task = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " is doing some work...");
                Thread.sleep(2000); // 模拟工作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
            System.out.println(Thread.currentThread().getName() + " finished work.");
        };
        for (int i = 0; i < 5; i++) {
            new Thread(task).start();
        }
    }
}
