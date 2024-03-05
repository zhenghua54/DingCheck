package com.yu.ding.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 08:33
 * 用户请假情况
 */
@Data
@Builder
public class UserVocation {

//    /**
//     * 请假的开始时间
//     */
//    private Long startTime;
//    /**
//     * 请假的结束时间
//     */
//    private Long endTime;

    private final List<VocationItem> vocationItemList = new ArrayList<>(3);

    /**
     * 请假的类型
     */
    private String type;

    public void addVocation(VocationItem vocationItem){
        if(vocationItemList.size() >= 3){
            System.out.println("单天请假次数不得超过3次!!!");
            return;
        }
        this.vocationItemList.add(vocationItem);
    }
    public boolean judgeIfHasVocation(long startTime, long endTime){
        for(VocationItem vocationItem : vocationItemList){
            if(vocationItem.getStartTime() <= startTime && vocationItem.getEndTime() >= endTime){
                return true;
            }
            if(vocationItem.getStartTime() >= startTime && vocationItem.getStartTime() <=endTime){
                return true;
            }
            if(vocationItem.getEndTime() >= startTime && vocationItem.getEndTime() <=endTime){
                return true;
            }
        }
        return false;
    }
    public int vocationLength(){
        return this.vocationItemList.size();
    }
    public static class VocationItem{
        private long startTime;
        private long endTime;

        public VocationItem(long startTime, long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }
    }
}
