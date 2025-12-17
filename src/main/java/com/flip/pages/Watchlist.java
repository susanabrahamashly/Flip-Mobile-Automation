package com.flip.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.Map;

/**
 * @author 17636
 * @date 11-12-2025
 */
public class Watchlist {

    private static final Logger log = LoggerFactory.getLogger(Watchlist.class);
    public AndroidDriver driver;

    // Constructor
    public Watchlist(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @AndroidFindBy(accessibility = "My Watch")
    private WebElement myWatchButton;

    @AndroidFindBy(accessibility = "Create")
    private WebElement createButton;

    @FindBy(className = "android.widget.EditText")
    private WebElement watchlistName;

    @AndroidFindBy(accessibility = "Watchlist Saved!")
    private WebElement watchlistCreationSuccessPopUp;

    @AndroidFindBy(accessibility = "View Watchlist")
    private WebElement viewWatchlistButton;

    @FindBy(xpath = "//android.widget.ImageView[starts-with(@content-desc, \"NIFTY\")]")
    private WebElement niftySymbol;

    @AndroidFindBy(accessibility = "Edit Watchlist")
    private WebElement editWatchlistScreen;

    @FindBy(xpath = "//android.view.View[@content-desc=\"TATAGOLD\n" +
            "NSE\"]/android.widget.ImageView[2]")
    private WebElement lastSymbolCheckbox;

    public void createWatchlist(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(myWatchButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(createButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(watchlistName)).sendKeys(name);
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        wait.until(ExpectedConditions.elementToBeClickable(createButton)).click();
        Assert.assertTrue(watchlistCreationSuccessPopUp.isDisplayed());
        wait.until(ExpectedConditions.elementToBeClickable(viewWatchlistButton)).click();
    }

    public void longPressWebElement(RemoteWebElement element){
        driver.executeScript("gesture: longPress", Map.of("elementId", element.getId(), "pressure", 0.5, "duration", 2000));
    }

    public void editWatchlist(){
        wait.until(ExpectedConditions.elementToBeClickable(niftySymbol));
        RemoteWebElement element = (RemoteWebElement) niftySymbol;
        longPressWebElement(element);
        Assert.assertTrue(editWatchlistScreen.isDisplayed());
    }
}
