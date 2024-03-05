package com.yu.ding.services.interfaces;

import com.yu.ding.entity.UserCourse;
import java.time.LocalDate;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 11:02
 */
public interface CourseServiceInte {
    /**
     * 获取用户课程数据
     * @param name
     * @param dataTime
     * @return
     */
    UserCourse getUserCourseData(String name, LocalDate dataTime);
}
