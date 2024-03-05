package com.yu.ding.services;

import com.yu.ding.context.Context;
import com.yu.ding.context.Parameter;
import com.yu.ding.entity.UserCourse;
import com.yu.ding.services.interfaces.CourseServiceInte;
import com.yu.ding.util.CourseParseUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 19:22
 */
public class CourseService implements CourseServiceInte {
    public CourseService(String file) throws IOException, InvalidFormatException {
        if(Parameter.ifHasCourse) {
            //初始化
            init(file);
        }
    }
    private void init(String file) throws IOException, InvalidFormatException {
        //解析课程数据, 异常抛出，异常不处理
        CourseParseUtil.parseUserCourse(file);
    }
    @Override
    public UserCourse getUserCourseData(String name, LocalDate dataTime) {
        return Context.getUserCourseByDate(name, dataTime);
    }
}
