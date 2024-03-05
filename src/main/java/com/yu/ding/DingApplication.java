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
import com.yu.ding.util.CourseParseUtil;
import com.yu.ding.util.ParseUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author MrYu
 * @date  2021-11-16
 */
public class DingApplication {
    public static String depName;
    public static void main(String[] args) throws IOException, ApiException, InvalidFormatException {
        //初始化参数
        Parameter parameter = DingApplication.prompt();
        long startTime = System.currentTimeMillis();
        int count = 0;
        System.out.println("打卡时间声明文件路径："+ System.getProperty("user.dir")+"/课表+配置/打卡时间声明.txt");
        new ParseUtil().parseDefineTime(System.getProperty("user.dir")+"/课表+配置/打卡时间声明.txt");
        //是否严格执行
        boolean strict = parameter.isStrict();
        //获取用户打卡次数
        System.out.println("打卡规则文件路径："+ System.getProperty("user.dir")+"/课表+配置/打卡规则.xlsx");
        CourseParseUtil.parseCardRule(System.getProperty("user.dir")+"/课表+配置/打卡规则.xlsx");
        //执行器
        System.out.println("课表文件路径："+ System.getProperty("user.dir")+"/课表+配置/课表-2023.xlsx");
        ExecutorInte executor = new BaseExecutor(new CourseService(System.getProperty("user.dir")+"/课表+配置/课表-2023.xlsx"), new DingService(parameter.getDepName()));
        LocalDate start = parameter.getStartDate();
        LocalDate end = parameter.getEndDate();
        Map<String, List<AttendanceResult>> data = new HashMap<>(150);
        for(String name : Context.getUserNames()){
            name = name.trim();
            List<AttendanceResult> result = null;
            try {
                result = executor.getAttendanceResultOnDate(name, start, end, strict);
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
//        并发版本，存在BUG
//        Map<String, List<AttendanceResult>> data = new ConcurrentHashMap<>(150);
//        ExecutorService mulExecutor = Executors.newFixedThreadPool(10);
//        final CountDownLatch countDownLatch = new CountDownLatch(Context.getUserNames().size());
//        for (String name : Context.getUserNames()) {
//            try{
//                mulExecutor.execute(new Task(name, start, end, strict, executor, data, countDownLatch));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            countDownLatch.await();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            mulExecutor.shutdown();
//        }
        System.out.println();
        AttendanceGenerateUtil.generate(data, start, end);
        System.out.println("考勤分析已生成!");
        System.out.println("总用时:"+(System.currentTimeMillis()-startTime)/60000+"分"+(System.currentTimeMillis()-startTime)/1000%60+"秒  总人数:"+count);
    }
    public static Parameter prompt() throws ApiException, IOException {
        Parameter parameter = new Parameter();
        List<String> depNames = DingService.queryDepNames();
        //选择部门
        while (true) {
            System.out.print("请选择部门:[ ");
            depNames.forEach(s -> {
                System.out.print(s + " ");
            });
            System.out.println("]");
            // String depName = receiveStringByGbk();
            String depName = new Scanner(System.in).next();
            DingApplication.depName = depName;
            if(depNames.contains(depName)){
                parameter.setDepName(depName);
                break;
            }
        }
        //起始日期
        while (true) {
            System.out.println("请输入起始日期(格式:yyyy-MM-dd):");
            // String startDate = receiveStringByGbk();
            String startDate = new Scanner(System.in).next();
            if(startDate.matches(DefineAttendanceTime.DATE_REG)){
                parameter.setStartDate(LocalDate.parse(startDate, DefineAttendanceTime.DATE_TIME_FORMATTER));
                break;
            }
        }
        //结束日期
        while (true) {
            System.out.println("请输入结束日期(格式:yyyy-MM-dd):");
            // String endDate = receiveStringByGbk();
            String endDate = new Scanner(System.in).next();
            if(endDate.matches(DefineAttendanceTime.DATE_REG)){
                parameter.setEndDate(LocalDate.parse(endDate, DefineAttendanceTime.DATE_TIME_FORMATTER));
                break;
            }
        }
        //是否有课程
        while (true) {
            System.out.println("是否有课程(yes/no):");
            // String strict = receiveStringByGbk();
            String strict = new Scanner(System.in).next();
            if("yes".equals(strict) || "no".equals(strict)){
                Parameter.ifHasCourse = "yes".equals(strict);
                break;
            }
        }
        //是否严格执行
        while (true) {
            System.out.println("是否严格执行(yes/no):");
            // String strict = receiveStringByGbk();
            String strict = new Scanner(System.in).next();
            if("yes".equals(strict) || "no".equals(strict)){
                parameter.setStrict("yes".equals(strict));
                break;
            }
        }
        //是否严格执行6次打卡
        while (true) {
            System.out.println("是否附带打卡时间(yes/no):");
            // String strict = receiveStringByGbk();
            String strict = new Scanner(System.in).next();
            if("yes".equals(strict) || "no".equals(strict)){
                Parameter.withTime = "yes".equals(strict);
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

    // 多线程处理
    static class Task implements  Runnable {
        private String name;
        private LocalDate start;
        private LocalDate end;
        private boolean strict;
        private CountDownLatch countDownLatch;

        private ExecutorInte executor;
        private Map<String, List<AttendanceResult>> data;

        public Task(String name, LocalDate start, LocalDate end, boolean strict, ExecutorInte executor, Map<String, List<AttendanceResult>> data, CountDownLatch countDownLatch) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.strict = strict;
            this.executor = executor;
            this.data = data;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                List<AttendanceResult> result = null;
                try {
                    result = executor.getAttendanceResultOnDate(name, start, end, strict);
                } catch (ApiException | ParseException e) {
                    e.printStackTrace();
                }
                data.put(name, result);
                System.out.print(name+" 解析完成!  ");
            } catch (Exception e) {

            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
