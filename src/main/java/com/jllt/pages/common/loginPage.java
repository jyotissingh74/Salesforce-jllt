package com.jllt.pages.common;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import com.jllt.utils.configReader;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class loginPage extends basePage {
    private final WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "Login")
    private WebElement loginButton;

    @FindBy(xpath = "//lightning-icon[@icon-name='utility:setup']")
    private WebElement setupGear;

    @FindBy(xpath = "//a[@title='Setup']")
    private WebElement setupOption;

    @FindBy(xpath = "//div[@class='slds-global-header__item slds-global-header__item_search']//input[@title='Search Setup']")
    private WebElement searchInput;

    @FindBy(xpath = "//td[@id='topButtonRow']//input[@name='login']")
    private WebElement LoginButtonOnUsersPage;

    @FindBy(xpath = "//div[@class='panel scrollable slds-docked-composer slds-grid slds-grid_vertical has-utility-bar slds-is-open']")
    private List<WebElement> composerPanels;

    @FindBy(xpath = "(//div[@class='panel scrollable slds-docked-composer slds-grid slds-grid_vertical has-utility-bar slds-is-open']//button)[3]")
    private WebElement closeButton;


    public loginPage(context testContext) {
        super(testContext);
        PageFactory.initElements(driver, this);
        String timeoutStr = configReader.getProperty("timeout");
        int timeout = 60;
        if (timeoutStr != null && !timeoutStr.trim().isEmpty()) {
            try { timeout = Integer.parseInt(timeoutStr.trim()); } catch (NumberFormatException ignored) {}
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public void loginToSalesforce() {
        String username = configReader.getEnvironmentProperty("username");
        String password = configReader.getEnvironmentProperty("password");
        String url = configReader.getEnvironmentProperty("url");
        testContext.getLogger().info("Attempting to login with username and password: {}", username);
        extentReportListener.addScreenshotToStep("Click on App Launcher", getScreenshotBase64());
        driver.get(url);
        login(username, password);
    }

    public void login(String username, String password) {
        testContext.getLogger().info("Attempting to login with username: {}", username);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        testContext.getLogger().info("Login button clicked");
    }

    public void loginAsUser(String targetUsername) throws InterruptedException {
        testContext.getLogger().info("Attempting to login as user: {}", targetUsername);
        wait.until(ExpectedConditions.elementToBeClickable(setupGear)).click();
        wait.until(ExpectedConditions.elementToBeClickable(setupOption)).click();
        SearchUserOnSetupPage(targetUsername);
        Thread.sleep(3000);
        testContext.getCommonUtils().switchToUserIframeWithRetry("//iframe[contains(@title, 'User')]", 5, 3);
        if (LoginButtonOnUsersPage.isDisplayed()) {
            testContext.getLogger().info("Login button is displayed for user: {}", targetUsername);
            testContext.getCommonUtils().jsClickToElement(LoginButtonOnUsersPage);
        } else {
            testContext.getCommonUtils().retryingClick(LoginButtonOnUsersPage, 3);
            testContext.getLogger().info("Clicked Login button for user: {}", targetUsername);
        }
        Thread.sleep(2000);

        /*// Wait for the page title to confirm login
        String expectedTitle = "Home | Salesforce";
        boolean titleMatched = testContext.getCommonUtils().verifyPageTitle(expectedTitle, 60);
        if (!titleMatched) {
            throw new RuntimeException("Login as user did not succeed, Home page title not found. Last title: " + testContext.getCommonUtils().getPageTitle());
        }*/

        driver.switchTo().defaultContent();
        dismissPopUp();
    }

    public void SearchUserOnSetupPage(String inputString) throws InterruptedException {
        testContext.getLogger().info("Searching for user: {} on setup page", inputString);
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            Thread.sleep(5000);
            testContext.getCommonUtils().switchToTab(1);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Quick Find']"))).click();
            wait.until(ExpectedConditions.visibilityOf(searchInput));
            testContext.getCommonUtils().jsClickToElement(searchInput);
            searchInput.clear();
            searchInput.sendKeys(inputString);
            testContext.getLogger().debug("Entered search input: {}", inputString);

            // Wait for the dropdown to appear and find the user
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div//ul//li//a[@role='option']//div//span[@title]")));
            List<WebElement> userElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//div//ul//li//a[@role='option']//div//span[@title]")));

            userElements.stream()
                    .filter(element -> element.getText().toLowerCase().contains(inputString.toLowerCase()))
                    .findFirst()
                    .ifPresent(WebElement::click);

            testContext.getLogger().warn("No matching user found for: {}", inputString);
        } catch (Exception e) {
            testContext.getLogger().error("An error occurred while searching for user: {}", inputString, e);
        }
    }

    public void dismissPopUp() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(d -> !composerPanels.isEmpty());
        } catch (TimeoutException ignored) {
            return;
        }

        try {
            if (!composerPanels.isEmpty() && composerPanels.get(0).isDisplayed()) {
                testContext.getLogger().info("Composer panel is displayed, attempting to close it.");
                WebElement panel = composerPanels.get(0); // capture once

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeButton);
                }

                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.invisibilityOf(panel));
                testContext.getLogger().info("Composer panel closed successfully.");
            }
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ignored) {
            // Popup disappeared by itself or already closed
        }
    }
}
