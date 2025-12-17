package com.flip.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedExcelDataReader implements AutoCloseable {
    private final Workbook workbook;
    private final Map<String, Integer> columnMap;

    public AdvancedExcelDataReader(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            this.workbook = new XSSFWorkbook(fis);
            this.columnMap = new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Excel file: " + filePath, e);
        }
    }

    public List<Map<String, Object>> readSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + sheetName);
        }

        List<Map<String, Object>> sheetData = new ArrayList<>();
        Row headerRow = sheet.getRow(0);

        // Map column headers
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                columnMap.put(cell.getStringCellValue(), i);
            }
        }

        // Read data rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Map<String, Object> rowData = new HashMap<>();
                for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
                    Cell cell = row.getCell(entry.getValue());
                    rowData.put(entry.getKey(), getCellValue(cell));
                }
                sheetData.add(rowData);
            }
        }

        return sheetData;
    }

    public Map<String, Object> findTestCase(String sheetName, String testCaseId) {
        List<Map<String, Object>> sheetData = readSheet(sheetName);
        return sheetData.stream()
                .filter(row -> testCaseId.equals(row.get("TestCaseID")))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test case not found: " + testCaseId));
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                double value = cell.getNumericCellValue();
                return value == (long) value ? (long) value : value;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    @Override
    public void close() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
    }
}
