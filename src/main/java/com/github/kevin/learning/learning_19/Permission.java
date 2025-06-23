package com.github.kevin.learning.learning_19;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Permission {

    /**
     * 权限标识(通常为spEl表达式)
     *
     * @return 权限标识
     */
    String value();
}
