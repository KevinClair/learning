package com.github.kevin.learning.learning_6;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(CustomConditional.class)
public @interface ConditionalOnPropertyContains {

    String value();

    String havingValue();
}
