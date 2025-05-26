package com.github.kevin.learning.learning_10;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/test")
public class TestBeanController {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @GetMapping("/getBean")
    public String getBean(@RequestParam("name") String name){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        // 通过Spring的BeanFactory获取Bean
        TestBean testBean = (TestBean) applicationContext.getBean(name);
        // 动态注册的Bean需要特殊处理
        BeanDefinitionRegistry registry =
                (BeanDefinitionRegistry) applicationContext.getBeanFactory();

        // 2. 移除Bean定义
        if (registry.containsBeanDefinition(name)) {
            registry.removeBeanDefinition(name);
        }
        if (testBean != null) {
            return "获取到的TestBean: " + testBean.getName();
        } else {
            return "没有找到名为 " + name + " 的TestBean";
        }
    }
}
