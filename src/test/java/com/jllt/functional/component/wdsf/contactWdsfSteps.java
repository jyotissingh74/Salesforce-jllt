package com.jllt.functional.component.wdsf;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class contactWdsfSteps {
    private final context context;
    //private final List<Map<String, String>> testData;
    // private String currentTestCaseNumber;

    public contactWdsfSteps(context context) {
        this.context = context;
        //this.testData = excelUtils.readTestData("ContactData");
    }

    @Then("I navigate to the Contacts page")
    public void navigateToContactsPage() throws InterruptedException {
        extentLogger.info("Navigating to Contacts Page");
        context.getContactsPage().clickNewContact();
        Thread.sleep(5000);
        Assert.assertTrue(context.getCommonUtils().getPageTitle().contains("New Contact: Contact | Salesforce"));
        extentLogger.pass("Contacts Page opened successfully");
    }

    @When("I click on the New Contact button And I fill in the following Contact details:")
    public void fillContactDetailsFromDataTable(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> contactDetails = dataTable.asMaps(String.class, String.class);
        //Map<String, String> details = contactDetails.get(0); // Get the first (and only) row of data
        Map<String, String> details = new HashMap<>(contactDetails.get(0));

        String accountName = (String) context.getContextData("accountName");
        extentLogger.info("Retrieve account name from context");
        context.getLogger().info("Retrieved account name from context: {}", accountName);
        if (accountName != null && !accountName.isEmpty()) {
            details.put("account Name", accountName);
        }
        context.getLogger().info("Using Company Name: {}",details.get("Company Name"));
        context.setContextData("contactName", details.get("First Name") + " " + details.get("Last Name"));
        extentLogger.info("Filling contact details" + details.toString());

        context.getContactsPage().CreateNewContact(details);
        context.getLogger().info("Filled in contact details from DataTable");
        context.getContactsPage().setSaveEdit();
        context.getLogger().info("Save Contact Details");
        extentLogger.pass("Contact details saved");
    }

    @Then("I should be redirected to the contact landing page")
    public void NavigatedToContactLandingPage() throws InterruptedException {
        context.getContactsPage().verifyContactLandingPageTitle();
    }
}
