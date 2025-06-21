package com.jllt.functional.component.wdsf;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import io.cucumber.datatable.DataTable;
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
    public void navigateToAccountsPage() throws InterruptedException {
        //extentReportListener.getTest().log(Status.INFO,"Navigating to Accounts Page");
        extentLogger.info("Navigating to Accounts Page");
        context.getAccountsPage().navigateToAccountsTab();
        //context.getAccountsPage().clickNewAccount();
        extentReportListener.addScreenshotToStep("Clicked on New Contact and adding screenshot", getScreenshotBase64());
        Thread.sleep(500);
//        Assert.assertTrue(context.getCommonUtils().getPageTitle().contains("New Account | Salesforce"));
//        context.getAccountsPage().SearchAndSelectAccount("Test Account");
//        extentLogger.pass("Accounts Page opened successfully");
    }

    @When("I click on the New Account Request Form button And I fill in the following account details:")
    public void fillAccountDetailsFromDataTable(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> accountDetails = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = accountDetails.get(0); // Get the first (and only) row of data

        // Store the account name in the context
        context.setContextData("accountName", details.get("Company Name"));
        extentLogger.info("Filling account details" + details.toString());

        context.getAccountsPage().clickNewAccountRequestFormButton();
        context.getAccountsPage().CreateNewAccountRequestForm(details);
        context.getLogger().info("Filled in account details from DataTable");
        context.getAccountsPage().setSubmitButton();
        extentLogger.pass("Account request form submitted");
    }

    @Then("I should see the confirmation message")
    public void verifyConfirmationMessage() {
        String expectedMessage = "request sent successfully. You'll soon hear back from the Salesforce Specialist team who will be dealing with this request. Thanks.";
        String actualMessage = context.getAccountsPage().getConfirmationMessage();

        extentLogger.info("Verifying confirmation message....");
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Confirmation message does not contain the expected text.\nExpected: " + expectedMessage + "\nActual: " + actualMessage);
        context.getLogger().info("Confirmation message verified successfully");
        extentLogger.pass("Confirmation message verified" + actualMessage);
        context.getAccountsPage().cancelButton();
        context.getAccountsPage().refreshAccountPage();
    }

    @When("I click on the New Account button with record type {string}")
    public void selectAccountRecordType(String recordType) throws InterruptedException {
        extentLogger.info("Clicking on New Account with record type: " + recordType);
        context.getAccountsPage().clickNewAccount(recordType);
        context.setContextData("currentRecordType", recordType);
    }

    @Then("I should see the Account Search page")
    public void VerifyAccountSearchPage() throws InterruptedException {
        Thread.sleep(2000);
        String recordType = (String) context.getContextData("currentRecordType");
        String expectedTitle = "New Account | Salesforce";
        //String expectedTitle = "New Account: " + recordType + " | Salesforce";
        String actualTitle = context.getCommonUtils().getPageTitle();

        Assert.assertTrue(actualTitle.contains(expectedTitle),
                "Expected page title to contain '" + expectedTitle + "', but found: " + actualTitle);

        extentLogger.pass("Accounts Page opened successfully");
    }

    @Then("I search for existing account with name {string}")
    public void SearchForExistingAccount(String accountName) throws InterruptedException {
        //       Assert.assertTrue(context.getCommonUtils().getPageTitle().contains("New Account | Salesforce"));
        context.getAccountsPage().SearchAndSelectAccount(accountName);
        extentLogger.pass("Accounts Page opened successfully");
    }

    /*@Then("I fill in the following account details:")
    public void FillInTheFollowingAccountDetails(DataTable dataTable) {
        List<Map<String, String>> accountDetails = dataTable.asMaps(String.class, String.class);
        Map<String, String> details = accountDetails.get(0); // Get the first (and only) row of data

        // Store the account name in the context
        context.setContextData("accountName", details.get("Company Name"));
        extentLogger.info("Filling account details" + details.toString());

        context.getAccountsPage().clickNewAccountRequestFormButton();
        context.getAccountsPage().CreateNewAccountRequestForm(details);
        context.getLogger().info("Filled in account details from DataTable");
        context.getAccountsPage().setSubmitButton();
        extentLogger.pass("Account request form submitted");
    }*/

    @When("I click on the D&B Account Name from the searched results")
    public void ClickOnTheDBAccountNameFromTheSearchedResults() throws InterruptedException {
        context.getAccountsPage().clickOnDAndBAccountName();
    }

    @Then("I should be redirected to the account landing page")
    public void NavigateToAccountLandingPage() throws InterruptedException {
        context.getAccountsPage().verifyAccountLandingPageTitle();
    }
}
