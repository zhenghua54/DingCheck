package com.yu.ding.core.mapper;

import com.yu.ding.core.mapper.exception.MapperNotFoundException;
import com.yu.ding.core.mapper.listener.MapperListener;
import com.yu.ding.core.mapper.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MrYu(YWG)
 * @date 2022-07-26 10:34:17
 * manager the mapper info
 */
public class MapperManager {
    private final Logger logger = LoggerFactory.getLogger(MapperManager.class);
    /**
     * key resource Name, mapper info key : class name
     */
    private ConcurrentHashMap<String, Map<String, MapperInfo>> mapperInfos;
    /**
     * parser cache
     */
    private ConcurrentHashMap<String, Parser> parserCaches;

    private List<MapperListener> listeners;
    /**
     * TODO sql proxy
     */
    /**
     * mapper resource holder
     */
    private MapperResourceHolder mapperResourceHolder;
    /**
     * parser
     */
    private List<Parser> parsers;

    public MapperManager(){
        init();
    }
    public MapperManager(List<Parser> parsers){
        this();
        this.parsers.addAll(parsers);

    }
    private void init(){
        mapperResourceHolder = new MapperResourceHolder();
        //inject mapperManager
        mapperResourceHolder.injectMapperManager(this);
        mapperInfos = new ConcurrentHashMap<>(2);
        parserCaches = new ConcurrentHashMap<>(2);
        listeners = Collections.emptyList();
        //TODO is or not thread safe?
//        parsers = Collections.synchronizedList(Collections.emptyList());
        parsers = Collections.emptyList();
        parseResources();
    }

    /**
     * init parse
     */
    private void parseResources(){
        for(Parser parser : parsers){
            mapperResourceHolder.getMapperResource(parser).forEach(r->{
                try {
                    mapperInfos.put(r.getResourceName(), parser.parseMapper(r));
                } catch (Exception e) {
                    logger.error("parse error: resource is not correspond. parse[{}], resource[{}]", parser.getClass().getSimpleName(), r.getResourceName());
                }
            });
        }
    }
    public void addParser(Parser parser){
        parsers.add(parser);
    }

    /**
     * throw the exception if parse object is not correspond
     * @param name
     * @throws Exception
     */
    public void reParse(String name) throws Exception {
        if(!mapperInfos.containsKey(name)){
            logger.warn("the resource name[{}] don't exist! please check.", name);
            return;
        }
        mapperInfos.put(name, parserCaches.get(name).parseMapper(mapperResourceHolder.getMapperResource(name)));

        //callback
        listeners.forEach(l -> {
            l.reParse(name);
        });
    }

    /**
     * reparse single mapper
     */
    public void reParseMapper(String resourceName, String mapperName){
        Parser p = parserCaches.get(resourceName);
        try {
            mapperInfos.get(resourceName).put(mapperName, p.parseSingleMapper(mapperName));
        } catch (MapperNotFoundException e) {
            logger.error("the mapper[{}] is not found in the mapper resource[{}]", mapperName, resourceName);
        }
    }
    public void addListener(MapperListener listener){
        this.listeners.add(listener);
    }

    public <T> T getMapper(Class<T> tClass){
        return null;
    }

    static class MapperInvocationHandler implements InvocationHandler{
        private MapperManager mapperManager;

        //TODO the persistence proxy need implement the lookup of the persistence Type , so they can choice which db to save data

        public MapperInvocationHandler(MapperManager mapperManager) {
            this.mapperManager = mapperManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
        private void before(Object proxy, Method method, Object[] args){
            mapperManager.listeners.forEach(l -> {
                l.beforeInvoke(proxy, method, args);
            });
        }
        private void after(Object res){
            mapperManager.listeners.forEach(l -> {
                l.afterInvoke(res);
            });
        }
    }


}
