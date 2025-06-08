package com.jllt.base;

import com.jllt.scenarioContext.context;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class commonUtils extends basePage{
    private final JavascriptExecutor executor;
    //private WebDriverWait wait;

    public commonUtils(context testContext) {
        super(testContext);
        this.executor = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public void switchToTab(int index) {
        ArrayList<String> tabs = new ArrayList<>(testContext.getDriver().getWindowHandles());
        if (index >= 0 && index < tabs.size()) {
            testContext.getDriver().switchTo().window(tabs.get(index));
        } else {
            throw new IllegalArgumentException("Invalid tab index");
        }
    }

    public void switchToWindow() {
        String originalWindow = testContext.getDriver().getWindowHandle();
        testContext.getDriver().getWindowHandles().stream()
                .filter(handle -> !originalWindow.equals(handle))
                .findFirst()
                .ifPresent(handle -> testContext.getDriver().switchTo().window(handle));
    }

    public void jsScrollDown(WebElement element) {
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void jsScrollUp(WebElement element) {
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void takeScreenshot(String fileName) {

    }

    public String getPageTitle() {
        //return testContext.getDriver().getTitle();
        wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("")));
        return driver.getTitle();
    }

    public void refreshPage() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        testContext.getLogger().info("Page refreshed");
    }

    public String readToastMessage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'toastDescription')]/span[contains(@class,'toastMessage')]")));
        String message= driver.findElement(By.xpath("//div[contains(@id,'toastDescription')]/span[contains(@class,'toastMessage')]")).getText();
        return message;
    }

    public String generateRandomChars(int length) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        return random.ints(length, 0, alphanumeric.length())
                .mapToObj(alphanumeric::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public String getFutureDateFormatted(int daysToAdd) {
        LocalDate date = LocalDate.now().plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        return date.format(formatter);
    }
}
