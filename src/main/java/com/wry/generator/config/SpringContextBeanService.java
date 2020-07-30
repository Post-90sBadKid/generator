package com.wry.generator.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 获取spring上下文
 *
 */
@Order(1)
@Component
public class SpringContextBeanService implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);

    }

    public static <T> Map<String, T> getBeans(Class<T> beanClass) {
        return context.getBeansOfType(beanClass);

    }


}

