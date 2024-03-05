package com.yu.ding.services;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.yu.ding.cache.DingCache;
import com.yu.ding.constant.DefineAttendanceTime;
import com.yu.ding.context.Context;
import com.yu.ding.entity.AttendanceResult;
import com.yu.ding.entity.AttendanceTime;
import com.yu.ding.entity.CardInfo;
import com.yu.ding.entity.UserVocation;
import com.yu.ding.services.interfaces.DingServiceInte;
import com.yu.ding.util.TransDateLocalDateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MrYu (YWG)
 * @date 2021-11-13 19:34
 */
public class DingService implements DingServiceInte {
    /**
     * 格式化日期的类
     */
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static String accessToken;
    private DingTalkClient client;
    public DingService(String depName) throws ApiException {
        //初始化用户信息
        saveUserIds(depName);
    }
    private static void init() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(Context.getAppKey());
        request.setAppsecret(Context.getAppSecret());
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        if(response.isSuccess()){
            accessToken = response.getAccessToken();
        }else{
            System.out.println(response.getErrmsg());
            System.out.println("请检查AppKey或者Appsecret, 联系QQ:2392205247");
            throw new ApiException("accessToken is null");
        }
    }
    public static List<String> queryDepNames() throws ApiException {
        //初始化
        init();
        //获取部门列表
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
        ArrayList<String> depNames = new ArrayList<>();
        if(rsp.isSuccess()){
            for(OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse  : rsp.getResult()){
                depNames.add(deptBaseResponse.getName());
            }
            return depNames;
        }else{
            System.out.println("获取部门失败!请联系QQ:2392205247!");
            throw new ApiException("获取部门失败!");
        }
    }
    private boolean saveUserIds(String depName) throws ApiException {
        //获取部门列表

        Map<String, Long> depData = new HashMap<>();
        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        req.setDeptId(1L);
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
        rsp.getResult().forEach(r -> {
            depData.put(r.getName(), r.getDeptId());
        });
        Long depId = depData.get(depName);
        if(depId == null){
            System.out.println("无["+depName+"]部门信息!");
            return false;
        }

        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
        OapiUserListsimpleRequest requ = new OapiUserListsimpleRequest();
        requ.setDeptId(depId);
        requ.setCursor(0L);
        requ.setCursor(0L);
        requ.setSize(100L);
        requ.setLanguage("zh_CN");
        OapiUserListsimpleResponse rspu = client.execute(requ, accessToken);
        rspu.getResult().getList().forEach(item -> {
            //存储cache信息
            DingCache.saveUserId(item.getName(), item.getUserid());
            //加入全局用户
            Context.addUserName(item.getName());
        });
        if(rspu.getResult().getHasMore()){
            System.out.println("["+depName+"] 还有考勤信息没有生成，请联系QQ:2392205247");
        }
        return true;
    }
    @Override
    public UserVocation getUserVocationData(String name, Date dataTime) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavestatus");
        OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();
        req.setUseridList(DingCache.getUserId(name));
        req.setStartTime(dataTime.getTime());
        req.setEndTime(dataTime.getTime()+86400000L);
        req.setOffset(0L);
        req.setSize(3L);
        try{
            // Thread.sleep(500);
        } catch (Exception e) {

        }

        OapiAttendanceGetleavestatusResponse rsp = client.execute(req, accessToken);
        UserVocation userVocation = UserVocation.builder().type("请假").build();
        for(OapiAttendanceGetleavestatusResponse.LeaveStatusVO leaveStatusVO : rsp.getResult().getLeaveStatus()){
//            System.out.println(new Date(leaveStatusVO.getStartTime()).toLocaleString()+"  "+new Date(leaveStatusVO.getEndTime()).toLocaleString());
            userVocation.addVocation(new UserVocation.VocationItem((leaveStatusVO.getStartTime()-dataTime.getTime())/1000, (leaveStatusVO.getEndTime()-dataTime.getTime())/1000));
        }
        return rsp.isSuccess() ? userVocation.vocationLength() == 0 ? null : userVocation : null;
    }
    @Override
    public AttendanceTime getUserDutyTime(String name, Date dataTime) {
        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getupdatedata");
        OapiAttendanceGetupdatedataRequest req = new OapiAttendanceGetupdatedataRequest();
        req.setUserid(DingCache.getUserId(name));
        req.setWorkDate(dataTime);
        OapiAttendanceGetupdatedataResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        //TODO

        rsp.getResult().getAttendanceResultList().forEach(atAttendanceResultForOpenVo -> {
            System.out.println(atAttendanceResultForOpenVo.getUserCheckTime()+"  "+atAttendanceResultForOpenVo.getCheckType()+"  "+atAttendanceResultForOpenVo.getSourceType()+"  "+atAttendanceResultForOpenVo.getLocationResult().toString());
        });
        //TODO
        if(rsp.isSuccess()){
            AttendanceTime res = new AttendanceTime();
            OapiAttendanceGetupdatedataResponse.AtAttendanceResultForOpenVo d1 = rsp.getResult().getAttendanceResultList().get(0);
            OapiAttendanceGetupdatedataResponse.AtAttendanceResultForOpenVo d2 = rsp.getResult().getAttendanceResultList().get(1);
            if("OnDuty".equals(d1.getCheckType())){
                res.setOnDutyCard(d1);
                res.setOffDutyCard(d2);
            }else{
                res.setOnDutyCard(d2);
                res.setOffDutyCard(d1);
            }
            return res;
        }else{
            return null;
        }
    }
    @Override
    public Map<String, List<CardInfo>> getUserAllTime(String name, String  startDate, String endDate) throws ApiException, ParseException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
        OapiAttendanceListRecordRequest req = new OapiAttendanceListRecordRequest();
//        List<String> ids = new ArrayList<String>();
//        ids.add(DingCache.getUserId(name));
        req.setUserIds(Arrays.asList(DingCache.getUserId(name)));
//        String dateString = TransDateLocalDateUtil.toLocalDateTime(dataTime).toLocalDate().toString();
        req.setCheckDateFrom(startDate);
        req.setCheckDateTo(endDate);
        req.setIsI18n(false);
        OapiAttendanceListRecordResponse rsp = client.execute(req, DingService.accessToken);
        Map<String, List<CardInfo>> res = new HashMap<>(16);

        String last = "";
        List<CardInfo> times = null;
        Long tempTimestamp = -1L;
        for(OapiAttendanceListRecordResponse.Recordresult recordresult : rsp.getRecordresult()){
            String checkTime = DATE_FORMAT.format(recordresult.getUserCheckTime());
            if(!last.equals(checkTime)){
                if(!last.isEmpty()){
                    res.put(last, times);
                    //记录一段时间第一次打卡情况如果大于两次则表示6次打卡
                    if(Context.getUserRule(name) == null){
                        Context.putUserRule(name, times.size() > 2);
                    }
                }
                last = checkTime;
                times = new ArrayList<>();
                tempTimestamp = DefineAttendanceTime.DATE_FORMAT.parse(last+" 00:00:00").getTime();
            }
            times.add(new CardInfo((recordresult.getUserCheckTime().getTime()-tempTimestamp)/1000, recordresult.getSourceType(), recordresult.getUserAddress()));
        }
        res.putIfAbsent(last, times);
        //唯一一天的特殊处理
        if(Context.getUserRule(name) == null && times != null){
            Context.putUserRule(name, times.size() > 2);
        }
//        List<Long> res = new ArrayList<>();
//        rsp.getRecordresult().forEach(recordresult -> {
//            try {
//                res.putif
//                res.add((recordresult.getUserCheckTime().getTime() - DefineAttendanceTime.DATE_FORMAT.parse(dateString+" 00:00:00").getTime())/1000);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        });
        return res;
    }
    @Override
    public AttendanceResult ifHasAttendanceOnSunday(String name, Date date) {
        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getupdatedata");
        OapiAttendanceGetupdatedataRequest req = new OapiAttendanceGetupdatedataRequest();
        req.setUserid(DingCache.getUserId(name));
        req.setWorkDate(date);
        OapiAttendanceGetupdatedataResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        AttendanceResult res = AttendanceResult.REST();
        res.setDate(TransDateLocalDateUtil.toLocalDateTime(date).toLocalDate());
        if(rsp.getResult().getAttendanceResultList().size() == 0) {
            return res;
        }else{
            rsp.getResult().getAttendanceResultList().forEach(i -> {
                if("OnDuty".equals(i.getCheckType())){

                    res.setOnDuty(TransDateLocalDateUtil.toLocalDateTime(i.getUserCheckTime()).toLocalTime().toString());
                }else{
                    res.setOffDuty(TransDateLocalDateUtil.toLocalDateTime(i.getUserCheckTime()).toLocalTime().toString());
                }
            });
            return res.withAttendanceTime();
        }
    }
}
