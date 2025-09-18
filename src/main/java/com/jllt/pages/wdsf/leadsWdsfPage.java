package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class leadsWdsfPage extends basePage {
    private final JavascriptExecutor executor;

    @FindBy(xpath = "//span[text()='Leads']/parent::a")
    private WebElement leadsTab;

    @FindBy(xpath = "//a[@title='New']")
    private WebElement newLeadsButton;

    @FindBy(xpath = "//span[text()='Is New Account']/preceding-sibling::span")
    private WebElement IsNewAccountRadioButton;

    @FindBy(xpath = "//span[text()='Is Existing Account']/preceding-sibling::span")
    private WebElement IsExistingAccountRadioButton;

    @FindBy(xpath = "//label[text()='First Name']/..//input[@name='FirstName'] | //input[@name='firstName']")
    private WebElement firstName;

    @FindBy(xpath = "//input[@name='LastName'] | //input[@name='lastName']")
    private WebElement lastName;

    @FindBy(xpath = "//input[@name='company'] | //input[@name='Company']")
    private WebElement isNewAccountTxtBox;

    @FindBy(xpath = "//label[text()='Account Name']/..//input")
    private WebElement accountLookupInput;

    @FindBy(xpath = "//input[@name='Phone']")
    private WebElement phone;

    @FindBy(xpath = "//input[@name='Email']")
    private WebElement email;

    @FindBy(xpath = "//input[@name='MobilePhone']")
    private WebElement mobile;

    @FindBy(xpath = "//button[@name='LeadSource'] | //button[@aria-label='Lead Source']")
    private WebElement leadSourceDropdown;

    @FindBy(xpath = "//button[@aria-label='Lead Sub Source'] | (//button[@aria-label='Lead Sub Source'])[2]")
    private WebElement leadSubSourceDropdown;

    @FindBy(xpath = "//span[text()='Lead Owner']/parent::div/following-sibling::div//a//span[text()]")
    private WebElement leadOwner;

    @FindBy(xpath = "//button[@name='save'] | //button[@name='SaveEdit']")
    private WebElement saveButton;

    @FindBy(xpath = "//span[text()='Edit Lead Source']/parent::button[@title='Edit Lead Source']")
    private WebElement editLeadSourceBtn;

    @FindBy(xpath = "//button[@name='Region__c']")
    private WebElement region;

    @FindBy(xpath = "//records-lwc-highlights-panel")
    private WebElement highlightsPanel;

    @FindBy(xpath = "//records-highlights-details-item/div")
    private List<WebElement> highlightsItemsIntuitLead;

    @FindBy(xpath = "//div[@data-aura-class='oneRecordHomeFlexipage2Wrapper']//ul[@aria-label='Path Options']/li/a/span[text()]")
    private List<WebElement> statusPathStages;

    public leadsWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    public void navigateToLeadsTab() throws InterruptedException {
        testContext.getLogger().info("Attempting to click on New Leads Tab");
        testContext.getDriver().switchTo().defaultContent();
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        testContext.getCommonUtils().waitForPageLoad();
        testContext.getGenericWdsfPage().waitForToastMessageToDisappear();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(leadsTab));

        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", leadsTab);
        } catch (Exception e) {
            testContext.getLogger().warn("JS click failed, trying direct click: {}", e.getMessage());
            testContext.getCommonUtils().retryingClick(leadsTab, 3);
        }
        Thread.sleep(2000);
        testContext.getLogger().info("Clicked on Leads Tab");
        testContext.getCommonUtils().waitForPageLoad();
        Thread.sleep(1000);
    }

    public void clickNewLeads(String recordType) throws InterruptedException {
        testContext.getLogger().info("Clicking New Leads button and selecting record type: {}", recordType);

        wait.until(ExpectedConditions.elementToBeClickable(newLeadsButton));
        Thread.sleep(1000);
        testContext.getCommonUtils().retryingClick(newLeadsButton,3);
        testContext.getLogger().info("Clicked on New Lead button");
        Thread.sleep(500);

        if (testContext.getAccountsWdsfPage().isRecordTypeSelectionPresent()) {
            if (recordType != null && !recordType.isEmpty()) {
                selectLeadsRecordType(recordType);
            }
        } else {
            testContext.getLogger().info("Record type selection screen not shown - continuing with leads creation flow");
        }
        Thread.sleep(500);
    }

    private void selectLeadsRecordType(String recordType) throws InterruptedException {
        testContext.getLogger().info("Attempting to select record type: {}", recordType);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='changeRecordTypeRow']")));
            Thread.sleep(5000);
            WebElement recordTypeElement = driver.findElement(By.xpath("//div[@class='changeRecordTypeRow']//../following-sibling::div/span[text()='" + recordType + "'] | //div[@class='changeRecordTypeRow']//../lightning-input//span[text()='" + recordType + "']"));

            wait.until(ExpectedConditions.elementToBeClickable(recordTypeElement));
            executor.executeScript("arguments[0].click();", recordTypeElement);
            testContext.getLogger().info("Selected record type: {}", recordType);

            // Verify Intuit FSM description before proceeding
            if ("Intuit FSM".equalsIgnoreCase(recordType)) {
                By intuitDescBy = By.xpath("//span[text()='Intuit FSM']/../div[contains(@class,'changeRecordTypeItemDescription')]");
                wait.until(ExpectedConditions.visibilityOfElementLocated(intuitDescBy));
                String actual = driver.findElement(intuitDescBy).getText().trim();
                String expected = "Leads that have signed up for Intuit FSM.";
                testContext.getLogger().info("Intuit FSM description: {}", actual);
                if (!actual.equals(expected)) {
                    throw new AssertionError("Intuit FSM description mismatch. Expected: " + expected + " but found: " + actual);
                }
            }

            WebElement nextButton = driver.findElement(By.xpath("//button/span[text()='Next']"));
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            executor.executeScript("arguments[0].click();", nextButton);
            testContext.getLogger().info("Clicked Next button after record type selection");

            Thread.sleep(500);
        } catch (Exception e) {
            testContext.getLogger().error("Error selecting record type: {}", e.getMessage());
            throw e;
        }
    }

    public void selectAccountOption(String accountType, String recordType) {
        if (recordType != null && recordType.equalsIgnoreCase("Intuit FSM")) {
            //no account selection radio button to select for Intuit FSM Lead
            testContext.getLogger().info("Intuit Lead record type: No radio button to select, skipping selection.");
        } else {
            if (accountType.equalsIgnoreCase("Is New Account")) {
                wait.until(ExpectedConditions.visibilityOf(IsNewAccountRadioButton));
                executor.executeScript("arguments[0].click();", IsNewAccountRadioButton);
                testContext.getLogger().info("Is New Account radio button is selected: {}", IsNewAccountRadioButton);
            } else {
                wait.until(ExpectedConditions.visibilityOf(IsExistingAccountRadioButton));
                executor.executeScript("arguments[0].click();", IsExistingAccountRadioButton);
                testContext.getLogger().info("Is Existing Account radio button is selected: {}", IsExistingAccountRadioButton);
            }
        }
    }

    private String handleAccountField(Map<String, String> data, String recordType, String accountType, String randomSuffix) throws InterruptedException {
        String accountName;
        if (accountType.equalsIgnoreCase("Is New Account") || recordType.equalsIgnoreCase("Intuit FSM")) {
            // Use a default if Account Name is missing for Intuit FSM
            String baseAccountName = data.get("Account Name");
            if (baseAccountName == null || baseAccountName.trim().isEmpty()) {
                baseAccountName = recordType.equalsIgnoreCase("Intuit FSM") ? "IntuitAccount" : "LeadAccount";
            }
            accountName = baseAccountName + "_" + randomSuffix;
            if (isNewAccountTxtBox.isEnabled()) {
                fillField(isNewAccountTxtBox, accountName, "Account Name");
            }
            testContext.setContextData("accountName", accountName);
        } else {
            accountName = (String) testContext.getContextData("accountName");
            if (accountName == null || accountName.trim().isEmpty()) {
                throw new IllegalStateException("No accountName found in context for 'Is Existing Account'. Ensure the account creation step ran and stored the randomized account name.");
            }
            testContext.getCommonUtils().waitForModalToDisappear();
            if (accountLookupInput.isEnabled()) {
                testContext.getLogger().info("Account name for Is Existing Account- {}", accountName);
                testContext.getGenericWdsfPage().selectAccount(accountName);
            } else {
                testContext.getLogger().warn("Account lookup input is not enabled!");
            }
        }
        return accountName;
    }

    /*public void fillLeadForm(Map<String, String> data) throws InterruptedException {
        testContext.getLogger().info("Filling New Lead Details");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        testContext.getLogger().info("Before updateAccountNameForLeadType: Lead Type = {}, Account Name = {}",
                testContext.getContextData("Lead Type"),
                testContext.getContextData("accountName"));

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);
        String randomizedLastName = data.get("Last Name") + "_" + randomSuffix;
        testContext.getLogger().info("randomized Last Name: {}", randomizedLastName);

        Thread.sleep(2000);
        if (firstName.isDisplayed()){
            fillField(firstName, data.get("First Name"), "First Name");
        }
        else {
            fillField(firstName, data.get("First Name"), "First Name");
        }
        //fillField(firstName, data.get("First Name"), "First Name");
        fillField(lastName, randomizedLastName, "Last Name");

        //Storing recordType and accountType
        String recordType = testContext.getContextData("currentRecordType") != null
                ? testContext.getContextData("currentRecordType").toString().trim()
                : "";
        testContext.getLogger().info("Current Record Type: {}", recordType);

        String accountType = testContext.getContextData("Lead Type") != null
                ? testContext.getContextData("Lead Type").toString()
                : "Is New Account";
        testContext.getLogger().info("Current Account Type: {}", accountType);

        //Handle Account Name based on Record Type and Account Type
        String accountName = handleAccountField(data, recordType, accountType, randomSuffix);

        //Select Phone, Email, and Mobile
        fillField(phone, data.get("Phone"), "Phone");
        fillField(email, data.get("Email"), "Email");
        fillField(mobile, data.get("Mobile"), "Mobile");
        Thread.sleep(1000);

        // Select Region (robust combobox selection scoped to Region field)
        if (testContext.getCommonUtils().isElementDisplayed(region)){
            wait.until(ExpectedConditions.elementToBeClickable(region));
            executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", region);
            executor.executeScript("arguments[0].click();", region);
            Thread.sleep(500);
            String regionVal = data.get("Region");
            try {
                // Scope selection to the dropdown list associated with Region combobox
                WebElement option = driver.findElement(By.xpath(
                        "//button[@name='Region__c']/ancestor::lightning-combobox//lightning-base-combobox-item//span[@title='" + regionVal + "']"
                ));
                executor.executeScript("arguments[0].click();", option);
                testContext.getLogger().info("Selected Region on Lead form: {}", regionVal);
            } catch (Exception e) {
                // Fallback: generic dropdown selection
                testContext.getLogger().warn("Scoped Region selection failed, falling back. Reason: {}", e.getMessage());
                testContext.getGenericWdsfPage().selectDropDown(regionVal, "Region");
            }
            Thread.sleep(300);
        } else {
            testContext.getLogger().info("Region combobox not displayed on Lead form. Skipping selection.");
        }

        //Select Lead Source
        wait.until(ExpectedConditions.elementToBeClickable(leadSourceDropdown));
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", leadSourceDropdown);
        leadSourceDropdown.click();
        testContext.getGenericWdsfPage().selectDropDown(data.get("Lead Source"), "Lead Source");
        Thread.sleep(200);

        //Select Lead Sub Source
        if (testContext.getCommonUtils().isElementDisplayed(leadSubSourceDropdown)){
            wait.until(ExpectedConditions.elementToBeClickable(leadSubSourceDropdown));
            executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", leadSubSourceDropdown);
            //leadSubSourceDropdown.click();
            executor.executeScript("arguments[0].click();", leadSubSourceDropdown);
            testContext.getGenericWdsfPage().selectDropDown(data.get("Lead Sub Source"), "Lead Sub Source");
            Thread.sleep(200);
        }
        setEntityContextData(data, randomizedLastName, accountName);
    }*/

    public void fillLeadForm(Map<String, String> data) throws InterruptedException {
        testContext.getLogger().info("Filling New Lead Details");
        testContext.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        testContext.getLogger().info("Before updateAccountNameForLeadType: Lead Type = {}, Account Name = {}",
                testContext.getContextData("Lead Type"),
                testContext.getContextData("accountName"));

        String randomSuffix = testContext.getCommonUtils().generateRandomChars(6);
        String randomizedLastName = data.get("Last Name") + "_" + randomSuffix;
        testContext.getLogger().info("randomized Last Name: {}", randomizedLastName);

        Thread.sleep(2000);
        if (firstName.isDisplayed()){
            fillField(firstName, data.get("First Name"), "First Name");
        }
        else {
            fillField(firstName, data.get("First Name"), "First Name");
        }
        fillField(lastName, randomizedLastName, "Last Name");

        //Storing recordType and accountType
        String recordType = testContext.getContextData("currentRecordType") != null
                ? testContext.getContextData("currentRecordType").toString().trim()
                : "";
        testContext.getLogger().info("Current Record Type: {}", recordType);

        String accountType = testContext.getContextData("Lead Type") != null
                ? testContext.getContextData("Lead Type").toString()
                : "Is New Account";
        testContext.getLogger().info("Current Account Type: {}", accountType);

        //Handle Account Name based on Record Type and Account Type
        String accountName = handleAccountField(data, recordType, accountType, randomSuffix);

        // Randomize Phone, Mobile, and Email and store in context
        String randPhone = testContext.getCommonUtils().generateRandomUSPhone();
        String randMobile = testContext.getCommonUtils().generateRandomUSPhone();
        String randEmail = testContext.getCommonUtils().generateRandomEmail(data.get("Email"), randomSuffix);

        fillField(phone, randPhone, "Phone");
        fillField(email, randEmail, "Email");
        fillField(mobile, randMobile, "Mobile");

        testContext.setContextData("LeadPhone", randPhone);
        testContext.setContextData("LeadMobile", randMobile);
        testContext.setContextData("LeadEmail", randEmail);
        Thread.sleep(1000);

        // Select Region (robust combobox selection scoped to Region field)
        if (testContext.getCommonUtils().isElementDisplayed(region)){
            wait.until(ExpectedConditions.elementToBeClickable(region));
            executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", region);
            executor.executeScript("arguments[0].click();", region);
            Thread.sleep(500);
            String regionVal = data.get("Region");
            try {
                // Scope selection to the dropdown list associated with Region combobox
                WebElement option = driver.findElement(By.xpath(
                        "//button[@name='Region__c']/ancestor::lightning-combobox//lightning-base-combobox-item//span[@title='" + regionVal + "']"
                ));
                executor.executeScript("arguments[0].click();", option);
                testContext.getLogger().info("Selected Region on Lead form: {}", regionVal);
            } catch (Exception e) {
                // Fallback: generic dropdown selection
                testContext.getLogger().warn("Scoped Region selection failed, falling back. Reason: {}", e.getMessage());
                testContext.getGenericWdsfPage().selectDropDown(regionVal, "Region");
            }
            Thread.sleep(300);
        } else {
            testContext.getLogger().info("Region combobox not displayed on Lead form. Skipping selection.");
        }

        //Select Lead Source
        wait.until(ExpectedConditions.elementToBeClickable(leadSourceDropdown));
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", leadSourceDropdown);
        leadSourceDropdown.click();
        testContext.getGenericWdsfPage().selectDropDown(data.get("Lead Source"), "Lead Source");
        Thread.sleep(200);

        //Select Lead Sub Source
        if (testContext.getCommonUtils().isElementDisplayed(leadSubSourceDropdown)){
            wait.until(ExpectedConditions.elementToBeClickable(leadSubSourceDropdown));
            executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", leadSubSourceDropdown);
            executor.executeScript("arguments[0].click();", leadSubSourceDropdown);
            testContext.getGenericWdsfPage().selectDropDown(data.get("Lead Sub Source"), "Lead Sub Source");
            Thread.sleep(200);
        }
        setEntityContextData(data, randomizedLastName, accountName);
    }

    private void fillField(WebElement field, String value, String fieldName) {
        if (field.isEnabled()) {
            wait.until(ExpectedConditions.visibilityOf(field)).clear();
            field.sendKeys(value);
            testContext.getLogger().info("Entered {}: {}", fieldName, value);
        } else {
            testContext.getLogger().info("Skipped {} as it is disabled", fieldName);
        }
    }

    private void setEntityContextData(Map<String, String> data, String randomizedLastName, String accountName) {
        testContext.setContextData("LeadName", data.get("First Name") + " " + randomizedLastName);
        testContext.setContextData("accountName", accountName);
    }

    public boolean isSelectedAccountFieldEnabled(String leadType) {
        if (leadType.equalsIgnoreCase("Is New Account")) {
            return isNewAccountTxtBox.isEnabled() && !accountLookupInput.isEnabled();
        } else if (leadType.equalsIgnoreCase("Is Existing Account")) {
            return accountLookupInput.isEnabled() && !isNewAccountTxtBox.isEnabled();
        }
        return false;
    }

    public void saveLead() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        Thread.sleep(500);
        testContext.getLogger().info("Clicked Save button");
    }

    public void verifyLeadLandingPageTitle() throws InterruptedException {
        Thread.sleep(1000);
        String expectedLeadName = String.valueOf(testContext.getContextData("LeadName"));
        String expectedTitle = expectedLeadName + " | Lead | Salesforce";

        wait.until(ExpectedConditions.titleContains(expectedLeadName));
        String actualTitle = driver.getTitle();

        testContext.getLogger().info("Expected Page Title: {}", expectedTitle);
        testContext.getLogger().info("Actual Page Title: {}", actualTitle);

        assert actualTitle != null;
        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Lead landing page title mismatch. Expected: " + expectedTitle + ", but got: " + actualTitle);
        }
        testContext.getLogger().info("Lead landing page title verified successfully.");
        Thread.sleep(1200);
    }

    // For Lead Owner
    public String getLeadOwnerValue() {
        /*String xpath = "//span[text()='Lead Owner']/parent::div/following-sibling::div//a/span";
        WebElement element = driver.findElement(By.xpath(xpath));*/
        testContext.getLogger().info("Get text for Lead Owner - {}",leadOwner.getText().trim());
        return leadOwner.getText().trim();
    }

    // Select Lead Source and verify Lead Sub Source dropdown options
    public List<String> verifyLeadSubSourceDropdownValues(String leadSource) throws InterruptedException {
        // Click the edit button for Lead Source
        testContext.getCommonUtils().scrollToEndOfPageUsingKeys();
        Thread.sleep(500);
        executor.executeScript("arguments[0].click();", editLeadSourceBtn);
        Thread.sleep(1000);

        // Try to find all possible Lead Source dropdowns/buttons
        List<WebElement> leadSourceButtons = driver.findElements(By.xpath(
                "//button[@name='LeadSource'] | //button[@aria-label='Lead Source'] | (//button[contains(@title,'Lead Source')])"
        ));
        testContext.getLogger().info("Found {} Lead Source button(s) after clicking edit.", leadSourceButtons.size());
        for (int i = 0; i < leadSourceButtons.size(); i++) {
            testContext.getLogger().info("Lead Source button {}: {}", i, leadSourceButtons.get(i).getAttribute("outerHTML"));
        }

        WebElement leadSourceToClick = null;
        if (leadSourceButtons.size() > 1) {
            leadSourceToClick = leadSourceButtons.get(1); // Global Lead
        } else if (leadSourceButtons.size() == 1) {
            leadSourceToClick = leadSourceButtons.getFirst(); // Intuit Lead
        } else {
            // Try fallback: look for a combobox or input
            List<WebElement> comboBoxes = driver.findElements(By.xpath(
                    "//div[@role='combobox' and contains(@aria-label,'Lead Source')] | //input[contains(@aria-label,'Lead Source')]"
            ));
            if (!comboBoxes.isEmpty()) {
                leadSourceToClick = comboBoxes.getFirst();
            }
        }

        if (leadSourceToClick == null) {
            throw new RuntimeException("No Lead Source button or combobox found after clicking edit!");
        }

        wait.until(ExpectedConditions.elementToBeClickable(leadSourceToClick));
        executor.executeScript("arguments[0].click();", leadSourceToClick);
        Thread.sleep(1000);
        testContext.getGenericWdsfPage().selectDropDown(leadSource, "Lead Source");
        Thread.sleep(1000);

        //Click on Lead Sub Source dropdown
        wait.until(ExpectedConditions.elementToBeClickable(leadSubSourceDropdown));
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", leadSubSourceDropdown);
        Thread.sleep(300);
        executor.executeScript("arguments[0].click();", leadSubSourceDropdown);
        Thread.sleep(500);

        // Get all options from Lead Sub Source dropdown
        List<WebElement> options = driver.findElements(By.xpath("//button[@aria-label='Lead Sub Source']/parent::div/..//lightning-base-combobox-item//span[text()]"));
        List<String> actualOptions = options.stream().map(e -> e.getText().trim()).collect(Collectors.toList());
        leadSubSourceDropdown.click();

        return actualOptions;
    }

    public String getLeadSourceFieldValue() throws InterruptedException {
        // Find all matching elements
        testContext.getCommonUtils().scrollToEndOfPageUsingKeys();
        List<WebElement> elements = driver.findElements(By.xpath("//span[text()='Lead Source']/parent::div/..//lightning-formatted-text"));
        if (elements.size() > 1) {
            // Global Lead: second occurrence
            testContext.getLogger().info("Second occurrence for Lead Source. {}",elements.get(1).getText().trim());
            return elements.get(1).getText().trim();
        } else if (elements.size() == 1) {
            // Intuit Lead: first/only occurrence
            testContext.getLogger().info("first/only occurrence for Lead Source {}",elements.get(0).getText().trim());
            return elements.get(0).getText().trim();
        } else {
            throw new RuntimeException("Lead Source field not found!");
        }
    }

    public String getLeadSubSourceFieldValue() {
        List<WebElement> elements = driver.findElements(By.xpath("//span[text()='Lead Sub Source']/parent::div/..//lightning-formatted-text"));
        if (elements.size() > 1) {
            testContext.getLogger().info("Second occurrence for Lead Sub Source. {}",elements.get(1).getText().trim());
            return elements.get(1).getText().trim();
        } else if (elements.size() == 1) {
            testContext.getLogger().info("first/only occurrence for Lead Sub Source {}",elements.get(0).getText().trim());
            return elements.get(0).getText().trim();
        } else {
            throw new RuntimeException("Lead Sub Source field not found!");
        }
    }

    public List<String> verifyHighlightsLabels(List<String> labels) {
        wait.until(ExpectedConditions.visibilityOfAllElements(highlightsItemsIntuitLead));

        Set<String> present = new HashSet<>();
        for (WebElement item : highlightsItemsIntuitLead) {
            for (WebElement p : item.findElements(By.xpath(".//p[normalize-space()]"))) {
                testContext.getLogger().info("Highlights panel item found: {}", p.getText().trim());
                present.add(p.getText().trim());
            }
        }

        List<String> missing = new ArrayList<>();
        for (String raw : labels) {
            String label = raw.trim();
            if (label.equalsIgnoreCase("Owner")) label = "Lead Owner";
            if (!present.contains(label)) missing.add(raw);
        }
        return missing;
    }

    public List<String> getStatusPathStages() {
        wait.until(ExpectedConditions.visibilityOfAllElements(statusPathStages));
        return statusPathStages.stream().map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    // Full verification for Status Path stages
    public void verifyStatusPathEquals(List<String> expected) {
        List<String> actual = getStatusPathStages();
        testContext.getLogger().info("Expected Lead Status Path: {}", expected);
        testContext.getLogger().info("Actual Lead Status Path: {}", actual);
        if (!actual.equals(expected)) {
            throw new AssertionError("Status Path labels mismatch! Expected: " + expected + ", Actual: " + actual);
        }
    }
}
