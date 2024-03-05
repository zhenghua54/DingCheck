package com.yu.ding.core.persistence.model.database.sql;

/**
 * @author MrYu(YWG)
 * @date 2022-07-08 10:01:29
 */
@FunctionalInterface
public interface SqlCondition {
    String getCondition(SqlConditionUtil conditionUtil);
}
