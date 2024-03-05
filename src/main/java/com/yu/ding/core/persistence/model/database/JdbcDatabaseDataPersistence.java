package com.yu.ding.core.persistence.model.database;

import com.yu.ding.core.environment.Environment;
import com.yu.ding.core.environment.SpringEnvironment;
import com.yu.ding.core.persistence.model.AbstractDataPersistence;
import com.yu.ding.core.persistence.model.database.filter.Filter;
import com.yu.ding.core.persistence.model.database.filter.FilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:06:22
 */
public class JdbcDatabaseDataPersistence extends AbstractDataPersistence{
    private final Logger logger = LoggerFactory.getLogger(JdbcDatabaseDataPersistence.class);
    private DataSource dataSource;
    private DatabaseOperate operation;
    private FilterChain filterChain;
    /**
     * env
     */
    private Environment environment;
//    /**
//     * sql builder holder
//     */
//    private final SqlBuilderHolder sqlBuilderHolder = SqlBuilderHolder.getINSTANCE();
    private ConcurrentHashMap<String, Object> resCaches;


    public JdbcDatabaseDataPersistence(Environment environment){
        this.environment = environment;
    }
    public JdbcDatabaseDataPersistence(DataSource dataSource){
        this.dataSource = dataSource;
    }
    private void init(){
        //get the datasource from the env
        if(SpringEnvironment.class.isAssignableFrom(environment.getClass())){
            this.dataSource = ((SpringEnvironment)environment).getApplicationContext().getBean(DataSource.class);
            injectFilters(((SpringEnvironment)environment).getApplicationContext());
        }
        if(this.dataSource == null){
            //create the datasource
        }else{
            logger.info("load datasource from the spring environment!");
        }
        operation = new DatabaseOperateImpl(dataSource);

    }
    //inject filters if application context exist
    private void injectFilters(ApplicationContext context){
        context.getBeansOfType(Filter.class).forEach((k, v) -> {
            logger.info("inject filter from spring: name={}, class={}", v.getName(), v.getClass().getSimpleName());
            filterChain.addFilter(v);
        });
    }

    /**
     * add filter
     * @param filter
     */
    public void addFilter(Filter filter){
        this.filterChain.addFilter(filter);
    }

    /**
     * remove filter
     * @param name
     */
    public void removeFilter(String name){
        this.filterChain.removeFilter(name);
    }
    public void clearCaches(){

        resCaches = new ConcurrentHashMap<>();
    }
    /***********should support the cache**********/
    @Override
    public <R> R selectOne(String sql, Class<R> rClass) {
        return null;
    }

    @Override
    public <R> List<R> selectList(String sql, Class<R> rClass) {
        return null;
    }

    @Override
    public Integer insert(String sql) {
        return null;
    }

    @Override
    public Integer delete(String sql) {
        return null;
    }

    @Override
    public Integer update(String sql) {
        return null;
    }
}
