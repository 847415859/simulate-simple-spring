package com.spring;

/**
 * @Description: Bean 初始化接口
 * @Date : 2023/03/24 19:23
 * @Auther : tiankun
 */
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;

}
