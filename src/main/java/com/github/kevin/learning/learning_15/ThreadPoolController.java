package com.github.kevin.learning.learning_15;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thread-pool")
@RequiredArgsConstructor
public class ThreadPoolController {

    private final ThreadPoolTest threadPoolTest;

    @RequestMapping("/submit")
    public String submit() {

        for (int i = 0; i < 10; i++) {
            threadPoolTest.submit(() -> {
                try {
                    // 模拟任务执行
                    Thread.sleep(3000);
                    System.out.println("Task executed successfully");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Task interrupted: " + e.getMessage());
                }
            });
        }
        return "Task submitted successfully";
    }
}
