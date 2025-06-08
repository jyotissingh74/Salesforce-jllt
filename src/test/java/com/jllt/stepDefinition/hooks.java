package com.jllt.stepDefinition;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.jllt.utils.extentReportListener;
import com.jllt.utils.webDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class hooks {
    private WebDriver driver;

    @Before
    public void setUp(Scenario scenario) {
        extentReportListener.setupExtentReport();
        extentReportListener.createTest(scenario.getName());
        driver = webDriverManager.getDriver();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (driver != null) {
            String screenshotName = scenario.getName() + " - " + scenario.getLine();
            String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            extentReportListener.getTest().info("Step: " + scenario.getName(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64, screenshotName).build());
            scenario.attach(screenshotBase64.getBytes(), "image/png", screenshotName);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("After Scenario - Logging result in Extent Report");
        WebDriver driver = webDriverManager.getDriver();
        if (scenario.isFailed()) {
            String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            scenario.attach(screenshotBase64.getBytes(), "image/png", "Screenshot");
            extentReportListener.getTest().fail("Scenario failed")
                    .fail(MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64, "Failure Screenshot").build());
        } else {
            extentReportListener.getTest().pass("Scenario passed");
        }
        extentReportListener.getTest().info("Ending scenario: " + scenario.getName());
        webDriverManager.quitDriver();
    }
}
