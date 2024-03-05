package com.yu.ding.core.mapper.parser;

import com.yu.ding.core.mapper.MapperInfo;
import com.yu.ding.core.mapper.exception.MapperNotFoundException;
import com.yu.ding.core.mapper.resource.container.Container;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-07-25 17:28:24
 * parse the mapper info from the application context;
 *
 */
public class ContainerParser implements Parser<Container>{
    @Override
    public Map<String, MapperInfo> parseMapper(Container target) throws Exception {
        return null;
    }

    @Override
    public MapperInfo parseSingleMapper(String mapperName) throws MapperNotFoundException {
        return null;
    }
}
