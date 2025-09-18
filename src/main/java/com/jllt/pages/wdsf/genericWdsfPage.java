package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class genericWdsfPage extends basePage {
    protected JavascriptExecutor executor;

    @FindBy(xpath = "//input[@aria-label='Search this list...']")
    private WebElement recordSearchInput;

    public genericWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    public void selectAccount(String accName) throws InterruptedException {
        testContext.getCommonUtils().waitForModalToDisappear();
        testContext.getLogger().info("account value for Is Existing Account- {}", accName);
        WebElement accountLookup = driver.findElement(By.xpath("//label[text()='Account Name']/..//input[@placeholder='Search Accounts...']"));

        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", accountLookup);
        wait.until(ExpectedConditions.elementToBeClickable(accountLookup));
        executor.executeScript("arguments[0].click();", accountLookup);

        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(accountLookup));
                executor.executeScript("arguments[0].click();", accountLookup);
                Thread.sleep(500);
                //accountLookup.clear();
                testContext.getCommonUtils().clearFieldUsingKeys(accountLookup);
                accountLookup.sendKeys(accName);
                Thread.sleep(1000);

                String optionXpath = "//lightning-base-combobox-item/span[2]/span[1]/lightning-base-combobox-formatted-text[@title='" + accName + "']";
                WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXpath)));
                Thread.sleep(800);
                executor.executeScript("arguments[0].click();", option);
                testContext.getLogger().info("Company selected successfully: {}", accName);
                break;
            } catch (Exception e) {
                logger.warn("Attempt {} to select account failed: {}", attempts + 1, e.getMessage());
                Thread.sleep(500);
                attempts++;
            }
        }
    }

    public void selectDropDown(String val, String label) throws InterruptedException {
        Thread.sleep(5000);
        WebElement option = driver.findElement(By.xpath("//label[text()='" + label + "']/..//lightning-base-combobox-item//span[contains(text(),'" + val + "')] | //lightning-base-combobox-item//span[contains(text(),'" + val + "')]"));
        testContext.getCommonUtils().jsScrollToElement(option);
        Thread.sleep(500);
        executor.executeScript("arguments[0].click();", option);
        Thread.sleep(200);
    }

    public void waitForToastMessageToDisappear() throws InterruptedException {
        try {
            WebElement toastMessage = testContext.getDriver().findElement(
                    By.xpath("//div[contains(@class, 'slds-notify--toast')]"));
            if (toastMessage.isDisplayed()) {
                testContext.getLogger().info("Toast message detected, waiting for it to disappear");
                Thread.sleep(2000); // Wait for toast to disappear
            }
        } catch (Exception e) {
            // Toast message not found, continue
            testContext.getLogger().info("No toast message detected, proceeding");
        }
    }

    // Reusable method to get field value by label and tag
    public String getFieldValueByLabel(String label, String tag) {
        String xpath = String.format("//span[text()='%s']/parent::div/..//%s", label, tag);
        WebElement element = null;
        try {
            // Try to find the element
            executor.executeScript("window.scrollBy(0, 500);");
            element = driver.findElement(By.xpath(xpath));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Try scrolling down and searching again
            executor.executeScript("window.scrollBy(0, 500);");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            element = driver.findElement(By.xpath(xpath));
        }
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        testContext.getLogger().info("Get text for fields value - {}",element.getText().trim());
        return element.getText().trim();
    }

    // Reusable method to get field value by label and tag
    public String getFieldValueByLabel1(String label, String tag) {
        String xpath = String.format("//label[text()='%s']/following-sibling::div/..//%s", label, tag);
        WebElement element = null;
        try {
            // Try to find the element
            executor.executeScript("window.scrollBy(0, 500);");
            element = driver.findElement(By.xpath(xpath));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Try scrolling down and searching again
            executor.executeScript("window.scrollBy(0, 500);");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            element = driver.findElement(By.xpath(xpath));
        }
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        testContext.getLogger().info("Get text for fields value.. - {}",element.getText().trim());
        return element.getText().trim();
    }

    // Reusable method to get field value by label and tag
    public String getFieldValueByLabel3(String label, String tag) {
        String xpath = String.format("//span[text()='%s']/../..//%s", label, tag);
        WebElement element = null;
        try {
            // Try to find the element
            executor.executeScript("window.scrollBy(0, 500);");
            element = driver.findElement(By.xpath(xpath));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Try scrolling down and searching again
            executor.executeScript("window.scrollBy(0, 500);");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            element = driver.findElement(By.xpath(xpath));
        }
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        testContext.getLogger().info("Get text for fields value... - {}",element.getText().trim());
        return element.getText().trim();
    }

    // For fields that are links (like Phone, Mobile, Email)
    public String getFieldLinkValueByLabel(String label) {
        String xpath = String.format("//span[text()='%s']/parent::div/..//a", label);
        WebElement element = driver.findElement(By.xpath(xpath));
        testContext.getLogger().info("Get text for fields that are links - {}",element.getText().trim());
        return element.getText().trim();
    }

    //Get the Select object for a dropdown by its label.
    public Select getDropdownByLabel(String label) {
        String xpath = String.format("//span[text()='%s']/parent::label/..//select", label);
        WebElement dropdown = driver.findElement(By.xpath(xpath));
        return new Select(dropdown);
    }

    //Get the selected value of a dropdown by label.
    public String getSelectedDropdownValue(String label) {
        testContext.getLogger().info("Get selected value for dropdown - {}", getDropdownByLabel(label).getFirstSelectedOption().getText().trim());
        return getDropdownByLabel(label).getFirstSelectedOption().getText().trim();
    }

    // Setter for text fields
    public void setValueToTextField(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
        testContext.getLogger().info("Set value to the field {}", value);
    }

    public String getRecordNameFromContext() throws InterruptedException {
        String currentTab = String.valueOf(testContext.getContextData("currentTab"));
        if (currentTab != null) {
            switch (currentTab.trim()) {
                case "Accounts": return String.valueOf(testContext.getContextData("accountName"));
                case "Contacts": return String.valueOf(testContext.getContextData("contactName"));
                case "Opportunities": return String.valueOf(testContext.getContextData("Opportunity Name"));
                case "Leads": return String.valueOf(testContext.getContextData("LeadName"));
                default: break;
            }
        }

        // Fallback to page title if currentTab not set
        String title = testContext.getCommonUtils().getPageTitle();
        if (title != null) {
            if (title.contains("Accounts")) return String.valueOf(testContext.getContextData("accountName"));
            if (title.contains("Contacts")) return String.valueOf(testContext.getContextData("contactName"));
            if (title.contains("Opportunities")) return String.valueOf(testContext.getContextData("Opportunity Name"));
            if (title.contains("Leads")) return String.valueOf(testContext.getContextData("LeadName"));
        }

        // Last fallback: first non-empty known key
        String[] keys = {"accountName", "contactName", "Opportunity Name", "LeadName"};
        for (String k : keys) {
            Object v = testContext.getContextData(k);
            if (v != null && !String.valueOf(v).isBlank()) return String.valueOf(v);
        }
        Thread.sleep(1000);
        throw new IllegalStateException("No record name found for the current page/tab: " + title + " / " + currentTab);
    }

    public void goToTab(String tabName) throws InterruptedException {
        testContext.getDriver().switchTo().defaultContent();
        testContext.getCommonUtils().waitForPageLoad();

        By locator = By.xpath(String.format("//span[text()='%s']/parent::a", tabName));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(locator));
        // ensure the tab is clickable and page is ready

        try {
            tab.click();
        } catch (Exception e) {
            executor.executeScript("arguments[0].click();", tab);
        }

        testContext.setContextData("currentTab", tabName); // record which object tab we're on
        testContext.getLogger().info("Navigated to the {} tab", tabName);
        //testContext.getCommonUtils().waitForSpinnerToDisappear();
        testContext.getCommonUtils().verifyPageTitle(tabName, 15);
        testContext.getLogger().info("Page title verified to contain: {}", tabName);
        Thread.sleep(2000);
    }

    public void SearchRecordNameOnListView(String InputValue) {
        testContext.getLogger().info("Searching record on My recently viewed page: {}", InputValue);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean tableFound = false;
        int maxAttempts = 2;
        int attempts = 0;

        while (!tableFound && attempts < maxAttempts) {
            try {
                executor.executeScript("location.reload(true);");
                Thread.sleep(800);

                wait.until(ExpectedConditions.visibilityOf(recordSearchInput));
                if (recordSearchInput.isDisplayed()) {
                    recordSearchInput.click();
                    Thread.sleep(500);
                }
                else {
                    testContext.getCommonUtils().jsClickToElement(recordSearchInput);
                    Thread.sleep(500);
                }
                recordSearchInput.clear();
                recordSearchInput.sendKeys(InputValue);
                Thread.sleep(2000);
                recordSearchInput.sendKeys(Keys.ENTER);

                Thread.sleep(8000);

                WebElement tableRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//table[@aria-label='Recently Viewed']/tbody")));
                testContext.getLogger().info("Table found: {}", tableRow);

                if (tableRow.isDisplayed()) {
                    tableFound = true;
                    searchAndClickContact(InputValue, wait);
                }
            } catch (Exception e) {
                testContext.getLogger().info("Attempt {} failed. Retrying...", attempts + 1);
                attempts++;
                if (attempts >= maxAttempts) {

                    throw new RuntimeException("Table not found after multiple attempts", e);
                }
            }
        }
    }

    private void searchAndClickContact(String InputValue, WebDriverWait wait) {
        List<WebElement> cells = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//table[@aria-label='Recently Viewed']/tbody/tr/th//a")
                )
        );

        for (WebElement cell : cells) {
            try {
                String name = "";
                try { name = cell.getText() == null ? "" : cell.getText().trim(); } catch (Exception ignored) {}
                if (name.isEmpty()) {
                    String title = "";
                    try { title = cell.getAttribute("title"); } catch (Exception ignored) {}
                    if (title != null && !title.trim().isEmpty()) {
                        name = title.trim();
                    } else {
                        Object tc = ((JavascriptExecutor) driver)
                                .executeScript("return (arguments[0].textContent || '').trim();", cell);
                        name = tc == null ? "" : tc.toString();
                    }
                }

                testContext.getLogger().info("Record Value : {}", name);

                if (name.contains(InputValue)) {
                    try {
                        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", cell);
                        executor.executeScript("arguments[0].click();", cell);
                    } catch (Exception e) {
                        cell.click();
                    }
                    Thread.sleep(2000);
                    break;
                }
            } catch (StaleElementReferenceException | InterruptedException e) {
                cells = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.xpath("//table[@aria-label='Recently Viewed']/tbody/tr/th//a")
                        )
                );
            }
        }
    }
}
