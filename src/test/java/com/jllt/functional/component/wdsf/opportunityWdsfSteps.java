package com.jllt.functional.component.wdsf;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class opportunityWdsfSteps {
    private final context context;

    public opportunityWdsfSteps(context context) {
        this.context = context;
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    @Then("I navigate to the Opportunities page")
    public void NavigateToOpportunitiesPage() throws InterruptedException {
        extentLogger.info("Navigating to Opportunities Page");
        context.getOpportunityWdsfPage().navigateToOpportunitiesTab();
        //context.getAccountsPage().clickNewAccount();
        extentReportListener.addScreenshotToStep("Clicked on New Opportunities and adding screenshot", getScreenshotBase64());
        Thread.sleep(2000);
    }

    @When("I click on the New Opportunity button")
    public void ClickOnTheNewOpportunityButton() throws InterruptedException {
        extentLogger.info("Click on the New Button on opportunities page");
        context.getOpportunityWdsfPage().clickNewOpportunityButton();
    }

    @Then("I should see the New Opportunity form")
    public void ShouldSeeTheNewOpportunityForm() throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertTrue(context.getCommonUtils().getPageTitle().contains("New Opportunity | Salesforce"));
    }

    @When("I fill in the following opportunity details:")
    public void FillInTheFollowingOpportunityDetails(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> opportunityDetails = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = opportunityDetails.get(0);

        context.getOpportunityWdsfPage().CreateNewOpportunityForm(details);
    }

    @And("I click the Save button")
    public void ClickTheSaveButton() throws InterruptedException {
        context.getOpportunityWdsfPage().ClickOnSaveButton();
    }

    @Then("I should see the new opportunity record")
    public void ShouldSeeTheNewOpportunityRecord() {
    }
}
