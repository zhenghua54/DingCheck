package com.yu.ding.entity;

import com.yu.ding.constant.AttendanceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * @author MrYu (YWG)
 * @date 2021-11-12 21:46
 * 考情结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResult {
    /**
     * 日期
     */
    private LocalDate date;
    /**
     * 早上的结果
     */
    private String morningRes;

    /**
     * 中午的结果
     */
    private String afternoonRes;

    /**
     * 晚上的结果
     */
    private String eveningRes;
    /**
     * 上班时间
     */
    private String onDuty;
    /**
     * 下班时间
     */
    private String offDuty;

    public static AttendanceResult NORMAL () {
        return new AttendanceResult(null, AttendanceEnum.NORMAL, AttendanceEnum.NORMAL, AttendanceEnum.NORMAL, null, null);
    }
    public static AttendanceResult HASCOURSE(){
        return new AttendanceResult(null, AttendanceEnum.COURSE,AttendanceEnum.COURSE,AttendanceEnum.COURSE, null, null);
    }
    public static AttendanceResult REST(){
        return new AttendanceResult(null, AttendanceEnum.REST,AttendanceEnum.REST,AttendanceEnum.REST, null, null);
    }
    public AttendanceResult withAttendanceTime(){
        this.morningRes = this.morningRes + " (" +  (this.onDuty == null ? null : this.onDuty) + ")";
        this.eveningRes = this.eveningRes + " (" +  (this.offDuty == null ? null : this.offDuty) + ")";
        return this;
    }

    public void addMorRes(String res){
        this.morningRes = this.morningRes + "+" + res;
    }
    public void addAftRes(String res){
        this.afternoonRes = this.afternoonRes + "+" + res;
    }
    public void addEveRes(String res){
        this.eveningRes = this.eveningRes + "+" + res;
    }
    @Override
    public String toString() {
        return "AttendanceResult{" +
                "date=" + date +
                ", morningRes='" + morningRes + '\'' +
                ", afternoonRes='" + afternoonRes + '\'' +
                ", eveningRes='" + eveningRes + '\'' +
                ", onDuty=" + onDuty +
                ", offDuty=" + offDuty +
                '}';
    }

    public AttendanceResult setDate(LocalDate date) {
        this.date = date;
        return this;
    }
}
