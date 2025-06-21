package com.jllt.pages.common;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import com.jllt.utils.configReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class loginPage extends basePage {
    private WebDriverWait wait;
    private JavascriptExecutor executor;
    private static final Logger logger = LoggerFactory.getLogger(loginPage.class);

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


    public loginPage(context testContext) {
        super(testContext);
        PageFactory.initElements(driver, this);
        this.executor = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        int timeout = Integer.parseInt(configReader.getProperty("timeout"));
    }

    public void loginToSalesforce() {
        String username = configReader.getEnvironmentProperty("username");
        String password = configReader.getEnvironmentProperty("password");
        String url = configReader.getEnvironmentProperty("url");
        logger.info("Attempting to login with username and password: {}", username);
        driver.get(url);
        login(username, password);
    }

    public void login(String username, String password) {
        logger.info("Attempting to login with username: {}", username);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        logger.info("Login button clicked");
    }

    /*public void loginAsUser(String targetUsername) throws InterruptedException {
        logger.info("Attempting to login as user: {}", targetUsername);
        wait.until(ExpectedConditions.elementToBeClickable(setupGear)).click();
        logger.debug("Setup gear clicked");
        wait.until(ExpectedConditions.visibilityOf(setupOption));
        wait.until(ExpectedConditions.elementToBeClickable(setupOption)).click();
        logger.debug("Setup option clicked");
        SearchUserOnSetupPage(targetUsername);
        logger.debug("Searched for user on setup page");
        Thread.sleep(5000);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[contains(@title, 'User')]")));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[contains(@title, 'User')]")));
        //driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@title, 'User')]")));
        Thread.sleep(5000);
        wait.until(ExpectedConditions.elementToBeClickable(LoginButtonOnUsersPage)).click();
        logger.debug("Login button on Users page clicked");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        logger.info("Logged in as user successfully");
    }*/

    public void loginAsUser(String targetUsername) throws InterruptedException {
        logger.info("Attempting to login as user: {}", targetUsername);
        wait.until(ExpectedConditions.elementToBeClickable(setupGear)).click();
        wait.until(ExpectedConditions.elementToBeClickable(setupOption)).click();
        SearchUserOnSetupPage(targetUsername);
        Thread.sleep(3000);
        testContext.getCommonUtils().switchToUserIframeWithRetry("//iframe[contains(@title, 'User')]", 5, 3); // Retry 5 times with 2 seconds wait
        wait.until(ExpectedConditions.elementToBeClickable(LoginButtonOnUsersPage)).click();
        logger.info("Logged in as user successfully");
    }

    public void SearchUserOnSetupPage(String inputString) throws InterruptedException {
        logger.info("Searching for user: {} on setup page", inputString);
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            Thread.sleep(5000);
            testContext.getCommonUtils().switchToTab(1);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Quick Find']"))).click();
            executor.executeScript("arguments[0].click();", searchInput);
            wait.until(ExpectedConditions.visibilityOf(searchInput));
            searchInput.clear();
            searchInput.sendKeys(inputString);
            logger.debug("Entered search input: {}", inputString);

            List<WebElement> userElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//div//ul//li//a[@role='option']//div//span[@title]")));

            userElements.stream()
                    .filter(element -> element.getText().toLowerCase().contains(inputString.toLowerCase()))
                    .findFirst()
                    .ifPresent(WebElement::click);

            logger.warn("No matching user found for: {}", inputString);
        } catch (Exception e) {
            logger.error("An error occurred while searching for user: {}", inputString, e);
        }
    }
}
