package com.jllt.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class extentReportListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String reportPath;

    public static void setupExtentReport(){
        if (extent == null) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            reportPath = "Tests/target/extent-reports/ExtentReport_" + timeStamp + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("JLL Automation Report");
            sparkReporter.config().setReportName("Salesforce Automation Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", "UAT");
        }
    }

    public static void createTest(String testName){
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    public static ExtentTest getTest(){
        return test.get();
    }

    public static ExtentReports getReport(){
        return extent;
    }

    public static void addScreenshotToStep(String stepName, String base64Screenshot) {
        getTest().info(stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
    }

    public static void flushReport() throws IOException {
        if (extent != null) {
            extent.flush();
            openReportInBrowser();
        }
    }

    private static void openReportInBrowser() throws IOException {
        File reportFile = new File(reportPath);
        if (reportFile.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(reportFile.toURI());
        }
    }
}
