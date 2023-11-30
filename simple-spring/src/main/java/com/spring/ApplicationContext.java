package com.spring;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Description:
 * @Date : 2023/03/24 15:42
 * @Auther : tiankun
 */
@Slf4j
public class ApplicationContext {

    private Class configClass;


    private static Map<String,BeanDefinition> beanDefinitionMape = new HashMap<>();

    private static Map<String,Object> singletonObjects = new HashMap<>();

    private static List<BeanPostProcessor> beanPostProcessors = new LinkedList<>();

    public ApplicationContext() {
    }

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        // 扫描路径下所有的Bean
        scanBean(configClass);
        // 如果是单例Bean需要在程序启动时创建
        beanDefinitionMape.forEach((beanName,beanDefinition) -> {
            String scopeType = beanDefinition.getType();
            if("singleton".equals(scopeType)){
                Object bean = createBean(beanName,beanDefinition);
                if(bean != null) {
                    singletonObjects.put(beanName, bean);
                }
            }
        });
    }


    private void scanBean(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
            // 获取到扫描路径
            String scanPath = componentScan.value();
            // 根据扫描路径找类上带有 @Component 注解的类
            log.info("scan path :{}",scanPath);
            // 获取到target/classes文件目录
            // file:/D:/Studynotes/self-study/ssjava/3.SSM%20&&%20Springboot/02.spring/SourceCode/simulate-simple-spring/simple-spring/target/classes/
            URL url = this.getClass().getResource("/");
            String replacePath = scanPath.replaceAll("\\.", "/");
            // URLDecoder.decode,避免文件路径被URL编码，需要进行URL解码
            File file = new File(URLDecoder.decode(url.getFile()), replacePath);
            if(file.isDirectory()){
                for (String fileName : file.list()) {
                    String filePath = scanPath + "." + fileName.substring(0,fileName.lastIndexOf(".class"));
                    try {
                        Class<?> clazz = Class.forName(filePath);
                        // 加载BeanPostProcessor
                        if(clazz.isAnnotationPresent(Component.class)){
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                beanPostProcessors.add(beanPostProcessor);
                            }else {
                                Component component = clazz.getDeclaredAnnotation(Component.class);
                                BeanDefinition beanDefinition = new BeanDefinition();
                                String beanName = component.value();
                                if(Objects.isNull(beanName) || Objects.equals(beanName.trim(), "")){
                                    beanName = getBeanNameByClazz(clazz);
                                }
                                boolean lazy = component.lazy();
                                Scope scopeAnno = clazz.getAnnotation(Scope.class);
                                String scopeValue = "singleton";
                                if(scopeAnno != null) {
                                    scopeValue = scopeAnno.value();
                                }
                                beanDefinition.setClazz(clazz);
                                beanDefinition.setType(scopeValue);
                                beanDefinition.setLazy(lazy);
                                beanDefinitionMape.put(beanName, beanDefinition);
                            }
                        }

                    } catch (ClassNotFoundException e) {
                        log.warn("load Class error:  path:{},{}",filePath,e);
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        log.info("bean defination map :{}",beanDefinitionMape);
    }

    /**
     * 根据类获取Bean名称
     * @param clazz
     * @return
     */
    private String getBeanNameByClazz(Class<?> clazz) {
        String beanName;
        String clazzName = clazz.getName();
        int beginIndex = clazzName.lastIndexOf(".");
        if(beginIndex > -1) {
            clazzName = clazzName.substring(beginIndex + 1);
        }
        if (clazzName.length() == 1){
            beanName = clazzName.toLowerCase();
        }else {
            beanName = clazzName.substring(0,1).toLowerCase() + clazzName.substring(1);
        }
        return beanName;
    }

    public Object getBean(String userService) {
        BeanDefinition beanDefinition = beanDefinitionMape.get(userService);
        if(beanDefinition == null){
            throw new RuntimeException("not fond bean");
        }
        String scopeType = beanDefinition.getType();
        if("singleton".equals(scopeType)){
            return singletonObjects.get(userService);
        }
        return createBean(userService,beanDefinition);
    }



    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        try {
            Class clazz = beanDefinition.getClazz();
            Object instance = clazz.getConstructor().newInstance();
            // 判断是否实现 InitializingBean接口
            if(instance instanceof InitializingBean){
                InitializingBean initializingBean = (InitializingBean) instance;
                try {
                    initializingBean.afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 实现 BeanPostProcessor
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            }

            // 依赖注入
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Autowire.class)) {
                    // 根据属性名获取进行注入
                    String name = declaredField.getName();
                    Object refBean = singletonObjects.get(name);
                    if(refBean != null) {
                        declaredField.setAccessible(true);
                        declaredField.set(instance, refBean);
                    }
                }
            }

            // Bean的后置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
