package com.yu.ding.core.persistence.model.database.sql.builder;

import com.yu.ding.core.persistence.model.database.sql.AbstractSqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MrYu(YWG)
 * @date 2022-07-15 11:15:13
 */
public class UpdateSqlBuilder extends AbstractSqlBuilder {
    private final Logger logger = LoggerFactory.getLogger(UpdateSqlBuilder.class);
    @Override
    protected String createSql(String[] fields, String conditionStr, String table) {
        String sql = "update {table} set {field=param} where {condition}";
        sql = sql.replace(TABLE, table);
        sql = sql.replace(FIELD_PARAM, parseFields(fields));
        sql = sql.replace(CONDITION, conditionStr);
        return sql;
    }
    private String parseFields(String[] fields){
        if(fields == null || fields.length == 0){
            logger.warn("the fields is null!");
        }
        StringBuilder stringBuilder = new StringBuilder(fields[0]+"=?");
        for(int i = 1; i < fields.length; i++){
            stringBuilder.append(",").append(fields[i]).append("=?");
        }
        return stringBuilder.toString();
    }
}
