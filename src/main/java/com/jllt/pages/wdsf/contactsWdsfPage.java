package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
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

    public contactsWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.logger = LoggerFactory.getLogger(contactsWdsfPage.class);
        this.executor = (JavascriptExecutor) driver;
    }

    public void clickNewContact() throws InterruptedException {
        testContext.getLogger().info("Attempting to click on New Contact");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        Thread.sleep(5000);
        wait.until(ExpectedConditions.elementToBeClickable(contactsTab)).click();
        testContext.getLogger().info("Clicked on Contacts Tab");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(newContactButton)).click();
        testContext.getLogger().info("Clicked on New Contact button");
        Thread.sleep(1000);
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

        try {
            selectContactJobRole(contactDetails.get("Contact Job Role"));
            testContext.getLogger().info("Selected Contact Job Role: {}", contactDetails.get("Contact Job Role"));
        } catch (InterruptedException e) {
            testContext.getLogger().error("Error selecting Contact Job Role: {}", e.getMessage());
        }

        testContext.setContextData("contactName", contactDetails.get("First Name") + " " + contactName);

        /*String accountName = testContext.getContextData("accountName");
        if (accountName != null && !accountName.isEmpty()) {
            contactDetails.put("Company Name", accountName);
        }*/
    }

    private void fillNewContactInfo(WebElement field, String value, String fieldName) {
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        testContext.getLogger().info("Entered {}: {}", fieldName, value);
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
            throw new AssertionError("Account landing page title mismatch. Expected: " + expectedTitle + ", but got: " + actualTitle);
        }
        testContext.getLogger().info("Account landing page title verified successfully.");
    }

    public void tearDown() {
        if (testContext.getDriver() != null) {
            testContext.getDriver().quit();
        }
        testContext.getLogger().info("ContactsPage teardown completed");
    }
}
