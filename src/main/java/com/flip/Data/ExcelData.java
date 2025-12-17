package com.flip.Data;

//import Data.ExcelDataReader;

import java.util.Map;
import java.util.Optional;

public class ExcelData {
    private static Map<String, String> testData;
    private static final String FILE_PATH = "D:\\old system\\SSO\\Testing\\src\\main\\resources\\testdata\\TestData.xlsx";
    private static final String SHEET_NAME = "TestData";

    private ExcelData() {
        // Private constructor to prevent instantiation
    }

    public static void initializeTestData(String testCaseId) {
        try (ExcelDataReader excelReader = new ExcelDataReader(FILE_PATH)) {
            testData = excelReader.getTestData(SHEET_NAME, testCaseId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize test data", e);
        }
    }

    public static String getMobileNumber() {
        return getValueOrThrow("mobileNumber");
    }

    public static String getName() {
        return getValueOrThrow("name");
    }

    public static String getEmailId() {
        return getValueOrThrow("emailId");
    }

    public static String getPanNumber() {
        return getValueOrThrow("panNumber");
    }

    public static String getDay() {
        return getValueOrThrow("day");
    }

    public static String getMonth() {
        return getValueOrThrow("month");
    }

    private static String getValueOrThrow(String key) {
        return Optional.ofNullable(testData)
                .map(data -> data.get(key))
                .orElseThrow(() -> new IllegalStateException(
                    "Test data not initialized. Call initializeTestData() first."));
    }
}
