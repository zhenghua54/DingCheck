package com.yu.ding.core.environment;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author MrYu(YWG)
 * @date 2022-07-21 14:00:00
 */
public abstract class AbstractEnvironment implements Environment{
    private HashMap<String, String> properties;

    public AbstractEnvironment(HashMap<String, String> pros){
        this.properties = pros;
    }
    public AbstractEnvironment(){
        this.properties = new HashMap<>(8);
    }
    @Override
    public String getValue(String key) {
        return properties.get(key);
    }

    protected void appendProperties(Properties properties){
        properties.forEach(properties::putIfAbsent);
    }
    @Override
    public void addProperty(String key, String value){
        properties.putIfAbsent(key, value);
    }
}
