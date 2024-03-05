package com.yu.ding.core.persistence.model.database.filter;

import com.yu.ding.core.persistence.model.database.sql.SqlContext;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:28:19
 * TODO order
 */
public interface Filter {
    default void doFilter(SqlContext sqlContext, FilterChain chain){
        chain.filter(sqlContext);
    }
    String getName();
}
