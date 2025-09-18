package com.jllt.stepDefinition;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.time.Duration;
import java.util.Map;

public class baseSteps {
    private final context context;

    public baseSteps(context context) {
        this.context = context;
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    @Given("I am logged in to Salesforce")
    public void loginToSalesforce() {
        context.getLoginPage().loginToSalesforce();
        extentLogger.info("Login into salesforce");
        extentReportListener.addScreenshotToStep("login to salesforce....", getScreenshotBase64());
    }

    @Then("I should be logged in successfully")
    public void verifySuccessfulLogin() throws InterruptedException {
        Thread.sleep(6000);
        Assert.assertTrue(context.getCommonUtils().getPageTitle().contains("Home | Salesforce"));
        extentLogger.info("Verifying page title");
        extentReportListener.addScreenshotToStep("Verifying page title....", getScreenshotBase64());
    }

    @When("I log in as user {string}")
    public void iLogInAsUser(String targetUsername) throws InterruptedException {
        context.getLoginPage().loginAsUser(targetUsername);
        extentReportListener.addScreenshotToStep("login as a salesforce user", getScreenshotBase64());
    }

    @Then("I click on App Launcher and search {string}")
    public void clickOnAppLauncherAndSearch(String val) throws InterruptedException{
        context.getAppLauncherPage().clickAppLauncherAndSearchApp(val);
        context.getCommonUtils().pressEnter();
        context.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        context.getLogger().info("Projects list view page is displayed");
        Thread.sleep(1000);
        extentReportListener.addScreenshotToStep("Click on App Launcher", getScreenshotBase64());
    }

    /*@And("I navigate to Global search and search {string}")
    public void navigateToGlobalSearchAndSearch(String projectName) throws InterruptedException{
        Map<String, String> projectDetails = Map.of("Project", projectName);
        context.getGlobalSearchAndProjectSearchCertPage().searchProjectOnGlobalSearch(projectDetails);
        context.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        context.getLogger().info("Project details page is displayed");
    }*/

    /*@When("I click on Milestones Related list and click on New button")
    public void clickOnMilestonesRelatedListAndClickOnNewButton() throws InterruptedException{
        context.getGlobalSearchAndProjectSearchCertPage().dismissPopup();
        context.getGlobalSearchAndProjectSearchCertPage().clickMilestonesRelatedListAndClickNext();
    }*/

    @Given("An account is created with the following details:")
    public void createAccountRequest(DataTable dataTable) throws InterruptedException {
        Map<String, String> accountDetails = dataTable.asMaps(String.class, String.class).getFirst();
        String recordType = accountDetails.getOrDefault("Account Record Type", "");
        String searchAccountName = accountDetails.getOrDefault("Search Account Name",
                accountDetails.getOrDefault("Account Name", ""));
        context.getAccountsWdsfPage().createAccountRequest(accountDetails, recordType, searchAccountName);
    }

    @Then("I search the record on the recently viewed page with {string}")
    public void searchRecordOnRecentlyViewedPage(String arg) throws InterruptedException {
        String recordName = context.getGenericWdsfPage().getRecordNameFromContext();
        context.getLogger().info("Searching record on list view1: {}", recordName);
        //context.getGenericWdsfPage().SearchRecordOnListView(recordName);
        context.getGenericWdsfPage().SearchRecordNameOnListView(recordName);
        extentReportListener.addScreenshotToStep("Searched record on list view: " + recordName, getScreenshotBase64());
    }

    @When("I go to the {string} tab")
    public void goToTheTab(String Tab) throws InterruptedException {
        context.getGenericWdsfPage().goToTab(Tab);
        extentReportListener.addScreenshotToStep("Navigated to " + Tab + " tab", getScreenshotBase64());
    }
}
