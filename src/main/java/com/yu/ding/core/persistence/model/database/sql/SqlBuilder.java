package com.yu.ding.core.persistence.model.database.sql;

import java.util.function.Function;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:13:02
 *   example:
 *          select {fields} from {table} where {condition}
 *          insert into {table}({fields}) values({params})
 *          update {table} set {fields}={params} where {condition}
 *          delete from {table} where {condition}
 *
 */
public interface SqlBuilder {
    /**
     * build sql
     */
    SqlContext build(String[] fields, Object[] params, Function<SqlConditionUtil, SqlConditionUtil.ConditionData[]> sqlCondition, String table);


    /**
     * if print the sql
     * @param var
     */
    void printSql(boolean var);
}
