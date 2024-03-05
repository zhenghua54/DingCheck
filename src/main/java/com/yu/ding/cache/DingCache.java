package com.yu.ding.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 10:56
 */
public class DingCache {

    private static volatile DingCache instance;




    private DingCache(){}
    /**
     * 缓存name - id 信息
     */
    private static final Cache<String, String> idCache = CacheBuilder.newBuilder().build();
    public static String getUserId(String name){
        return idCache.getIfPresent(name);
    }
    public static void saveUserId(String name, String id){
        idCache.put(name, id);
    }

    /**
     * 双重校验实现单例模式
     * @return
     */
    public static DingCache getInstance(){
        if(instance == null){
            synchronized (DingCache.class){
                if(instance == null) {
                    instance = new DingCache();
                }
            }
        }
        return instance;
    }


}