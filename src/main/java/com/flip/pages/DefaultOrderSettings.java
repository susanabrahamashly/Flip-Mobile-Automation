package com.flip.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
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
 * @date 11-12-2025
 */
public class DefaultOrderSettings {
    private static final Logger log = LoggerFactory.getLogger(DefaultOrderSettings.class);
    public AndroidDriver driver;

    // Constructor
    public DefaultOrderSettings(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @AndroidFindBy(accessibility = "More")
    private WebElement moreButton;

    @AndroidFindBy(accessibility = "Settings")
    private WebElement settingsOption;

    @AndroidFindBy(accessibility = "Default Order Settings\nSet up your order settings for faster order\nplacement")
    private WebElement defaultOrderSettingsOption;

    @AndroidFindBy(accessibility = "Stocks\nTab 1 of 5")
    private WebElement stocksTab;

    @FindBy(xpath = "//android.widget.ImageView[starts-with(@content-desc, 'Type')]")
    private WebElement typeDropDown;

    @FindBy(xpath = "//android.widget.ImageView[starts-with(@content-desc, 'Exchange')]")
    private WebElement exchangeDropDown;

    @FindBy(xpath = "//android.widget.ImageView[starts-with(@content-desc, 'Product Type')]")
    private WebElement productTypeDropDown;

    @AndroidFindBy(accessibility = "Save")
    private WebElement saveButton;

    @AndroidFindBy(accessibility = "Cancel")
    private WebElement cancelButton;

    @AndroidFindBy(accessibility = "Settings saved successfully")
    private WebElement successMessage;

    public void selectOrderType(String orderType) {
        wait.until(ExpectedConditions.elementToBeClickable(typeDropDown)).click();
        log.info("Order Type Selected: " +orderType);
        String xpath = "//android.view.View[starts-with(@content-desc, '"
                + orderType
                + "')]/android.view.View";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void selectExchange(String exchange) {
        wait.until(ExpectedConditions.elementToBeClickable(exchangeDropDown)).click();
        log.info("Exchange Selected: " +exchange);
        String xpath = "//android.view.View[starts-with(@content-desc, '"
                + exchange
                + "')]/android.view.View";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }
    public void selectProductType(String productType) {
        wait.until(ExpectedConditions.elementToBeClickable(productTypeDropDown)).click();
        log.info("Product Type Selected: " +productType);
        String xpath = "//android.view.View[starts-with(@content-desc, '"
                + productType
                + "')]/android.view.View";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void setDefaultOrderSettings() {
        moreButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(settingsOption)).click();
        wait.until(ExpectedConditions.elementToBeClickable(defaultOrderSettingsOption)).click();
        wait.until(ExpectedConditions.elementToBeClickable(stocksTab)).click();
    }

    public void saveDefaultOrderSettings() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        Assert.assertTrue(successMessage.isDisplayed());
        log.info("Settings saved successfully!!!");
    }
}
