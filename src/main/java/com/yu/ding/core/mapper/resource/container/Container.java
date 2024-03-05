package com.yu.ding.core.mapper.resource.container;

import com.yu.ding.core.mapper.Mapper;
import com.yu.ding.core.mapper.MapperManager;
import com.yu.ding.core.mapper.resource.MapperResource;

/**
 * @author MrYu(YWG)
 * @date 2022-07-25 19:03:51
 *
 */
public interface Container extends MapperResource {
    /**
     * get the mapper object from the container
     * @param clazz
     * @param <T>
     * @return the exact mapper object of the clazz
     */
    default  <T extends Mapper> T getMapper(Class<T> clazz){
        return getBean(clazz);
    }

    /**
     * get mapper through mapper name
     * @param mapperName
     * @return
     */
    default Mapper getMapper(String mapperName){
        return (Mapper) getBean(mapperName);
    }

    /**
     * get bean from the container by bean Name
     * @param beanName
     * @param <T>
     * @return
     */
    Object getBean(String beanName);

    /**
     * get the bean from the container by the class of bean
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> tClass);

    /**
     * inject mapper manager
     * @param mapperManager
     */
    void injectMapperManager(MapperManager mapperManager);
}
