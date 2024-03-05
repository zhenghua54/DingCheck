package com.yu.ding.services;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.yu.ding.cache.DingCache;
import com.yu.ding.entity.CardInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-01-18 12:47:39
 */
public class ServicesTest {
    public static void main(String[] args) throws ApiException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException {
//        DingService.queryDepNames().forEach(s -> {
//            System.out.println(s);
//        });

//        Method method = DingService.class.getDeclaredMethod("init");
//        Field field  = DingService.class.getDeclaredField("accessToken");
//
//        method.setAccessible(true);
//        method.invoke(null);
//        method.setAccessible(false);
//
//
//        field.setAccessible(true);
////        System.out.println(field.get(null));
//        String accessToken = field.get(null).toString();
//        field.setAccessible(false);
//


        //获取部门列表

//        System.out.println(accessToken);

//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
//        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
//        req.setDeptId(1L);
//        req.setLanguage("zh_CN");
//        OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
//        System.out.println(rsp.getBody());

//
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
//        OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
//        req.setDeptId(497517001L);
//        req.setCursor(0L);
//        req.setSize(100L);
//        req.setOrderField("modify_desc");
//        req.setContainAccessLimit(false);
//        req.setLanguage("zh_CN");
//        OapiUserListsimpleResponse rsp = client.execute(req, accessToken);
////        System.out.println(rsp.getBody());
//        rsp.getResult().getList().forEach(listUserSimpleResponse -> {
//            System.out.println(listUserSimpleResponse.getName()+"   "+listUserSimpleResponse.getUserid());
//        });
//

        //获取部门列表
        DingService.queryDepNames();
        DingService dingService = new DingService("云技术2021");

        Field field  = DingService.class.getDeclaredField("accessToken");
        field.setAccessible(true);
        String accessToken = (String) field.get(null);
        field.setAccessible(false);

//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
//        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
//        req.setDeptId(1L);
//        req.setLanguage("zh_CN");
//        OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
//        System.out.println(rsp.getBody());
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
        OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
        req.setDeptId(387278613L);
        req.setCursor(0L);
        req.setSize(100L);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiUserListsimpleResponse rsp = client.execute(req, accessToken);
//        System.out.println(rsp.getBody());
        rsp.getResult().getList().forEach(listUserSimpleResponse -> {
            System.out.println(listUserSimpleResponse.getName()+"   "+listUserSimpleResponse.getUserid());
        });

//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavestatus");
//        OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();
//        req.setUseridList(DingCache.getUserId("闫鹏"));
//        req.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-03-05 00:00:00").getTime());
//        req.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-03-05 23:59:59").getTime());
//        req.setOffset(0L);
//        req.setSize(10L);
//        OapiAttendanceGetleavestatusResponse rsp = client.execute(req, accessToken);
//        System.out.println(rsp.getBody());

//        System.out.println(new Date(1646488800000L));
//        System.out.println(new Date(1646470800000L));

//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
//        OapiAttendanceListRecordRequest reqq = new OapiAttendanceListRecordRequest();
//        reqq.setUserIds(Arrays.asList(DingCache.getUserId("闫鹏")));
//        reqq.setCheckDateFrom("2022-03-05 00:00:00");
//        reqq.setCheckDateTo("2022-03-05 23:59:59");
//        reqq.setIsI18n(false);
//        OapiAttendanceListRecordResponse rspp = client.execute(reqq, accessToken);
//        System.out.println(rspp.getBody());

//        Map<String, List<CardInfo>>  res = dingService.getUserAllTime("俞万刚", "2022-03-05 00:00:00", "2022-03-05 23:59:59");
//
//        for(Map.Entry<String, List<CardInfo>> entry : res.entrySet()){
//            entry.getValue().sort(CardInfo::compare);
//            entry.getValue().forEach(System.out::println);
//        }
//        DingService dingService = new DingService("云技术2021");
//        Map<String, List<Long>> test = dingService.getUserAllTime("俞万刚", "2022-02-21 00:00:00", "2022-02-26 23:59:59");
//        for(Map.Entry<String, List<Long>> entry : test.entrySet()){
//            System.out.print(entry.getKey()+":");
//            entry.getValue().forEach(aLong -> {System.out.print(aLong+" ");});
//            System.out.println();
//        }
    }
}
