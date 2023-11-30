package com.tk.service;

import com.spring.Autowire;
import com.spring.Component;
import com.spring.InitializingBean;
import com.spring.Scope;
import org.w3c.dom.ls.LSOutput;

/**
 * @Description:
 * @Date : 2023/03/24 15:43
 * @Auther : tiankun
 */
@Component
@Scope("singleton")
public class UserService implements InitializingBean,UserServiceInterface {

    @Autowire
    private OrderService orderService;

    @Override
    public void insert(){
        System.out.println("插入数据");
    }

    @Override
    public void soutOrderService(){
        System.out.println(orderService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化...");
    }
}
