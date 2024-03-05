package com.yu.ding.util;

import com.yu.ding.context.Context;
import com.yu.ding.entity.UserCourse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author MrYu
 * @date 2021-11-14 23:02:45
 */
public class CourseParseUtil {
    /**
     * 解析课程
     */
    public static boolean parseUserCourse(String file) throws IOException, InvalidFormatException {
        File f = new File(file);
        if(!f.exists()){
            System.out.println("课表信息不存在!请添加课表信息!");
            return false;
        }
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(f);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        for(int i = 1; i<xssfSheet.getLastRowNum(); i += 3){
            UserCourse[] courses = new UserCourse[7];
            String name = xssfSheet.getRow(i).getCell(1).getStringCellValue();
            for(int j = 3; j <= 8; j++){
                String mor = xssfSheet.getRow(i).getCell(j) == null ? "" : xssfSheet.getRow(i).getCell(j).getStringCellValue();
                String aft = xssfSheet.getRow(i+1).getCell(j) == null ? "" : xssfSheet.getRow(i+1).getCell(j).getStringCellValue();
                String eve = xssfSheet.getRow(i+2).getCell(j) == null ? "" : xssfSheet.getRow(i+2).getCell(j).getStringCellValue();
//                System.out.println("name="+name+" mor="+mor+" aft="+aft+" eve="+eve);
                courses[j-3] = new UserCourse("".equals(mor) ? null : mor, "".equals(aft) ? null : aft, "".equals(eve) ? null : eve);
            }
            //存储数据
            Context.putUserCourse(name, courses);
        }
        xssfWorkbook.close();
        return true;
    }
//    private boolean isBlank(String s){
//        return s == null || s.trim().replace("\n", "").isEmpty();
//    }
    public static void parseCardRule(String file) throws IOException, InvalidFormatException {
        File f = new File(file);
        if(!f.exists()){
            System.out.println("打卡规则文件不存在!请添加打卡规则信息!");
            return;
        }
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(f);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row = null;
        for(int i=1; i <= xssfSheet.getLastRowNum(); i++){
            row = xssfSheet.getRow(i);
            if(row.getCell(2).getStringCellValue().equals("是")){
                Context.putUserRule(row.getCell(1).getStringCellValue(), true);
            }else{
                Context.putUserRule(row.getCell(1).getStringCellValue(), false);
            }
        }
        xssfWorkbook.close();
    }
}
