package com.yu.ding.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MrYu (YWG)
 * @date 2021-11-12 21:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCourse {
    /**
     * 早上课程
     */
    private String morningCourse;
    /**
     * 下午课程
     */
    private String afternoonCourse;
    /**
     * 晚上课程
     */
    private String eveningCourse;
}
