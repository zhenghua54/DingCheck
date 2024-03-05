package com.yu.ding.util;

import com.yu.ding.context.Context;
import com.yu.ding.entity.AttendanceResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu
 * @date 2021-11-15 01:09:17
 */
public class AttendanceGenerateUtil {
    public static boolean generate (Map<String, List<AttendanceResult>> data, LocalDate startDate, LocalDate endDate) throws IOException {
        int dayCount = 1;
        LocalDate temp = startDate;
        while((temp = temp.plusDays(1)).isBefore(endDate)){
            dayCount++;
        }
        if(!startDate.toString().equals(endDate.toString())){
            dayCount++;
        }
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        int index = 0;
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();
        //固定行
        xssfSheet.createFreezePane(2, 1, 1,1 );
        //添加头部
        XSSFRow xssfRow = xssfSheet.createRow(index++);
        xssfRow.createCell(0).setCellValue("姓名");
        xssfRow.createCell(1).setCellValue("早/中/晚");
        for(int i=2; i<=dayCount+1; i++){
            xssfSheet.setColumnWidth(i, 12000);
            XSSFCell cell =  xssfRow.createCell(i);
            XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(startDate.plusDays(i-2).toString());
        }
        for(Map.Entry<String, List<AttendanceResult>> entry : data.entrySet()){
            index++;
            short height = 500;
            XSSFRow morRow = xssfSheet.createRow(index);
            XSSFRow aftRow = xssfSheet.createRow(index+1);
            XSSFRow eveRow = xssfSheet.createRow(index+2);

            morRow.setHeight(height);
            aftRow.setHeight(height);
            eveRow.setHeight(height);
            //合并姓名单元格
            xssfSheet.addMergedRegion(new CellRangeAddress(index, index+2, 0, 0));
            XSSFCell c1 = morRow.createCell(0);
            XSSFCellStyle cs1 = xssfWorkbook.createCellStyle();
            cs1.setAlignment(HorizontalAlignment.CENTER);
            cs1.setVerticalAlignment(VerticalAlignment.CENTER);
            Font f = xssfWorkbook.createFont();
            //true 表示6次卡
            if(Context.getUserRule(entry.getKey()) == null || Context.getUserRule(entry.getKey())) {
                f.setColor(Context.getUserRule(entry.getKey()) == null ? IndexedColors.DARK_RED.getIndex() : IndexedColors.BLUE.getIndex());
            }else{
                f.setColor(IndexedColors.BLACK.getIndex());
            }
            cs1.setFont(f);
            c1.setCellStyle(cs1);
            c1.setCellValue(entry.getKey());
            //设置早中晚
            morRow.createCell(1).setCellValue("早");
            aftRow.createCell(1).setCellValue("中");
            eveRow.createCell(1).setCellValue("晚");
            for(int j = 2; j<=dayCount+1; j++){
                List<AttendanceResult> v = entry.getValue();
                //早
                XSSFCell cellMor = morRow.createCell(j);
                cellMor.setCellValue(v.get(j-2).getMorningRes());
                cellMor.setCellStyle(generateColor(v.get(j-2).getMorningRes(), xssfWorkbook.createCellStyle()));
                //中
                XSSFCell cellAft = aftRow.createCell(j);
                cellAft.setCellValue(v.get(j-2).getAfternoonRes());
                cellAft.setCellStyle(generateColor(v.get(j-2).getAfternoonRes(), xssfWorkbook.createCellStyle()));
                //晚
                XSSFCell cellEve = eveRow.createCell(j);
                cellEve.setCellStyle(generateColor(v.get(j-2).getEveningRes(), xssfWorkbook.createCellStyle()));
                cellEve.setCellValue(v.get(j-2).getEveningRes());
            }
            index+=3;
        }
        //判断文件夹
        File dir = new File(System.getProperty("user.dir")+"/考勤结果/");
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(System.getProperty("user.dir")+"/考勤结果/考勤分析"+new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date())+".xlsx");
        if(!file.exists()){
            file.createNewFile();
        }
        xssfWorkbook.write(new FileOutputStream(file));
        xssfWorkbook.close();
        return true;
    }
    private static CellStyle generateColor(String res, CellStyle cellStyle){
        CellStyle resStyle = cellStyle;
        resStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        resStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        resStyle.setBorderBottom(BorderStyle.THIN);
        resStyle.setBorderLeft(BorderStyle.THIN);
        resStyle.setBorderRight(BorderStyle.THIN);
        resStyle.setBorderTop(BorderStyle.THIN);
        resStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        resStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        if(res.contains("弹性打卡")){
            resStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        }
        if(res.contains("迟到") || res.contains("早退")){
            resStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        }
        if(res.contains("外勤卡")){
            resStyle.setFillForegroundColor(IndexedColors.PINK.getIndex());
        }
        if(res.contains("缺上班卡") || res.contains("缺下班卡") || res.contains("缺卡")){
            resStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        }
        return resStyle;
    }
}
