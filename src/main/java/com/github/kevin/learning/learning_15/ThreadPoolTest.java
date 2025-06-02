package com.github.kevin.learning.learning_15;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class ThreadPoolTest implements DisposableBean {

    private final AtomicBoolean running = new AtomicBoolean(true);

    private final ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolTest() {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                2,
                5,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadPoolExecutor.AbortPolicy()
        );
        ;
    }

    public void submit(Runnable task) {
        if (running.get()) {
            threadPoolExecutor.submit(task);
        } else {
            throw new IllegalStateException("Thread pool is not running");
        }
    }

    @Override
    public void destroy() throws Exception {
        running.compareAndSet(true, false);
        // 优雅关闭线程池
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.error("Thread pool did not terminate");
                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Thread pool shutdown interrupted", e);
            threadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Thread pool has been shut down gracefully, status: {}", threadPoolExecutor.isTerminated());
        // 确保线程池已关闭
        if (!threadPoolExecutor.isTerminated()) {
            // 如果此时线程池仍未关闭，循环关闭线程池，直到成功为止
            while (!threadPoolExecutor.isTerminated()) {
                threadPoolExecutor.shutdownNow();
                try {
                    if (threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                        log.info("Thread pool has been shut down successfully after forced shutdown.");
                        break;
                    }
                } catch (InterruptedException e) {
                    log.error("Thread pool shutdown interrupted during final termination", e);
                    threadPoolExecutor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
