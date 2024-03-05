package com.yu.ding.util;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrYu(YWG)
 * @date 2022-01-18 14:10:23
 */
public class PoiUtils {
    public static List<String> getAllUser(String path) throws IOException {
        List<String> res = new ArrayList<>();

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(path));
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        System.out.println(sheet.getLastRowNum());
        for(int i = 0; i < sheet.getLastRowNum(); i++){
            try {
                System.out.println(sheet.getRow(i).getCell(0).getStringCellValue());
                res.add(sheet.getRow(i).getCell(0).getStringCellValue());
            }catch (Exception e){
                break;
            }

        }

        //关闭资源
        xssfWorkbook.close();

        return res;
    }

    public static void main(String[] args) throws IOException {
        //read
        FileInputStream fis1 = new FileInputStream("C:\\Users\\23922\\Desktop\\fiel\\俞万金5月份续重费9332.4元-圆通拉重量(1)(1).xlsx");
        Map<String, Double> caches = new HashMap<>();
        XSSFWorkbook xssfWorkbook1 = new XSSFWorkbook(fis1);
        xssfWorkbook1.getSheetAt(1).forEach(row -> {
            if(row.getRowNum()==0){
                return;
            }
            caches.put(row.getCell(0).getStringCellValue().trim(), row.getCell(1).getNumericCellValue());
//            System.out.println(row.getCell(1));
        });
//        caches.forEach((k, v) -> {
//            System.out.println(k+" = "+ v);
//        });

////        getAllUser("C:\\Users\\23922\\Desktop\\研一东区.xlsx").stream().forEach(System.out::println);
        FileInputStream fis = new FileInputStream("C:\\Users\\23922\\Desktop\\fiel\\俞万金5月份续重费9332.4元.xlsx");
        FileOutputStream fos = new FileOutputStream("C:\\Users\\23922\\Desktop\\fiel\\res.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
        xssfWorkbook.getSheetAt(0).forEach(row -> {
            if(caches.containsKey(row.getCell(0).getStringCellValue().trim())){
                row.getCell(2).setCellValue(caches.get(row.getCell(0).getStringCellValue().trim()));
            }

        });
        xssfWorkbook.write(fos);
        xssfWorkbook.close();
        fos.close();
        fis.close();
        xssfWorkbook1.close();
        fis1.close();

    }
}
