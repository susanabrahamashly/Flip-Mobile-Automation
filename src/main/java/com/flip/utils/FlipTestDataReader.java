package com.flip.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlipTestDataReader {
    private static final String TEST_DATA_PATH = "D:\\FlipMobile\\src\\main\\resources\\testdata\\flipTestData.xlsx";
    private static final Map<String, Map<String, String>> testDataMap = new HashMap<>();
    private static boolean isDataLoaded = false;

    public static void loadTestData(String sheetName) {
        if (!isDataLoaded) {
            try (FileInputStream fis = new FileInputStream(TEST_DATA_PATH);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new RuntimeException("Sheet '" + sheetName + "' not found");
                }

                Row headerRow = sheet.getRow(0);
                int columnCount = headerRow.getLastCellNum();
                List<String> headers = new ArrayList<>();

                // Read headers
                for (int i = 0; i < columnCount; i++) {
                    Cell cell = headerRow.getCell(i);
                    headers.add(cell.getStringCellValue());
                }

                // Read data rows
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Map<String, String> rowData = new HashMap<>();
                        String testCaseId = null;

                        for (int j = 0; j < columnCount; j++) {
                            Cell cell = row.getCell(j);
                            String value = getCellValueAsString(cell);
                            String header = headers.get(j);

                            if (header.equals("TestCaseID")) {
                                testCaseId = value;
                            }
                            rowData.put(header, value);
                        }

                        if (testCaseId != null) {
                            testDataMap.put(testCaseId, rowData);
                        }
                    }
                }
                isDataLoaded = true;

            } catch (IOException e) {
                throw new RuntimeException("Failed to load test data: " + e.getMessage());
            }
        }
    }

    private static String getCellValueAsString(Cell cell) {
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
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public static String getData(String testCaseId, String columnName) {
        if (!isDataLoaded) {
            throw new RuntimeException("Test data not loaded. Call loadTestData() first.");
        }

        Map<String, String> testCase = testDataMap.get(testCaseId);
        if (testCase == null) {
            throw new RuntimeException("Test case not found: " + testCaseId);
        }

        String value = testCase.get(columnName);
        if (value == null) {
            throw new RuntimeException("Column '" + columnName + "' not found for test case: " + testCaseId);
        }

        return value;
    }

    public static void clearTestData() {
        testDataMap.clear();
        isDataLoaded = false;
    }
}

