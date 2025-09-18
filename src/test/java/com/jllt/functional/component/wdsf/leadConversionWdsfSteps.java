package com.jllt.functional.component.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class leadConversionWdsfSteps{
    private final context context;

    public leadConversionWdsfSteps(context context) {
        this.context = context;
    }

    @Given("A lead is created with the following details:")
    public void createLead(DataTable dataTable) throws InterruptedException {
        Map<String, String> leadDetails = dataTable.asMaps(String.class, String.class).getFirst();
        String recordType = leadDetails.get("Lead Record Type");
        String accountType = leadDetails.get("Lead Type");
        context.getLeadConversionWdsfPage().createLead(leadDetails, recordType, accountType);
    }

    @When("I click on the Convert Lead button")
    public void clickOnConvertLeadButton() throws InterruptedException {
        context.getLeadConversionWdsfPage().clickConvertLeadButton();
    }

    @Then("I should see the Convert Lead dialog")
    public void verifyConvertLeadDialog() throws InterruptedException {
        context.getLeadConversionWdsfPage().waitForConvertLeadDialog();
    }

    @And("I verify the following fields on Convert Lead dialog:")
    public void verifyFieldsOnConvertLeadDialog(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String field = row.get("Field Name");
            String expected = row.get("Expected Value");
            String actual = context.getLeadConversionWdsfPage().getSelectedDropdownValue(field);
            Assert.assertEquals(actual, expected, "Mismatch for field: " + field);
        }
    }

    @And("I click on the Next button on Convert Lead dialog")
    public void clickOnNextButtonOnConvertLeadDialog() {
        context.getLeadConversionWdsfPage().clickNextOnConvertLeadDialog();
    }

    @Then("I navigate to the Convert Lead dialog")
    public void navigateToConvertLeadDialog() throws InterruptedException {
        Assert.assertTrue(context.getLeadConversionWdsfPage().isConvertLeadDialogAvailable(),
                "Convert Lead dialog is not available");
    }

    @And("the selected account should be {string}")
    public void verifySelectedAccount(String expectedAccount) {
        String actual = context.getLeadConversionWdsfPage().getSelectedAccount();
        String expected = (String) context.getContextData("accountName");
        Assert.assertEquals(actual, expected, "Selected account does not match");
    }

    @When("I expand the {string} section on convert lead dialog")
    public void expandSectionOnLeadCovertDialog(String section) throws InterruptedException {
        context.getLeadConversionWdsfPage().expandSectionOnLeadConvertDialog(section);
    }

    @Then("the {string} radio button {string} should be selected")
    public void verifyRadioButtonSelected(String section, String option) throws InterruptedException {
        context.getLeadConversionWdsfPage().selectRadioButtonOnConvertLeadDialog(section,option);
    }

    @And("I verify fields in contact section:")
    public void verifyContactSectionFields(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> fields = dataTable.asMaps(String.class, String.class);
        context.getLeadConversionWdsfPage().verifyContactSectionFields(fields);
    }

    @And("I verify fields in Opportunity section on convert lead dialog:")
    public void verifyOpportunitySectionFields(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> fields = dataTable.asMaps(String.class, String.class);
        context.getLeadConversionWdsfPage().verifyOpportunitySectionFields(fields);
    }

    @And("the Future Opportunity Owner should be {string}")
    public void verifyFutureOpportunityOwner(String expected) {
        String actual = context.getLeadConversionWdsfPage().getFutureOpportunityOwner();
        Assert.assertEquals(actual, expected, "Future Opportunity Owner does not match");
        context.getLogger().info("Future Opportunity Owner is verified {}: {}", actual,expected);
    }

    @When("I click the Convert button")
    public void clickConvertButton() throws InterruptedException {
        Thread.sleep(500);
        context.getLeadConversionWdsfPage().clickConvertButton();
    }

    @Then("I verify the error message {string} on the Convert Lead dialog")
    public void verifyErrorMessageOnConvertLeadDialog(String expectedMessage) {
        String actualMessage = context.getLeadConversionWdsfPage().getErrorMessage();
        // Normalize whitespace (spaces, tabs, newlines) to a single space
        String normalizedActual = actualMessage.replaceAll("\\s+", " ").trim();
        context.getLogger().info("Actual Error Message: {}", normalizedActual);
        String normalizedExpected = expectedMessage.replaceAll("\\s+", " ").trim();
        context.getLogger().info("Expected Error Message: {}", normalizedExpected);
        Assert.assertEquals(normalizedActual, normalizedExpected, "Error message mismatch");
    }

    @And("I should click the Cancel button on the Convert Lead dialog")
    public void clickCancelButtonOnConvertLeadDialog() {
        context.getLeadConversionWdsfPage().clickCancelButtonOnConvertLeadDialog();
    }

    @And("I fill in the Pain, Budget, & Decision Process Process fields in order to convert the Lead:")
    public void fillPainBudgetDecisionProcessProcessFields(DataTable dataTable) throws InterruptedException {
        Map<String, String> leadConversionDetails = dataTable.asMaps(String.class, String.class).getFirst();
        context.getLeadConversionWdsfPage().fillPainBudgetDecisionProcessProcessFields(leadConversionDetails);
        context.getLogger().info("Pain, Budget, & Decision Process fields filled successfully");
    }

    @Then("I should see the Converted Lead success page")
    public void navigatedToConvertedLeadSuccessPage() throws InterruptedException {
        context.getLeadConversionWdsfPage().waitForLeadConvertedLanding();
    }

    @And("I verify Account, Contact and Opportunity Cards on converted landing page")
    public void verifyCardsOnConvertedLandingPage() {
        context.getLeadConversionWdsfPage().verifyConvertedCardsOnLanding();
    }

    @And("I click on Go to Leads button")
    public void clickOnGoToLeadsButton() throws InterruptedException {
        context.getLeadConversionWdsfPage().clickGoToLeadsButton();
        Thread.sleep(1000);
    }
}
