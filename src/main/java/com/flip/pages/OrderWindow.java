package com.flip.pages;

import io.appium.java_client.AppiumBy;
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
import java.util.List;

/**
 * @author 17636
 * @date 12-12-2025
 */
public class OrderWindow {
    private static final Logger log = LoggerFactory.getLogger(OrderWindow.class);
    public AndroidDriver driver;
    private WebDriverWait wait;

    // Constructor
    public OrderWindow(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(accessibility = "Market by Price (MBP)")
    private WebElement mbpDisplayed;

    @FindBy(xpath = "(//android.view.View[contains(@content-desc, '\n')])[15]")
    private WebElement lowerCircuitLimit;

    @AndroidFindBy(accessibility = "Buy")
    private WebElement buyButton;

    @FindBy(xpath = "//android.view.View[starts-with(@content-desc, 'NSE')]")
    private WebElement nseRadioButton;

    @AndroidFindBy(accessibility = "Limit\nTab 2 of 4")
    private WebElement limitOrderType;

    @FindBy(xpath = "//android.view.View[starts-with(@content-desc, 'Search & Add')]")
    private WebElement searchAndAddField;

    @FindBy(className = "android.widget.EditText")
    private WebElement searchField;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.widget.ImageView")
    private WebElement addFirstSymbol;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[1]")
    private WebElement symbolSearchBackButton;

    @FindBy(xpath = "//android.widget.ImageView[starts-with(@content-desc, 'Product Type')]")
    private WebElement productTypeSelection;

    @FindBy(xpath = "//android.widget.ImageView[starts-with(@content-desc, 'Time Condition')]")
    private WebElement timeConditionSelection;

    @FindBy(xpath = "//android.view.View[starts-with(@content-desc, 'Cash')]")
    private WebElement cashRadioButton;

    @FindBy(xpath = "//android.view.View[starts-with(@content-desc, 'Day')]")
    private WebElement dayRadioButton;

    @AndroidFindBy(accessibility = "Review order")
    private WebElement reviewOrderButton;

    @AndroidFindBy(accessibility = "Place Order")
    private WebElement placeOrderButton;

    @AndroidFindBy(accessibility = "Surveillance Alert")
    private WebElement surveillanceAlertPopup;

    @AndroidFindBy(accessibility = "Yes")
    private WebElement yesButton; //surveilance alert yes button.

    @FindBy(xpath = "//android.view.View[@content-desc=\"Open Orders\"]")
    private WebElement openOrdersButton;

    @FindBy(xpath = "//android.view.View[contains(@content-desc,'Order')]")
    private WebElement orderMessage;

    @FindBy(xpath = "android.widget.Button")
    private WebElement closeButtonOrderPlaced;

    @FindBy(xpath = "(//android.widget.EditText[@focusable='true'])[2]")
    private WebElement priceXpath;

    public WebElement qtyInput(String scripName) {
        String qtyXpath = "//android.view.View[contains(@content-desc,'" + scripName + "')]//android.widget.EditText[@focusable='true']";
        return driver.findElement(By.xpath(qtyXpath));
    }

    public void tapFirstSymbol() {
        String xpath = "(//android.widget.ImageView[contains(@content-desc, '\n')])[1]";
        driver.findElement(By.xpath(xpath)).click();
    }

    public void tapOnAddedSymbol(String scripName) {
        String xpath = "//android.widget.ImageView[starts-with(@content-desc, '" + scripName + "')]";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public String getLTP() {
        String priceElement = "//android.view.View[contains(@content-desc,'NSE:')]";
        String rawText = driver.findElement(By.xpath(priceElement))
                .getAttribute("content-desc");
        String price = rawText.replaceAll("[^0-9.]", "");
        double ltp = Double.parseDouble(price);
        double twoPercentLess = ltp * 0.98;
        long result = Math.round(twoPercentLess); // or cast / floor as needed
        log.info("LTP: {}", ltp);
        log.info("2% less than LTP (integer): {}", result);
        return String.valueOf(result);
    }


    //NSE, Buy, Limit, Cash and Day Order
    public void buyLimitCashDayOrder(){
        wait.until(ExpectedConditions.elementToBeClickable(buyButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(nseRadioButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(limitOrderType)).click();
        wait.until(ExpectedConditions.elementToBeClickable(productTypeSelection)).click();
        wait.until(ExpectedConditions.elementToBeClickable(cashRadioButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(timeConditionSelection)).click();
        wait.until(ExpectedConditions.elementToBeClickable(dayRadioButton)).click();
    }

    public void scrollToLowerCircuit(){
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"Lower Circuit\"))"
        ));
    }

    public void searchAndAddSymbol(String scripName){
        wait.until(ExpectedConditions.elementToBeClickable(searchAndAddField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchField.sendKeys(scripName);
        wait.until(ExpectedConditions.elementToBeClickable(addFirstSymbol)).click();
        wait.until(ExpectedConditions.elementToBeClickable(symbolSearchBackButton)).click();
        String xpath ="//android.widget.ImageView[starts-with(@content-desc, '" + scripName + "')]";
        Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
        log.info("Symbol added successfully");
    }

    public void buyOrderWindowDetailFilling(String scripName, String qty){
        String priceEntered = getLTP();

//        String qtyXpath = "//android.view.View[contains(@content-desc,'" + scripName + "')]//android.widget.EditText[@focusable='true']";
//        driver.findElement(By.xpath(qtyXpath)).click();
//        driver.findElement(By.xpath(qtyXpath)).clear();
//        driver.findElement(By.xpath(qtyXpath)).sendKeys(qty);
        qtyInput(qty).click();
        String priceXpath = "(//android.widget.EditText[@focusable='true'])[2]";
        driver.findElement(By.xpath(priceXpath)).click();
        driver.findElement(By.xpath(priceXpath)).clear();

        log.info("Price enetered: {}", priceEntered);

        driver.findElement(By.xpath(priceXpath)).sendKeys(priceEntered);
    }

    public void placeOrder() {

        wait.until(ExpectedConditions.elementToBeClickable(reviewOrderButton)).click();

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            if (surveillanceAlertPopup.isDisplayed()) {
                log.info("Surveillance Alert popup displayed");
                yesButton.click();
                wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
            }
        } catch (Exception e) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            log.info("No Surveillance Alert");
            wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
        }
            Assert.assertTrue(orderMessage.isDisplayed());
            log.info("Order placed!");
        logAllVisibleTexts();
    }

    public void logAllVisibleTexts() {
        List<WebElement> elements = driver.findElements(By.xpath("//*[@content-desc]"));

        log.info("------ Visible Page Contents ------");
        for (WebElement el : elements) {
            String text = el.getAttribute("content-desc");
            if (text != null && !text.trim().isEmpty()) {
                    log.info(text);
                }
        }
        log.info("------ End of Page Contents ------");
    }

    //buyOrderEntry_nseLimitCashDay

    public void buyOrderEntry_nseLimitCashDay(String scripName, String qty){
        searchAndAddSymbol(scripName);
        tapOnAddedSymbol(scripName);
        wait.until(ExpectedConditions.elementToBeClickable(mbpDisplayed));
        Assert.assertTrue(mbpDisplayed.isDisplayed());
        buyLimitCashDayOrder();
        buyOrderWindowDetailFilling(scripName, qty);
        placeOrder();
    }

}

