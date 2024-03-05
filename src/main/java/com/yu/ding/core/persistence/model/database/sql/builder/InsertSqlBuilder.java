package com.yu.ding.core.persistence.model.database.sql.builder;

import com.yu.ding.core.persistence.model.database.sql.AbstractSqlBuilder;

/**
 * @author MrYu(YWG)
 * @date 2022-07-15 11:14:21
 */
public class InsertSqlBuilder extends AbstractSqlBuilder {
    @Override
    protected String createSql(String[] fields, String conditionStr, String table) {
        String sql = "insert into {table}({fields}) values({params})";
        sql = sql.replace(FIELDS, String.join(",", fields));
        sql = sql.replace(TABLE, table);
        sql = sql.replace(PARAMS,genQueMark(fields.length));
        return sql;
    }
    private String genQueMark(int count){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i < count; i++){
            stringBuilder.append("?").append(i==count-1?"":",");
        }
        return stringBuilder.toString();
    }
}
