package com.jllt.stepDefinition;

import com.jllt.scenarioContext.context;
import com.jllt.utils.extentLogger;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

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
}
