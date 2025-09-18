package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import com.jllt.utils.webDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class opportunityWdsfPage extends basePage {
    private JavascriptExecutor executor;

    @FindBy(xpath = "//span[text()='Opportunities']/parent::a")
    private WebElement OpportunitiesTab;

    @FindBy(xpath = "//a[@title='New']")
    private WebElement NewOpportunityButton;

    @FindBy(xpath = "//input[@name='Name']")
    private WebElement OpportunityNameTxt;

    @FindBy(xpath = "//label[text()='Account Name']/following-sibling::div//input")
    private WebElement AccountNameLookup;

    @FindBy(xpath = "//input[@name='CloseDate']")
    private WebElement CloseDateDatePicker;

    @FindBy(xpath = "//button[@name='Country__c']")
    private WebElement CountryDropDown;

    @FindBy(xpath = "//input[@name='Opportunity_Division__c'] | //button[@name='Opportunity Division']")
    private WebElement OpportunityDivisionDropDown;

    @FindBy(xpath = "//button[@name='Opportunity_Region__c']")
    private WebElement OpportunityRegionFieldTxt;

    @FindBy(xpath = "//input[@aria-label='Opportunity Currency']")
    private WebElement OpportunityCurrencyDropDown;

    @FindBy(xpath = "//button[@name='Vertical_Business_Line__c']")
    private WebElement ClientValueAndGrowthPrimaryVertical;

    @FindBy(xpath = "//input[@name='Vertical_Business_Unit__c']")
    private WebElement ClientValueAndGrowthSecondaryVertical;

    @FindBy(xpath = "//input[@name='Division_1__c']")
    private WebElement ClientValueAndGrowthTeam;

    @FindBy(xpath = "//button[@name='Save'] | //button[@name='SaveEdit']")
    private WebElement Save;

    @FindBy(xpath = "//span[text()='Business Group']/parent::div/following-sibling::div//lightning-formatted-text")
    private WebElement BusinessGroup;

    @FindBy(xpath = "//span[text()='Sub Business Group']/parent::div/following-sibling::div//lightning-formatted-text")
    private WebElement SubBusinessGroup;

    @FindBy(xpath = "(//span[text()='Opportunity Record Type']/parent::div/..//records-record-type//span)[1]")
    private WebElement RecordType;

    @FindBy(xpath = "//button[@aria-label='Country']")
    private WebElement OpportunityCountry_GWD;

    @FindBy(xpath = "//button[@aria-label='Opportunity Division']")
    private WebElement OpportunityDivision_GWD;

    @FindBy(xpath = "//button[@aria-label='Opportunity Region']")
    private WebElement OpportunityRegion_GWD;;

    @FindBy(xpath = "//button[@aria-label='Opportunity Currency']")
    private WebElement OpportunityCurrency_GWD;

    @FindBy(xpath = "//button[@aria-label='Forecast Category']")
    private WebElement OpportunityForecastCat_GWD;

    @FindBy(xpath = "//button[@aria-label='Stage']")
    private WebElement OpportunityStage;

    @FindBy(xpath = "//button[@aria-label='Vertical']")
    private WebElement OpportunityVertical_GWD;

    @FindBy(xpath = "//button[@aria-label='Primary Vertical']")
    private WebElement ClientValueAndGrowthPrimaryVertical_GWD;

    @FindBy(xpath = "//label[text()='Account Name']/..//input[@placeholder='Search Accounts...']")
    private WebElement AccountName;

    @FindBy(xpath = "//button[@name='Opportunity_Source__c']")
    private WebElement OpportunitySource;

    @FindBy(xpath = "//button[@name='Confidentiality__c']")
    private WebElement Confidentiality;

    @FindBy(xpath = "//button[@title='Edit Opportunity Pursuit Type']")
    private WebElement opportunityPursuitTypeEditButton;

    @FindBy(xpath = "//button[@aria-label='Opportunity Pursuit Type']")
    private WebElement opportunityPursuitTypeDropdown;

    @FindBy(xpath = "//span[text()='Opportunity Pursuit Type']/parent::div/following-sibling::div//span")
    private WebElement opportunityPursuitTypeValue;

    @FindBy(xpath = "//span[text()='Opportunity Name']/parent::div/..//lightning-formatted-text")
    private WebElement opportunityNameOnLandingPage;

    @FindBy(xpath = "(//span[text()='Opportunity Owner']/parent::div/..//a//span)[3]")
    private WebElement opportunityOwnerOnLandingPage;

    @FindBy(xpath = "//span[text()='Opportunity Region']/parent::div/..//lightning-formatted-text")
    private WebElement opportunityRegionOnLandingPage;

    @FindBy(xpath = "//span[text()='Opportunity Division']/parent::div/..//lightning-formatted-text")
    private WebElement opportunityDivisionOnLandingPage;

    @FindBy(xpath = "(//span[text()='Account Name']/parent::div/..//a//span)[3]")
    private WebElement accountNameOnOpportunityLandingPage;

    @FindBy(xpath = "(//span[text()='Lead Source']/parent::div/..//lightning-formatted-text)[1]")
    private WebElement leadSourceOnOpportunityLandingPage;

    @FindBy(xpath = "(//span[text()='Lead']/parent::div/..//a//span)[3]")
    private WebElement leadOnOpportunityLandingPage;

    @FindBy(xpath = "(//span[text()='Pain']/parent::div/..//lightning-formatted-text)")
    private WebElement painOnOpportunityLandingPage;

    @FindBy(xpath = "(//span[text()='Budget']/parent::div/..//lightning-formatted-text)")
    private WebElement budgetOnOpportunityLandingPage;

    @FindBy(xpath = "(//span[text()='Decision Process']/parent::div/..//lightning-formatted-text)")
    private WebElement decisionProcessOnOpportunityLandingPage;

    @FindBy(xpath = "//button[@name='Regional_Division__c']")
    private WebElement regionalDivision;

    @FindBy(xpath = "//input[@name='JLL_Account_Type__c']")
    private WebElement jLLAccountType;

    @FindBy(xpath = "//input[@name='Opportunity_Pursuit_Type__c']")
    private WebElement opportunityPursuitType;


    public opportunityWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public void navigateToOpportunitiesTab() throws InterruptedException {
        testContext.getLogger().info("Navigating to Opportunities tab");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(2000);
        //wait.until(ExpectedConditions.elementToBeClickable(OpportunitiesTab)).click();
        wait.until(ExpectedConditions.elementToBeClickable(OpportunitiesTab));
        executor.executeScript("arguments[0].click();", OpportunitiesTab);
        Thread.sleep(2000);
        testContext.getLogger().info("Clicked on Opportunities Tab");
        Thread.sleep(3000);
    }

    public void clickNewOpportunityButton() throws InterruptedException {
        testContext.getLogger().info("Clicking New Opportunity button");

        //wait.until(ExpectedConditions.elementToBeClickable(NewOpportunityButton)).click();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(NewOpportunityButton));
        executor.executeScript("arguments[0].click();", NewOpportunityButton);
        testContext.getLogger().info("Clicked on New Opportunity button");
        Thread.sleep(3000);
    }

    public void CreateNewOpportunityForm(Map<String, String> opportunityDetails) throws InterruptedException {
        testContext.getLogger().info("Filling New Account Request Form");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(1500);

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(7);

        String opportunityName = opportunityDetails.get("Name")+ "_" + randomSuffix;
        testContext.getLogger().info("Opportunity: {}", opportunityName);

        //String companyName = accountDetails.get("Company Name") + "_" + randomSuffix;
        testContext.setContextData("Opportunity Name", opportunityName);
        testContext.getLogger().info("Set Opportunity Name: {}", opportunityName);

        Thread.sleep(1000);
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        //testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityNameTxt)).click();
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityNameTxt));
        executor.executeScript("arguments[0].click();", OpportunityNameTxt);
        OpportunityNameTxt.clear();
        OpportunityNameTxt.sendKeys(opportunityName);

        selectCountryField(opportunityDetails.get("Country"),"Country");
        Thread.sleep(2000);

        //SelectAccountName();
        //Select Account
        String accountName = String.valueOf(testContext.getContextData("accountName"));
        logger.info("Account Name...........................: {}", accountName);
        if (accountName != null && !accountName.isEmpty()) {
            selectAccount(accountName);
            logger.info("Selected Company: {}", accountName);
        } else {
            logger.warn("No account name found in context. Using default from opportunityDetails.");
            selectAccount(opportunityDetails.get("Account Name"));
        }

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityRegionFieldTxt)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Region"),"Opportunity Region");
        testContext.getLogger().info("Selected Region: {}", opportunityDetails.get("Opportunity Region"));

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityCurrencyDropDown)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Currency"),"Opportunity Currency");
        testContext.getLogger().info("Selected Currency: {}", opportunityDetails.get("Opportunity Currency"));

        if (testContext.getCommonUtils().isElementDisplayed(OpportunityDivisionDropDown)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityDivisionDropDown)).click();
            SelectDropDown(opportunityDetails.get("Opportunity Division"),"Opportunity Division");
            testContext.getLogger().info("Selected Division: {}", opportunityDetails.get("Opportunity Division"));
        }

        /*selectCountryField(opportunityDetails.get("Country"),"Country");
        Thread.sleep(2000);*/

        if (testContext.getCommonUtils().isElementDisplayed(ClientValueAndGrowthPrimaryVertical)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthPrimaryVertical)).click();
            SelectDropDown(opportunityDetails.get("Primary Vertical"),"Primary Vertical");
            testContext.getLogger().info("Primary Vertical: {}", opportunityDetails.get("Primary Vertical"));
        }

        /*if (testContext.getCommonUtils().isElementDisplayed(ClientValueAndGrowthSecondaryVertical)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthSecondaryVertical)).click();
            SelectDropDown(opportunityDetails.get("Client Value & Growth Secondary Vertical"),"Client Value & Growth Secondary Vertical");
            testContext.getLogger().info("Client Value & Growth Secondary Vertical: {}", opportunityDetails.get("Client Value & Growth Secondary Vertical"));
        }*/

        if (testContext.getCommonUtils().isElementDisplayed(ClientValueAndGrowthTeam)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthTeam)).click();
            SelectDropDown(opportunityDetails.get("Client Value & Growth Team"),"Client Value & Growth Team");
            testContext.getLogger().info("Client Value & Growth Team: {}", opportunityDetails.get("Client Value & Growth Team"));
        }

        if (testContext.getCommonUtils().isElementDisplayed(OpportunitySource)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunitySource)).click();
            SelectDropDown(opportunityDetails.get("Opportunity Source"),"Opportunity Source");
            testContext.getLogger().info("Opportunity Source: {}", opportunityDetails.get("Opportunity Source"));
        }

        if (testContext.getCommonUtils().isElementDisplayed(Confidentiality)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(Confidentiality)).click();
            SelectDropDown(opportunityDetails.get("Confidentiality"),"Confidentiality");
            testContext.getLogger().info("Confidentiality: {}", opportunityDetails.get("Confidentiality"));
        }

        if (testContext.getCommonUtils().isElementDisplayed(regionalDivision)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(regionalDivision)).click();
            SelectDropDown(opportunityDetails.get("Regional Division"),"Regional Division");
            testContext.getLogger().info("Regional Division: {}", opportunityDetails.get("Regional Division"));
        }

        if (testContext.getCommonUtils().isElementDisplayed(jLLAccountType)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(jLLAccountType)).click();
            SelectDropDown(opportunityDetails.get("JLL Account Type"),"JLL Account Type");
            testContext.getLogger().info("JLL Account Type: {}", opportunityDetails.get("JLL Account Type"));
        }

        if (testContext.getCommonUtils().isElementDisplayed(opportunityPursuitType)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(opportunityPursuitType)).click();
            SelectDropDown(opportunityDetails.get("Opportunity Pursuit Type"),"Opportunity Pursuit Type");
            testContext.getLogger().info("Opportunity Pursuit Type: {}", opportunityDetails.get("Opportunity Pursuit Type"));
        }

        // Setting Close Date to today + 2 days
        String closeDate = testContext.getCommonUtils().getFutureDateFormatted(2);
        testContext.getCommonUtils().jsScrollToElement(CloseDateDatePicker);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(CloseDateDatePicker)).click();
        CloseDateDatePicker.clear();
        CloseDateDatePicker.sendKeys(closeDate);
        testContext.getLogger().info("Selected Close Date: {}", closeDate);

        /*String closeDate = testContext.getCommonUtils().getFutureDateFormatted(2);
        setCloseDate(CloseDateDatePicker, closeDate);
        testContext.getLogger().info("Selected Close Date: {}", closeDate);*/

        Thread.sleep(1000);
    }

    private void setCloseDate(WebElement input, String date) throws InterruptedException {
        // Bring input to a safe click position
        try { testContext.getCommonUtils().scrollIntoViewCenter(input); } catch (Exception ignored) {}
        wait.until(ExpectedConditions.visibilityOf(input));

        // Try a safe click first; fall back to JS click if intercepted
        try {
            testContext.getCommonUtils().clickWhenReadyCentered(input);
        } catch (Exception e) {
            try { testContext.getCommonUtils().jsClickToElement(input); } catch (Exception ignored) {}
        }

        // Clear and set value using sendKeys; fall back to JS assignment
        try {
            input.clear();
            input.sendKeys(date);
            Thread.sleep(800);
            input.clear();
            input.sendKeys(date);
            try { input.sendKeys(Keys.TAB); } catch (Exception ignored) {}
        } catch (Exception e) {
            executor.executeScript(
                    "arguments[0].value=arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('blur', {bubbles:true}));",
                    input, date
            );
        }

        Thread.sleep(300);
    }

    public void CreateNewOpportunityForm_GWD(Map<String, String> opportunityDetails) throws InterruptedException {
        testContext.getLogger().info("Filling New Opportunity Request Form for Generic Work Dynamics");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);

        String opportunityName = opportunityDetails.get("Name")+ "_" + randomSuffix;
        testContext.getLogger().info("Generic WorkDynamics opportunity: {}", opportunityName);

        //String companyName = accountDetails.get("Company Name") + "_" + randomSuffix;
        testContext.setContextData("Opportunity Name", opportunityName);
        testContext.getLogger().info("Set Opportunity Name for Generic WorkDynamics: {}", opportunityName);

        Thread.sleep(1000);
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityNameTxt));
        executor.executeScript("arguments[0].click();", OpportunityNameTxt);
        OpportunityNameTxt.sendKeys(opportunityName);

        //SelectAccountName();
        //Select Account
        String accountName = String.valueOf(testContext.getContextData("accountName"));
        logger.info("Account Name: {}", accountName);
        if (accountName != null && !accountName.isEmpty()) {
            selectAccount(accountName);
            logger.info("Selected Account: {}", accountName);
        } else {
            logger.warn("No account name found in context Using default from contactDetails.");
            selectAccount(opportunityDetails.get("Account Name"));
        }

        testContext.getCommonUtils().jsScrollToElement(OpportunityRegion_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityRegion_GWD)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Region"),"Opportunity Region");
        testContext.getLogger().info("Selected Generic WorkDynamics opportunity Region: {}", opportunityDetails.get("Opportunity Region"));

        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", OpportunityCurrency_GWD);
        //testContext.getCommonUtils().jsScrollToElement(OpportunityCurrency_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityCurrency_GWD)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Currency"),"Opportunity Currency");
        testContext.getLogger().info("Selected Generic WorkDynamics opportunity Currency: {}", opportunityDetails.get("Opportunity Currency"));

        testContext.getCommonUtils().jsScrollToElement(OpportunityDivision_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityDivision_GWD)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Division"),"Opportunity Division");
        testContext.getLogger().info("Selected Generic WorkDynamics opportunity Division: {}", opportunityDetails.get("Opportunity Division"));

        selectCountryField(opportunityDetails.get("Country"),"Country");
        Thread.sleep(2000);

       /* testContext.getCommonUtils().jsScrollToElement(OpportunityCountry_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityCountry_GWD)).click();
        SelectDropDown(opportunityDetails.get("Country"),"Country");
        testContext.getLogger().info("Generic WorkDynamics opportunity Country: {}", opportunityDetails.get("Country"));
*/
        testContext.getCommonUtils().jsScrollToElement(ClientValueAndGrowthPrimaryVertical_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthPrimaryVertical_GWD)).click();
        SelectDropDown(opportunityDetails.get("Client Value & Growth Primary Vertical"),"Client Value & Growth Primary Vertical");
        testContext.getLogger().info("Client Value & Growth Primary Vertical for Generic WorkDynamics: {}", opportunityDetails.get("Client Value & Growth Primary Vertical"));

        // Setting Close Date to today + 2 days
        String closeDate = testContext.getCommonUtils().getFutureDateFormatted(2);
        testContext.getCommonUtils().jsScrollToElement(CloseDateDatePicker);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(CloseDateDatePicker)).click();
        CloseDateDatePicker.clear();
        CloseDateDatePicker.sendKeys(closeDate);
        testContext.getLogger().info("Selected Opportunity Close Date: {}", closeDate);

        testContext.getCommonUtils().jsScrollToElement(OpportunityStage);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityStage)).click();
        SelectDropDown(opportunityDetails.get("Stage"),"Stage");
        testContext.getLogger().info("Generic WorkDynamics opportunity Stage: {}", opportunityDetails.get("Stage"));

        testContext.getCommonUtils().jsScrollToElement(OpportunityForecastCat_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityForecastCat_GWD)).click();
        SelectDropDown(opportunityDetails.get("Forecast Category"),"Forecast Category");
        testContext.getLogger().info("Generic WorkDynamics opportunity Stage: {}", opportunityDetails.get("Forecast Category"));

        Thread.sleep(500);
    }

    private void selectCountryField(String country,String label) throws InterruptedException {
        Thread.sleep(1500);
        testContext.getLogger().info("Selecting {} for Opportunity Creation", country);

        try { executor.executeScript("arguments[0].scrollIntoView({block:'center'});", CountryDropDown);
        }
        catch (Exception ignored) {}

        // Open dropdown using your locator
        try {
            wait.until(ExpectedConditions.visibilityOf(CountryDropDown));
            Thread.sleep(1000);
            executor.executeScript("arguments[0].click();", CountryDropDown);
        } catch (Exception e) {
            // Fallback to standard click if JS click throws
            wait.until(ExpectedConditions.elementToBeClickable(CountryDropDown)).click();
        }
        Thread.sleep(700);

        // Select option using your locator
        String optionXPath = "//label[text()='Country']//following-sibling::div//lightning-base-combobox-item//span[@title='" + country + "'] | " +
                "//button[@aria-label='Country']/parent::div/..//lightning-base-combobox-item//span[text()='" + country + "']";
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath)));
        Thread.sleep(1000);
        executor.executeScript("arguments[0].click();", option);
        Thread.sleep(700);
    }

    public void SelectDropDown(String Val, String label) throws InterruptedException {
        Thread.sleep(1000);
        WebElement Option = driver.findElement(By.xpath("//label[text()='" + label + "']/../following-sibling::div//lightning-base-combobox-item//span[contains(@title,'" + Val + "')]"));
        testContext.getCommonUtils().jsScrollToElement(Option);
        Thread.sleep(1000);
        executor.executeScript("arguments[0].click();", Option);
        Thread.sleep(2000);
    }

    public void ClickOnSaveButton() throws InterruptedException {
        Thread.sleep(2000);
        testContext.getCommonUtils().jsScrollToElement(Save);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(Save)).click();
    }

    public void verifyOpportunityLandingPageTitle() throws InterruptedException {
        Thread.sleep(2000);
        String expectedAccountName = testContext.getContextData("Opportunity Name").toString();
        String expectedTitle = expectedAccountName + " | Opportunity | Salesforce";

        wait.until(ExpectedConditions.titleContains(expectedAccountName));
        String actualTitle = driver.getTitle();

        testContext.getLogger().info("Expected Page Title: {}", expectedTitle);
        testContext.getLogger().info("Actual Page Title: {}", actualTitle);

        assert actualTitle != null;
        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Opportunity landing page title mismatch. Expected: " + expectedTitle + ", but got: " + actualTitle);
        }
        testContext.getLogger().info("Opportunity landing page title verified successfully.");
    }

    public void verifyBusinessGroupValues(Map<String, String> opportunityBusinessGroup) throws InterruptedException {
        testContext.getLogger().info("Verifying Business Group and Sub Business Group values:");

        String expectedBusinessGroup = opportunityBusinessGroup.get("Business Group");
        String expectedSubBusinessGroup = opportunityBusinessGroup.get("Sub Business Group");

        testContext.getLogger().info("Expected Business Group: {}", expectedBusinessGroup);
        testContext.getLogger().info("Expected Sub Business Group: {}", expectedSubBusinessGroup);

        // Ensure page is ready
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        Thread.sleep(1000);

        // Try to bring the fields into view without introducing new locators
        try { executor.executeScript("window.scrollTo(0,0)"); } catch (Exception ignored) {}
        int attempts = 0;
        while (attempts < 4) {
            try {
                testContext.getCommonUtils().scrollIntoViewIfNeeded(BusinessGroup);
                testContext.getCommonUtils().scrollIntoViewIfNeeded(SubBusinessGroup);
                break;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                testContext.getCommonUtils().preScrollToBottom();
                attempts++;
            }
        }

        // Wait for visibility instead of clickable; these are read-only fields
        wait.until(ExpectedConditions.visibilityOf(BusinessGroup));
        wait.until(ExpectedConditions.visibilityOf(SubBusinessGroup));

        String actualBusinessGroup = BusinessGroup.getText() == null ? "" : BusinessGroup.getText().replace('\u00A0',' ').trim();
        String actualSubBusinessGroup = SubBusinessGroup.getText() == null ? "" : SubBusinessGroup.getText().replace('\u00A0',' ').trim();

        testContext.getLogger().info("Actual Business Group: {}", actualBusinessGroup);
        testContext.getLogger().info("Actual Sub Business Group: {}", actualSubBusinessGroup);

        if (!actualBusinessGroup.equals(expectedBusinessGroup)) {
            throw new AssertionError("Business Group value mismatch. Expected: " + expectedBusinessGroup + ", but got: " + actualBusinessGroup);
        }
        if (!actualSubBusinessGroup.equals(expectedSubBusinessGroup)) {
            throw new AssertionError("Sub Business Group value mismatch. Expected: " + expectedSubBusinessGroup + ", but got: " + actualSubBusinessGroup);
        }
        testContext.getLogger().info("Business Group and Sub Business Group values verified successfully");
    }

    public void verifyOpportunityRecordType(String expectedOpportunityRecordType) throws InterruptedException {
        testContext.getLogger().info("Verifying Opportunity Record Type");
        testContext.getLogger().info("Expected Opportunity Record Type: {}", expectedOpportunityRecordType);

        Thread.sleep(1000);
        testContext.getCommonUtils().scrollToEndOfPageUsingKeys();
        testContext.getCommonUtils().jsScrollToElement(RecordType);
        testContext.getWait().until(ExpectedConditions.visibilityOf(RecordType));
        String actualOpportunityRecordType = RecordType.getText();

        testContext.getLogger().info("Actual Opportunity Record Type: {}", actualOpportunityRecordType);

        // Verify record type matches expected value
        if (!actualOpportunityRecordType.equals(expectedOpportunityRecordType)) {
            throw new AssertionError("Opportunity Record Type mismatch. Expected: " + expectedOpportunityRecordType + ", but got: " + actualOpportunityRecordType);
        }
        testContext.getLogger().info("Opportunity Record Type verified successfully");
    }

    public void selectAccount(String val) throws InterruptedException {
        logger.info("Attempting to select company: {}", val);
        executor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", AccountName);
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(AccountName));
        wait.until(ExpectedConditions.elementToBeClickable(AccountName)).click();
        Thread.sleep(1000);
        AccountName.sendKeys(val);
        Thread.sleep(200);
        WebElement Option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//lightning-base-combobox-item//parent::div//ul//lightning-base-combobox-formatted-text[@title='" + val + "']/..")));
        executor.executeScript("arguments[0].click();", Option);
        logger.info("Account selected successfully: {}", val);
    }

    public void clickAndVerifyOpportunityPursuitType(String expectedValue) throws InterruptedException {
        testContext.getLogger().info("Clicking on Opportunity Pursuit Type and verifying picklist value: {}", expectedValue);

        // Wait for page to load completely
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        Thread.sleep(2000);

        // Scroll to Opportunity Pursuit Type field if needed
        WebElement pursuitTypeField = driver.findElement(By.xpath("//span[text()='Opportunity Pursuit Type']"));
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", pursuitTypeField);
        Thread.sleep(1000);

        // Click edit button for Opportunity Pursuit Type
        wait.until(ExpectedConditions.elementToBeClickable(opportunityPursuitTypeEditButton));
        executor.executeScript("arguments[0].click();", opportunityPursuitTypeEditButton);
        Thread.sleep(1000);

        // Click to open the dropdown
        wait.until(ExpectedConditions.elementToBeClickable(opportunityPursuitTypeDropdown));
        executor.executeScript("arguments[0].click();", opportunityPursuitTypeDropdown);
        Thread.sleep(1000);

        // Verify if the expected value is present in the dropdown
        List<WebElement> options = driver.findElements(
                By.xpath("//button[@aria-label='Opportunity Pursuit Type']/parent::div/following-sibling::div/lightning-base-combobox-item"));

        boolean valueFound = false;
        for (WebElement option : options) {
            String optionText = option.getText().trim();
            testContext.getLogger().info("Found option: {}", optionText);
            if (optionText.equals(expectedValue)) {
                valueFound = true;
                testContext.getLogger().info("Found expected value: {}", expectedValue);
                break;
            }
        }

        if (!valueFound) {
            testContext.getLogger().error("Expected value not found in dropdown: {}", expectedValue);
            throw new AssertionError("Expected value '" + expectedValue + "' not found in Opportunity Pursuit Type dropdown");
        }

        testContext.getLogger().info("Successfully verified '{}' is in the picklist", expectedValue);
    }

    public void selectOpportunityPursuitTypeAndSave(String valueToSelect) throws InterruptedException {
        testContext.getLogger().info("Selecting {} from Opportunity Pursuit Type dropdown and saving", valueToSelect);

        // Ensure edit mode
        if (!testContext.getCommonUtils().isElementDisplayed(driver.findElement(By.xpath("//button[@aria-label='Opportunity Pursuit Type']")))) {
            wait.until(ExpectedConditions.elementToBeClickable(opportunityPursuitTypeEditButton));
            testContext.getCommonUtils().jsClickToElement(opportunityPursuitTypeEditButton);
            Thread.sleep(2000);
        }

        // Select the specified value
        String optionXPath = "//button[@aria-label='Opportunity Pursuit Type']/parent::div/following-sibling::div/lightning-base-combobox-item//span[text()='" + valueToSelect + "']";
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath)));
        Thread.sleep(1000);
        executor.executeScript("arguments[0].click();", option);
        Thread.sleep(500);

        // Save
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@name='SaveEdit']")));
        testContext.getCommonUtils().jsClickToElement(saveButton);
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        testContext.getLogger().info("Selected {} and clicked save button", valueToSelect);
    }

    public void verifyOpportunityField(String fieldName, String expectedValue) throws InterruptedException {
        String label = fieldName == null ? "" : fieldName.trim();

        if (expectedValue == null || expectedValue.isBlank()) {
            if (label.equalsIgnoreCase("Opportunity Name")) {
                Object v = testContext.getContextData("Opportunity Name");
                if (v == null || String.valueOf(v).isBlank()) v = testContext.getContextData("OpportunityName");
                expectedValue = v == null ? "" : v.toString().trim();
            } else if (label.equalsIgnoreCase("Lead")) {
                Object v = testContext.getContextData("LeadName");
                expectedValue = v == null ? "" : v.toString().trim();
            } else if (label.equalsIgnoreCase("Account Name") || label.equalsIgnoreCase("Account")) {
                Object v = testContext.getContextData("accountName");
                expectedValue = v == null ? "" : v.toString().trim();
            }
        }

        WebElement valueEl;
        switch (label) {
            case "Opportunity Name":
                valueEl = opportunityNameOnLandingPage;
                break;
            case "Opportunity Owner":
                valueEl = opportunityOwnerOnLandingPage;
                break;
            case "Opportunity Region":
                valueEl = opportunityRegionOnLandingPage;
                break;
            case "Opportunity Division":
                valueEl = opportunityDivisionOnLandingPage;
                break;
            case "Account Name":
            case "Account":
                valueEl = accountNameOnOpportunityLandingPage;
                break;
            case "Lead Source":
                valueEl = leadSourceOnOpportunityLandingPage; // (//span[text()='Lead Source']/parent::div/..//lightning-formatted-text)[1]
                break;
            case "Lead":
                valueEl = leadOnOpportunityLandingPage;
                break;
            case "Pain":
                valueEl = painOnOpportunityLandingPage;
                break;
            case "Budget":
                valueEl = budgetOnOpportunityLandingPage;
                break;
            case "Decision Process":
                valueEl = decisionProcessOnOpportunityLandingPage;
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + label);
        }

        if (label.equalsIgnoreCase("Lead Source") ||
                label.equalsIgnoreCase("Lead") ||
                label.equalsIgnoreCase("Pain") ||
                label.equalsIgnoreCase("Budget") ||
                label.equalsIgnoreCase("Decision Process")) {
            testContext.getCommonUtils().preScrollToBottom();
        }

        try {
            testContext.getCommonUtils().jsScrollToElement(valueEl);
        } catch (Exception ignored) {
            try { executor.executeScript("arguments[0].scrollIntoView({block:'center'});", valueEl); } catch (Exception e) {}
        }
        wait.until(ExpectedConditions.visibilityOf(valueEl));

        String actual = valueEl.getText() == null ? "" : valueEl.getText().replace('\u00A0',' ').trim();
        String expected = expectedValue == null ? "" : expectedValue.replace('\u00A0',' ').trim();

        testContext.getLogger().info("Verifying '{}' → Expected: '{}', Actual: '{}'", label, expected, actual);
        if (!actual.equals(expected)) {
            throw new AssertionError("Mismatch for '" + label + "'. Expected: '" + expected + "', Actual: '" + actual + "'");
        }
    }
}
