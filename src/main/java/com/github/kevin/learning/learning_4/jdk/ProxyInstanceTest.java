package com.github.kevin.learning.learning_4.jdk;

import java.lang.reflect.Proxy;

public class ProxyInstanceTest{

    public static void main(String[] args) {
        // 创建目标对象
        IHelloService target = new IHelloServiceImpl();

        // 创建代理对象
        IHelloService proxy = (IHelloService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy1, method, methodArgs) -> {
                    System.out.println("Before method: " + method.getName());
                    Object result = method.invoke(target, methodArgs);
                    System.out.println("After method: " + method.getName());
                    return result;
                }
        );

        // 调用代理对象的方法
        System.out.println(proxy.sayHello("Kevin"));
        System.out.println(proxy.sayHi("Kevin"));
    }
}
