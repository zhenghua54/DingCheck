package com.yu.ding.core.persistence.model.database.sql.builder;

import com.yu.ding.core.persistence.model.database.sql.AbstractSqlBuilder;

/**
 * @author MrYu(YWG)
 * @date 2022-07-15 11:15:27
 */
public class SelectSqlBuilder extends AbstractSqlBuilder {
    @Override
    protected String createSql(String[] fields, String conditionStr, String table) {
        String sql = "select {fields} from {table} where {condition}";
        sql = sql.replace(FIELDS, String.join(",", fields));
        sql = sql.replace(TABLE, table);
        sql = sql.replace(CONDITION, conditionStr);
        return sql;
    }
}
