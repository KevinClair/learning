package com.github.kevin.learning.learning_19;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * 权限AOP拦截器
 */
@Component
@Slf4j
@Aspect
public class PermissionAopInterceptor {


    /**
     * 定义切入点：拦截所有带有@PostMapping或@GetMapping注解的方法
     */
    @Pointcut("@annotation(com.github.kevin.learning.learning_19.Permission)")
    public void permissionInterceptor() {
    }

    /**
     * 方法调用前执行
     */
    @Before("permissionInterceptor()")
    public void logBefore(JoinPoint joinPoint) {
        // 获取用户信息
        User user = SessionUserLocal.getUser();
        if (Objects.isNull(user)) {
            // 直接返回异常信息
            throw new RuntimeException("用户未登录或会话已过期");
        }
        // 获取注解中的数据
        Permission permission = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Permission.class);
        String value = permission.value();
        // 解析value中的权限标识
        boolean isValid = this.parse(value, user);
        if (!isValid) {
            throw new RuntimeException("permission denied: " + value);
        }
    }

    // 解析表达式并验证权限
    public boolean parse(String expression, User user) {
        // 使用SpEL(Spring Expression Language)解析
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 注册自定义函数
        context.setRootObject(new PermissionExpressionRoot(user));

        try {
            return Boolean.TRUE.equals(parser.parseExpression(expression).getValue(context, Boolean.class));
        } catch (Exception e) {
            throw new IllegalStateException("权限表达式解析失败: " + expression, e);
        }
    }

    // 表达式根对象，提供hasPermissions/hasRoles方法
    @RequiredArgsConstructor
    public static class PermissionExpressionRoot {
        private final User user;

        public boolean hasPermissions(String... permissions) {
            // 实现权限检查逻辑
            return user.getPermissions().stream()
                    .anyMatch(auth -> Arrays.asList(permissions).contains(auth));
        }

        public boolean hasRoles(String... roles) {
            // 实现角色检查逻辑
            return user.getRoles().stream()
                    .anyMatch(auth -> Arrays.asList(roles).contains(auth));
        }
    }

}
