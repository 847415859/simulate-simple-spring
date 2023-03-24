package com.tk;

import com.spring.ApplicationContext;
import com.tk.service.UserService;
import com.tk.service.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Date : 2023/03/24 15:43
 * @Auther : tiankun
 */
@Slf4j
public class Test {


    public static void main(String[] args) {
        log.info("Spring start...");
        // 创建单例Bean并
        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        UserServiceInterface userService1 = (UserServiceInterface) applicationContext.getBean("userService");
        userService1.soutOrderService();
        System.out.println("userService1 = " + userService1);
        // UserService userService2 = (UserService) applicationContext.getBean("userService");
        // UserService userService3 = (UserService) applicationContext.getBean("userService");
        // OrderService orderService1 = (OrderService) applicationContext.getBean("orderService");
        // OrderService orderService2 = (OrderService) applicationContext.getBean("orderService");
        // OrderService orderService3 = (OrderService) applicationContext.getBean("orderService");
        // System.out.println(userService1);
        // System.out.println(userService2);
        // System.out.println(userService3);
        // System.out.println(orderService1);
        // System.out.println(orderService2);
        // System.out.println(orderService3);
    }
}
