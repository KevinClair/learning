package com.github.kevin.learning.learning_11.sensitive;

import com.github.kevin.learning.learning_11.SensitiveDataInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public SensitiveDataInterceptor sensitiveDataInterceptor() {
        return new SensitiveDataInterceptor();
    }
}