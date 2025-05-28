package com.github.kevin.learning.learning_11;

import com.github.kevin.learning.learning_11.annodation.SensitiveEntity;
import com.github.kevin.learning.learning_11.annodation.SensitiveField;
import com.github.kevin.learning.learning_11.sensitive.SensitiveDataCryptoUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SensitiveDataInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        Object[] args = invocation.getArgs();
        Object parameter = args[1];
        if ("update".equals(methodName)) {
            // 处理插入/更新操作
            processEncrypt(parameter);
        } else if ("query".equals(methodName)) {
            // 处理查询操作
            Object result = invocation.proceed();
            if (result instanceof List) {
                for (Object item : (List<?>) result) {
                    processDecrypt(item);
                }
            } else {
                processDecrypt(result);
            }
            return result;
        }

        return invocation.proceed();
    }

    /**
     * 加密处理
     */
    private void processEncrypt(Object parameter) throws IllegalAccessException {
        if (parameter == null) return;

        Class<?> clazz = parameter.getClass();
        if (clazz.isAnnotationPresent(SensitiveEntity.class)) {
            for (Field field : getAllFields(clazz)) {
                if (field.isAnnotationPresent(SensitiveField.class)) {
                    field.setAccessible(true);
                    Object value = field.get(parameter);
                    if (value instanceof String) {
                        String encrypted = SensitiveDataCryptoUtil.encrypt((String) value);
                        field.set(parameter, encrypted);
                    }
                }
            }
            return;
        }
        if (parameter instanceof MapperMethod.ParamMap) {
            // 处理 MapperMethod.ParamMap
            encryptUpdateParamMap((MapperMethod.ParamMap<Object>) parameter);
            return;
        }
    }

    private void encryptUpdateParamMap(MapperMethod.ParamMap<Object> parameterObject) {
        parameterObject.entrySet().forEach(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!key.startsWith("param") && value != null) {
                Class<?> valueClass = value.getClass();
                if (valueClass.isAnnotationPresent(SensitiveEntity.class)) {
                    try {
                        for (Field field : getAllFields(valueClass)) {
                            if (field.isAnnotationPresent(SensitiveField.class)) {
                                field.setAccessible(true);
                                Object fieldValue = field.get(value);
                                if (fieldValue instanceof String) {
                                    String encrypted = SensitiveDataCryptoUtil.encrypt((String) fieldValue);
                                    field.set(value, encrypted);
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("加密敏感数据失败", e);
                    }
                }
            }
        });
    }

    /**
     * 解密处理
     */
    private void processDecrypt(Object result) throws IllegalAccessException {
        if (result == null) return;

        Class<?> clazz = result.getClass();
        if (!clazz.isAnnotationPresent(SensitiveEntity.class)) {
            return;
        }

        for (Field field : getAllFields(clazz)) {
            if (field.isAnnotationPresent(SensitiveField.class)) {
                field.setAccessible(true);
                Object value = field.get(result);
                if (value instanceof String) {
                    String decrypted = SensitiveDataCryptoUtil.decrypt((String) value);
                    field.set(result, decrypted);
                }
            }
        }
    }

    /**
     * 获取类及其父类的所有字段
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可配置属性
    }
}