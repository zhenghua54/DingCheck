package com.yu.ding.executor;

import com.taobao.api.ApiException;
import com.yu.ding.entity.AttendanceResult;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 11:06
 */
public interface ExecutorInte {
//    /**
//     * 获取用户某一天得考勤结果
//     * @param name
//     * @param time
//     * @return
//     */
//    AttendanceResult getAttendanceResult(String name, LocalDate date, boolean strict) throws ApiException, ParseException;
    /**
     * 获取一段时间的考勤
     */
    List<AttendanceResult> getAttendanceResultOnDate(String name, LocalDate startDate, LocalDate endDate, boolean strict) throws ApiException, ParseException;
}
