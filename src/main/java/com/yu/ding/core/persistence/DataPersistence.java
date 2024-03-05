package com.yu.ding.core.persistence;

import java.util.List;

/**
 * the jt will manage transaction
 * @author MrYu(YWG)
 * @date 2022-07-06 17:05:21
 */
public interface DataPersistence {
    /**
     * query one record from the db
     * @param sql sql statement
     * @param rClass the return class
     * @param <R> the exactly class of return
     * @return
     */
    <R> R selectOne(String sql, Class<R> rClass);
    <R> List<R> selectList(String sql, Class<R> rClass);

    /**
     * insert into db
     * @param sql
     * @return
     */
    Integer insert(String sql);

    /**
     * delete method
     * @param sql
     * @return
     */
    Integer delete(String sql);

    /**
     * update method
     * @param sql
     * @return
     */
    Integer update(String sql);

    /**
     * query timeout
     * @param timeout timeout
     */
    void setTimeout(Integer timeout);
//    /**
//     * load the data Persistence
//     */
//    void load();
    /**
     * get name
     * @return the name of the data persistence
     */
    default String getName(){
        return this.getClass().getSimpleName();
    }
}
