package com.yu.ding.executor;

import com.taobao.api.ApiException;
import com.yu.ding.constant.AttendanceEnum;
import com.yu.ding.constant.DefineAttendanceTime;
import com.yu.ding.constant.SourceType;
import com.yu.ding.constant.TimeResult;
import com.yu.ding.context.Context;
import com.yu.ding.context.Parameter;
import com.yu.ding.entity.*;
import com.yu.ding.services.interfaces.CourseServiceInte;
import com.yu.ding.services.interfaces.DingServiceInte;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * @author MrYu (YWG)
 * @date 2021-11-13 11:08
 */
public class BaseExecutor implements ExecutorInte{
    private final CourseServiceInte courseService;
    private final DingServiceInte dingService;
    /**
     * 缓存用户一段时间得所有打卡记录
     */
    private final Map<String, Map<String, List<CardInfo>>> recordCache;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BaseExecutor(CourseServiceInte courseService, DingServiceInte dingService) {
        this.courseService = courseService;
        this.dingService = dingService;
        recordCache = new HashMap<>(128);
    }
    //    @Override
//    public AttendanceResult getAttendanceResult(String name, LocalDate time, boolean strict) throws ApiException, ParseException {
//        Date date = DefineAttendanceTime.DATE_FORMAT.parse(time.toString()+" 00:00:00");
//        if(time.getDayOfWeek().getValue() == 7){
//            return  dingService.ifHasAttendanceOnSunday(name, date);
//        }
//
//        return  null;
//    }
    //可添加个人的打卡次数，请假的情况
    private AttendanceResult getAttendanceResult(String name, LocalDate time, boolean strict, boolean isSixCard) throws ApiException, ParseException {
        name = name.trim();
        Date date = DefineAttendanceTime.DATE_FORMAT.parse(time.toString()+" 00:00:00");
        if(time.getDayOfWeek().getValue() == 7){
            return  dingService.ifHasAttendanceOnSunday(name, date);
        }

        UserVocation userVocation = dingService.getUserVocationData(name,date);
//        AttendanceTime attendanceTime = dingService.getUserDutyTime(name ,date);
//        List<Long> times = dingService.getUserAllTime(name, "date", "");
        List<CardInfo> times = recordCache.get(name).get(time.toString());
        //对time进行排序
        if(times != null) {
            times.sort(CardInfo::compare);
        }
        //无课程
        if(!Parameter.ifHasCourse){
            return getAttendanceResultWithCourse(userVocation,new UserCourse(), times, isSixCard,strict).setDate(time);
        }
        //有课程
        UserCourse userCourse = courseService.getUserCourseData(name, time);
//        System.out.println(userCourse.getMorningCourse()+"  "+userCourse.getAfternoonCourse()+"  "+userCourse.getEveningCourse());
        //课程处理输出
        if(userCourse == null){
            System.out.println("name = "+name+" 在系统中存在信息,无课程信息!");
            userCourse = new UserCourse();
        }
        //有无课程统一处理
//        if(strict) {
        return getAttendanceResultWithCourse(userVocation, userCourse, times, isSixCard, strict).setDate(time);
//        }else{
//            AttendanceResult res = AttendanceResult.HASCOURSE();
//            res.setDate(time);
//            return res;
//        }
    }
    //    public static void main(String[] args) throws ApiException {
//        DingService.init();
//        DingService service = new DingService("云技术2021");
//        new BaseExecutor().getAttendanceResult("俞万刚", DefineAttendanceTime.DATE_TIME_FORMATTER.parse("2022-02-24"));
//    }
    private boolean checkSourceTypeCorrect(String sourceType){
        return SourceType.ATM.equals(sourceType) || SourceType.DING_ATM.equals(sourceType);
    }
    //上班外勤
    private void onDutyOutside(AttendanceResult res, Long onDutyTime, Long startPlanTime){
        //上班迟到
        if(onDutyTime > startPlanTime){
            // 外勤+迟到
            res.addMorRes(AttendanceEnum.LATE);
            res.addAftRes(AttendanceEnum.LATE);
        }
    }
    //下班外勤
    private void offDutyOutside(AttendanceResult res, Long offDutyTime, Long endPlanTime){
        //下班早退
        if(offDutyTime < endPlanTime){
            res.addEveRes(AttendanceEnum.EARLY);
        }
    }
    //处理假期的情况
    private void processVocation(UserVocation userVocation, UserCourse userCourse){
        if(userVocation == null){
            return;
        }
        //早上是否有假期
        if(userVocation.judgeIfHasVocation(DefineAttendanceTime.morOnTime, DefineAttendanceTime.morOffTime)){
            userCourse.setMorningCourse(userVocation.getType());
        }
        if(userVocation.judgeIfHasVocation(DefineAttendanceTime.aftOnTime, DefineAttendanceTime.aftOffTime)){
            userCourse.setAfternoonCourse(userVocation.getType());
        }
        if(userVocation.judgeIfHasVocation(DefineAttendanceTime.eveOnTime, DefineAttendanceTime.eveOffTime)){
            userCourse.setEveningCourse(userVocation.getType());
        }
        if(!isEmpty(userCourse.getMorningCourse()) && !isEmpty(userCourse.getEveningCourse())){
            userCourse.setAfternoonCourse("无需打卡");
        }
    }
    private boolean isEmpty(String s){
        return s == null || s.trim().isEmpty();
    }
    private boolean betweenTime(Long targetTime, Long start, Long end){
        return targetTime >= start && targetTime <= end;
    }
    //    /**
//     * 无课程打卡
//     * @param userVocation
//     * @return
//     */
//    private AttendanceResult getAttendanceResultWithNoCourse(UserVocation userVocation, List<CardInfo> times, boolean isSixCard) throws ParseException {
//        AttendanceResult res = AttendanceResult.NORMAL();
////        if(userVocation != null){
////            hasVocation(res, userVocation, attendanceTime);
////            return res;
////        }
//        if(isSixCard) {
//            res.setMorningRes(DefineAttendanceTime.initMorning().analysis(times));
//            res.setAfternoonRes(DefineAttendanceTime.initAfternoon().analysis(times));
//            res.setEveningRes(DefineAttendanceTime.initEvening().analysis(times));
////        analysis(attendanceTime,DefineAttendanceTime.morOnTime, DefineAttendanceTime.eveOffTime, res, DefineAttendanceTime.elastic);
//        }else{
//            String rr = DefineAttendanceTime.initAllDay().analysis(times);
//            res.setMorningRes(rr);
//            res.setAfternoonRes(rr);
//            res.setEveningRes(rr);
//        }
//        //分析假期情况
//        processVocation(userVocation, res);
//        return res;
//    }
    private String convertLocalTime(Long time){
        return time == null ? null : time/3600+":"+time%3600/60+":"+time%60;
    }
    /**
     * 有课程打卡
     * @param userVocation
     * @return
     */
    private AttendanceResult getAttendanceResultWithCourse(UserVocation userVocation,UserCourse userCourse, List<CardInfo> times, boolean isSixCard, boolean strict) throws ParseException {
        AttendanceResult res = AttendanceResult.NORMAL();
        //分析假期情况, 归类到课程中
        processVocation(userVocation, userCourse);
        //0没课 1有课
        int mor = userCourse.getMorningCourse() == null ? 0 : 1;
        int aft = userCourse.getAfternoonCourse() == null ? 0 : 1;
        int eve = userCourse.getEveningCourse() == null ? 0 : 1;
        if(isSixCard) {
            res.setMorningRes(mor == 1 ? userCourse.getMorningCourse() : DefineAttendanceTime.initMorning(0).analysis(times));
            res.setAfternoonRes(aft == 1 ? userCourse.getAfternoonCourse() : DefineAttendanceTime.initAfternoon(0).analysis(times));
            res.setEveningRes(eve == 1 ? userCourse.getEveningCourse() : DefineAttendanceTime.initEvening(0).analysis(times));
        }else {
            String rr = "";
            String situation = String.valueOf(mor) + aft + eve;
            switch (situation) {
                case "000":
                    //无课得情况
                    String r1 = DefineAttendanceTime.initAllDay(DefineAttendanceTime.elastic).analysis(times);
                    res.setMorningRes(r1);
                    res.setAfternoonRes(r1);
                    res.setEveningRes(r1);
                    break;
                case "100":
                    if(strict) {
                        rr = new AttendanceItem(DefineAttendanceTime.aftDefineOnTime, DefineAttendanceTime.eveDefineOffTime, DefineAttendanceTime.aftOnTime, DefineAttendanceTime.eveOffTime, DefineAttendanceTime.elastic).analysis(times);
                    }else{
                        if(times == null){
                            rr = AttendanceEnum.LACK_OFFDUTY_CARD;
                        }else {
                            Long cardTime = times.get(times.size()-1).getCardTime();
                            rr = cardTime >= DefineAttendanceTime.eveOffTime ? AttendanceEnum.NORMAL + convertLocalTime(cardTime) : AttendanceEnum.EARLY + convertLocalTime(cardTime);
                        }
//                        rr = new AttendanceItem(DefineAttendanceTime.morDefineOnTime, DefineAttendanceTime.eveDefineOffTime, DefineAttendanceTime.aftOnTime, DefineAttendanceTime.eveOffTime, DefineAttendanceTime.elastic).analysis(times);
                    }
                    res.setMorningRes(userCourse.getMorningCourse());
                    res.setAfternoonRes(rr);
                    res.setEveningRes(rr);
                    break;
                case "010":
                    rr = DefineAttendanceTime.initAllDay(DefineAttendanceTime.elastic).analysis(times);
                    res.setMorningRes(rr);
                    res.setAfternoonRes(userCourse.getAfternoonCourse());
                    res.setEveningRes(rr);
                    break;
                case "110":
                    //设置课程
                    res.setMorningRes(userCourse.getMorningCourse());
                    res.setAfternoonRes(userCourse.getAfternoonCourse());
                    if(strict){
                        res.setEveningRes(DefineAttendanceTime.initEvening(DefineAttendanceTime.elastic).analysis(times));
                    }else{
                        if(times == null){
                            res.setEveningRes(AttendanceEnum.LACK_OFFDUTY_CARD);
                        }else{
                            Long cardTime = times.get(times.size()-1).getCardTime();
                            res.setEveningRes(cardTime >= DefineAttendanceTime.eveOffTime ? AttendanceEnum.NORMAL + convertLocalTime(cardTime) : AttendanceEnum.EARLY + convertLocalTime(cardTime));
                        }
                    }
                    break;
                case "001":
                    if(strict) {
                        rr = new AttendanceItem(DefineAttendanceTime.morDefineOnTime, DefineAttendanceTime.aftDefineOffTime, DefineAttendanceTime.morOnTime, DefineAttendanceTime.aftOffTime, DefineAttendanceTime.elastic).analysis(times);
                    }else{
                        if(times == null) {
                            rr = AttendanceEnum.LACK_OFFDUTY_CARD;
                        }else {
                            long cardTime = times.get(0).getCardTime();
                            rr = cardTime <= DefineAttendanceTime.morOnTime + DefineAttendanceTime.elastic ? AttendanceEnum.NORMAL + convertLocalTime(cardTime) : AttendanceEnum.LATE + convertLocalTime(cardTime);
                        }
//                        rr = new AttendanceItem(DefineAttendanceTime.morDefineOnTime, DefineAttendanceTime.eveDefineOffTime, DefineAttendanceTime.morOnTime, DefineAttendanceTime.aftOffTime, DefineAttendanceTime.elastic).analysis(times);
                    }

                    res.setEveningRes(userCourse.getEveningCourse());
                    res.setMorningRes(rr);
                    res.setAfternoonRes(rr);
                    break;
                case "101":
                    if(times == null){
                        res.setMorningRes(userCourse.getMorningCourse());
                        res.setAfternoonRes(AttendanceEnum.NO_NEED_CARD);
                        res.setEveningRes(userCourse.getEveningCourse());
                        break;
                    }
                    res.setMorningRes(userCourse.getMorningCourse());
                    res.setAfternoonRes(strict ? DefineAttendanceTime.initAfternoon(DefineAttendanceTime.elastic).analysis(times) : AttendanceEnum.NO_NEED_CARD);
                    res.setEveningRes(userCourse.getEveningCourse());
                    break;
                case "011":
                    if(strict){
                        res.setMorningRes(DefineAttendanceTime.initMorning(DefineAttendanceTime.elastic).analysis(times));
                    }else{
                        if(times == null){
                            res.setMorningRes(AttendanceEnum.LACK_ONDUTY_CARD);
                        }else{
                            long cardTime = times.get(0).getCardTime();
                            res.setMorningRes(cardTime <= DefineAttendanceTime.morOnTime + DefineAttendanceTime.elastic ? AttendanceEnum.NORMAL + convertLocalTime(cardTime): AttendanceEnum.LATE + convertLocalTime(cardTime));
                        }
                    }
//                    res.setMorningRes(strict ? DefineAttendanceTime.initMorning(DefineAttendanceTime.elastic).analysis(times) : new AttendanceItem(DefineAttendanceTime.morDefineOnTime, DefineAttendanceTime.eveDefineOffTime, DefineAttendanceTime.morOnTime, DefineAttendanceTime.morOffTime, DefineAttendanceTime.elastic).analysis(times));
                    res.setAfternoonRes(userCourse.getAfternoonCourse());
                    res.setEveningRes(userCourse.getEveningCourse());
                    break;
                case "111":
                    res.setMorningRes(userCourse.getMorningCourse());
                    res.setAfternoonRes(userCourse.getAfternoonCourse());
                    res.setEveningRes(userCourse.getEveningCourse());
                    break;
                default:
                    System.out.println("ERROR");
            }
        }
        return res;
    }

    private void  cacheAttendanceTimeByRange(String name, String  startDate, String endDate) throws ParseException, ApiException {
        recordCache.put(name, dingService.getUserAllTime(name, startDate, endDate));
    }
    @Override
    public List<AttendanceResult> getAttendanceResultOnDate(String name, LocalDate startDate, LocalDate endDate, boolean strict) throws ApiException, ParseException {
        //记录范围时间得用户打卡情况, 并且记录了打卡类型
        cacheAttendanceTimeByRange(name, startDate.toString()+" 00:00:00", endDate.toString()+" 23:59:59");
        List<AttendanceResult> res = new ArrayList<>();
        boolean isSixCard = Context.getUserRule(name) == null || Context.getUserRule(name);
        res.add(getAttendanceResult(name, startDate, strict, isSixCard));
        LocalDate temp = startDate;
        while((temp = temp.plusDays(1)) .isBefore(endDate)){
            res.add(getAttendanceResult(name, temp, strict, isSixCard));
        }
        //大于一日，加入结束日期
        if(!startDate.toString().equals(endDate.toString())) {
            res.add(getAttendanceResult(name, endDate, strict, isSixCard));
        }
        return res;
    }
}
