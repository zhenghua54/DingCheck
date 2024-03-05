package com.yu.ding.entity;

import com.yu.ding.DingApplication;
import com.yu.ding.constant.AttendanceEnum;
import com.yu.ding.constant.SourceType;
import com.yu.ding.context.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrYu(YWG)
 * @date 2022-02-26 10:59:55
 * 打卡的子项
 */
public class AttendanceItem {
    /**
     * 定义打卡时间
     */
    private Long defineBeginTime;
    private Long defineEndTime;
    /**
     * 打卡的时间
     */
    private Long beginTime;
    private Long endTime;
    /**
     * 用户打卡时间
     */
    private CardInfo userBeginCardInfo;
    private CardInfo userEndCardInfo;
    /**
     * 弹性时间    min
     */
    private Integer elasticTime;

    public AttendanceItem(Long defineBeginTime, Long defineEndTime, Long beginTime, Long endTime, Integer elasticTime) {
        this.defineBeginTime = defineBeginTime;
        this.defineEndTime = defineEndTime;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.elasticTime = elasticTime;
    }

    public String analysisWithTime(List<CardInfo> dateList){
        StringBuilder res = new StringBuilder();
        // 查找打卡时间
        final List<CardInfo> times = new ArrayList<>();
        if(dateList == null){
            return AttendanceEnum.LACK_CARD;
        }
        dateList.forEach(s -> {
            if(s.getCardTime() >= defineBeginTime && s.getCardTime() <= defineEndTime){
                times.add(s);
            }
        });
//        for(Long l : times) System.out.println(l);
//        times.sort(CardInfo::compare);
//        for(Long l : times) System.out.println(l);
        if(times.size() == 0){
            return res.append(AttendanceEnum.LACK_CARD).toString();
        }
        if(times.size() == 1){
            userBeginCardInfo = times.get(0);
            if(times.get(0).getCardTime() > beginTime){
                res.append(AttendanceEnum.LATE).append("+");
            }
            return res.append(AttendanceEnum.LACK_OFFDUTY_CARD).append("+").toString();
        }
        userBeginCardInfo = times.get(0);
        userEndCardInfo = times.get(times.size()-1);
        //判断正常的情况
        if(userBeginCardInfo.getCardTime() <= beginTime + elasticTime * 60){
            //弹性情况
            if(userBeginCardInfo.getCardTime() >= beginTime){
                if(userEndCardInfo.getCardTime() >= endTime + userBeginCardInfo.getCardTime() - beginTime){
                    return AttendanceEnum.NORMAL;
                }else{
                    return AttendanceEnum.ELASTIC_EARLY;
                }
            }else if(userEndCardInfo.getCardTime() >= endTime){
                return AttendanceEnum.NORMAL;
            }else {
                return AttendanceEnum.EARLY;
            }
        }else if(userEndCardInfo.getCardTime() < endTime){
            if(elasticTime == 0){
                return AttendanceEnum.LATE+"+"+AttendanceEnum.EARLY;
            }else {
                return AttendanceEnum.ELASTIC_LATE + "+" + AttendanceEnum.EARLY;
            }
        }else{
            if(elasticTime == 0){
                return AttendanceEnum.LATE;
            }else {
                return AttendanceEnum.ELASTIC_LATE;
            }
        }
    }
    private String convertLocalTime(Long time){
        return time == null ? null : time/3600+":"+time%3600/60+":"+time%60;
    }
    /**
     * 打卡时间分析
     * @param dateList
     * @return
     */
    public String analysis(List<CardInfo> dateList){
        String res = analysisWithTime(dateList);
        //正常的打卡情况
        //外请打卡设置
        if(userBeginCardInfo!=null && userEndCardInfo!=null && !"云技术2020".equals(DingApplication.depName)) {
            if (SourceType.USER.equals(userBeginCardInfo.getSourceType())) {
                res += "+" + AttendanceEnum.ONDUTY_OUTSIDE_CARD + "(" + userBeginCardInfo.getUserAddress() + ")";
            }
            if (SourceType.USER.equals(userEndCardInfo.getSourceType())) {
                res += "+" + AttendanceEnum.OFFDUTY_OUTSIDE_CARD + "(" + userBeginCardInfo.getUserAddress() + ")";
            }
        }
        if(Parameter.withTime){
            res += "(" + convertLocalTime(userBeginCardInfo == null ? null : userBeginCardInfo.getCardTime()) + " -- " + convertLocalTime(userEndCardInfo == null ? null : userEndCardInfo.getCardTime()) + ")";
        }
        return  res;
    }
}
