package com.github.kevin.learning.learning_6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfiguration {

    @ConditionalOnPropertyContains(value = "custom.property", havingValue = "custom")
    @Bean
    public CustomBean customBean() {
        System.out.println("Custom bean created");
        return new CustomBean();
    }
}
