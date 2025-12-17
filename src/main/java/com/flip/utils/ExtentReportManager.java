package com.flip.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();


    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        // Create report directory if it doesn't exist
        String reportDir = System.getProperty("user.dir") + "/test-output/reports/";
        new File(reportDir).mkdirs();

        // Create report file with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = reportDir + "Flip Mobile Automation Report_" + timestamp + ".html";

        System.out.println("📄 Extent Report: " + reportPath);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure report
        sparkReporter.config().setTheme(Theme.STANDARD);
         sparkReporter.config().setDocumentTitle("Flip Mobile Automation Test Report");
        sparkReporter.config().setReportName("Automation Test Results");
        sparkReporter.config().setCss("css-string");
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setProtocol(Protocol.HTTPS);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Name", "Ashly Susan Abraham");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
