package com.yu.ding.core.environment;

import java.util.Properties;

/**
 * @author MrYu(YWG)
 * @date 2022-07-16 12:13:10
 * the application context holder environment should implement the listener interface
 */
public interface Environment {
    /**
     * get property
     */
    String getValue(String key);
    /**
     * add
     */
    void addProperty(String key, String value);
//    /**
//     * add propertis
//     */
//    void appendProperties(Properties properties);
    /**
     * get name
     */
    String getName();
}
