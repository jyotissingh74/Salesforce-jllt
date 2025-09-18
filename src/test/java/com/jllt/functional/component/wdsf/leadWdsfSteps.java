package com.jllt.functional.component.wdsf;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
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

public class leadWdsfSteps {
    private final context context;

    public leadWdsfSteps(context context) {
        this.context = context;
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    @Then("I navigate to the Leads page")
    public void navigateToAccountsPage() throws InterruptedException {
        extentLogger.info("Navigating to Contacts Page");
        context.getLeadsWdsfPage().navigateToLeadsTab();
        Thread.sleep(1000);
    }

    @When("I click on the New Lead button with record type {string}")
    public void selectLeadRecordType (String recordType) throws InterruptedException {
        extentLogger.info("Clicking on New Lead with record type: " + recordType);
        context.getLeadsWdsfPage().clickNewLeads(recordType);
        context.setContextData("currentRecordType", recordType);
    }

    @When("I fill in the following Lead details:")
    public void fillLeadDetails(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> leadDetails = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = new HashMap<>(leadDetails.getFirst());
        context.getLeadsWdsfPage().fillLeadForm(details);
    }

    @And("I select {string} account option")
    public void selectAccountOption(String accountType) {
        String recordType = context.getContextData("currentRecordType").toString();
        context.getLogger().info("Selecting account option: {} for record type: {}", accountType, recordType);
        context.getLeadsWdsfPage().selectAccountOption(accountType, recordType);
        context.setContextData("Lead Type", accountType);
    }

    @Then("I should verify {string} text box is enabled")
    public void verifyTextBoxIsEnabled(String leadType) {
        Assert.assertTrue(context.getLeadsWdsfPage().isSelectedAccountFieldEnabled(leadType),
                "Account fields are not in the correct enabled/disabled state for: " + leadType);
    }

    @When("I save the lead")
    public void saveLead() throws InterruptedException {
        context.getLeadsWdsfPage().saveLead();
    }

    @Then("I should be redirected to the lead landing page")
    public void navigatedToLeadLandingPage() throws InterruptedException {
        context.getLeadsWdsfPage().verifyLeadLandingPageTitle();
    }

    @And("I should verify the fields on the lead record page:")
    public void verifyFieldsOnLeadRecordPage(DataTable dataTable) {
        Map<String, Function<String, String>> fieldExtractors = getStringFunctionMap();

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String field = row.get("Field Name");
            String expected = row.get("Expected Value");
            Function<String, String> extractor = fieldExtractors.get(field);

            if (extractor == null) {
                throw new AssertionError("Unknown field: " + field);
            }

            String actual = extractor.apply(field);

            // Special handling for Account and Name fields
            if (field.equals("Account")) {
                expected = String.valueOf(context.getContextData("accountName"));
            } else if (field.equals("Name")) {
                expected = String.valueOf(context.getContextData("LeadName"));
            }

            // --- Case-insensitive check for Email field ---
            if ("Email".equals(field)) {
                Assert.assertEquals(actual.toLowerCase(), expected.toLowerCase(), "Mismatch for field: " + field);
            } else {
                Assert.assertEquals(actual, expected, "Mismatch for field: " + field);
            }
        }
    }

    private Map<String, Function<String, String>> getStringFunctionMap() {
        Map<String, Function<String, String>> fieldExtractors = new HashMap<>();
        fieldExtractors.put("Account", label -> context.getGenericWdsfPage().getFieldValueByLabel("Account", "lightning-formatted-text"));
        fieldExtractors.put("Name", label -> context.getGenericWdsfPage().getFieldValueByLabel("Name", "lightning-formatted-name"));
        fieldExtractors.put("Lead Status", label -> context.getGenericWdsfPage().getFieldValueByLabel("Lead Status", "lightning-formatted-text"));
        fieldExtractors.put("Region", label -> context.getGenericWdsfPage().getFieldValueByLabel("Region", "lightning-formatted-text"));
        fieldExtractors.put("Phone", label -> context.getGenericWdsfPage().getFieldLinkValueByLabel("Phone"));
        fieldExtractors.put("Mobile", label -> context.getGenericWdsfPage().getFieldLinkValueByLabel("Mobile"));
        fieldExtractors.put("Email", label -> context.getGenericWdsfPage().getFieldLinkValueByLabel("Email"));
        fieldExtractors.put("Lead Owner", label -> context.getLeadsWdsfPage().getLeadOwnerValue());
        fieldExtractors.put("Lead Source", label -> {
            try {
                return context.getLeadsWdsfPage().getLeadSourceFieldValue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        fieldExtractors.put("Lead Sub Source", label -> context.getLeadsWdsfPage().getLeadSubSourceFieldValue());
        return fieldExtractors;
    }

    @And("I select Lead Source as {string} and should see Lead Sub Source options:")
    public void selectLeadSourceAndVerifySubSourceOptions(String leadSource, String expectedSubSources) throws InterruptedException {
        List<String> expectedOptions = Arrays.stream(expectedSubSources.split("\\|")).map(String::trim).collect(Collectors.toList());

        List<String> actualOptions = context.getLeadsWdsfPage().verifyLeadSubSourceDropdownValues(leadSource)
                .stream().map(String::trim).collect(Collectors.toList());

        context.getLogger().info("Expected Lead Sub Source options: {}", expectedOptions);
        context.getLogger().info("Actual Lead Sub Source options: {}", actualOptions);

        Assert.assertEquals(actualOptions, expectedOptions, "Lead Sub Source options mismatch!");
    }

    @And("I should verify fields on highlights panel:")
    public void verifyFieldsOnHighlightsPanel(String docString) {
        List<String> labels = java.util.Arrays.stream(
                        docString.replace("\r", "").replace("\n", " ")
                                .split("\\|"))
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !"null".equalsIgnoreCase(s))
                .toList();

        List<String> missing = context.getLeadsWdsfPage().verifyHighlightsLabels(labels);
        Assert.assertTrue(missing.isEmpty(), "Missing highlights labels: " + String.join(", ", missing));
    }

    @And("I should verify Status Path on lead record page:")
    public void verifyStatusPathOnLeadRecordPage(DocString body) {
        List<String> expected = java.util.Arrays.stream(body.getContent().split("\\|"))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();
        context.getLeadsWdsfPage().verifyStatusPathEquals(expected);
    }
}
