package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Date : 2023/03/24 17:26
 * @Auther : tiankun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    // BeanName
    String value() default "";

    // 是否懒加载
    boolean lazy() default false;
}
