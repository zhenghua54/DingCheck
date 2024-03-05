package com.yu.ding.core.mapper.parser;

import com.yu.ding.core.mapper.MapperInfo;
import com.yu.ding.core.mapper.exception.MapperNotFoundException;
import com.yu.ding.core.mapper.resource.MapperResource;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-07-25 16:22:40
 */
public interface Parser<T extends MapperResource> {
    /**
     * parse mapper info from the target
     * @param target
     * @return
     * @throws Exception
     */
    Map<String, MapperInfo> parseMapper(T target) throws Exception;

    /**
     * parse single
     * @param mapperName
     * @return
     */
    MapperInfo parseSingleMapper(String mapperName) throws MapperNotFoundException;

    /**
     * if need parse special resource , return the resource name
     * @return
     */
    default String getResourceName(){
        return null;
    }
}
