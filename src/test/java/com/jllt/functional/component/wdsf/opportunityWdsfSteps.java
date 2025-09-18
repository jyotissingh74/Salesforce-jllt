package com.jllt.functional.component.wdsf;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.docstring.DocString;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        //need to uncomment
        Assert.assertTrue(context.getCommonUtils().getPageTitle().contains("New Opportunity | Salesforce"));
    }

    @When("I fill in the following opportunity details:")
    public void FillInTheFollowingOpportunityDetails(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> opportunityDetails = dataTable.asMaps(String.class, String.class);
        //Map<String, String> details = opportunityDetails.get(0);
        Map<String, String> details = new HashMap<>(opportunityDetails.getFirst());

        String accountName = (String) context.getContextData("accountName");
        extentLogger.info("Retrieve account name from context");
        context.getLogger().info("Retrieved account name from context: {}", accountName);
        if (accountName != null && !accountName.isEmpty()) {
            details.put("Account Name", accountName);
        }
        context.getLogger().info("Using Account Name: {}",details.get("Account Name"));

        context.getOpportunityWdsfPage().CreateNewOpportunityForm(details);
    }

    @And("I click the Save button")
    public void ClickTheSaveButton() throws InterruptedException {
        context.getOpportunityWdsfPage().ClickOnSaveButton();
    }

    @Then("I should be redirected to the opportunity landing page")
    public void NavigatedToOpportunityLandingPage() throws InterruptedException {
        context.getOpportunityWdsfPage().verifyOpportunityLandingPageTitle();
    }

    @And("verify Business Group and Sub Business Group values on the opportunity record page:")
    public void verifyBusinessGroupAndSubBusinessGroupValuesOnTheOpportunityRecordPage(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> opportunityBusinessGroup = dataTable.asMaps(String.class, String.class);
        Map<String, String> businessGroup = opportunityBusinessGroup.get(0);

        context.getOpportunityWdsfPage().verifyBusinessGroupValues(businessGroup);
    }

    @And("verify opportunity record type {string}")
    public void verifyOpportunityRecordType(String expectedRecordType) throws InterruptedException {
        extentLogger.info("Verifying opportunity record type: " + expectedRecordType);
        context.getOpportunityWdsfPage().verifyOpportunityRecordType(expectedRecordType);
        extentReportListener.addScreenshotToStep("Verified opportunity record type", getScreenshotBase64());
    }

    @When("I fill in the following opportunity details for Generic WorkDynamics Opportunity:")
    public void FillInOpportunityDetailsForGWDOpp(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> opportunityDetails = dataTable.asMaps(String.class, String.class);
        //Map<String, String> details = opportunityDetails.get(0);
        Map<String, String> details = new HashMap<>(opportunityDetails.get(0));

        String accountName = (String) context.getContextData("accountName");
        extentLogger.info("Retrieve account name from context");
        context.getLogger().info("Retrieved account name from context for GWD: {}", accountName);
        if (accountName != null && !accountName.isEmpty()) {
            details.put("Account Name", accountName);
        }
        context.getLogger().info("Using Account Name for GWD: {}",details.get("Account Name"));

        context.getOpportunityWdsfPage().CreateNewOpportunityForm_GWD(details);
    }

    @When("I click on Opportunity Pursuit type and verify {string} picklist value is displayed")
    public void verifyOpportunityPursuitTypePicklist(String expectedValue) throws InterruptedException {
        extentLogger.info("Clicking on Opportunity Pursuit type and verifying " + expectedValue + " picklist value is displayed");
        context.getOpportunityWdsfPage().clickAndVerifyOpportunityPursuitType(expectedValue);
        extentReportListener.addScreenshotToStep("Verified picklist values", getScreenshotBase64());
    }

    @Then("I select {string} picklist value and Click on save")
    public void selectOpportunityPursuitTypeAndSave(String valueToSelect) throws InterruptedException {
        extentLogger.info("Selecting " + valueToSelect + " picklist value and clicking save");
        context.getOpportunityWdsfPage().selectOpportunityPursuitTypeAndSave(valueToSelect);
        extentReportListener.addScreenshotToStep("Selected value and clicked save", getScreenshotBase64());
    }

    @And("I should verify the fields on the Opportunity record page:")
    public void verifyFieldsOnOpportunityRecordPage(DataTable datable) throws InterruptedException {
        for (Map<String, String> r : datable.asMaps(String.class, String.class)) {
            context.getOpportunityWdsfPage().verifyOpportunityField(
                    r.get("Field Name"),
                    r.getOrDefault("Expected Value", "")
            );
        }
    }
}
