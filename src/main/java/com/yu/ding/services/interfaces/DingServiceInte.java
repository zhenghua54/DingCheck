package com.yu.ding.services.interfaces;

import com.taobao.api.ApiException;
import com.yu.ding.entity.AttendanceResult;
import com.yu.ding.entity.AttendanceTime;
import com.yu.ding.entity.CardInfo;
import com.yu.ding.entity.UserVocation;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 10:26
 */
public interface DingServiceInte {

    /**
     * 获取用户假期时间
     * @param name
     * @param dataTime
     * @return
     */
    UserVocation getUserVocationData(String name, Date dataTime) throws ApiException;

    /**
     * 获取用户打卡时间
     * @param name
     * @param dataTime
     * @return
     */
    AttendanceTime getUserDutyTime(String name, Date dataTime);

    /**
     * 获取用户打卡的所有时间, 时间格式yyyy-MM-dd HH:mm:ss
     */
    Map<String, List<CardInfo>> getUserAllTime(String name, String startDate, String endDate) throws ApiException, ParseException;



    /**
     * 用户是否周日打卡
     */
    AttendanceResult ifHasAttendanceOnSunday(String name, Date date);
}
