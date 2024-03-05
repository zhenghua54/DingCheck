package com.yu.ding.entity;

import com.dingtalk.api.response.OapiAttendanceGetupdatedataResponse;
import lombok.Data;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 10:33
 */
@Data
public class AttendanceTime {
    /**
     * 上班
     */
    private OapiAttendanceGetupdatedataResponse.AtAttendanceResultForOpenVo onDutyCard;
    /**
     * 下班
     */
    private OapiAttendanceGetupdatedataResponse.AtAttendanceResultForOpenVo offDutyCard;


}
