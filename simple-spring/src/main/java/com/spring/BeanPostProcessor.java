package com.spring;

/**
 * @Description: Bean前置处理器
 * @Date : 2023/03/24 19:28
 * @Auther : tiankun
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName)  {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName)  {
        return bean;
    }
}


