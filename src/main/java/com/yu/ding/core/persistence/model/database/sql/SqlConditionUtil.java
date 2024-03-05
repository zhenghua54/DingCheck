package com.yu.ding.core.persistence.model.database.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MrYu(YWG)
 * @date 2022-07-08 10:03:01
 */
public class SqlConditionUtil {
    private final Logger logger = LoggerFactory.getLogger(SqlConditionUtil.class);
    /*************con**************/
    private static final String GREATER = ">";
    private static final String LESS = "<";
    private static final String EQUALS = "=";
    /***************************/
    private static volatile SqlConditionUtil instance;
    private SqlConditionUtil(){}
    public static SqlConditionUtil getInstance(){
        if(instance == null){
            synchronized (SqlConditionUtil.class){
                if(instance == null) {
                    instance = new SqlConditionUtil();
                }
            }
        }
        return instance;
    }

    public ConditionData greaterThan(String field, Object value){
        return new ConditionData(field, value, GREATER);
    };
    public ConditionData lessThan(String field, String value){
        return new ConditionData(field, value, LESS);
    }
    public ConditionData equal(String field, String value){
        return new ConditionData(field, value, EQUALS);
    }

    public static class ConditionData{
        private String conditionKey;
        private Object conditionValue;
        //ex : = , > , <
        private String con;
        private boolean paramSetLater = true;
        public ConditionData(String conditionKey, Object conditionValue, String con){
            this.con = con;
            this.conditionKey = conditionKey;
            this.conditionValue = conditionValue;
        }

        public ConditionData(String conditionKey, String conditionValue, String con, boolean paramSetLater) {
            this.conditionKey = conditionKey;
            this.conditionValue = conditionValue;
            this.con = con;
            this.paramSetLater = paramSetLater;
        }

        public String getConditionKey() {
            return conditionKey;
        }

        public Object getConditionValue() {
            return conditionValue;
        }

        public String getCon() {
            return con;
        }

        public boolean isParamSetLater() {
            return paramSetLater;
        }
        public void setParamSetLater(){
            this.paramSetLater = false;
        }
        public String toConditionString(){
            //copy the value
            Object v = conditionValue;
            if(String.class.isAssignableFrom(v.getClass()) || Boolean.class.isAssignableFrom(v.getClass())){
                v = "'"+v+"'";
            }
            return paramSetLater ? conditionKey + con + "?" : conditionKey + con + v;
        }
    }
}
