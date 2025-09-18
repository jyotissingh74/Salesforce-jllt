package com.jllt.pages.common;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import com.jllt.utils.configReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

public class appLauncherPage extends basePage {
    private WebDriverWait wait;
    private JavascriptExecutor executor;
    private Actions actions;
    private static final Logger logger = LoggerFactory.getLogger(appLauncherPage.class);

    @FindBy(xpath = "//one-app-launcher-header//button[@title='App Launcher']")
    private WebElement AppLauncherTab;

    @FindBy(xpath = "//input[@placeholder='Search apps and items...']")
    private WebElement AppLauncherSearch;

    @FindBy(xpath = "//label[contains(text(),'Search apps and items...')]/following-sibling::div/input/parent::div")
    private WebElement AppLauncherSearchbox;

    public appLauncherPage(context testContext) {
        super(testContext);
        PageFactory.initElements(driver, this);
        this.executor = (JavascriptExecutor) driver;
        this.actions= new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        int timeout = Integer.parseInt(configReader.getProperty("timeout"));
    }

    public void fillAppScreen(Map<String, String> appDetails) throws InterruptedException {

        fillAppScreen(AppLauncherSearchbox, appDetails.get("AppName"), "AppName");
        if (appDetails.containsKey("AppName") && appDetails.get("AppName") != null) {
            wait.until(ExpectedConditions.visibilityOf(AppLauncherSearchbox));
            AppLauncherSearchbox.click();
            clickAppLauncherAndSearchApp(appDetails.get("AppName"));
        }
    }
    public void fillAppScreen (WebElement field, String value, String fieldName){
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        logger.info("Entered {}: {}", fieldName, value);
    }

    public void clickAppLauncherAndSearchApp(String appName) throws InterruptedException {
        try {
            testContext.getDriver().switchTo().defaultContent();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            Thread.sleep(5000);
            wait.until(ExpectedConditions.elementToBeClickable(AppLauncherTab));
            executor.executeScript("arguments[0].click();", AppLauncherTab);
            logger.debug("App Launcher clicked");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.visibilityOf(AppLauncherSearchbox));
            executor.executeScript("arguments[0].click();", AppLauncherSearchbox);
            AppLauncherSearchbox.sendKeys(appName);
            logger.debug("Entered App Name successfully: {}", appName);
            Thread.sleep(2000);
        } catch (Exception e) {
            logger.error("An error occurred while searching: {}", appName, e);
        }
    }
}
