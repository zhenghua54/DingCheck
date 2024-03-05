package com.yu.ding.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MrYu(YWG)
 * @date 2022-09-26 10:10:56
 */
public class TimeUtil {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String timeToString(long time){
        return simpleDateFormat.format(new Date(time));
    }
}
