package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class accountsWdsfPage extends basePage {
    private JavascriptExecutor executor;

    @FindBy(xpath = "//one-app-nav-bar-item-root/a/span[text()='Accounts']/parent::*/parent::*")
    private WebElement AccountsTab;

    @FindBy(xpath = "//a[@title='New']")
    private WebElement newAccountButton;

    @FindBy(xpath = "//input[@name='accountName']")
    private WebElement SearchAndSelectAccountTxt;

    @FindBy(xpath = "//button[text()='Search']")
    private WebElement SearchBtn;

    @FindBy(xpath = "//button[text()='Search in Dun & Bradstreet']")
    private WebElement SearchInDunAndBradstreetBtn;

    @FindBy(xpath = "//button[text()='Create Account Request']")
    private WebElement CreateNewAccountRequestBtn;

    @FindBy(xpath = "//input[@name='dnbaccountName']")
    private WebElement AccountNameField;

    @FindBy(xpath = "//button[@name='dnbaccountRegion']")
    private WebElement RegionField;

    @FindBy(xpath = "//label[text()='Currency']")
    private WebElement CurrencyField;

    @FindBy(xpath = "//button[@aria-label='Industry']")
    private WebElement IndustryField;

    @FindBy(xpath = "(//label[text()='Street']/following::div/textarea)[1]")
    private WebElement StreetField;

    @FindBy(xpath = "//input[@name='cityValue']")
    private WebElement CityField;

    @FindBy(xpath = "//input[@name='stateValue']")
    private WebElement StateField;

    @FindBy(xpath = "//input[@name='postalCodeValue']")
    private WebElement ZipOrPostalCodeField;

    @FindBy(xpath = "//span[text()='Phone']/ancestor::div/following-sibling::lightning-input//input")
    private WebElement Phone;

    @FindBy(xpath = "//span[text()='Website']/ancestor::div/following-sibling::lightning-input//input")
    private WebElement Website;

    @FindBy(xpath = "//span[contains(text(),'Additional Request Details')]//ancestor::div//lightning-textarea//textarea")
    private WebElement AdditionalRequestDetailsField;

    @FindBy(xpath = "//input[@name='countryValue']")
    private WebElement CountryField;

    @FindBy(xpath = "//span[text()='Select Country']")
    private WebElement CountryDropdownOnAccountSearch;

    @FindBy(xpath = "//button[text()='Submit Account Request']")
    private WebElement SubmitButton;

    @FindBy(xpath = "//button[text()='Cancel']")
    private WebElement CancelButton;

    @FindBy(xpath = "//div[@class='slds-modal__content slds-p-around_medium']//lightning-formatted-rich-text//p/span")
    private List<WebElement> confirmationMessageParts;

    public accountsWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    public void navigateToAccountsTab() throws InterruptedException {
        testContext.getLogger().info("Navigating to Accounts tab");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(AccountsTab)).click();
        testContext.getLogger().info("Clicked on Accounts Tab");
        Thread.sleep(3000);
    }

    public void clickNewAccount(String recordType) throws InterruptedException {
        testContext.getLogger().info("Clicking New Account button and selecting record type: {}", recordType);

        // Click the New button
        wait.until(ExpectedConditions.elementToBeClickable(newAccountButton)).click();
        testContext.getLogger().info("Clicked on New Account button");
        Thread.sleep(3000);

        // Select the record type if specified
        if (recordType != null && !recordType.isEmpty()) {
            selectAccountRecordType(recordType);
        }
    }

    public void SearchAndSelectAccount(String accountName) throws InterruptedException {
        testContext.getLogger().info("Attempting to search and select account: {}", accountName);
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOf(SearchAndSelectAccountTxt));
        SearchAndSelectAccountTxt.sendKeys(accountName);
        logger.info("Entered account name in search field");
        Thread.sleep(1000);

        //,................................................
        selectCountryDropdownOnAccountSearch("United States");
        Thread.sleep(1000);

        wait.until(ExpectedConditions.elementToBeClickable(SearchBtn)).click();
        testContext.getLogger().info("Clicked on Search button");
        wait.until(ExpectedConditions.visibilityOf(SearchInDunAndBradstreetBtn));
        executor.executeScript("arguments[0].click();", SearchInDunAndBradstreetBtn);
        testContext.getLogger().info("Clicked on Search in Dun & Bradstreet button");
    }

    public void clickNewAccountRequestFormButton() {
        testContext.getLogger().info("Attempting to create new account: ");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        testContext.getWait().until(ExpectedConditions.visibilityOf(CreateNewAccountRequestBtn));
        executor.executeScript("arguments[0].click();", CreateNewAccountRequestBtn);
        testContext.getLogger().info("Clicked on Create New Company Request button");
    }

    public void CreateNewAccountRequestForm(Map<String, String> accountDetails) {
        testContext.getLogger().info("Filling New Account Request Form");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);

        String accountName = accountDetails.get("Account Name")+ "_" + randomSuffix;
        testContext.getLogger().info("Account: {}", accountName);

        //String companyName = accountDetails.get("Company Name") + "_" + randomSuffix;
        testContext.setContextData("accountName", accountName);
        testContext.getLogger().info("Set Company Name: {}", accountName);

        fillFormField(AccountNameField, accountName, "Account Name");
        //fillFormField(RegionField, accountDetails.get("Region"), "Region");
        fillFormField(StreetField, accountDetails.get("Street"), "Street");
        fillFormField(CityField, accountDetails.get("City"), "City");
        fillFormField(StateField, accountDetails.get("State/Province"), "State/Province");
        fillFormField(CountryField, accountDetails.get("Country"), "Country");

        //Select Region
        try {
            selectRegionDropdown(accountDetails.get("Region"));
            testContext.getLogger().info("Selected Region: {}", accountDetails.get("Region"));
        } catch (InterruptedException e) {
            testContext.getLogger().error("Error selecting Region: {}", e.getMessage());
        }

        //Select Industry
        try {
            selectIndustryDropdown(accountDetails.get("Industry"));
            testContext.getLogger().info("Selected Industry: {}", accountDetails.get("Industry"));
        } catch (InterruptedException e) {
            testContext.getLogger().error("Error selecting Industry: {}", e.getMessage());
        }

        //Select Currency
        try {
            selectCurrencyDropdown(accountDetails.get("Currency"));
            testContext.getLogger().info("Selected Currency: {}", accountDetails.get("Currency"));
        } catch (InterruptedException e) {
            testContext.getLogger().error("Error selecting Currency: {}", e.getMessage());
        }

        fillFormField(ZipOrPostalCodeField, accountDetails.get("Zip/PostalCode"), "Zip/PostalCode");
        //fillFormField(Phone, accountDetails.get("Phone"), "Phone");
        //fillFormField(Website, accountDetails.get("Website"), "Website");
        //fillFormField(AdditionalRequestDetailsField, accountDetails.get("Additional Request"), "Additional Request Details");
    }

    private void fillFormField(WebElement field, String value, String fieldName) {
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        testContext.getLogger().info("Entered {}: {}", fieldName, value);
    }

    private void selectRegionDropdown(String Val) throws InterruptedException {
        Thread.sleep(1000);
        RegionField.click();
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectCurrencyDropdown(String Val) throws InterruptedException {
        CurrencyField.click();
        Thread.sleep(6000);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectIndustryDropdown(String Val) throws InterruptedException {
        IndustryField.click();
        Thread.sleep(6000);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectCountryDropdownOnAccountSearch(String Val) throws InterruptedException {
        CountryDropdownOnAccountSearch.click();
        Thread.sleep(6000);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectAccountRecordType(String recordType) throws InterruptedException {
        testContext.getLogger().info("Attempting to select record type: {}", recordType);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='changeRecordTypeRow']")));
            Thread.sleep(1000);
            WebElement recordTypeElement = driver.findElement(By.xpath("//div[@class='changeRecordTypeRow']//../following-sibling::div/span[text()='" + recordType + "']"));

            wait.until(ExpectedConditions.elementToBeClickable(recordTypeElement));
            executor.executeScript("arguments[0].click();", recordTypeElement);
            testContext.getLogger().info("Selected record type: {}", recordType);

            // Click the Next button
            WebElement nextButton = driver.findElement(By.xpath("//button/span[text()='Next']"));
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            executor.executeScript("arguments[0].click();", nextButton);
            testContext.getLogger().info("Clicked Next button after record type selection");

            // Wait for the page to load after record type selection
            Thread.sleep(2000);
        } catch (Exception e) {
            testContext.getLogger().error("Error selecting record type: {}", e.getMessage());
            throw e;
        }
    }

    public String clickOnDAndBAccountName() throws InterruptedException {
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(5000);
        String clickedAccountName = null;

        try {
            // Get the number of rows in the table
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr/th[@data-label='Exist in SF']"));
            int rowCount = rows.size();
            testContext.getLogger().info("Found " + rowCount + " rows in results table");


            for (int i = 1; i <= rowCount; i++) {
                // Check if '+' icon exists for this row
                String plusIconXPath = "(//table/tbody/tr[" + i + "]/th//lightning-icon[contains(@class,'slds-icon-action-new')])";
                testContext.getLogger().info("Found " + plusIconXPath + " plusIconXPath");

                String checkIconXPath = "(//table/tbody/tr[" + i + "]/th//lightning-icon[contains(@class,'slds-icon_container_circle slds-icon-action-approval')])";
                testContext.getLogger().info("Found " + checkIconXPath + " checkIconXPath");

                boolean plusExists = !driver.findElements(By.xpath(plusIconXPath)).isEmpty();
                boolean checkExists = !driver.findElements(By.xpath(checkIconXPath)).isEmpty();

                // Only click if '+' exists (and no check mark)
                if (plusExists && !checkExists) {
                    // Get the account name before clicking
                    String accountButtonXPath = "(//table/tbody/tr[" + i + "]/th[@data-label='Exist in SF'])/following::td[1]//button";
                    WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(accountButtonXPath)));
                    testContext.getLogger().info("clicked on + sign");
                    executor.executeScript("arguments[0].scrollIntoView(true);", accountButton);

                    clickedAccountName = accountButton.getText();
                    testContext.getLogger().info("clickedAccountName" + clickedAccountName);
                    accountButton.click();
                    Thread.sleep(50000);

                    //waitForPageToLoad(driver);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in clickFirstDunAndBradStreetAccount method: " + e.getMessage());
            e.printStackTrace();
        }
        return clickedAccountName;
    }

    public String getConfirmationMessage() {
        wait.until(ExpectedConditions.visibilityOfAllElements(confirmationMessageParts));

        StringBuilder fullMessage = new StringBuilder();
        for (WebElement part : confirmationMessageParts) {
            fullMessage.append(part.getText()).append(" ");
        }

        String message = fullMessage.toString().trim();
        testContext.getLogger().info("Full confirmation message: {}", message);
        return message;
    }

    public void setSubmitButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(SubmitButton));
        executor.executeScript("arguments[0].click();", SubmitButton);
        testContext.getLogger().info("Clicked Submit button");
        Thread.sleep(2000);
    }

    public void cancelButton() {
        wait.until(ExpectedConditions.elementToBeClickable(CancelButton));
        executor.executeScript("arguments[0].click();", CancelButton);
        testContext.getLogger().info("Clicked Cancel button");
    }

    public void refreshAccountPage() {
        testContext.getCommonUtils().refreshPage();
    }

    public void verifyAccountLandingPageTitle() throws InterruptedException {
        Thread.sleep(2000);
        String expectedAccountName = testContext.getContextData("accountName").toString();
        String expectedTitle = expectedAccountName + " | Account | Salesforce";

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
        testContext.getLogger().info("AccountsPage teardown completed");
    }
}
