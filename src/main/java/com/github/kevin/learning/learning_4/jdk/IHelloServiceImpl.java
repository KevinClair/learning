package com.github.kevin.learning.learning_4.jdk;

/**
 * 代理类
 */
public class IHelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String sayHi(String name) {
        return "Hi " + name;
    }
}
