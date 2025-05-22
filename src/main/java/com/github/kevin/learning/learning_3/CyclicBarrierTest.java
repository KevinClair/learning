package com.github.kevin.learning.learning_3;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static void main(String[] args) throws InterruptedException {
         CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All parties have arrived at the barrier, let's proceed!"));
         Thread t1 = new Thread(() -> {
             try {
                 Thread.sleep(2000);
                 System.out.println("Thread 1 is waiting at the barrier");
                 barrier.await();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         });
         Thread t2 = new Thread(() -> {
             try {
                 Thread.sleep(3000);
                 System.out.println("Thread 2 is waiting at the barrier");
                 barrier.await();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         });
         Thread t3 = new Thread(() -> {
             try {
                 Thread.sleep(4000);
                 System.out.println("Thread 3 is waiting at the barrier");
                 barrier.await();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         });
         t1.start();
         t2.start();
         t3.start();

         Thread.sleep(
                 10000
         );
    }
}
