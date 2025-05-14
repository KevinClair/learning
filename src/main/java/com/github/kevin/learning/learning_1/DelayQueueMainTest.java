package com.github.kevin.learning.learning_1;

import java.util.concurrent.DelayQueue;

public class DelayQueueMainTest {

    public static void main(String[] args) {
        Long currentTime = System.currentTimeMillis();
        DelayQueueTest task1 = new DelayQueueTest("Task1", 5000, currentTime);
        DelayQueueTest task2 = new DelayQueueTest("Task2", 3000, currentTime);
        DelayQueueTest task3 = new DelayQueueTest("Task3", 1000, currentTime);
        DelayQueueTest task4 = new DelayQueueTest("Task4", 2000, currentTime);

        DelayQueue<DelayQueueTest> delayQueue = new DelayQueue<>();
        delayQueue.add(task1);
        delayQueue.add(task2);
        delayQueue.add(task3);
        delayQueue.add(task4);

        while (true) {
            try {
                // 获取并移除队列中最早到期的元素
                DelayQueueTest task = delayQueue.take();
                System.out.println("Executing: " + task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }
    }
}
