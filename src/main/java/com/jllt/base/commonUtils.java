package com.jllt.base;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentReportListener;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    public void pressEnter(){
        Actions actions=new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();
    }

    public void waitForSpinnerToDisappear() {
        //By spinner = By.cssSelector("lightning-spinner.slds-spinner_container");
        By spinner = By.xpath("//lightning-spinner[contains(@class,'slds-spinner_container')] | //*[contains(@class,'slds-spinner')] | //*[contains(@class,'slds-spinner_container')]");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /*public void switchToUserIframeWithRetry(String iframeXpath, int maxRetries, int waitSeconds) {
        By iframeLocator = By.xpath(iframeXpath);
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeLocator));
                logger.info("Successfully switched to user iframe on attempt: " + (attempts + 1));
                return;
            } catch (Exception e) {
                logger.warn("Attempt " + (attempts + 1) + " failed to find iframe. Retrying...");
                try {
                    Thread.sleep(waitSeconds * 1000L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                attempts++;
            }
        }
        throw new RuntimeException("Failed to switch to user iframe after " + maxRetries + " attempts.");
    }*/

    public void switchToUserIframeWithRetry(String iframeXpath, int maxRetries, int waitSeconds) {
        By iframeLocator = By.xpath(iframeXpath);
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                testContext.getLogger().info("Attempt {}: Found {} iframes", attempts + 1, iframes.size());

                for (WebElement iframe : iframes) {
                    String title = iframe.getAttribute("title");
                    String id = iframe.getAttribute("id");
                    String name = iframe.getAttribute("name");
                    String src = iframe.getAttribute("src");
                    testContext.getLogger().info("Iframe - title: '{}', id: '{}', name: '{}', src: '{}'", title, id, name, src);
                }

                // Try direct wait for target iframe
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeLocator));
                testContext.getLogger().info("Switched to iframe using XPath: {}", iframeXpath);
                return;

            } catch (Exception e) {
                testContext.getLogger().warn("Attempt {} failed. Retrying after {}s...", attempts + 1, waitSeconds);

                // Fallback: look for iframe with 'user' in title/name/src
                try {
                    List<WebElement> fallbackIframes = driver.findElements(By.tagName("iframe"));
                    for (WebElement iframe : fallbackIframes) {
                        String title = iframe.getAttribute("title");
                        String name = iframe.getAttribute("name");
                        String src = iframe.getAttribute("src");

                        if ((title != null && title.toLowerCase().contains("user")) ||
                                (name != null && name.toLowerCase().contains("user")) ||
                                (src != null && src.toLowerCase().contains("user"))) {

                            driver.switchTo().frame(iframe);
                            testContext.getLogger().info("Fallback: Switched to iframe via matching attributes");
                            return;
                        }
                    }
                } catch (Exception ignored) {
                    testContext.getLogger().warn("Fallback attempt to switch iframe failed.");
                }

                try {
                    Thread.sleep(waitSeconds * 1000L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            attempts++;
        }

        // ❌ After retries fail - take screenshot and log to Extent Report
        testContext.getLogger().error("Failed to switch to iframe after {} attempts.", maxRetries);
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String base64Screenshot = ts.getScreenshotAs(OutputType.BASE64);
            extentReportListener.addScreenshotToStep("Failed to switch to iframe after retries", base64Screenshot);
            testContext.getLogger().info("Screenshot added to Extent Report");
        } catch (Exception e) {
            testContext.getLogger().error("Error capturing screenshot", e);
        }

        throw new RuntimeException("Failed to switch to user iframe after " + maxRetries + " attempts.");
    }
}
