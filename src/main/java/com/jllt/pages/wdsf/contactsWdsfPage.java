package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

public class contactsWdsfPage extends basePage {
    private JavascriptExecutor executor;

    @FindBy(xpath = "//one-app-nav-bar-item-root/a/span[text()='Contacts']/parent::*/parent::*")
    private WebElement contactsTab;

    @FindBy(xpath = "//a[@title='New']")
    private WebElement newContactButton;

    @FindBy(xpath = "//input[@name='firstName']")
    private WebElement firstName;

    @FindBy(xpath = "//input[@name='lastName']")
    private WebElement lastName;

    @FindBy(xpath = "//button[@aria-label='Contact Job Role']")
    private WebElement contactJobRole;

    @FindBy(xpath = "//label[text()='Account Name']/..//input[@placeholder='Search Accounts...']")
    private WebElement AccountName;

    @FindBy(xpath = "//label[text()='Account Name']/..//input")
    private WebElement companyNameLookup;

    @FindBy(xpath = "//input[@name='Phone']")
    private WebElement phone;

    @FindBy(xpath = "//input[@name='Email']")
    private WebElement email;

    @FindBy(xpath = "//input[@name='MobilePhone']")
    private WebElement mobile;

    @FindBy(xpath = "//button[@name='SaveEdit']")
    private WebElement saveEdit;

    @FindBy(xpath = "(//span[text()='Name']/parent::div/..//lightning-formatted-name)")
    private WebElement contactNameOnLandingPage;

    @FindBy(xpath = "(//span[text()='Account Name']/parent::div/..//a//span)[3]")
    private WebElement accountNameOnContactLandingPage;

    @FindBy(xpath = "//span[text()='Lead Source']/parent::div/..//lightning-formatted-text")
    private WebElement leadSourceOnContactLandingPage;

    @FindBy(xpath = "//span[text()='Edit Business Group']")
    private WebElement editBusinessGroup;

    @FindBy(xpath = "//button[@aria-label='Business Group']")
    private WebElement businessGroupButton;

    @FindBy(xpath = "//button[@aria-label='Sub Business Group']")
    private WebElement subBusinessGroupButton;

    @FindBy(xpath = "//button[@name='CancelEdit']")
    private WebElement cancelEdit;


    public contactsWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.logger = LoggerFactory.getLogger(contactsWdsfPage.class);
        this.executor = (JavascriptExecutor) driver;
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public void navigateToContactsTab() throws InterruptedException {
        testContext.getLogger().info("Attempting to click on New Contact");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        Thread.sleep(5000);
        wait.until(ExpectedConditions.elementToBeClickable(contactsTab)).click();
        Thread.sleep(200);
        extentReportListener.addScreenshotToStep("Click on Contacts Tab", getScreenshotBase64());
        testContext.getLogger().info("Clicked on Contacts Tab");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(2000);
    }

    public void clickNewContact(String recordType) throws InterruptedException {
        testContext.getLogger().info("Clicking New Contact button and selecting record type: {}", recordType);
        // Click the New button
        wait.until(ExpectedConditions.elementToBeClickable(newContactButton)).click();
        testContext.getLogger().info("Clicked on New Contact button");
        Thread.sleep(1000);
        extentReportListener.addScreenshotToStep("Click on New Contact", getScreenshotBase64());

        if (testContext.getAccountsWdsfPage().isRecordTypeSelectionPresent()) {
            if (recordType != null && !recordType.isEmpty()) {
                selectContactRecordType(recordType);
            }
        } else {
            testContext.getLogger().info("Record type selection screen not shown - continuing with contacts creation flow");
        }
    }

    public void CreateNewContact(Map<String, String> contactDetails) throws InterruptedException {
        testContext.getLogger().info("Filling New Contact Details");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);
        String contactName = contactDetails.get("Last Name") + "_" + randomSuffix;
        //String companyName = contactDetails.get("Company Name") + "_" + randomSuffix;

        fillNewContactInfo(firstName, contactDetails.get("First Name"), "First Name");
        fillNewContactInfo(lastName, contactName, "Contact Name");

        String accountName = String.valueOf(testContext.getContextData("accountName"));
        logger.info("Account Name...........................: {}", accountName);
        if (accountName != null && !accountName.isEmpty()) {
            selectAccount(accountName);
            logger.info("Selected Company: {}", accountName);
        } else {
            logger.warn("No account name found in context. Using default from contactDetails.");
            selectAccount(contactDetails.get("Account Name"));
        }

        fillNewContactInfo(phone, contactDetails.get("Phone"), "Phone");
        fillNewContactInfo(email, contactDetails.get("Email"), "Email");
        fillNewContactInfo(mobile, contactDetails.get("Mobile"), "Mobile");
        //Thread.sleep(1000);
        //extentReportListener.addScreenshotToStep("Fill the fields", getScreenshotBase64());

        if (testContext.getCommonUtils().isElementDisplayed(contactJobRole)){
            try {
                selectContactJobRole(contactDetails.get("Contact Job Role"));
                testContext.getLogger().info("Selected Contact Job Role: {}", contactDetails.get("Contact Job Role"));
            } catch (InterruptedException e) {
                testContext.getLogger().error("Error selecting Contact Job Role: {}", e.getMessage());
            }
        }

        testContext.setContextData("contactName", contactDetails.get("First Name") + " " + contactName);

        /*String accountName = testContext.getContextData("accountName");
        if (accountName != null && !accountName.isEmpty()) {
            contactDetails.put("Company Name", accountName);
        }*/
    }

    private void fillNewContactInfo(WebElement field, String value, String fieldName) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        testContext.getLogger().info("Entered {}: {}", fieldName, value);
        Thread.sleep(1000);
        extentReportListener.addScreenshotToStep("Fill the form", getScreenshotBase64());
    }

    public void selectContactJobRole(String Val) throws InterruptedException {
        contactJobRole.click();
        contactJobRole.sendKeys(Val);
        Thread.sleep(1000);
        WebElement Option= driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='"+Val+"']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    public void selectAccount(String val) throws InterruptedException {
        logger.info("Attempting to select company: {}", val);
        executor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", AccountName);
        wait.until(ExpectedConditions.visibilityOf(AccountName));
        wait.until(ExpectedConditions.elementToBeClickable(AccountName)).click();
        Thread.sleep(1000);
        AccountName.sendKeys(val);
        Thread.sleep(1000);
        WebElement Option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//lightning-base-combobox-formatted-text[@title='" + val + "']/..")));
        executor.executeScript("arguments[0].click();", Option);
        logger.info("Company selected successfully: {}", val);
        extentReportListener.addScreenshotToStep("Select Account", getScreenshotBase64());
    }

    public void setSaveEdit() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(saveEdit));
        executor.executeScript("arguments[0].click();", saveEdit);
        testContext.getLogger().info("Clicked Save button");
    }

    public void verifyContactLandingPageTitle() throws InterruptedException {
        Thread.sleep(2000);
        String expectedAccountName = testContext.getContextData("contactName").toString();
        String expectedTitle = expectedAccountName + " | Contact | Salesforce";

        wait.until(ExpectedConditions.titleContains(expectedAccountName));
        String actualTitle = driver.getTitle();

        testContext.getLogger().info("Expected Page Title: {}", expectedTitle);
        testContext.getLogger().info("Actual Page Title: {}", actualTitle);

        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Contact landing page title mismatch. Expected: " + expectedTitle + ", but got: " + actualTitle);
        }
        testContext.getLogger().info("Contact landing page title verified successfully.");
        extentReportListener.addScreenshotToStep("Verify Contact Landing Page   ", getScreenshotBase64());
    }

    private void selectContactRecordType(String recordType) throws InterruptedException {
        testContext.getLogger().info("Attempting to select record type: {}", recordType);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='changeRecordTypeRow']")));
            Thread.sleep(1000);
            WebElement recordTypeElement = driver.findElement(By.xpath("//div[@class='changeRecordTypeRow']//../following-sibling::div/span[text()='" + recordType + "'] | //div[@class='changeRecordTypeRow']//../lightning-input//span[text()='" + recordType + "']"));

            wait.until(ExpectedConditions.elementToBeClickable(recordTypeElement));
            executor.executeScript("arguments[0].click();", recordTypeElement);
            testContext.getLogger().info("Selected record type: {}", recordType);

            WebElement nextButton = driver.findElement(By.xpath("//button/span[text()='Next']"));
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            executor.executeScript("arguments[0].click();", nextButton);
            testContext.getLogger().info("Clicked Next button after record type selection");

            Thread.sleep(500);
        } catch (Exception e) {
            testContext.getLogger().error("Error selecting record type: {}", e.getMessage());
            throw e;
        }
    }

    public void verifyContactField(String fieldName, String expectedValue) throws InterruptedException {
        String label = fieldName == null ? "" : fieldName.trim();

        // Default expected values from context when blank in data table
        if (expectedValue == null || expectedValue.isBlank()) {
            if (label.equalsIgnoreCase("Name")) {
                Object v = testContext.getContextData("contactName");
                expectedValue = v == null ? "" : v.toString().trim();
            } else if (label.equalsIgnoreCase("Account Name") || label.equalsIgnoreCase("Account")) {
                Object v = testContext.getContextData("accountName");
                expectedValue = v == null ? "" : v.toString().trim();
            }
        }

        WebElement valueEl;
        switch (label) {
            case "Name":
                valueEl = contactNameOnLandingPage; // (//span[text()='Name']/parent::div/..//lightning-formatted-name)
                break;
            case "Account Name":
            case "Account":
                valueEl = accountNameOnContactLandingPage; // (//span[text()='Account Name']/parent::div/..//a//span)[3]
                break;
            case "Lead Source":
                valueEl = leadSourceOnContactLandingPage; // //span[text()='Lead Source']/parent::div/..//lightning-formatted-text
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + label);
        }

        try { executor.executeScript("arguments[0].scrollIntoView({block:'center'});", valueEl); } catch (Exception ignored) {}
        wait.until(ExpectedConditions.visibilityOf(valueEl));

        String actual = valueEl.getText() == null ? "" : valueEl.getText().replace('\u00A0',' ').trim();
        String expected = expectedValue == null ? "" : expectedValue.replace('\u00A0',' ').trim();

        testContext.getLogger().info("Verifying '{}' → Expected: '{}', Actual: '{}'", label, expected, actual);
        if (!actual.equals(expected)) {
            throw new AssertionError("Mismatch for '" + label + "'. Expected: '" + expected + "', Actual: '" + actual + "'");
        }
    }

    public void clickEditBusinessGroup() throws InterruptedException {
        try { executor.executeScript("arguments[0].scrollIntoView({block:'center'});", businessGroupButton); } catch (Exception ignored) {}
        WebElement edit = wait.until(ExpectedConditions.elementToBeClickable(editBusinessGroup));
        executor.executeScript("arguments[0].click();", edit);
        Thread.sleep(300);
    }

    public void selectBusinessGroup(String value) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(businessGroupButton));
        executor.executeScript("arguments[0].click();", businessGroupButton);
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//lightning-base-combobox-item//span[@title='" + value + "']")));
        testContext.getLogger().info("Business Group option: {}",option);
        executor.executeScript("arguments[0].click();", option);
        Thread.sleep(300);
    }

    /*public boolean isSubBusinessGroupOptionPresent(String optionText) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(subBusinessGroupButton));
        executor.executeScript("arguments[0].click();", subBusinessGroupButton);
        Thread.sleep(300);
        try {
            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//lightning-base-combobox-item//span[@title='" + optionText + "']")));
            testContext.getLogger().info("Sub Business Group option: {}",option);
            return option.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        } finally {
            try { subBusinessGroupButton.sendKeys(Keys.ESCAPE); } catch (Exception ignored) {}
        }
    }*/

    public boolean isSubBusinessGroupOptionPresent(String optionText) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(subBusinessGroupButton));
        executor.executeScript("arguments[0].click();", subBusinessGroupButton);
        Thread.sleep(300);

        boolean present;
        try {
            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//lightning-base-combobox-item//span[@title='" + optionText + "']")));
            testContext.getLogger().info("Sub Business Group option: {}",option);
            present = option.isDisplayed();
        } catch (TimeoutException e) {
            present = false;
        } finally {
            try { subBusinessGroupButton.sendKeys(Keys.ESCAPE); } catch (Exception ignored) {}
            try {
                wait.until(ExpectedConditions.elementToBeClickable(cancelEdit));
                executor.executeScript("arguments[0].click();", cancelEdit);
                Thread.sleep(2000);
            } catch (Exception ignored) {}
        }
        return present;
    }
}
