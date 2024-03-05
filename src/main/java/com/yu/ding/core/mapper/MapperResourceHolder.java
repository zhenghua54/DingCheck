package com.yu.ding.core.mapper;

import com.yu.ding.core.mapper.parser.Parser;
import com.yu.ding.core.mapper.resource.MapperResource;
import com.yu.ding.core.mapper.resource.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrYu(YWG)
 * @date 2022-07-26 09:32:28
 * hold the mapper resource
 */
public class MapperResourceHolder {
    private Logger logger = LoggerFactory.getLogger(MapperResourceHolder.class);
    private Map<String, MapperResource> resourceMap;

    public MapperResourceHolder(){
        resourceMap = new HashMap<>(2);
        loadResource();
    }
    /**
     * load resource by spi
     */
    private void loadResource(){
        ServiceLoader<MapperResource> resources = ServiceLoader.load(MapperResource.class);
        Iterator<MapperResource> iterator = resources.iterator();
        MapperResource m = null;
        while(iterator.hasNext()){
            m = iterator.next();
            if(this.resourceMap.putIfAbsent(m.getResourceName(), m)!=null){
                logger.warn("it already exist the {} resource! please don't repeat to add.", m.getResourceName());
            }
        }
    }

    public MapperResource getMapperResource(String name){
        return this.resourceMap.get(name);
    }

    /**
     * get the mapper resource by parser
     * @param parser
     * @return null if don't have special resource
     */
    public List<MapperResource> getMapperResource(Parser parser){
        //judge if the resource name is null
        MapperResource resource = parser.getResourceName() == null ? null : getMapperResource(parser.getResourceName());
        if(resource == null){
            logger.warn("the resource holder don't have the name[{}] resource! try parse the same class...", parser.getResourceName());
            //parse the super interface parameterized type
            Class typeClass = (Class) ((ParameterizedType)parser.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            ArrayList<MapperResource> resourceList = this.resourceMap.values().stream().filter(v -> {
                return typeClass.isAssignableFrom(v.getClass());
            }).collect(Collectors.toCollection(ArrayList::new));
            if(resourceList.isEmpty()){
                logger.warn("don't have correspond mapper resource for the parse[{}]", parser.getClass().getSimpleName());
                return null;
            }else{
                //return the first resource of correspond resource list
                return resourceList;
            }
        }
        //to list
        return Collections.singletonList(resource);
    }
//    public Integer getResourceSize(){
//        return this.resourceMap.size();
//    }

    /**
     * inject the mapperManager if necessary
     * @param mapperManager
     */
    public void injectMapperManager(MapperManager mapperManager){
        this.resourceMap.values().stream().filter(c ->
                Container.class.isAssignableFrom(c.getClass())
        ).forEach(v -> {
            ((Container) v).injectMapperManager(mapperManager);
        });
    }
//    public static void main(String[] args){
////        System.out.println(new MapperResourceHolder().getMapperResource(new Parser<Container>() {
////            @Override
////            public List<MapperInfo> parseMapper(Container target) throws Exception {
////                return null;
////            }
////
////            @Override
////            public String getResourceName() {
////                return null;
////            }
////        }));
//        System.out.println(MapperResourceHolder.class.getSimpleName());
//    }
}
