package com.yu.ding.core.persistence.model.database;

import com.yu.ding.core.persistence.model.database.sql.SqlContext;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:07:06
 */
public interface DatabaseOperate {
//    /**
//     * 初始化datasource
//     */
//    void initDatasource(DataSource dataSource);
    /**
     * single query
     */
    Map<String, Object> queryOne(String sql);
    <R> R queryOne(String sql, Object[] args, Class<R> tClazz);
    <R> R queryOne(String sql, Object[] args, RowMapper<R> rowMapper);

    /**
     * many query
     */
    List<Map<String, Object>> queryMany(String sql, Object[] args);
    <R> List<R> queryMany(String sql, Object[] args, Class<R> tClazz);
    <R> List<R> queryMany(String sql, Object[] args, RowMapper<R> rowMapper);


    Boolean update(SqlContext sqlContext);

    Boolean batchUpdate(List<SqlContext> sqlContexts);
    /**
     * get datasource
     */
    DataSource getDatasource();

}
