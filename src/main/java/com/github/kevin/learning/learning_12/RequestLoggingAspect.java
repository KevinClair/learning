package com.github.kevin.learning.learning_12;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RequestLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    /**
     * 定义切入点：拦截所有带有@PostMapping或@GetMapping注解的方法
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void requestMappingMethods() {
    }

    /**
     * 方法调用前执行
     */
    @Before("requestMappingMethods()")
    public void logBefore(JoinPoint joinPoint) {
        // 获取请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        // 收集请求信息
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("URL", request.getRequestURL().toString());
        requestInfo.put("HTTP Method", request.getMethod());
        requestInfo.put("IP", request.getRemoteAddr());
        requestInfo.put("Class Method", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        // 获取请求参数
        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            requestInfo.put("Parameters", params);
        }

        // 获取请求头
        Enumeration<String> headers = request.getHeaderNames();
        Map<String, String> headersMap = new HashMap<>();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            headersMap.put(headerName, request.getHeader(headerName));
        }
        if (!headersMap.isEmpty()) {
            requestInfo.put("Headers", headersMap);
        }

        // 获取方法参数
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            requestInfo.put("Method Arguments", Arrays.toString(joinPoint.getArgs()));
        }

        // 打印日志
        logger.info("Request Info: {}", requestInfo);
    }

    /**
     * 方法返回后执行
     */
    @AfterReturning(pointcut = "requestMappingMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method {} returned with value: {}",
                joinPoint.getSignature().getName(), result);
    }

    /**
     * 方法抛出异常时执行
     */
    @AfterThrowing(pointcut = "requestMappingMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method {}: {}",
                joinPoint.getSignature().getName(), exception.getMessage());
    }
}