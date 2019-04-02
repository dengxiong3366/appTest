package com.dx.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataproviderFromExcel {
	private static File file = null;
	private static XSSFWorkbook work = null;
	private static XSSFSheet sheet = null;
	private static XSSFRow row = null;
	private static XSSFCell cell = null;
	
	/**
	 *初始化excel文档，设定待操作的文件路径和sheet页
	 * @throws Exception 
	 */
	
	public static void getExcel(String filePath) throws Exception {
		file = new File(filePath);
		FileInputStream inputStream = new FileInputStream(file);
		work = new XSSFWorkbook(inputStream);
		if(null == work) {
			throw new Exception("创建Excel工作簿为空");
		}
	}
	
	/*
	 * 读取指定单元格的值
	 */
	
	public static String getCellData(String sheetName,int rowNum,int colNum)throws Exception {
		sheet = work.getSheet(sheetName);
		
		try {
			cell = sheet.getRow(rowNum).getCell(colNum);
			String cellData = getCellValue(cell);
			return cellData;
			
		} catch (Exception e) {
			return "";
		}
		
	}
	
	/*
	 * 根据Excel中格式的不同来读取不同格式的值
	 */
	
	private static String getCellValue(XSSFCell xssfCell) {
		String strCell="";
		if(cell.getCellTypeEnum() == CellType.STRING) {
			strCell = cell.getStringCellValue();
		}
		else if(cell.getCellTypeEnum() == CellType.NUMERIC) {
			strCell = String.valueOf(cell.getNumericCellValue());
			//strCell = strCell.split(".")[0];
		}
		else if(cell.getCellTypeEnum() == CellType.BOOLEAN) {
			strCell = String.valueOf(cell.getBooleanCellValue());
		}
		else if(cell.getCellTypeEnum() == CellType.BLANK) {
			strCell = "";
		}
		else {
			strCell = "";
		}
		return strCell;
	}
	
	/*
	 * 向指定的单元格写入数据
	 */
	
	public static void setCellData(int rowNum,int colNum,boolean result,String sheetName,String filePath) throws Exception{
		sheet = work.getSheet(sheetName);
		
		try {
			row=sheet.getRow(rowNum);
			cell=row.getCell(colNum);
			if(cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(result);
			}
			else {
				cell.setCellValue(result);
			}
			FileOutputStream outputStream = new FileOutputStream(filePath);
			work.write(outputStream);
			outputStream.close();
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/*
	 * 获取指定Sheet页的单元格行数
	 */
	public static int getAllRowNum(String sheetName) {
		sheet = work.getSheet(sheetName);
		return sheet.getLastRowNum();
		
	}
}
