package com.jllt.base;

import com.jllt.scenarioContext.context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;


public abstract class basePage {
    protected WebDriver driver;
    protected Logger logger;
    protected context testContext;
    protected WebDriverWait wait;

    public basePage(context testContext){
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.logger = testContext.getLogger();
        this.wait = testContext.getWait();
        PageFactory.initElements(driver, this);
    }
}
