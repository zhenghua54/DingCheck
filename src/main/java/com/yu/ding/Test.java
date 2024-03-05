package com.yu.ding;

import com.taobao.api.ApiException;
import com.yu.ding.constant.DefineAttendanceTime;
import com.yu.ding.context.Context;
import com.yu.ding.entity.AttendanceResult;
import com.yu.ding.context.Parameter;
import com.yu.ding.executor.BaseExecutor;
import com.yu.ding.executor.ExecutorInte;
import com.yu.ding.services.CourseService;
import com.yu.ding.services.DingService;
import com.yu.ding.util.AttendanceGenerateUtil;
import com.yu.ding.util.ParseUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-02-26 14:25:32
 */
public class Test {
    public static void main(String[] args) throws IOException, ApiException, InvalidFormatException {
        Parameter.ifHasCourse = false;
        Parameter.withTime = true;
        DingService.queryDepNames();
        //初始化参数
//        Parameter parameter = DingApplication.prompt();
        long startTime = System.currentTimeMillis();
        int count = 0;
        new ParseUtil().parseDefineTime("C:\\Users\\23922\\Desktop\\考勤脚本\\打卡时间声明.txt");
        //是否严格执行
        boolean strict = false;
        //执行器
        ExecutorInte executor = new BaseExecutor(new CourseService("C:\\Users\\23922\\Desktop\\考勤脚本\\课表.xlsx"), new DingService("云技术2021"));
        LocalDate start = LocalDate.parse("2022-11-12");
        LocalDate end = LocalDate.parse("2022-11-12");

        Map<String, List<AttendanceResult>> data = new HashMap<>(150);
        for(String name : Context.getUserNames()){
            if(!name.equals("黄钰淦")){
                continue;
            }
            List<AttendanceResult> result = null;
            try {
                result = executor.getAttendanceResultOnDate(name, start, end, strict);
                System.out.println(result.toString());
            } catch (ApiException | ParseException e) {
                e.printStackTrace();
            }
            data.put(name, result);
            System.out.print(name+" 解析完成!  ");
            count++;
            //格式换行
            if(count%5 ==0){
                System.out.println();
            }
        }
//        System.out.println();
//        AttendanceGenerateUtil.generate(data, start, end);
//        System.out.println("考勤分析已生成!");
//        System.out.println("总用时:"+(System.currentTimeMillis()-startTime)/60000+"分"+(System.currentTimeMillis()-startTime)/1000%60+"秒  总人数:"+count);
    }
    public static Parameter prompt() throws ApiException, IOException {
        Parameter parameter =  new Parameter();
        List<String> depNames = DingService.queryDepNames();
        //选择部门
        while (true) {
            System.out.print("请选择部门:[ ");
            depNames.forEach(s -> {
                System.out.print(s + " ");
            });
            System.out.println("]");
            String depName = receiveStringByGbk();
            if(depNames.contains(depName)){
                parameter.setDepName(depName);
                break;
            }
        }
        //起始日期
        while (true) {
            System.out.println("请输入起始日期(格式:yyyy-MM-dd):");
            String startDate = receiveStringByGbk();
            if(startDate.matches(DefineAttendanceTime.DATE_REG)){
                parameter.setStartDate(LocalDate.parse(startDate, DefineAttendanceTime.DATE_TIME_FORMATTER));
                break;
            }
        }
        //结束日期
        while (true) {
            System.out.println("请输入结束日期(格式:yyyy-MM-dd):");
            String endDate = receiveStringByGbk();
            if(endDate.matches(DefineAttendanceTime.DATE_REG)){
                parameter.setEndDate(LocalDate.parse(endDate, DefineAttendanceTime.DATE_TIME_FORMATTER));
                break;
            }
        }
        //是否有课程
        while (true) {
            System.out.println("是否有课程(yes/no):");
            String strict = receiveStringByGbk();
            if("yes".equals(strict) || "no".equals(strict)){
                Parameter.ifHasCourse = "yes".equals(strict);
                break;
            }
        }
        //是否严格执行
        while (true) {
            System.out.println("是否严格执行(yes/no):");
            String strict = receiveStringByGbk();
            if("yes".equals(strict) || "no".equals(strict)){
                parameter.setStrict("yes".equals(strict));
                break;
            }
        }
        return parameter;
    }
    public static String receiveStringByGbk() throws IOException {
        //最多支持64个汉字
        byte[] data = new byte[128];
        int length = 0;
        int temp = -1;
        //GBK下换行是 13 10
        while((temp = System.in.read()) != 13){
            //清除上一个的换行编码
            if(temp == 10){
                continue;
            }
            data[length++] = (byte) temp;
        }
        byte[] res = new byte[length];
        for(temp = 0; temp < length; temp++){
            res[temp] = data[temp];
        }
        return new String(res, "gbk");
    }
}
