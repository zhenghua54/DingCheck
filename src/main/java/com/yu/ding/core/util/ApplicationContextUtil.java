package com.yu.ding.core.util;

import com.yu.ding.core.listener.SpringEventListener;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author MrYu(YWG)
 * @date 2022-07-16 09:39:23
 */
public class ApplicationContextUtil implements ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext applicationContext;
    /**
     * listener
     */
    private static ArrayList<SpringEventListener<ContextRefreshedEvent>> listeners = new ArrayList<>(8);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for(SpringEventListener<ContextRefreshedEvent> listener : listeners){
            listener.onListener(contextRefreshedEvent);
        }
    }
    public static void addListener(SpringEventListener<ContextRefreshedEvent> listener){
        listeners.add(listener);
    }
}
