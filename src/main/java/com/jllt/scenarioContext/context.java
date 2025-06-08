package com.jllt.scenarioContext;

import com.jllt.base.commonUtils;
import com.jllt.pages.certinia.accountsCreationPage;
import com.jllt.pages.certinia.timePeriodsPage;
import com.jllt.pages.common.loginPage;
import com.jllt.pages.wdsf.accountsWdsfPage;
import com.jllt.pages.wdsf.contactsWdsfPage;
import com.jllt.pages.wdsf.opportunityWdsfPage;
import com.jllt.utils.excelUtils;
import com.jllt.utils.webDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


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
    private accountsCreationPage accountsCreationPage;
    private timePeriodsPage timePeriodsPage;

    private Map<String, String> contextData;


    public context() {
        this.logger = LoggerFactory.getLogger(context.class);
        this.contextData = new HashMap<>();
        initializeDriver();
    }

    private void initializeDriver(){
        this.driver = webDriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(60));
        this.excelUtils = new excelUtils();
        this.commonUtils = new commonUtils(this);
        this.accountsWdsfPage = new accountsWdsfPage(this);
        this.contactsWdsfPage = new contactsWdsfPage(this);
        this.opportunityWdsfPage = new opportunityWdsfPage(this);
        this.loginPage = new loginPage(this);
        //this.accountsCreationPage=new accountsCreationPage(this);
    }

    public WebDriver getDriver() {
        if (driver == null) {
            driver = webDriverManager.getDriver();
        }
        return driver;
    }

    public void restartDriver(){
        getLogger().info("Restarting WebDriver...");
        tearDown();
        initializeDriver();
        getLogger().info("WebDriver restarted successfully.");
    }

    public loginPage getLoginPage() {
        return loginPage;
    }

    public accountsWdsfPage getAccountsPage() {
        return accountsWdsfPage;
    }

    public contactsWdsfPage getContactsPage() {
        return contactsWdsfPage;
    }

    public opportunityWdsfPage getOpportunityWdsfPage() {
        return opportunityWdsfPage;
    }

    public commonUtils getCommonUtils() {
        return commonUtils;
    }

    public Logger getLogger() {
        return logger;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    /*public accountsCreationPage getAccountsCreationPage() {
        return accountsCreationPage;
    }

    public timePeriodsPage getTimePeriodsPage() {
        return timePeriodsPage;
    }*/

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
}
