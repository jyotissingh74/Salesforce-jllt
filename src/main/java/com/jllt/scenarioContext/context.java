package com.jllt.scenarioContext;

import com.jllt.base.commonUtils;
import com.jllt.pages.common.appLauncherPage;
import com.jllt.pages.common.loginPage;
import com.jllt.pages.wdsf.*;
import com.jllt.utils.excelUtils;
import com.jllt.utils.webDriverManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//@Getter
//@Setter
//@AllArgsConstructor
public class context {
    private WebDriver driver;
    private Logger logger;
    private commonUtils commonUtils;
    private excelUtils excelUtils;
    private accountsWdsfPage accountsWdsfPage;
    private contactsWdsfPage contactsWdsfPage;
    private opportunityWdsfPage opportunityWdsfPage;
    private loginPage loginPage;
    private WebDriverWait wait;
    private appLauncherPage appLauncherPage;
    private leadsWdsfPage leadsWdsfPage;
    private genericWdsfPage genericWdsfPage;
    private leadConversionWdsfPage leadConversionWdsfPage;

    // Scenario context data
    private final Map<String, String> contextData;

    // Custom constructor for initialization
    public context() {
        this.logger = LoggerFactory.getLogger(context.class);
        this.contextData = new HashMap<>();
        initializeDriver();
    }

    // Initializes driver, wait, utilities, and all page objects
    private void initializeDriver(){
        this.driver = webDriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(60));
        this.excelUtils = new excelUtils();
        this.commonUtils = new commonUtils(this);
        this.accountsWdsfPage = new accountsWdsfPage(this);
        this.contactsWdsfPage = new contactsWdsfPage(this);
        this.opportunityWdsfPage = new opportunityWdsfPage(this);
        this.loginPage = new loginPage(this);
        this.appLauncherPage=new appLauncherPage(this);
        this.leadsWdsfPage = new leadsWdsfPage(this);
        this.genericWdsfPage = new genericWdsfPage(this);
        this.leadConversionWdsfPage = new leadConversionWdsfPage(this);
    }

    //Returns the WebDriver, initializing if necessary
    public WebDriver getDriver() {
        if (driver == null) {
            driver = webDriverManager.getDriver();
        }
        return driver;
    }

    //Restarts the WebDriver (teardown only, re-init optional)
    public void restartDriver(){
        getLogger().info("Restarting WebDriver...");
        tearDown();
        initializeDriver();
        getLogger().info("WebDriver restarted successfully.");
    }

    public void setContextData(String key, String value) {
        contextData.put(key, value);
    }

    public Object getContextData(String key) {
        return contextData.get(key);
    }

    public void tearDown() {
        if (driver != null) {
            webDriverManager.quitDriver();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public commonUtils getCommonUtils() {
        return commonUtils;
    }

    public excelUtils getExcelUtils() {
        return excelUtils;
    }

    public accountsWdsfPage getAccountsWdsfPage() {
        return accountsWdsfPage;
    }

    public contactsWdsfPage getContactsWdsfPage() {
        return contactsWdsfPage;
    }

    public opportunityWdsfPage getOpportunityWdsfPage() {
        return opportunityWdsfPage;
    }

    public loginPage getLoginPage() {
        return loginPage;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public void setWait(WebDriverWait wait) {
        this.wait = wait;
    }

    public appLauncherPage getAppLauncherPage() {
        return appLauncherPage;
    }

    public leadsWdsfPage getLeadsWdsfPage() {
        return leadsWdsfPage;
    }


    public genericWdsfPage getGenericWdsfPage() {
        return genericWdsfPage;
    }

    public leadConversionWdsfPage getLeadConversionWdsfPage() {
        return leadConversionWdsfPage;
    }
}
