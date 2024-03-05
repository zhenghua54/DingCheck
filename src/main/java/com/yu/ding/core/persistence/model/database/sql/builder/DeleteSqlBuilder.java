package com.yu.ding.core.persistence.model.database.sql.builder;

import com.yu.ding.core.persistence.model.database.sql.AbstractSqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MrYu(YWG)
 * @date 2022-07-15 11:14:52
 */
public class DeleteSqlBuilder extends AbstractSqlBuilder {
    private Logger logger = LoggerFactory.getLogger(DeleteSqlBuilder.class);
    @Override
    protected String createSql(String[] fields, String conditionStr, String table) {
        if(conditionStr.isEmpty()){
            logger.warn("the condition is empty!");
        }
        String sql = "delete from {table} where {condition}";
        sql = sql.replace(TABLE, table);
        sql = sql.replace(CONDITION, conditionStr);
        return sql;
    }
}
