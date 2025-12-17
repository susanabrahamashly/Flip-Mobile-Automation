package com.flip.base;

import com.flip.utils.FlipTestDataReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 17636
 * @date 09-12-2025
 */
public class BaseTest {
    public AndroidDriver driver;
    public AppiumDriverLocalService service;

//    protected AppiumDriver driver;
//
    public AndroidDriver getDriver() {
        return driver;
    }

    @BeforeSuite
    public void setupTestData() {
        FlipTestDataReader.loadTestData("TestData");
    }


    @BeforeMethod
    public void configureAppium() throws MalformedURLException {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\17636\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(() -> "--use-plugins", "gestures");


        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Xiaomi 2201116SI");

//        options.setDeviceName("Nothing A059");
        options.setApp("D:\\FlipMobile\\src\\main\\resources\\Flip_v1.1.65_debug_prod.apk");
//        options.setApp("D:\\Appium\\src\\main\\resources\\Flip_Prod_Debug.apk");

        options.setCapability("autoGrantPermissions", true);
        options.setNoReset(false);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), options);
    }


    @AfterClass
    public void tearDown(){
        driver.quit();
        service.stop();
    }
}
