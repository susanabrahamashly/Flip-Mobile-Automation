package com.flip.utils;
import com.flip.base.BaseTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(result.getMethod().getMethodName(),
            result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test Passed");
    }


    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, "Test Failed");
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        try {
            // Get the driver instance from the test class
            Object testClass = result.getInstance();
            WebDriver driver = ((BaseTest) testClass).getDriver();

            if (driver != null) {
                // Capture timestamp for unique screenshot name
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String testMethodName = result.getMethod().getMethodName();

                // Take screenshot and convert to Base64
                String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

                // Attach screenshot to Extent Report
                ExtentReportManager.getTest().fail("Failure Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());

                // Add failure details
                ExtentReportManager.getTest().fail("Test Case: " + testMethodName + " has failed");
                ExtentReportManager.getTest().fail(result.getThrowable());
            }
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Exception while capturing screenshot: " + e.getMessage());
        }
//        ExtentReportManager.getTest().log(Status.FAIL, "Test Failed");
//        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());
//
//        // Capture screenshot on failure
//        try {
//            Object testClass = result.getInstance();
//            WebDriver driver = ((BaseTest) testClass).getDriver();
//            String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//            ExtentReportManager.getTest().addScreenCaptureFromBase64String(base64Screenshot,
//                "Failure Screenshot");
//        } catch (Exception e) {
//            ExtentReportManager.getTest().log(Status.WARNING,
//                "Could not capture screenshot: " + e.getMessage());
//        }
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped");
        if (result.getThrowable() != null) {
            ExtentReportManager.getTest().log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();
    }
}
