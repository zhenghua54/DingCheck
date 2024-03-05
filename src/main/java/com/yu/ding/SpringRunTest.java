package com.yu.ding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author MrYu(YWG)
 * @date 2022-07-16 09:53:38
 */
@SpringBootApplication
public class SpringRunTest {
    public static ApplicationContext applicationContext;
    public static void main(String[] args){
        SpringApplication.run(SpringRunTest.class, args);
        System.out.println("last="+applicationContext.getBeanDefinitionCount());
    }
}
