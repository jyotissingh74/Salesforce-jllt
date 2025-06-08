package com.jllt.runners;

import com.jllt.utils.extentReportListener;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

import java.io.IOException;

@CucumberOptions(
        features = {
                "Tests/src/test/resources/features/wdsf",
                "Tests/src/test/resources/features/certinia",
                "Tests/src/test/resources/features/common"
        },
        glue = {
                "com.jllt.stepDefinition",
                "com.jllt.functional.component.wdsf",
                "com.jllt.functional.component.certinia",
                "com.jllt.smoke.wdsf",
                "com.jllt.smoke.certinia",
                "com.jllt.endtoend.wdsf",
                "com.jllt.endtoend.certinia"
        },
        plugin = {"pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "html:target/cucumber-reports/report.html"
        },
        tags = "@account or @contact or @opportunity"
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @AfterSuite
    public void tearDown() throws IOException {
        extentReportListener.flushReport();
    }
}
