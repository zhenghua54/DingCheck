package com.yu.ding.core.persistence.model.database.sql;

import com.yu.ding.core.persistence.model.database.sql.builder.DeleteSqlBuilder;
import com.yu.ding.core.persistence.model.database.sql.builder.InsertSqlBuilder;
import com.yu.ding.core.persistence.model.database.sql.builder.SelectSqlBuilder;
import com.yu.ding.core.persistence.model.database.sql.builder.UpdateSqlBuilder;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 15:24:07
 */
public class SqlBuilderHolder {
    private static volatile SqlBuilderHolder INSTANCE;

    private  InsertSqlBuilder insertSqlBuilder;
    private  DeleteSqlBuilder deleteSqlBuilder;
    private  UpdateSqlBuilder updateSqlBuilder;
    private  SelectSqlBuilder selectSqlBuilder;

    public SqlBuilderHolder() {
        insertSqlBuilder = new InsertSqlBuilder();
        deleteSqlBuilder = new DeleteSqlBuilder();
        updateSqlBuilder = new UpdateSqlBuilder();
        selectSqlBuilder = new SelectSqlBuilder();
    }

    public static SqlBuilderHolder getINSTANCE(){
        if(INSTANCE == null){
            synchronized (SqlBuilderHolder.class){
                if(INSTANCE == null){
                    INSTANCE = new SqlBuilderHolder();
                }
            }
        }
        return INSTANCE;
    }

    public SqlBuilder getDeleteSqlBuilder() {
        return deleteSqlBuilder;
    }

    public SqlBuilder getInsertSqlBuilder() {
        return insertSqlBuilder;
    }

    public SqlBuilder getSelectSqlBuilder() {
        return selectSqlBuilder;
    }

    public SqlBuilder getUpdateSqlBuilder() {
        return updateSqlBuilder;
    }
}
