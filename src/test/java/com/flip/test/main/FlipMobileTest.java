package com.flip.test.main;

import com.flip.base.BaseTest;
import com.flip.pages.DefaultOrderSettings;
import com.flip.pages.LoginPage;
import com.flip.pages.OrderWindow;
import com.flip.pages.Watchlist;
import com.flip.utils.FlipTestDataReader;
import com.flip.utils.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author 17636
 * @date 09-12-2025
 */
@Listeners(TestListener.class)
public class FlipMobileTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(FlipMobileTest.class);
    private LoginPage loginPage;
    private DefaultOrderSettings defaultOrderSettings;
    private Watchlist watchlist;
    private OrderWindow orderWindow;
    private FlipTestDataReader testDataReader;

    @BeforeMethod
    public void setUpTest() {

        testDataReader = new FlipTestDataReader();

        loginPage = new LoginPage(driver);
        defaultOrderSettings = new DefaultOrderSettings(driver);
        watchlist = new Watchlist(driver);
        orderWindow = new OrderWindow(driver);
    }

    @Test(priority = 1, description = "Verify user primary login with valid credentials.")
    public void testTC001_successfulPrimaryLogin() {
        String testCaseId = "TC001";
        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
        try{
            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
            log.info("User logged in successfully!!!");
        }catch (Exception e){
            Assert.fail("Error message not displayed. Error: " +e.getMessage());
        }
    }

    @Test(priority = 2, description = "Default Order Settings - Order Type")
    public void testTC002_defaultOrderSettingsOrderType() {
        String testCaseId = "TC002";
        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
        try{
            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
            defaultOrderSettings.setDefaultOrderSettings();
            //Order Types must be either Limit, Market, Stop Loss Limit or Stop Loss Market in excel
            defaultOrderSettings.selectOrderType(testDataReader.getData(testCaseId, "Order Type"));
            defaultOrderSettings.saveDefaultOrderSettings();
        }catch (Exception e){
            Assert.fail("Error message not displayed. Error: " +e.getMessage());
        }
    }

    @Test(priority = 3, description = "Default Order Settings - Exchange Selection")
    public void testTC003_defaultOrderSettingsExchangeSelection() {
        String testCaseId = "TC003";
        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
        try{
            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
            defaultOrderSettings.setDefaultOrderSettings();
            //Order Types must be either Ask Everytime, NSE or BSE.
            defaultOrderSettings.selectExchange(testDataReader.getData(testCaseId, "Exchange"));
            defaultOrderSettings.saveDefaultOrderSettings();
        }catch (Exception e){
            Assert.fail("Error message not displayed. Error: " +e.getMessage());
        }
    }

    @Test(priority = 4, description = "Default Order Settings - Product Type Selection")
    public void testTC004_defaultOrderSettingsProductTypeSelection() {
        String testCaseId = "TC004";
        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
        try{
            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
            defaultOrderSettings.setDefaultOrderSettings();
            //Order Types must be either Cash, Intraday, BTST or MTF.
            defaultOrderSettings.selectProductType(testDataReader.getData(testCaseId, "Product Type"));
            defaultOrderSettings.saveDefaultOrderSettings();
        }catch (Exception e){
            Assert.fail("Error message not displayed. Error: " +e.getMessage());
        }
    }

    @Test(priority = 5, description = "Watchlist Creation")
    public void testTC005_watchlistCreation() {
        String testCaseId = "TC005";
        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
        try{
            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
            watchlist.createWatchlist(testDataReader.getData(testCaseId, "WatchlistName"));
        }catch (Exception e){
            Assert.fail("Error message not displayed. Error: " +e.getMessage());
        }
    }

//    @Test(priority = 6, description = "Long Press, Edit Watchlist display assertion, scroll and checkbox selection")
//    public void testTC006_editWatchlist() {
//        String testCaseId = "TC006";
//        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
//        try{
//            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
//            watchlist.editWatchlist();
//            watchlist.scrollDownToTheLastElement();
//            watchlist.checkBoxSelection();
//        }catch (Exception e){
//            Assert.fail("Error message not displayed. Error: " +e.getMessage());
//        }
//    }

    @Test(priority = 7, description = "Buy Order - NSE, Limit, Cash, Day")
    public void testTC007_buyOrderEntry_nseLimitCashDay() {
        String testCaseId = "TC007";
        log.info("----------------------------Test Case ID: " + testCaseId + "----------------------");
        try{
            loginPage.performLogin(testDataReader.getData(testCaseId, "Username"), testDataReader.getData(testCaseId, "Password"));
            orderWindow.buyOrderEntry_nseLimitCashDay(testDataReader.getData(testCaseId, "ScripName"), testDataReader.getData(testCaseId, "Quantity"));
        }catch (Exception e){
            Assert.fail("Error message not displayed. Error: " +e.getMessage());
        }
    }


}
