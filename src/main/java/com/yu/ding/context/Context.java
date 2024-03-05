package com.yu.ding.context;

import com.yu.ding.cache.DingCache;
import com.yu.ding.entity.UserCourse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 10:50
 */
public class Context {

    /**
     * 全局用户
     */
    private static final ArrayList<String> USER_NAMES = new ArrayList<>();

    /**
     * key
     */
    private static final String APP_KEY = "dingiu4ncfmgliacgcbo";
    /**
     * appSecret
     */
    private static final String APP_SECRET= "lO3HE0QOCq1ai2KhxEeGyuFv36Y5_g26d5DSTi4dEa1LSpkM4SIFXvZSC2Nt2YMO";

    public static String getAppKey(){
        return APP_KEY;
    }
    public static String getAppSecret(){
        return APP_SECRET;
    }
    /**
     * 用户电话号码
     */
    private static final Map<String, String> TELEPHONE_DATA = new HashMap<>();
    /**
     *  用户一周得课程情况
     */
    private static final Map<String, UserCourse[]> COURSE_DATA = new HashMap<>();

    /**
     * 用户的打卡规则True表示6次打卡， false 表示两次打卡
     */
    private static final Map<String, Boolean> USER_RULE = new HashMap<>();


    public static String getUserTelephone(String name) {
        return TELEPHONE_DATA.get(name);
    }
    public static void setUserTelephone(String name, String tel){
        TELEPHONE_DATA.put(name, tel);
    }
    public static UserCourse getUserCourseByDate(String name, LocalDate dateTime){
        UserCourse[] userCourse = COURSE_DATA.getOrDefault(name.trim(), new UserCourse[7]);
        return userCourse[dateTime.getDayOfWeek().getValue()-1];
    }

    /**
     *  courseArray不能存空值
     */
    public static void putUserCourse(String name, UserCourse[] courses){
        name = name.replaceAll("^[\\s　]+|[\\s　]+$", "");
        COURSE_DATA.put(name.trim(), courses);
    }
    public static void addUserName(String name){
        USER_NAMES.add(name.trim());
    }
    public static ArrayList<String> getUserNames(){
        return USER_NAMES;
    }

    /**
     * USERRULE
     */
    public static void putUserRule(String name, Boolean sixCard){
        USER_RULE.put(name.trim(), sixCard);
    }

    /**
     * 简化判断
     */
    public static Boolean getUserRule(String name){
//        if(USER_RULE.get(name) == null){
//            return false;
//        }else{
//            return USER_RULE.get(name);
//        }
        return USER_RULE.get(name);
    }
}
