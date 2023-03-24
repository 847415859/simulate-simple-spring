package com.tk.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Date : 2023/03/24 19:29
 * @Auther : tiankun
 */
@Component("myBeanPostProcessor")
@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        log.info("postProcessBeforeInitialization bean:{}  beanName:{}",bean,beanName);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        log.info("postProcessAfterInitialization bean:{}  beanName:{}",bean,beanName);
        if("userService".equals(beanName)){
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    log.info("代理");
                    return method.invoke(bean,args);
                }
            });
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
