package com.yu.ding.core.environment;

import com.yu.ding.core.listener.SpringEventListener;
import com.yu.ding.core.util.ApplicationContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

/**
 * @author MrYu(YWG)
 * @date 2022-07-16 12:17:39
 */
public class SpringEnvironment extends AbstractEnvironment implements SpringEventListener<ContextRefreshedEvent> {
    private final String NAME = "SPRING-ENVIRONMENT";
    /**
     * application holder
     */
    private ApplicationContext applicationContext;
    private Environment environment;

    public SpringEnvironment() {
        register();
    }

    private void register(){
        ApplicationContextUtil.addListener(this);
    }
    @Override
    public void onListener(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();
        this.environment = event.getApplicationContext().getEnvironment();
    }

    @Override
    public String getName() {
        return NAME;
    }
    public ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }

    @Override
    public String getValue(String key) {
        String res = super.getValue(key);
        return res == null ? environment.getProperty(key) : res;
    }
}
