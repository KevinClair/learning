package com.github.kevin.learning.learning_6;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.List;

public class CustomConditional implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String value = (String) metadata.getAnnotationAttributes(ConditionalOnPropertyContains.class.getName()).get("value");
        String havingValue = (String) metadata.getAnnotationAttributes(ConditionalOnPropertyContains.class.getName()).get("havingValue");

        // 获取环境变量
        List<String> envValue = Arrays.asList(context.getEnvironment().getProperty(value).split(","));
        // 判断环境变量是否包含指定值
        return envValue.contains(havingValue);
    }
}
