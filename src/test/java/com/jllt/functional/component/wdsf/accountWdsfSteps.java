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

public class accountWdsfSteps {
    private final context context;
    //private final List<Map<String, String>> testData;
    //private String currentTestCaseNumber;

    public accountWdsfSteps(context context) {
        this.context = context;
        //this.testData = excelUtils.readTestData("AccountData");
    }

    private String getScreenshotBase64() {
        return ((TakesScreenshot) webDriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    @Then("I navigate to the Accounts page")
    public void NavigateToAccountsPage() throws InterruptedException {
        extentLogger.info("Navigating to Accounts Page");
        context.getAccountsWdsfPage().navigateToAccountsTab();
        extentReportListener.addScreenshotToStep("Navigated to Accounts Page and adding screenshot", getScreenshotBase64());
        Thread.sleep(500);
    }

    @When("I click on the New Account Request Form button And I fill in the following account details:")
    public void FillAccountDetailsFromDataTable(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> accountDetails = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = accountDetails.getFirst(); // Get the first (and only) row of data

        /*// Store the account name in the context
        context.setContextData("accountName", details.get("Company Name"));
        extentLogger.info("Filling account details" + details.toString());*/

        // Store the account name in the context (support both headers)
        String acctName = details.getOrDefault("Company Name",
                details.getOrDefault("Account Name", details.get("Account")));
        context.setContextData("accountName", acctName);
        extentLogger.info("Filling account details" + details.toString());

        context.getAccountsWdsfPage().clickNewAccountRequestFormButton();
        context.getAccountsWdsfPage().CreateNewAccountRequestForm(details);
        context.getLogger().info("Filled in account details from DataTable");
        context.getAccountsWdsfPage().setSubmitButton();
        extentLogger.pass("Account request form submitted");
    }

    @Then("I should see the confirmation message")
    public void VerifyConfirmationMessage() {
        String expectedMessage = "request sent successfully. You'll soon hear back from the Salesforce Specialist team who will be dealing with this request. Thanks.";
        String actualMessage = context.getAccountsWdsfPage().getConfirmationMessage();

        extentLogger.info("Verifying confirmation message....");
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Confirmation message does not contain the expected text.\nExpected: " + expectedMessage + "\nActual: " + actualMessage);
        context.getLogger().info("Confirmation message verified successfully");
        extentLogger.pass("Confirmation message verified" + actualMessage);
        context.getAccountsWdsfPage().cancelButton();
        context.getAccountsWdsfPage().refreshAccountPage();
    }

    @When("I click on the New Account button with record type {string}")
    public void SelectAccountRecordType(String recordType) throws InterruptedException {
        extentLogger.info("Clicking on New Account with record type: " + recordType);
        context.getAccountsWdsfPage().clickNewAccount(recordType);
        context.setContextData("currentRecordType", recordType);
    }

    @Then("I should see the Account Search page")
    public void VerifyAccountSearchPage() throws InterruptedException {
        Thread.sleep(2000);
        String recordType = (String) context.getContextData("currentRecordType");
        //String expectedTitle = "New Account | Salesforce";
        String expectedTitle = "Lightning Experience | Salesforce";
        //String expectedTitle = "New Account: " + recordType + " | Salesforce";
        String actualTitle = context.getCommonUtils().getPageTitle();

        // Normalize spaces (trim and convert NBSP to normal space)
        String normExpected = expectedTitle.replace('\u00A0',' ').trim().replaceAll("\\s+"," ");
        String normActual = actualTitle.replace('\u00A0',' ').trim().replaceAll("\\s+"," ");

        Assert.assertTrue(normActual.contains(normExpected),
                "Expected page title to contain '" + expectedTitle + "', but found: " + actualTitle);

        extentLogger.pass("Accounts Page opened successfully");
    }

    @Then("I search for existing account with name {string}")
    public void SearchForExistingAccount(String accountName) throws InterruptedException {
        context.getAccountsWdsfPage().SearchAndSelectAccount(accountName);
        extentLogger.pass("Accounts Page opened successfully");
    }

    @Then("I should be redirected to the account landing page")
    public void NavigateToAccountLandingPage() throws InterruptedException {
        context.getAccountsWdsfPage().verifyAccountLandingPageTitle();
    }

    @And("I create a new account from D&B search results with details:")
    public void CreateANewAccountFromDBSearchResultsWithDetails(DataTable dataTable) throws InterruptedException {
        Map<String, String> accountDetails = dataTable.asMaps(String.class, String.class).getFirst();
        context.getAccountsWdsfPage().createAccountFromDnBSearch();
        context.getAccountsWdsfPage().fillAndCreateAccount(accountDetails);
    }

    @And("I should verify the fields on the account record page:")
    public void verifyFieldsOnAccountRecordPage(DataTable dt) throws InterruptedException {
        for (Map<String,String> r : dt.asMaps(String.class,String.class)) {
            context.getAccountsWdsfPage().verifyAccountField(
                    r.get("Field Name"),
                    r.getOrDefault("Expected Value","")
            );
        }
    }
}
