package com.github.kevin.learning.learning_14;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class CustomFeignConfig {

    @Value("${feign.secret-key:defaultSecretKey}")
    private String secretKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new SignInterceptor(secretKey);
    }
}
