package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class accountsWdsfPage extends basePage {
    private final JavascriptExecutor executor;

    @FindBy(xpath = "//span[text()='Accounts']/parent::a")
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

    @FindBy(xpath = "//input[@name='dnbaccountName'] | //input[@name='accountRequestName']")
    private WebElement AccountNameField;

    @FindBy(xpath = "//button[@name='dnbaccountRegion']")
    private WebElement RegionField;

    @FindBy(xpath = "//label[text()='Currency']")
    private WebElement CurrencyField;

    @FindBy(xpath = "//button[@aria-label='Industry']")
    private WebElement IndustryField;

    @FindBy(xpath = "(//label[text()='Street']/following::div/textarea)[1]")
    private WebElement StreetField;

    @FindBy(xpath = "//input[@name='cityValue'] | //input[@name='accountRequestCity']")
    private WebElement CityField;

    @FindBy(xpath = "//input[@name='stateValue']")
    private WebElement StateField;

    @FindBy(xpath = "//input[@name='postalCodeValue'] | //input[@name='accountRequestPostalCode']")
    private WebElement ZipOrPostalCodeField;

    @FindBy(xpath = "//span[text()='Phone']/ancestor::div/following-sibling::lightning-input//input")
    private WebElement Phone;

    @FindBy(xpath = "//span[text()='Website']/ancestor::div/following-sibling::lightning-input//input")
    private WebElement Website;

    @FindBy(xpath = "//span[contains(text(),'Additional Request Details')]//ancestor::div//lightning-textarea//textarea")
    private WebElement AdditionalRequestDetailsField;

    @FindBy(xpath = "//button[@name='countryValue']")
    private WebElement CountryField;

    @FindBy(xpath = "//span[text()='Country']")
    private WebElement CountryDropdownOnAccountSearch;

    @FindBy(xpath = "//button[text()='Submit Account Request']")
    private WebElement SubmitButton;

    @FindBy(xpath = "//button[text()='Cancel']")
    private WebElement CancelButton;

    @FindBy(xpath = "//div[@class='slds-modal__content slds-p-around_medium']//lightning-formatted-rich-text//p/span")
    private List<WebElement> confirmationMessageParts;

    @FindBy(xpath = "//button[@name='dnbaccountSector']")
    private WebElement Sector;

    @FindBy(xpath = "//button[text()='Create Account']")
    private WebElement CreateAccountButton;

    @FindBy(xpath = "//span[text()='Account Name']/parent::div/..//lightning-formatted-text[text()]")
    private WebElement AccountNameOnLandingPage;

    @FindBy(xpath = "//span[text()='Account Source']/parent::div/..//lightning-formatted-text[text()]")
    private WebElement AccountSourceOnLandingPage;

    @FindBy(xpath = "//span[text()='Industry']/parent::div/..//lightning-formatted-text[text()]")
    private WebElement IndustryOnLandingPage;

    @FindBy(xpath = "(//span[text()='Account Record Type']/parent::div/following-sibling::div//span[text()])[1]")
    private WebElement AccountRecordTypeOnLandingPage;

    @FindBy(xpath = "//span[text()='Account Region']/parent::div/..//lightning-formatted-text[text()]")
    private WebElement AccountRegionOnLandingPage;

    public accountsWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public void navigateToAccountsTab() throws InterruptedException {
        testContext.getLogger().info("Navigating to Accounts tab");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        testContext.getCommonUtils().waitForPageLoad();

        testContext.getGenericWdsfPage().waitForToastMessageToDisappear();
        Thread.sleep(500);
        wait.until(ExpectedConditions.elementToBeClickable(AccountsTab));

        // Try using JavaScript click instead of direct WebElement click
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", AccountsTab);
        } catch (Exception e) {
            testContext.getLogger().warn("JS click failed, trying direct click: {}", e.getMessage());
            testContext.getCommonUtils().retryingClick(AccountsTab, 3);
        }

        Thread.sleep(1000);
        //wait.until(ExpectedConditions.elementToBeClickable(AccountsTab)).click();
        //wait.until(ExpectedConditions.elementToBeClickable(AccountsTab));
        //executor.executeScript("arguments[0].click();", AccountsTab);
        testContext.getLogger().info("Clicked on Accounts Tab");
        testContext.getCommonUtils().waitForPageLoad();
        Thread.sleep(500);
    }

    public void clickNewAccount(String recordType) throws InterruptedException {
        testContext.getLogger().info("Clicking New Account button and selecting record type: {}", recordType);

        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(newAccountButton));
        Thread.sleep(500);
        if(newAccountButton.isDisplayed()){
            testContext.getCommonUtils().jsClickToElement(newAccountButton);
        }else {
            testContext.getCommonUtils().retryingClick(newAccountButton,3);
            testContext.getLogger().info("Clicked on New Account button");
        }
        Thread.sleep(500);

        boolean attempted = false;
        try {
            if (isRecordTypeSelectionPresent()) {
                selectAccountRecordType(recordType);  // uses ONLY your XPaths
                attempted = true;
            }
        } catch (Exception e) {
            testContext.getLogger().warn("Record type presence path failed: {}", e.getMessage());
        }
        if (!attempted) {
            try {
                // Fallback: still try to select using your XPaths
                selectAccountRecordType(recordType);
            } catch (Exception e) {
                testContext.getLogger().info("Record type selection not available or failed; continuing. Reason: {}", e.getMessage());
            }
        }

        wait.until(ExpectedConditions.visibilityOf(SearchAndSelectAccountTxt));
    }

    public boolean isRecordTypeSelectionPresent() throws InterruptedException {
        Thread.sleep(1000);
        return !driver.findElements(By.xpath("//div[@class='changeRecordTypeRow']")).isEmpty();
        /*try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='changeRecordTypeRow']"))) != null;
        } catch (Exception e) {
            testContext.getLogger().info("Record type selection dialog not found");
            return false;
        }*/
    }

    public void SearchAndSelectAccount(String accountName) throws InterruptedException {
        testContext.getLogger().info("Attempting to search and select account: {}", accountName);
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(200);
        wait.until(ExpectedConditions.visibilityOf(SearchAndSelectAccountTxt));
        SearchAndSelectAccountTxt.clear();
        SearchAndSelectAccountTxt.sendKeys(accountName);
        logger.info("Entered account name in search field");
        Thread.sleep(1000);

        selectCountryDropdownOnAccountSearch("United States");
        Thread.sleep(500);

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

    public void CreateNewAccountRequestForm(Map<String, String> accountDetails) throws InterruptedException {
        testContext.getLogger().info("Filling New Account Request Form");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(7);

        String accountName = accountDetails.get("Account Name")+ "_" + randomSuffix;
        testContext.getLogger().info("Account: {}", accountName);

        //String companyName = accountDetails.get("Company Name") + "_" + randomSuffix;
        testContext.setContextData("accountName", accountName);
        testContext.getLogger().info("Set Company Name: {}", accountName);

        Thread.sleep(2000);
        AccountNameField.clear();
        fillFormField(AccountNameField, accountName, "Account Name");
        //fillFormField(RegionField, accountDetails.get("Region"), "Region");
        fillFormField(StreetField, accountDetails.get("Street"), "Street");
        fillFormField(CityField, accountDetails.get("City"), "City");
        fillFormField(StateField, accountDetails.get("State/Province"), "State/Province");
        //fillFormField(CountryField, accountDetails.get("Country"), "Country");
        selectCountryField(accountDetails.get("Country"));

        //Select Region
        try {
            wait.until(ExpectedConditions.elementToBeClickable(RegionField));
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

        //Select Sector
        if(testContext.getCommonUtils().isElementDisplayed(Sector)){
            selectSectorDropdown(accountDetails.get("Sector"));
            testContext.getLogger().info("Selected Sector: {}", accountDetails.get("Sector"));
        }
        Thread.sleep(2000);
    }

    private void fillFormField(WebElement field, String value, String fieldName) {
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", field);
        field.clear();
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        testContext.getLogger().info("Entered {}: {}", fieldName, value);
    }

    private void selectCountryField(String country){
        try {
            testContext.getLogger().info("Setting Country field with JavaScript: {}", country);
            executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", CountryField);
            Thread.sleep(500);
            executor.executeScript("arguments[0].click();", CountryField);
            Thread.sleep(1000);
            WebElement countryOption = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + country + "']"));
            executor.executeScript("arguments[0].click();", countryOption);
            testContext.getLogger().info("Selected Country: {}", country);
            Thread.sleep(1000);
        } catch (Exception e) {
            testContext.getLogger().error("Error setting Country: {}", e.getMessage());
        }
    }

    private void selectRegionDropdown(String Val) throws InterruptedException {
        Thread.sleep(500);
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", RegionField);
        RegionField.click();
        Thread.sleep(1000);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectCurrencyDropdown(String Val) throws InterruptedException {
        CurrencyField.click();
        Thread.sleep(500);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectIndustryDropdown(String Val) throws InterruptedException {
        IndustryField.click();
        Thread.sleep(500);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectSectorDropdown(String Val) throws InterruptedException {
        testContext.getCommonUtils().jsScrollToElement(Sector);
        executor.executeScript("arguments[0].click();", Sector);
        Thread.sleep(1000);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectCountryDropdownOnAccountSearch(String Val) throws InterruptedException {
        CountryDropdownOnAccountSearch.click();
        Thread.sleep(500);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        executor.executeScript("arguments[0].click();", Option);
    }

    private void selectAccountRecordType(String recordType) throws InterruptedException {
        testContext.getLogger().info("Attempting to select record type: {}", recordType);

        try {
            By option = By.xpath("//div[@class='changeRecordTypeRow']//../following-sibling::div/span[text()='" + recordType + "']"
                    + " | //div[@class='changeRecordTypeRow']//../lightning-input//span[text()='" + recordType + "']");
            WebElement recordTypeElement = wait.until(ExpectedConditions.elementToBeClickable(option));
            executor.executeScript("arguments[0].click();", recordTypeElement);
            testContext.getLogger().info("Selected record type: {}", recordType);

            By nextBtn = By.xpath("//button/span[text()='Next'] | //button[text()='Next']");
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            executor.executeScript("arguments[0].click();", nextButton);
            testContext.getLogger().info("Clicked Next button after record type selection");

            Thread.sleep(500);
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
                    Thread.sleep(5000);

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

    public void createAccountFromDnBSearch() throws InterruptedException {
        // Wait for D&B results to load
        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//th[@data-label='Exist in SF']")));
        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr/th[@data-label='Exist in SF']"));
        testContext.getLogger().info("Found {} rows in results table.", rows.size());

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr/th[@data-label='Exist in SF']/..//button[@title='Create New Account']")));
        List<WebElement> plusButtons = driver.findElements(By.xpath("//table/tbody/tr/th[@data-label='Exist in SF']/..//button[@title='Create New Account']"));
        testContext.getLogger().info("Found {} Create New Account buttons", plusButtons.size());

        if (plusButtons.isEmpty()) {
            throw new RuntimeException("No D&B search results with a '+' (Create New Account) button were found. Check your search criteria or page state.");
        }

        boolean clicked = false;
        for (WebElement plusBtn : plusButtons) {
            if (plusBtn.isDisplayed() && plusBtn.isEnabled()) {
                try {
                    By overlay = By.cssSelector(".slds-utility-bar, .slds-backdrop, .slds-modal__container");
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
                } catch (Exception ignored) {}

                try {
                    executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", plusBtn);
                    Thread.sleep(500);
                    testContext.getLogger().info("Scrolling to and clicking on Create New Account button");
                    //executor.executeScript("arguments[0].click();", plusBtn);
                    testContext.getCommonUtils().jsClickToElement(plusBtn);
                    wait.until(ExpectedConditions.visibilityOf(AccountNameField));
                    clicked = true;
                    break;
                } catch (Exception e) {
                    testContext.getLogger().warn("Failed to click plus button: {}", e.getMessage());
                }
            }
        }

        if (!clicked) {
            throw new RuntimeException("Could not click any 'Create New Account' (+) button after trying all available.");
        }
    }

    /*public void fillAndCreateAccount(Map<String, String> accountDetails) throws InterruptedException {
        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);
        String accountName = accountDetails.get("Account Name") + "_" + randomSuffix;
        testContext.getLogger().info("Filling in account details with random suffix: {}", accountName);

        // Clear and fill Account Name
        wait.until(ExpectedConditions.visibilityOf(AccountNameField));
        //testContext.getCommonUtils().clearFieldUsingKeys(AccountNameField);
        AccountNameField.clear();
        AccountNameField.sendKeys(accountName);

        //Select Region
        wait.until(ExpectedConditions.elementToBeClickable(RegionField));
        selectRegionDropdown(accountDetails.get("Region"));
        testContext.getLogger().info("Selected Region for D&B account creation: {}", accountDetails.get("Region"));


        //Select Industry
        selectIndustryDropdown(accountDetails.get("Industry"));
        testContext.getLogger().info("Selected Industry for D&B account creation: {}", accountDetails.get("Industry"));

        //Select Currency
            selectCurrencyDropdown(accountDetails.get("Currency"));
            testContext.getLogger().info("Selected Currency for D&B account creation: {}", accountDetails.get("Currency"));
        Thread.sleep(5000);

        // Click Create Account button
        wait.until(ExpectedConditions.visibilityOf(CreateAccountButton));
        //CreateAccountButton.click();
        testContext.getCommonUtils().clickOnElementUsingKeys(CreateAccountButton);
        testContext.getCommonUtils().jsClickToElement(CreateAccountButton);
        Thread.sleep(2000);
        testContext.setContextData("accountName", accountName);
        testContext.getLogger().info("Clicked Create Account button with account name: {}", accountName);
    }*/

    public void fillAndCreateAccount(Map<String, String> accountDetails) throws InterruptedException {
        String accountName;
        Object ctx = testContext.getContextData("accountName");
        if (ctx != null && !String.valueOf(ctx).trim().isEmpty()) {
            accountName = String.valueOf(ctx).trim();
            testContext.getLogger().info("Using accountName from context for D&B account creation: {}", accountName);
        } else {
            String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);
            accountName = accountDetails.get("Account Name") + "_" + randomSuffix;
            testContext.getLogger().info("Context accountName not found; generating new: {}", accountName);
            testContext.setContextData("accountName", accountName);
        }

        // Clear and fill Account Name
        wait.until(ExpectedConditions.visibilityOf(AccountNameField));
        AccountNameField.clear();
        AccountNameField.sendKeys(accountName);

        // Select Region
        wait.until(ExpectedConditions.elementToBeClickable(RegionField));
        selectRegionDropdown(accountDetails.get("Region"));
        testContext.getLogger().info("Selected Region for D&B account creation: {}", accountDetails.get("Region"));

        // Select Industry
        selectIndustryDropdown(accountDetails.get("Industry"));
        testContext.getLogger().info("Selected Industry for D&B account creation: {}", accountDetails.get("Industry"));

        // Select Currency
        selectCurrencyDropdown(accountDetails.get("Currency"));
        testContext.getLogger().info("Selected Currency for D&B account creation: {}", accountDetails.get("Currency"));
        Thread.sleep(5000);

        // Click Create Account button
        wait.until(ExpectedConditions.visibilityOf(CreateAccountButton));
        testContext.getCommonUtils().clickOnElementUsingKeys(CreateAccountButton);
        testContext.getCommonUtils().jsClickToElement(CreateAccountButton);
        Thread.sleep(2000);
        testContext.setContextData("accountName", accountName);
        testContext.getLogger().info("Clicked Create Account button with account name: {}", accountName);
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
        Thread.sleep(1500);
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
        Thread.sleep(1500);
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

    // ADDED: consolidated helper for account creation
    public void createAccountRequest(Map<String, String> accountDetails, String recordType, String searchAccountName) throws InterruptedException {
        testContext.getLogger().info("Starting Account creation helper method");
        navigateToAccountsTab();
        clickNewAccount(recordType);

        String searchName = (searchAccountName == null || searchAccountName.isBlank())
                ? accountDetails.getOrDefault("Search Account Name", accountDetails.getOrDefault("Account Name", ""))
                : searchAccountName;
        if (searchName != null && !searchName.isBlank()) {
            SearchAndSelectAccount(searchName);
        }

        //SearchAndSelectAccount(searchAccountName);
        clickNewAccountRequestFormButton();
        CreateNewAccountRequestForm(accountDetails);
        setSubmitButton();
        verifyAccountLandingPageTitle();
        testContext.getLogger().info("Account created successfully via helper method");
    }

    public void verifyAccountField(String fieldName, String expectedValue) throws InterruptedException {
        String label = fieldName.equalsIgnoreCase("Account") ? "Account Name"
                : fieldName.equalsIgnoreCase("Region") ? "Account Region"
                : fieldName;

        if ((expectedValue == null || expectedValue.isBlank()) && fieldName.equalsIgnoreCase("Account")) {
            Object v = testContext.getContextData("accountName");
            expectedValue = v == null ? "" : v.toString().trim();
        }

        WebElement valueEl;

        if (label.equals("Account Record Type") || label.equals("Record Type")) {
            try {
                executor.executeScript("arguments[0].scrollIntoView({block:'center'});", AccountRecordTypeOnLandingPage);
            } catch (Exception ignored) {}
            valueEl = wait.until(ExpectedConditions.visibilityOf(AccountRecordTypeOnLandingPage));
        } else {
            switch (label) {
                case "Account Name":
                    valueEl = AccountNameOnLandingPage;
                    break;
                case "Account Region":
                    valueEl = AccountRegionOnLandingPage;
                    break;
                case "Industry":
                    valueEl = IndustryOnLandingPage;
                    break;
                case "Account Source":
                    valueEl = AccountSourceOnLandingPage;
                    break;
                default:
                    String baseXpath = String.format("//span[text()='%s']/parent::div/..", label);
                    By genericValue = By.xpath(baseXpath + "//lightning-formatted-text[normalize-space()] | " + baseXpath + "//span[normalize-space()]");
                    valueEl = wait.until(ExpectedConditions.visibilityOfElementLocated(genericValue));
            }

            try {
                executor.executeScript("arguments[0].scrollIntoView({block:'center'});", valueEl);
            } catch (Exception ignored) {}
            wait.until(ExpectedConditions.visibilityOf(valueEl));
        }

        String actual = valueEl.getText() == null ? "" : valueEl.getText().replace('\u00A0',' ').trim();
        String expected = expectedValue == null ? "" : expectedValue.replace('\u00A0',' ').trim();

        testContext.getLogger().info("Verifying '{}' → Expected: '{}', Actual: '{}'", label, expected, actual);

        if (!actual.equals(expected)) {
            throw new AssertionError("Mismatch for '" + label + "'. Expected: '" + expected + "', Actual: '" + actual + "'");
        }
    }
}
