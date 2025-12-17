package com.flip.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataReader implements AutoCloseable {
    private final Workbook workbook;
    private final String filePath;

    public ExcelDataReader(String filePath) {
        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            this.workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Excel file: " + filePath, e);
        }
    }

    public Map<String, String> getTestData(String sheetName, String testCaseId) {
        Map<String, String> testData = new HashMap<>();
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + sheetName);
        }

        // Get header row
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row not found in sheet: " + sheetName);
        }

        // Find test case column index
        int testCaseColIndex = findColumnIndex(headerRow, "TestCaseID");
        if (testCaseColIndex == -1) {
            throw new RuntimeException("TestCaseID column not found");
        }

        // Find matching test case row
        int testCaseRowIndex = findTestCaseRow(sheet, testCaseColIndex, testCaseId);
        if (testCaseRowIndex == -1) {
            throw new RuntimeException("Test case not found: " + testCaseId);
        }

        // Read data for the test case
        Row dataRow = sheet.getRow(testCaseRowIndex);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            Cell dataCell = dataRow.getCell(i);

            if (headerCell != null) {
                String header = getCellValueAsString(headerCell);
                String value = dataCell != null ? getCellValueAsString(dataCell) : "";
                testData.put(header, value);
            }
        }

        return testData;
    }

    private int findColumnIndex(Row headerRow, String columnName) {
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && columnName.equals(getCellValueAsString(cell))) {
                return i;
            }
        }
        return -1;
    }

    private int findTestCaseRow(Sheet sheet, int testCaseColIndex, String testCaseId) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(testCaseColIndex);
                if (cell != null && testCaseId.equals(getCellValueAsString(cell))) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    @Override
    public void close() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
    }
}
