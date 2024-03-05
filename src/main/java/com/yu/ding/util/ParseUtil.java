package com.yu.ding.util;

import com.alibaba.fastjson.JSONObject;
import com.yu.ding.constant.DefineAttendanceTime;
import com.yu.ding.context.Parameter;
import java.io.*;

/**
 * @author MrYu
 * @date 2021-11-14 20:53:01
 */
public class ParseUtil {

    public Parameter parseFile(String filePath) throws IOException {
        File f = new File(filePath);
        if(!f.exists()){
            System.out.println("配置文件不存在!");
            return null;
        }
        return JSONObject.parseObject(new BufferedReader(new FileReader(f)).readLine(), Parameter.class);
    }
    public void parseDefineTime(String filePath) throws IOException {
        File f = new File(filePath);
        if(!f.exists()){
            System.out.println("配置文件不存在!");
        }else{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String s = "";
            s = bufferedReader.readLine();
            DefineAttendanceTime.morOnTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.morOffTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.aftOnTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.aftOffTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.eveOnTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.eveOffTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.elastic = Integer.parseInt(s.substring(s.indexOf(":")+1).trim()) * 60;
            s = bufferedReader.readLine();
            DefineAttendanceTime.morDefineOnTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.morDefineOffTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.aftDefineOnTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.aftDefineOffTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.eveDefineOnTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            s = bufferedReader.readLine();
            DefineAttendanceTime.eveDefineOffTime = DefineAttendanceTime.getSecondOfDayByTime(s.substring(s.indexOf(":")+1).trim());
            //释放资源
            bufferedReader.close();
        }
    }
}
