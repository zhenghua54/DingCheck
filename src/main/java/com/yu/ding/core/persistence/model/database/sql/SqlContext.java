package com.yu.ding.core.persistence.model.database.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:29:15
 */
public class SqlContext implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(SqlContext.class);
    /**
     * sql statement
     */
    private String sql = "";
    /**
     * arguments
     */
    private Object[] args;

    public SqlContext() {
    }

    public SqlContext(String sql, Object[] args) {
        this.sql = sql;
        if(this.sql == null){
            logger.warn("sql is null!");
            this.sql = "";
        }
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object obj) {
        SqlContext sqlContext = (SqlContext) obj;
        return Objects.equals(this.sql, sqlContext.getSql()) && compareArgs(sqlContext.getArgs());
    }
    //compare with the object args
    private boolean compareArgs(Object[] args){
        if(args == this.args){
            return true;
        }
        if(args == null || this.args == null || args.length!=this.args.length){
            return false;
        }
        for(int i=0; i<args.length; i++){
            if(!args[i].equals(this.args[i])){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args){
        SqlContext sq = new SqlContext("aaa", new Object[]{"a"});
        SqlContext sq1 = new SqlContext("aaa", new Object[]{"a"});
        System.out.println(sq.equals(sq1));
    }
}
