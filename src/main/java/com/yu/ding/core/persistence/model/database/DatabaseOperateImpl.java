package com.yu.ding.core.persistence.model.database;

import com.yu.ding.core.persistence.model.database.sql.SqlContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import javax.sql.DataSource;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 19:42:35
 */
public class DatabaseOperateImpl implements DatabaseOperate{
    private DataSource dataSource;
    private JdbcTemplate jt;
    private TransactionTemplate tjt;

    public DatabaseOperateImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        jt = new JdbcTemplate();
        jt.setDataSource(dataSource);
        //inject the datasource the transactionManager can manage transaction
        tjt = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

//    public DatabaseOperateImpl(JdbcTemplate jt, TransactionTemplate tjt) {
//        this.jt = jt;
//        this.tjt = tjt;
//    }

    @Override
    public DataSource getDatasource() {
        return dataSource;
    }

    @Override
    public Map<String, Object> queryOne(String sql) {
        return jt.queryForMap(sql);
    }
    @Override
    public <R> R queryOne(String sql, Object[] args, Class<R> tClazz) {
        return jt.queryForObject(sql, args, tClazz);
    }

    @Override
    public <R> R queryOne(String sql, Object[] args, RowMapper<R> rowMapper) {
        return jt.queryForObject(sql, args, rowMapper);
    }

    @Override
    public List<Map<String, Object>> queryMany(String sql, Object[] args) {
        return jt.queryForList(sql, args);
    }

    @Override
    public <R> List<R> queryMany(String sql, Object[] args, Class<R> tClazz) {
        return jt.queryForList(sql, tClazz, args);
    }

    @Override
    public <R> List<R> queryMany(String sql, Object[] args, RowMapper<R> rowMapper) {
        return jt.query(sql, args, rowMapper);
    }

    /**
     * update in transaction
     * @param sqlContext
     * @return
     */
    @Override
    public Boolean update(SqlContext sqlContext) {
        return tjt.execute(transactionStatus -> {
            try {
                jt.update(sqlContext.getSql(), sqlContext.getArgs());
                return Boolean.TRUE;
            } catch (Exception e){
                //roll back
                transactionStatus.setRollbackOnly();
                return Boolean.FALSE;
            }
        });
    }

    @Override
    public Boolean batchUpdate(List<SqlContext> sqlContexts) {
        return tjt.execute(transactionStatus -> {
            try {
                sqlContexts.stream().forEach(sqlContext -> {
                    jt.update(sqlContext.getSql(), sqlContext.getArgs());
                });
                return Boolean.TRUE;
            } catch (Exception ex){
                transactionStatus.setRollbackOnly();
                return Boolean.FALSE;
            }
        });
    }
}
