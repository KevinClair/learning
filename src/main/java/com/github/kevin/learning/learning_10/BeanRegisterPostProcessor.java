package com.github.kevin.learning.learning_10;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BeanRegisterPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 注册多个TestBean的Bean定义
        for (int i = 0; i < 10; i++) {
            BeanDefinitionBuilder testBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(TestBean.class);
            testBeanBuilder.addConstructorArgValue(i);
            testBeanBuilder.addConstructorArgValue("name" + i);
            String beanName = "testBean" + i;
            beanDefinitionRegistry.registerBeanDefinition(beanName, testBeanBuilder.getRawBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
