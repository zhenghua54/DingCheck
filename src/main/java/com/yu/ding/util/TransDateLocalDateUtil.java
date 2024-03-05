package com.yu.ding.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author MrYu (YWG)
 * @date 2021-11-15 16:33
 */
public class TransDateLocalDateUtil {
    public static Date toDate(LocalDateTime dateTime){
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDateTime toLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
