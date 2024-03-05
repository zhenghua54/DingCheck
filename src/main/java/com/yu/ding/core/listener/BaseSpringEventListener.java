package com.yu.ding.core.listener;

import com.yu.ding.core.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author MrYu(YWG)
 * @date 2022-07-25 19:06:42
 * we recommend listener to extends this abstract event listener not to implement {@link SpringEventListener}
 */
public abstract class BaseSpringEventListener implements SpringEventListener<ContextRefreshedEvent>{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public BaseSpringEventListener(){
        register();
    }
    private void register(){
        ApplicationContextUtil.addListener(this);
    }
}
