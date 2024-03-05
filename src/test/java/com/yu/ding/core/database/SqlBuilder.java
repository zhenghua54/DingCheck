package com.yu.ding.core.database;

import com.yu.ding.core.persistence.model.database.sql.SqlConditionUtil;
import com.yu.ding.core.persistence.model.database.sql.SqlContext;
import com.yu.ding.core.persistence.model.database.sql.builder.DeleteSqlBuilder;
import com.yu.ding.core.persistence.model.database.sql.builder.InsertSqlBuilder;
import com.yu.ding.core.persistence.model.database.sql.builder.SelectSqlBuilder;
import com.yu.ding.core.persistence.model.database.sql.builder.UpdateSqlBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author MrYu(YWG)
 * @date 2022-07-15 11:38:56
 */
public class SqlBuilder {
    @Test
    public void insertSqlBuilder(){
        InsertSqlBuilder insertSqlBuilder = new InsertSqlBuilder();
        SqlContext sqlContext = insertSqlBuilder.build(new String[]{"a", "b", "c"}, new String[]{"var1", "var2", "var3"}, null , "testTable");
        System.out.println(sqlContext.getSql());
    }
    @Test
    public void deleteSqlBuilder(){
        DeleteSqlBuilder deleteSqlBuilder = new DeleteSqlBuilder();
        SqlContext sqlContext = deleteSqlBuilder.build(null, null, (sqlConditionUtil)->{
            SqlConditionUtil.ConditionData[] data = new SqlConditionUtil.ConditionData[0];
//            data[0] = new SqlConditionUtil.ConditionData("field1", "var1", "=");
//            data[1] = new SqlConditionUtil.ConditionData("field2", "var2", ">");
            return data;
        }, "testTable");
        System.out.println(sqlContext.getSql());
    }
    @Test
    public void selectSqlBuilder(){
        SelectSqlBuilder selectSqlBuilder = new SelectSqlBuilder();
        SqlContext sqlContext = selectSqlBuilder.build(new String[]{"a", "b", "c"}, null, (sqlConditionUtil)->{
            SqlConditionUtil.ConditionData[] data = new SqlConditionUtil.ConditionData[2];
            data[0] = new SqlConditionUtil.ConditionData("field1", "var1", "=");
            data[1] = new SqlConditionUtil.ConditionData("field2", "var2", ">");
            return data;
        }, "testTable");
    }
    @Test
    public void updateSqlBuilder(){
        UpdateSqlBuilder updateSqlBuilder = new UpdateSqlBuilder();
        SqlContext sqlContext = updateSqlBuilder.build(new String[]{"a", "b", "c"},  new String[]{"var1", "var2", "var3"}, (sqlConditionUtil)->{
            SqlConditionUtil.ConditionData[] data = new SqlConditionUtil.ConditionData[2];
            data[0] = new SqlConditionUtil.ConditionData("field1", "var1", "=");
            data[1] = new SqlConditionUtil.ConditionData("field2", "var2", ">");
            return data;
        }, "testTable");
    }

}
