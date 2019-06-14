package com.magic.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chloneda
 * Description:
 */
public class ExcelUtil {

    public static Workbook getWorkbook(String xlsFilePath) throws IOException {
        Workbook workbook=null;
        String extName=getExtName(xlsFilePath);
        InputStream inputStream=new FileInputStream(xlsFilePath);

        if(extName.equalsIgnoreCase("xls")){
            workbook=new HSSFWorkbook(inputStream);
        } else if(extName.equalsIgnoreCase("xlsx")){
            workbook=new XSSFWorkbook(inputStream);
        }else {
            throw new FileNotFoundException(xlsFilePath+"is invalid!");
        }
        return workbook;
    }

    public static String getExtName(String fileName){
        int index=fileName.lastIndexOf(".");
        if(index==-1)
            throw new IllegalArgumentException(fileName+ "is invalid");
        return fileName.substring(index+1);
    }

    public static String[] getSheets(Workbook workbook){
        int index=workbook.getNumberOfSheets();
        String[] sheets=new String[index];
        for(int i=0;i<index;i++){
            Sheet sheet=workbook.getSheetAt(i);
            sheets[i]=sheet.getSheetName();
        }
        return sheets;
    }

    public static void main(String[] args) {
        System.out.println(getExtName("afdk.xls"));
    }

}
