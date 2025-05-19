package com.github.kevin.learning.learning_4.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class HelloServiceTest {

    public static void main(String[] args) {
        // 2. 创建MethodInterceptor
        MethodInterceptor interceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                System.out.println("CGLIB前置处理");
                Object result = proxy.invokeSuper(obj, args);
                System.out.println("CGLIB后置处理");
                return result;
            }
        };

        // 3. 创建代理对象
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloService.class);
        enhancer.setCallback(interceptor);
        HelloService proxy = (HelloService) enhancer.create();

        // 4. 使用代理对象
        String result = proxy.sayHello("World");
        System.out.println(result);
    }
}
