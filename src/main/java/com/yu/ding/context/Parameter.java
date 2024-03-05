package com.yu.ding.context;

import lombok.Data;
import java.time.LocalDate;

/**
 * @author MrYu
 * @date 2021-11-15 10:12:43
 */
@Data
public class Parameter {
    private LocalDate startDate;
    private LocalDate endDate;
    private String depName;
    public static boolean ifHasCourse;
    private boolean strict;
    public static boolean withTime;
}
