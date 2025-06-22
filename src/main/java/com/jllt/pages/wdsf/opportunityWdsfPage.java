package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
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

    @FindBy(xpath = "//p[text()='Opportunity Record Type']/parent::div//span")
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

    @FindBy(xpath = "//button[@aria-label='Client Value & Growth Primary Vertical']")
    private WebElement ClientValueAndGrowthPrimaryVertical_GWD;

    @FindBy(xpath = "//label[text()='Account Name']/..//input[@placeholder='Search Accounts...']")
    private WebElement AccountName;

    @FindBy(xpath = "//button[@name='Opportunity_Source__c']")
    private WebElement OpportunitySource;

    @FindBy(xpath = "//button[@name='Confidentiality__c']")
    private WebElement Confidentiality;

    public opportunityWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    public void navigateToOpportunitiesTab() throws InterruptedException {
        testContext.getLogger().info("Navigating to Opportunities tab");
        //testContext.getDriver().switchTo().defaultContent();
        testContext.getCommonUtils().switchToUserIframeWithRetry("//iframe[contains(@title, 'dashboard')]", 5, 3);
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

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);

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

        /*testContext.getWait().until(ExpectedConditions.elementToBeClickable(CountryDropDown)).click();
        SelectDropDown(opportunityDetails.get("Country"),"Country");
        testContext.getLogger().info("Country: {}", opportunityDetails.get("Country"));*/

        selectcountryField(opportunityDetails.get("Country"),"Country");
        Thread.sleep(20000);

        if (testContext.getCommonUtils().isElementDisplayed(ClientValueAndGrowthPrimaryVertical)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthPrimaryVertical)).click();
            SelectDropDown(opportunityDetails.get("Client Value & Growth Primary Vertical"),"Client Value & Growth Primary Vertical");
            testContext.getLogger().info("Client Value & Growth Primary Vertical: {}", opportunityDetails.get("Client Value & Growth Primary Vertical"));
        }

        if (testContext.getCommonUtils().isElementDisplayed(ClientValueAndGrowthSecondaryVertical)){
            testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthSecondaryVertical)).click();
            SelectDropDown(opportunityDetails.get("Client Value & Growth Secondary Vertical"),"Client Value & Growth Secondary Vertical");
            testContext.getLogger().info("Client Value & Growth Secondary Vertical: {}", opportunityDetails.get("Client Value & Growth Secondary Vertical"));
        }

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

        // Setting Close Date to today + 2 days
        String closeDate = testContext.getCommonUtils().getFutureDateFormatted(2);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(CloseDateDatePicker)).click();
        CloseDateDatePicker.clear();
        CloseDateDatePicker.sendKeys(closeDate);
        testContext.getLogger().info("Selected Close Date: {}", closeDate);

        Thread.sleep(500);
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
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityNameTxt)).click();
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

        testContext.getCommonUtils().jsScrollDown(OpportunityRegion_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityRegion_GWD)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Region"),"Opportunity Region");
        testContext.getLogger().info("Selected Generic WorkDynamics opportunity Region: {}", opportunityDetails.get("Opportunity Region"));

        testContext.getCommonUtils().jsScrollDown(OpportunityCurrency_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityCurrency_GWD)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Currency"),"Opportunity Currency");
        testContext.getLogger().info("Selected Generic WorkDynamics opportunity Currency: {}", opportunityDetails.get("Opportunity Currency"));

        testContext.getCommonUtils().jsScrollDown(OpportunityDivision_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityDivision_GWD)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Division"),"Opportunity Division");
        testContext.getLogger().info("Selected Generic WorkDynamics opportunity Division: {}", opportunityDetails.get("Opportunity Division"));

        testContext.getCommonUtils().jsScrollDown(OpportunityCountry_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityCountry_GWD)).click();
        SelectDropDown(opportunityDetails.get("Country"),"Country");
        testContext.getLogger().info("Generic WorkDynamics opportunity Country: {}", opportunityDetails.get("Country"));

        testContext.getCommonUtils().jsScrollDown(ClientValueAndGrowthPrimaryVertical_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthPrimaryVertical_GWD)).click();
        SelectDropDown(opportunityDetails.get("Client Value & Growth Primary Vertical"),"Client Value & Growth Primary Vertical");
        testContext.getLogger().info("Client Value & Growth Primary Vertical for Generic WorkDynamics: {}", opportunityDetails.get("Client Value & Growth Primary Vertical"));

        // Setting Close Date to today + 2 days
        String closeDate = testContext.getCommonUtils().getFutureDateFormatted(2);
        testContext.getCommonUtils().jsScrollDown(CloseDateDatePicker);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(CloseDateDatePicker)).click();
        CloseDateDatePicker.clear();
        CloseDateDatePicker.sendKeys(closeDate);
        testContext.getLogger().info("Selected Opportunity Close Date: {}", closeDate);

        testContext.getCommonUtils().jsScrollDown(OpportunityStage);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityStage)).click();
        SelectDropDown(opportunityDetails.get("Stage"),"Stage");
        testContext.getLogger().info("Generic WorkDynamics opportunity Stage: {}", opportunityDetails.get("Stage"));

        testContext.getCommonUtils().jsScrollDown(OpportunityForecastCat_GWD);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityForecastCat_GWD)).click();
        SelectDropDown(opportunityDetails.get("Forecast Category"),"Forecast Category");
        testContext.getLogger().info("Generic WorkDynamics opportunity Stage: {}", opportunityDetails.get("Forecast Category"));

        Thread.sleep(500);
    }

    /*public void SelectAccountName() throws InterruptedException {
        testContext.getCommonUtils().jsScrollDown(AccountNameLookup);
        Thread.sleep(1000);
        testContext.getOpportunityWdsfPage().AccountNameLookup.click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//ul[@aria-label='Recent Accounts']//li[2]/lightning-base-combobox-item/span[2]/span[1]")).click();
    }*/

    private void fillFormField(WebElement field, String value, String fieldName) {
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        testContext.getLogger().info("Entered {}: {}", fieldName, value);
    }

    private void selectcountryField(String country,String label){
        try {
            testContext.getLogger().info("Setting Country field with JavaScript: {}", country);
            executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", CountryDropDown);
            Thread.sleep(1000);
            executor.executeScript("arguments[0].click();", CountryDropDown);
            Thread.sleep(2000);
            WebElement countryOption = driver.findElement(By.xpath("//label[text()='" + label + "']//following-sibling::div//lightning-base-combobox-item//span[@title='" + country + "']"));
            executor.executeScript("arguments[0].click();", countryOption);
            testContext.getLogger().info("Selected Country: {}", country);
            Thread.sleep(3000); // Give UI time to settle
        } catch (Exception e) {
            testContext.getLogger().error("Error setting Country: {}", e.getMessage());
        }
    }

    public void SelectDropDown(String Val, String label) throws InterruptedException {
        Thread.sleep(1000);
        WebElement Option = driver.findElement(By.xpath("//label[text()='" + label + "']//following-sibling::div//lightning-base-combobox-item//span[contains(@title,'" + Val + "')]"));
        testContext.getCommonUtils().jsScrollDown(Option);
        Thread.sleep(1000);
        executor.executeScript("arguments[0].click();", Option);
        /*Actions actions = new Actions(driver);
        actions.moveToElement(Option).click().build().perform();*/
        Thread.sleep(2000);
    }

    public void ClickOnSaveButton() throws InterruptedException {
        Thread.sleep(2000);
        testContext.getCommonUtils().jsScrollDown(Save);
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

    public void verifyBusinessGroupValues(Map<String, String> opportunityBusinessGroup) {
        testContext.getLogger().info("Verifying Business Group and Sub Business Group values");

        String expectedBusinessGroup = opportunityBusinessGroup.get("Business Group");
        String expectedSubBusinessGroup = opportunityBusinessGroup.get("Sub Business Group");

        testContext.getLogger().info("Expected Business Group: {}", expectedBusinessGroup);
        testContext.getLogger().info("Expected Sub Business Group: {}", expectedSubBusinessGroup);

        // Wait for elements to be visible
        wait.until(ExpectedConditions.elementToBeClickable(BusinessGroup));
        wait.until(ExpectedConditions.elementToBeClickable(SubBusinessGroup));

        String actualBusinessGroup = BusinessGroup.getText();
        String actualSubBusinessGroup = SubBusinessGroup.getText();

        testContext.getLogger().info("Actual Business Group: {}", actualBusinessGroup);
        testContext.getLogger().info("Actual Sub Business Group: {}", actualSubBusinessGroup);

        // Verify values
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
        wait.until(ExpectedConditions.visibilityOf(AccountName));
        wait.until(ExpectedConditions.elementToBeClickable(AccountName)).click();
        Thread.sleep(1000);
        AccountName.sendKeys(val);
        Thread.sleep(1000);
        WebElement Option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//lightning-base-combobox-item//parent::div//ul//lightning-base-combobox-formatted-text[@title='" + val + "']/..")));
        executor.executeScript("arguments[0].click();", Option);
        logger.info("Account selected successfully: {}", val);
    }
}
