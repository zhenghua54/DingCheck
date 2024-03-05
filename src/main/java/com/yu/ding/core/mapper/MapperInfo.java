package com.yu.ding.core.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author MrYu(YWG)
 * @date 2022-07-25 16:16:43
 */
public class MapperInfo {
    private final Logger logger = LoggerFactory.getLogger(MapperInfo.class);
    private Pattern pattern = Pattern.compile("\\$\\{\\S*\\}");
    private Map<String, String> sqlCaches;
    private Map<String, Map<String, Object>> paramCaches;
    /**
     * decide which persistence
     */
    private String persistenceType;
    private String name;


    public MapperInfo(){
        this.sqlCaches = new HashMap<>(8);
        this.paramCaches = new HashMap<>(8);
        this.name = this.getClass().getSimpleName();

    }

    public MapperInfo(Map<String, String> sqlCaches, Map<String, Map<String, Object>> paramCaches) {
        this.sqlCaches = sqlCaches;
        this.paramCaches = paramCaches;
        this.name = this.getClass().getSimpleName();
    }

    public void bindSql(String methodName, String sql, Map<String, Object> paramPairs){
        if(this.sqlCaches.putIfAbsent(methodName, sql)!=null){
            logger.warn("mapper[{}] already bind sql. method:{}", name, methodName);
        }else{
            //bind the args of method
            this.paramCaches.put(methodName, paramPairs);
        }
    }
    public String getSql(String methodName){
        String sql = this.sqlCaches.get(methodName);
        for(Map.Entry<String, Object> entry : paramCaches.get(methodName).entrySet()){
            sql = sql.replace("${"+entry.getKey()+"}", toSqlString(entry.getValue()));
        }
        if(pattern.matcher(sql).find()){
            logger.warn("the sql has not complete! sql:{}", sql);
        }
//        sql = sql.replaceAll(pattern.pattern(), "?");
//        paramCaches.get(methodName)
        return sql;
    }

    /**
     * if the object is string , wrapper string with ''
     * @param o
     * @return
     */
    private String toSqlString(Object o){
        if(String.class.isAssignableFrom(o.getClass())){
            return "'"+o+"'";
        }
        return o.toString();
    }
//    private boolean validateParam(String methodName){
//        Pattern.compile("$").matcher("").
//    }

    /**
     * get mapper name
     * @return
     */
    public String getMapperName(){
        return this.name;
    }
    /**
     * look up
     */
    public String lookUp(){
        return this.persistenceType;
    }



//    public static class ParamPair{
//        private String key;
//        private String value;
//        private ParamPair(String key, String value){
//            this.key = key;
//            this.value = value;
//        }
//        public static ParamPair newPair(String key, String value){
//            return new ParamPair(key, value);
//        }
//    }
}
