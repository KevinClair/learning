package com.github.kevin.learning.learning_11.annodation;

import java.lang.annotation.*;

/**
 * 标记需要加解密的实体类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SensitiveEntity {
}