package com.saucedemo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel file: " + filePath, e);
        }
    }

    /** Total rows that contain data (excludes header). */
    public int getRowCount() {
        return sheet.getLastRowNum(); // lastRowNum = index of last row; header at 0
    }

    public int getColCount() {
        return sheet.getRow(0).getLastCellNum();
    }

    /** Returns cell value as String (handles numeric, boolean, blank). */
    public String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) return "";
        Cell cell = row.getCell(colNum);
        if (cell == null) return "";

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    /** Returns cell value by column header name. */
    public String getCellData(int rowNum, String columnName) {
        Row header = sheet.getRow(0);
        for (int i = 0; i < header.getLastCellNum(); i++) {
            if (header.getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {
                return getCellData(rowNum, i);
            }
        }
        throw new RuntimeException("Column not found: " + columnName);
    }

    public void close() {
        try {
            if (workbook != null) workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}