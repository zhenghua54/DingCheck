package com.yu.ding.constant;

import com.yu.ding.entity.AttendanceItem;
import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 11:14
 */
public class DefineAttendanceTime {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * 时间正则表达式
     */
    public static final String DATE_TIME_REG = "^\\d{4}(-)(1[0-2]|0?\\d)\\1([0-2]\\d|\\d|30|31) (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    public static final String TIME_REG = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    public static final String DATE_REG = "^\\d{4}(-)(1[0-2]|0?\\d)\\1([0-2]\\d|\\d|30|31)$";

    public static long morOnTime = getSecondOfDayByTime("09:00:00");
    public static long morOffTime = getSecondOfDayByTime("11:30:00");
    public static long morDefineOnTime = getSecondOfDayByTime("00:00:00");
    public static long morDefineOffTime = getSecondOfDayByTime("12:30:00");


    public static long aftOnTime = getSecondOfDayByTime("13:30:00");
    public static long aftOffTime = getSecondOfDayByTime("17:30:00");
    public static long aftDefineOnTime = getSecondOfDayByTime("12:30:00");
    public static long aftDefineOffTime = getSecondOfDayByTime("18:00:00");

    public static long eveOnTime = getSecondOfDayByTime("18:30:00");
    public static long eveOffTime = getSecondOfDayByTime("20:55:00");
    public static long eveDefineOnTime = getSecondOfDayByTime("18:00:00");
    public static long eveDefineOffTime = getSecondOfDayByTime("23:59:59");

    /**
     * minute, 正常打卡得弹性时间， 6次打卡没有弹性时间
     */
    public static Integer elastic = 5;


    public static AttendanceItem initMorning(Integer elasticTime){
        return new AttendanceItem(morDefineOnTime, morDefineOffTime, morOnTime, morOffTime, elastic);
    }
    public static AttendanceItem initAfternoon(Integer elasticTime){
        return new AttendanceItem(aftDefineOnTime, aftDefineOffTime, aftOnTime, aftOffTime, elastic);
    }
    public static AttendanceItem initEvening(Integer elasticTime){
        return new AttendanceItem(eveDefineOnTime, eveDefineOffTime, eveOnTime, eveOffTime, elastic);
    }
    public static AttendanceItem initAllDay(Integer elasticTime){
        return new AttendanceItem(morDefineOnTime, eveDefineOffTime, morOnTime, eveOffTime, elastic);
    }
//    /**
//     * 根据时间获取当天所在的秒数
//     * @param args
//     * @throws ParseException
//     */
//    public static Long getSecondOfDayByDate(String time){
//        if(time == null){
//            System.out.println("时间为空!");
//            return -1L;
//        }
//        if(time.matches(DATE_TIME_REG)){
//            return getSecondOfDayByTime(time.split(" ")[1]);
//        }else if(time.matches(TIME_REG)){
//            return getSecondOfDayByTime(time);
//        }else {
//            System.out.println("时间格式不对!yyyy-MM-dd HH:mm:ss");
//            return -1L;
//        }
//    }
    public static Long getSecondOfDayByTime(String time){
        String[] split = time.trim().split(":");
        return Long.parseLong(split[0])*3600+Long.parseLong(split[1])*60+Long.parseLong(split[2]);
    }
}
