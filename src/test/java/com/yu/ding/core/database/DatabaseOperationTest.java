package com.yu.ding.core.database;

import com.yu.ding.core.persistence.model.database.DatabaseOperateImpl;
import com.yu.ding.core.persistence.model.database.sql.SqlContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-07-08 10:46:56
 */
public class DatabaseOperationTest {
    private static Logger logger = LoggerFactory.getLogger(DatabaseOperationTest.class);
    public static DataSource buildDatasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("654321.");
        return dataSource;
    }
    @Test
    public void queryMany(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "select * from user";
        Object[] args = new Object[]{};
        SqlContext sqlContext = new SqlContext( sql, args);
//        boolean res = databaseOperate.update(new SqlContext("insert into user(?, ?, ?) values(?, ?, ?)", new Object[]{"name", "age", "address", "mryu", "25", "杭州"}));
        databaseOperate.queryMany(sqlContext.getSql(), sqlContext.getArgs()).forEach(item->{
            StringBuilder stringBuilder = new StringBuilder();
            for(Map.Entry<String, Object> entry: item.entrySet()){
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
            }
            logger.info(stringBuilder.toString());
            logger.info("********************");
        });
    }
    @Test
    public void insert(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "insert into user(name, age, address) values(?, ?, ?)";
        Object[] args = new Object[]{"mryu", 25, "杭州"};
        SqlContext sqlContext = new SqlContext(sql, args);
        System.out.println(databaseOperate.update(sqlContext));
    }
    @Test
    public void batchInsert(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "insert into user(name, age, address) values(?, ?, ?)";
        Object[] args = new Object[]{"mryu", 25, "杭州"};
        SqlContext sqlContext = new SqlContext( sql, args);
        List<SqlContext> sqls = new ArrayList<>();
        sqls.add(sqlContext);
        sqls.add(new SqlContext(sql, new Object[]{"dyy", 20, "南昌"}));
        System.out.println(databaseOperate.batchUpdate(sqls));
    }
    @Test
    public void delete(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "delete from user where name = ?";
        Object[] args = new Object[]{"a"};
        SqlContext sqlContext = new SqlContext( sql, args);
        System.out.println(databaseOperate.update(sqlContext));
    }
    @Test
    public void deleteAll(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "delete from user where 1=1";
        Object[] args = new Object[]{"a"};
        SqlContext sqlContext = new SqlContext( sql, null);
        System.out.println(databaseOperate.update(sqlContext));
    }
    @Test
    public void modify(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "update user set age = ? where name = ?";
        Object[] args = new Object[]{100, "mryu"};
        SqlContext sqlContext = new SqlContext( sql, args);
        System.out.println(databaseOperate.update(sqlContext));
    }
    @Test
    public void doInsert(){
        DatabaseOperateImpl databaseOperate = new DatabaseOperateImpl(buildDatasource());
        String sql = "insert into user(name, age, address) values(?, ?, ?)";
        Object[] args = new Object[]{"hello", 25, "杭州"};
        SqlContext sqlContext = new SqlContext( sql, args);
        System.out.println(databaseOperate.update(sqlContext));
        sqlContext.setArgs(new Object[]{"mryu", 22, "fsfs"});
        System.out.println(databaseOperate.update(sqlContext));
    }
}
