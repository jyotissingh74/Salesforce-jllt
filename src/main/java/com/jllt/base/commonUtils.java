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
    Actions actions=new Actions(driver);

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

    public void jsScrollToElement(WebElement element) throws InterruptedException {
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(1000);
    }

    public void jsClickToElement(WebElement element) throws InterruptedException {
        executor.executeScript("arguments[0].click();", element);
        Thread.sleep(500);
    }

    public void jsExpandSection(WebElement element) throws InterruptedException {
        executor.executeScript("arguments[0].setAttribute('aria-expanded', 'true');", element);
        Thread.sleep(500);
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

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }

    public void switchToUserIframeWithRetry(String iframeXpath, int maxRetries, int waitSeconds) {
        By iframeLocator = By.xpath(iframeXpath);
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                testContext.getLogger().info("Attempt {}: Found {} iframes", attempts + 1, iframes.size());

                for (WebElement iframe : iframes) {
                    String title = iframe.getDomAttribute("title");
                    String id = iframe.getDomAttribute("id");
                    String name = iframe.getDomAttribute("name");
                    String src = iframe.getDomAttribute("src");
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
                        String title = iframe.getDomAttribute("title");
                        String name = iframe.getDomAttribute("name");
                        String src = iframe.getDomAttribute("src");

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

    public void retryingClick(WebElement element, int maxAttempts) throws InterruptedException {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.ElementClickInterceptedException e) {
                testContext.getLogger().warn("Retrying click due to exception: {} (attempt {}/{})", e.getClass().getSimpleName(), attempts + 1, maxAttempts);
                attempts++;
                Thread.sleep(1000);
            }
        }
        throw new RuntimeException("Failed to click element after " + maxAttempts + " attempts: " + element);
    }

    public void clearFieldUsingKeys(WebElement element) {
        // Clear the field by sending Ctrl+A then Delete
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
    }

    public void clickUsingKeys(WebElement element) {
        actions.moveToElement(element).click().perform();
    }

    public void waitForModalToDisappear() {
        By modal = By.cssSelector(
                "div.slds-modal__container, div.modal-container, .slds-modal, .slds-backdrop, .slds-modal--open, .slds-fade-in-open"
        );

        // Wait for all overlays to be invisible
        wait.until(driver -> {
            List<WebElement> overlays = driver.findElements(modal);
            for (WebElement overlay : overlays) {
                if (overlay.isDisplayed()) {
                    return false;
                }
            }
            return true;
        });
    }

    public void scrollToEndOfPageUsingKeys() throws InterruptedException {
        actions.sendKeys(Keys.END).perform();
        Thread.sleep(1000);
    }

    public void clickOnElementUsingKeys(WebElement element) throws InterruptedException {
        actions.moveToElement(element).click().build().perform();
        Thread.sleep(500);
    }

    public void verifyPageTitle(String expectedTitle, int maxWaitSeconds) throws InterruptedException {
        int waited = 0;
        while (waited < maxWaitSeconds) {
            String actualTitle = getPageTitle();
            if (actualTitle != null && actualTitle.contains(expectedTitle)) {
                logger.info("Expected page title found: {}", actualTitle);
                return;
            }
            Thread.sleep(1000);
            waited++;
        }
        logger.error("Expected page title '{}' not found after {} seconds. Last title: {}", expectedTitle, maxWaitSeconds, getPageTitle());
    }

    public String generateRandomUSPhone() {
        java.util.Random r = new java.util.Random();
        int area = 200 + r.nextInt(800);      // 200-999
        int exchange = 200 + r.nextInt(800);  // 200-999
        int line = 1000 + r.nextInt(9000);    // 1000-9999
        return String.format("+1 (%03d) %03d-%04d", area, exchange, line);
    }

    public String generateRandomEmail(String baseEmail, String suffix) {
        String defaultDomain = "example.com";
        if (baseEmail == null || baseEmail.isBlank()) {
            return "lead.auto_" + suffix + "@" + defaultDomain;
        }
        int at = baseEmail.indexOf('@');
        if (at < 0) {
            return "lead.auto_" + suffix + "@" + defaultDomain;
        }
        String local = baseEmail.substring(0, at);
        String domain = baseEmail.substring(at + 1).trim();
        if (domain.isEmpty()) domain = defaultDomain;
        return local + "_" + suffix + "@" + domain;
    }

    public void preScrollToBottom() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            try {
                testContext.getCommonUtils().scrollToEndOfPageUsingKeys();
            } catch (Exception ignored) {
                executor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            }
            try {
                executor.executeScript(
                        "document.querySelectorAll('*').forEach(function(e){var s=getComputedStyle(e);" +
                                "if(s && (s.overflowY==='auto'||s.overflowY==='scroll')){e.scrollTop=e.scrollHeight;}});"
                );
            } catch (Exception ignored) {}
            Thread.sleep(300);
        }
    }

    public void scrollIntoViewCenter(WebElement element) throws InterruptedException {
        executor.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
        Thread.sleep(300);
    }

    public void scrollIntoViewIfNeeded(WebElement element) throws InterruptedException {
        executor.executeScript(
                "const el=arguments[0],r=el.getBoundingClientRect();" +
                        "if(r.top<0||r.bottom>window.innerHeight){el.scrollIntoView({block:'center',inline:'nearest'});}"+
                        "try{el.focus({preventScroll:true});}catch(e){}",
                element
        );
        Thread.sleep(200);
    }

    public void clickWhenReadyCentered(WebElement element) throws InterruptedException {
        scrollIntoViewIfNeeded(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            actions.moveToElement(element).click().perform();
        } catch (Exception e) {
            executor.executeScript("arguments[0].click();", element);
        }
        Thread.sleep(200);
    }
}
