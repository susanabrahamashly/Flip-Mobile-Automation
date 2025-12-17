package com.flip.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;

/**
 * @author 17636
 * @date 09-12-2025
 */
public class LoginPage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    public AndroidDriver driver;
    private WebDriverWait wait;

    // Constructor
    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(accessibility = "Login")
    private WebElement getLoginButton;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.EditText")
    private WebElement loginField;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText")
    private WebElement passwordField;

    @FindBy(xpath = "//android.view.View[@content-desc=\"Log In\"]")
    private WebElement loginButton;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View")
    private WebElement outsideClick;

    @FindBy(xpath = "//android.view.View[@content-desc=\"Skip for now\"]")
    private WebElement skipForNowButton;

    @FindBy(xpath = "//android.view.View[@content-desc=\"Watchlist\"]")
    private WebElement watchlistMenu;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView")
    private WebElement flipLogo;

    public void enterUserId(String userId) {
        loginField.click();
        loginField.clear();
        loginField.sendKeys(userId);

        log.info("--------------Entered user ID: {}------------", userId);
    }

    public void enterPassword(String password) {
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void performLogin(String userId, String password) {

        wait.until(ExpectedConditions.elementToBeClickable(getLoginButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(loginField));
        enterUserId(userId);
        enterPassword(password);
        driver.hideKeyboard();
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        try{
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            skipForNowButton.click();
            Assert.assertTrue(watchlistMenu.isDisplayed());
        } catch (Exception e){
            Assert.assertTrue(watchlistMenu.isDisplayed());
        }
    }

}
