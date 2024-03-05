package com.yu.ding.core.persistence.model;

import com.yu.ding.core.cache.Cache;
import com.yu.ding.core.cache.CacheKey;
import com.yu.ding.core.persistence.DataPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 12:29:44
 * file and database
 */
public abstract class AbstractDataPersistence implements DataPersistence {
    private final Logger logger = LoggerFactory.getLogger(AbstractDataPersistence.class);
    protected Integer timeout;
    /**
     * record the result of the cache key, generally save persistence result
     * if you want to use advanced cache, please use decorators cache
     */
    private Cache<CacheKey, Object> caches;
    public AbstractDataPersistence(){
    }
    //TODO the proxy should implements the load methods
//    @Override
//    public void load() {
//    }
//    private void loadFromSpi(){}
//    private void loadFromContainer(Container container){};
    @Override
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    protected void putCache(CacheKey key, Object val){
        caches.put(key, val);
    }
    protected Object getCache(CacheKey key){
        return caches.get(key);
    }



}
