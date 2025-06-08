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

    @FindBy(xpath = "//one-app-nav-bar-item-root/a/span[text()='Opportunities']/parent::*/parent::*")
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

    @FindBy(xpath = "//input[@name='Opportunity_Division__c']")
    private WebElement OpportunityDivisionDropDown;

    @FindBy(xpath = "//button[@name='Opportunity_Region__c']")
    private WebElement OpportunityRegionFieldTxt;

    @FindBy(xpath = "//input[@aria-label='Opportunity Currency']")
    private WebElement OpportunityCurrencyDropDown;

    @FindBy(xpath = "//button[@name='Vertical_Business_Line__c']")
    private WebElement ClientValueAndGrowthPrimaryVertical;

    @FindBy(xpath = "//button[@name='Save']")
    private WebElement Save;


    public opportunityWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    public void navigateToOpportunitiesTab() throws InterruptedException {
        testContext.getLogger().info("Navigating to Opportunities tab");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(OpportunitiesTab)).click();
        testContext.getLogger().info("Clicked on Opportunities Tab");
        Thread.sleep(3000);
    }

    public void clickNewOpportunityButton() throws InterruptedException {
        testContext.getLogger().info("Clicking New Opportunity button");

        wait.until(ExpectedConditions.elementToBeClickable(NewOpportunityButton)).click();
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
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityNameTxt)).click();
        OpportunityNameTxt.sendKeys(opportunityName);

        SelectAccountName();

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityRegionFieldTxt)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Region"));
        testContext.getLogger().info("Selected Region: {}", opportunityDetails.get("Opportunity Region"));

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityCurrencyDropDown)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Currency"));
        testContext.getLogger().info("Selected Currency: {}", opportunityDetails.get("Opportunity Currency"));

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(OpportunityDivisionDropDown)).click();
        SelectDropDown(opportunityDetails.get("Opportunity Division"));
        testContext.getLogger().info("Selected Division: {}", opportunityDetails.get("Opportunity Division"));

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(CountryDropDown)).click();
        SelectDropDown(opportunityDetails.get("Country"));
        testContext.getLogger().info("Country: {}", opportunityDetails.get("Country"));

        testContext.getWait().until(ExpectedConditions.elementToBeClickable(ClientValueAndGrowthPrimaryVertical)).click();
        SelectDropDown(opportunityDetails.get("Client Value & Growth Primary Vertical"));
        testContext.getLogger().info("Client Value & Growth Primary Vertical: {}", opportunityDetails.get("Client Value & Growth Primary Vertical"));

        // Setting Close Date to today + 2 days
        String closeDate = testContext.getCommonUtils().getFutureDateFormatted(2);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(CloseDateDatePicker)).click();
        CloseDateDatePicker.clear();
        CloseDateDatePicker.sendKeys(closeDate);
        testContext.getLogger().info("Selected Close Date: {}", closeDate);

        Thread.sleep(20000);
    }

    public void SelectAccountName() throws InterruptedException {
        Thread.sleep(1000);
        testContext.getOpportunityWdsfPage().AccountNameLookup.click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//ul[@aria-label='Recent Accounts']//li[2]/lightning-base-combobox-item/span[2]/span[1]")).click();
    }

    private void fillFormField(WebElement field, String value, String fieldName) {
        wait.until(ExpectedConditions.visibilityOf(field)).sendKeys(value);
        testContext.getLogger().info("Entered {}: {}", fieldName, value);
    }

    public void SelectDropDown(String Val) throws InterruptedException {
        Thread.sleep(1000);
        WebElement Option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + Val + "']"));
        testContext.getCommonUtils().jsScrollDown(Option);
        Thread.sleep(10000);
        executor.executeScript("arguments[0].click();", Option);
    }

    public void ClickOnSaveButton() throws InterruptedException {
        Thread.sleep(1000);
        testContext.getWait().until(ExpectedConditions.elementToBeClickable(Save)).click();
    }
}
