package com.flip.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataReader {
    private Workbook workbook;
    private Sheet sheet;
    private String filePath;

    public ExcelDataReader(String filePath) {
        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel file: " + e.getMessage());
        }
    }

    public Map<String, String> getTestData(String sheetName, String testCaseId) {
        Map<String, String> testData = new HashMap<>();
        sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet " + sheetName + " not found");
        }

        // Get header row
        Row headerRow = sheet.getRow(0);

        // Find test case row
        int testCaseRow = findTestCaseRow(testCaseId);
        if (testCaseRow == -1) {
            throw new RuntimeException("Test case ID " + testCaseId + " not found");
        }

        // Read all columns for the test case
        Row dataRow = sheet.getRow(testCaseRow);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String header = getCellValueAsString(headerRow.getCell(i));
            String value = getCellValueAsString(dataRow.getCell(i));
            testData.put(header, value);
        }

        return testData;
    }

    private int findTestCaseRow(String testCaseId) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0);
                if (cell != null && testCaseId.equals(getCellValueAsString(cell))) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    public void closeWorkbook() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
