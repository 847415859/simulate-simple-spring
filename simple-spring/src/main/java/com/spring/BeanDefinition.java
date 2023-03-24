package com.spring;

/**
 * @Description: Bean 定义类
 * @Date : 2023/03/24 18:05
 * @Auther : tiankun
 */
public class BeanDefinition {

    /**
     * 类型
     */
    private Class clazz;

    /**
     * 单例还是多例
     */
    private String type;

    /**
     * 是否懒加载
     */
    private Boolean lazy;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }
}
