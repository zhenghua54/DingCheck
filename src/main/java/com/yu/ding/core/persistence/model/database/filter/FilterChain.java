package com.yu.ding.core.persistence.model.database.filter;

import com.yu.ding.core.persistence.model.database.sql.SqlContext;

import java.util.ArrayList;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:27:58
 */
public class FilterChain {
    private ArrayList<Filter> filters;
    private Integer index = 0;

    public FilterChain() {
        filters = new ArrayList<>(8);
    }

    public FilterChain(ArrayList<Filter> filters){
        this.filters = filters;
    }
    public void filter(SqlContext sqlContext){
        filters.get(index).doFilter(sqlContext, this);
        index++;
    }
    /**
     * add filter
     */
    public void addFilter(Filter filter){
        this.filters.add(filter);
    }
    /**
     *  remove filter
     */
    public void removeFilter(String name){
        filters.removeIf(f -> f.getName().equals(name));
    }
}
