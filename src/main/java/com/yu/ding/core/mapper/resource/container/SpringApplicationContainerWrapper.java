package com.yu.ding.core.mapper.resource.container;

import com.yu.ding.core.listener.BaseSpringEventListener;
import com.yu.ding.core.mapper.MapperManager;
import com.yu.ding.core.mapper.constant.ResourceConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author MrYu(YWG)
 * @date 2022-07-25 19:05:22
 */
public class SpringApplicationContainerWrapper extends BaseSpringEventListener implements Container{
    private ApplicationContext applicationContext;
    private MapperManager mapperManager;

//    @Override
//    public <T> T getMapper(Class<T> clazz) {
//        return this.applicationContext.getBean(clazz);
//    }


    @Override
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    @Override
    public void injectMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    /**
     * refresh the application context
     * @param event
     */
    @Override
    public void onListener(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();
        //reparse
        try{
            this.mapperManager.reParse(ResourceConstant.SPRING_CONTAINER);
        }catch (Exception ex){
            logger.error("reparse failed! the container may change but the mapper don't change. ex:{}", ex.getMessage());
        }

    }

    @Override
    public String getResourceName() {
        return ResourceConstant.SPRING_CONTAINER;
    }
}
