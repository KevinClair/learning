package com.github.kevin.learning.learning_4.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ByteBuddyDemo {
    public static void main(String[] args) throws Exception {
        // 2. 创建InvocationHandler
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("ByteBuddy前置处理");
                // 这里可以调用真实实现
                System.out.println("执行方法: " + method.getName());
                System.out.println("ByteBuddy后置处理");
                return method.invoke(proxy, args);
            }
        };
        
        // 3. 创建代理类
        Class<?> proxyType = new ByteBuddy()
            .subclass(Object.class)
            .implement(IHelloService.class)
            .method(ElementMatchers.isDeclaredBy(IHelloService.class))
            .intercept(InvocationHandlerAdapter.of(handler))
            .make()
            .load(ByteBuddyDemo.class.getClassLoader())
            .getLoaded();
        
        // 4. 实例化代理对象
        IHelloService proxy = (IHelloService) proxyType.getDeclaredConstructor().newInstance();
        
        // 5. 使用代理对象
        System.out.println(proxy.sayHello("World"));
    }
}