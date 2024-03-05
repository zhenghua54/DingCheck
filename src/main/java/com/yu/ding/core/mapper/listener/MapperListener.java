package com.yu.ding.core.mapper.listener;

import java.lang.reflect.Method;

/**
 * @author MrYu(YWG)
 * @date 2022-07-26 18:42:28
 */
public interface MapperListener {
    /**
     * reParse callback
     */
    void reParse(String resourceName);

    /**
     * before invoke method
     * @param proxy
     * @param method
     * @param args
     */
    void beforeInvoke(Object proxy, Method method, Object[] args);

    /**
     * after invoke
     * @param res
     */
    void afterInvoke(Object res);
}
