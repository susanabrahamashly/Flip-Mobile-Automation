package com.flip.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private AndroidDriver driver;
    private WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 60; // 1 minute

    public WaitUtils(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Smart wait that keeps session alive while waiting
     */
    public void waitForOneMinute() {
        int intervalSeconds = 15; // Send command every 15 seconds
        int totalIntervals = 60 / intervalSeconds;

        for (int i = 0; i < totalIntervals; i++) {
            try {
                // Execute a lightweight command to keep session alive
                driver.getPageSource();
                Thread.sleep(intervalSeconds * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
    }

    /**
     * Wait for element with explicit wait
     */
    public WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element with custom timeout
     */
    public WebElement waitForElement(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait with condition checking
     */
    public void waitWithCondition(int timeoutSeconds, WaitCondition condition) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            if (condition.check()) {
                return;
            }
            keepSessionAlive();
            sleep(1000); // Check every second
        }
        throw new RuntimeException("Timeout waiting for condition");
    }

    private void keepSessionAlive() {
        try {
            driver.getPageSource();
        } catch (Exception e) {
            // Log warning but don't fail the wait
            System.out.println("Warning: Failed to keep session alive: " + e.getMessage());
        }
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }
}

// Interface for custom wait conditions
interface WaitCondition {
    boolean check();
}
