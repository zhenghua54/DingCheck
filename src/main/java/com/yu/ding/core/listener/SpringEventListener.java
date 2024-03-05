package com.yu.ding.core.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @author MrYu(YWG)
 * @date 2022-07-16 11:56:19
 */
public interface SpringEventListener<T extends ApplicationEvent>{
    /**
     * spring event listener
     * @param event
     */
    void onListener(T event);
}
