package com.yu.ding.core.persistence.model.database.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author MrYu(YWG)
 * @date 2022-07-13 10:38:00
 */
public abstract class AbstractSqlBuilder implements SqlBuilder{
    /**************tag***************/
    protected static final String FIELDS = "{fields}";
    protected static final String TABLE = "{table}";
    protected static final String PARAMS = "{params}";
    protected static final String CONDITION = "{condition}";
    protected static final String FIELD_PARAM = "{field=param}";
    /*****************************/

    private final Logger logger = LoggerFactory.getLogger(AbstractSqlBuilder.class);
//    /**
//     * expire cache
//     */
//    private ConcurrentHashMap<String, SqlContext> sqlCaches;
    private AtomicBoolean printSql = new AtomicBoolean(true);
    @Override
    public SqlContext build(String[] fields, Object[] params, Function<SqlConditionUtil, SqlConditionUtil.ConditionData[]> sqlCondition, String table) {
//        String baseSql = createBaseSql();
        SqlContext res = new SqlContext();
        //update
        if(sqlCondition!=null) {
            //get the condition util TODO will be null?
            SqlConditionUtil.ConditionData[] conditions = sqlCondition.apply(SqlConditionUtil.getInstance());
            ArrayList<Object> conditionParams = new ArrayList<>();
            //the condition param will save in the conditionParams
            String conditionStr = parseConditionData(conditions, conditionParams);
            res.setSql(createSql(fields, conditionStr, table));
            res.setArgs(mergeParams(params, conditionParams));
        }else{
            //insert
            res.setSql(createSql(fields,null, table));
            res.setArgs(params);
//            res.seta
        }
        if(printSql.get()){
            logger.info("sql: {} , params:{}", res.getSql(), res.getArgs());
        }
//        //cache
//        sqlCaches.put(res.getSqlID(), res);
        return res;
    }
    private Object[] mergeParams(Object[] param1, List<Object> param2){
        Object[] res = null;
        if(param1 != null){
            res = new Object[param1.length+param2.size()];
            System.arraycopy(param1, 0, res, 0, param1.length);
            System.arraycopy(param2.toArray(), 0, res, param1.length, param2.size());
        }else{
            res = new Object[param2.size()];
            System.arraycopy(param2.toArray(), 0, res, 0, param2.size());
        }
        return res;
    }

    private String parseConditionData(SqlConditionUtil.ConditionData[] datas, List<Object> conditionParams){
        StringBuilder resStr = new StringBuilder();
        for(SqlConditionUtil.ConditionData data : datas){
            if(data.isParamSetLater()){
                conditionParams.add(data.getConditionValue());
            }
            resStr.append("".equals(resStr.toString()) ? "" : " and ").append(data.toConditionString());
        }
        return resStr.toString();
    };
//    private String generateSqlID(){
//        return UUID.randomUUID().toString();
//    }
    /**
     * create the sql need subclass to implements
     * ex:
     *    select {fields} from {table} where {condition}
     *    insert into {table}({fields}) values({params})
     *    update {table} set {fields}={params} where {condition}
     *    delete from {table} where {condition}
     */
    protected abstract String createSql(String[] fields, String conditionStr, String table);

    @Override
    public void printSql(boolean var) {
        this.printSql.set(var);
    }
}
