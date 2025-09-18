package com.jllt.pages.wdsf;

import com.jllt.base.basePage;
import com.jllt.scenarioContext.context;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.Map;

public class leadConversionWdsfPage extends basePage {
    private final JavascriptExecutor executor;

    @FindBy(xpath = "//button[text()='Convert Lead']")
    private WebElement convertLeadButtonOnLeadPage;

    @FindBy(xpath = "//span[text()='Converted Status']/parent::label/..//select[@name='status']")
    private WebElement convertedStatusDropdown;

    @FindBy(xpath = "//span[text()='Converted Opportunity Region']/parent::label/..//select[@name='oppregion']")
    private WebElement convertedRegionDropdown;

    @FindBy(xpath = "//span[text()='Converted Opportunity Business Group']/parent::label/..//select[@name='oppregion']")
    private WebElement convertedBusinessGroupDropdown;

    @FindBy(xpath = "//button[@title='Next']")
    private WebElement nextButton;

    @FindBy(xpath = "//div[@class='quick-actions-panel']")
    private WebElement convertLeadDialogPanel;

    @FindBy(xpath = "//button[text()='Convert Lead']")
    private WebElement convertLeadButton;

    @FindBy(xpath = "//button[@title='Convert']")
    private WebElement convertButton;

    @FindBy(xpath = "//span[text()='Selected Account']/..//input")
    private WebElement selectedAccountInput;

    @FindBy(xpath = "//div[@data-aura-class='cWDLeadConversionContactScreen']//a")
    private WebElement contactSectionExpand;

    @FindBy(xpath = "//input[@type='radio' and @name='contact']/parent::span//span[text()='Create New']/../../input")
    private WebElement contactCreateNewRadio;

    @FindBy(xpath = "//input[@type='radio' and @name='contact']/parent::span//span[text()='Choose Existing']/../../input")
    private WebElement contactChooseExistingRadio;

    @FindBy(xpath = "(//label[text()='First Name']/..//input[@name='FirstName'])[2]")
    private WebElement contactFirstNameInput;

    @FindBy(xpath = "(//label[text()='Last Name']/..//input[@name='LastName'])[2]")
    private WebElement contactLastNameInput;

    @FindBy(xpath = "(//label[text()='Middle Name']/..//input[@name='MiddleName'])[2]")
    private WebElement contactMiddleNameInput;

    @FindBy(xpath = "//span[text()='Opportunity Division']/../..//select")
    private WebElement opportunityDivisionDropdown;

    @FindBy(xpath = "//span[text()='Region']/../..//select")
    private WebElement opportunityRegionDropdown;

    @FindBy(xpath = "//input[@name='opportunityDate']")
    private WebElement closeDateDatePicker;

    @FindBy(xpath = "//label[text()='Future Opportunity Owner']/following-sibling::div/input")
    private WebElement futureOpportunityOwner;

    @FindBy(xpath = "//label[text()='Opportunity Name']/following-sibling::div/input")
    private WebElement opportunityNameInput;

    @FindBy(xpath = "//label[text()='Opportunity Close Date']/following-sibling::div/input")
    private WebElement opportunityCloseDateInput;

    @FindBy(xpath = "//span[text()='Region']/../..//select")
    private WebElement opportunityRegionDropDown;

    @FindBy(xpath = "//span[text()='Opportunity Division']/../..//select")
    private WebElement opportunityDivisionDropDown;

    @FindBy(xpath = "//span[text()='Opportunity Currency']/../..//select")
    private WebElement opportunityCurrencyDropDown;

    @FindBy(xpath = "//div[@data-key='error']//span[contains(@class,'toastMessage') and contains(text(),'You must fill in the Pain, Budget, & Decision Process fields')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//span[text()='Opportunity Currency']/../..//select")
    private WebElement cancelButtonOnLeadConvertDialog;

    @FindBy(xpath = "//li[contains(@data-target-selection-name,'Lead.Edit')]//button[text()='Edit']")
    private WebElement EditButtonOnLeadLandingPage;

    @FindBy(xpath = "//label[text()='Pain']//following-sibling::div/textarea")
    private WebElement painTextArea;

    @FindBy(xpath = "//label[text()='Budget']//following-sibling::div/textarea")
    private WebElement budgetTextArea;

    @FindBy(xpath = "//label[text()='Decision Process']//following-sibling::div/textarea")
    private WebElement decisionProcessTextArea;

    @FindBy(xpath = "(//label[text()='First Name']/..//input[@name='FirstName'])[1]")
    private WebElement contactFirstNameInputIntuit;

    @FindBy(xpath = "(//label[text()='First Name']/..//input[@name='FirstName'])[2]")
    private WebElement contactFirstNameInputGlobal;

    @FindBy(xpath = "(//label[text()='Last Name']/..//input[@name='LastName'])[1]")
    private WebElement contactLastNameInputIntuit;

    @FindBy(xpath = "(//label[text()='Last Name']/..//input[@name='LastName'])[2]")
    private WebElement contactLastNameInputGlobal;

    @FindBy(xpath = "(//label[text()='Middle Name']/..//input[@name='MiddleName'])[1]")
    private WebElement contactMiddleNameInputIntuit;

    @FindBy(xpath = "(//label[text()='Middle Name']/..//input[@name='MiddleName'])[2]")
    private WebElement contactMiddleNameInputGlobal;

    @FindBy(xpath = "//div[contains(@class,'cWDLeadConvertSuccess')]/p")
    private WebElement leadConvertedHeading;

    // Card primary links (Account, Contact, Opportunity in order)
    @FindBy(xpath = "//div[contains(@class,'records-section')]/div/div//ul/a")
    private List<WebElement> conversionCardLinks;

    @FindBy(xpath = "//button[normalize-space()='Go to Leads']")
    private WebElement goToLeadsButton;

    public leadConversionWdsfPage(context testContext) {
        super(testContext);
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.executor = (JavascriptExecutor) driver;
        this.wait = testContext.getWait();
    }

    // Helper to create a lead using the existing page object
    public void createLead(Map<String, String> leadDetails, String recordType, String accountType) throws InterruptedException {
        testContext.getLogger().info("Starting Lead creation helper method");
        testContext.getLeadsWdsfPage().navigateToLeadsTab();
        testContext.getLeadsWdsfPage().clickNewLeads(recordType);
        // Ensure downstream logic knows current record type and lead type
        testContext.setContextData("currentRecordType", recordType);
        testContext.setContextData("Lead Type", accountType);
        testContext.getLeadsWdsfPage().selectAccountOption(accountType, recordType);
        testContext.getLeadsWdsfPage().fillLeadForm(leadDetails);
        testContext.getLeadsWdsfPage().saveLead();
        testContext.getLeadsWdsfPage().verifyLeadLandingPageTitle();
        testContext.getLogger().info("Lead created successfully via helper method");
    }

    // Click Convert Lead button
    public void clickConvertLeadButton() throws InterruptedException {
        Thread.sleep(2000); // Wait for the page to load
        wait.until(ExpectedConditions.elementToBeClickable(convertLeadButtonOnLeadPage));
        testContext.getCommonUtils().jsClickToElement(convertLeadButtonOnLeadPage);
        testContext.getLogger().info("Clicked Convert Lead button");
        Thread.sleep(1000);
    }

    // Wait for the Convert Lead dialog to appear (by waiting for any dropdown)
    public void waitForConvertLeadDialog() throws InterruptedException {
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(convertedStatusDropdown));
        testContext.getLogger().info("Convert Lead dialog is displayed");
    }

    // Use generic page for dropdown value verification
    public String getSelectedDropdownValue(String label) {
        return testContext.getGenericWdsfPage().getSelectedDropdownValue(label);
    }

    // Click Next on the dialog
    public void clickNextOnConvertLeadDialog() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
        testContext.getLogger().info("Clicked Next on Convert Lead dialog");
    }

    // For radio buttons by label and option
    public void selectRadioButtonOnConvertLeadDialog(String section, String option) throws InterruptedException {
        Thread.sleep(1000);
        String name = section.equalsIgnoreCase("Contact") ? "contact" : "Opportunity";
        String xpath = String.format("//input[@type='radio' and @name='%s']/parent::span//span[text()='%s']", name, option);
        testContext.getLogger().info("Selecting radio button: {}", xpath);
        WebElement radio = driver.findElement(By.xpath(xpath));
        if (!radio.isSelected()) {
            //testContext.getCommonUtils().jsClickToElement(radio);
            testContext.getCommonUtils().clickUsingKeys(radio);
        }else {
            testContext.getCommonUtils().clickUsingKeys(radio);
        }
    }

    // For expanding sections
    public void expandSectionOnLeadConvertDialog(String section) throws InterruptedException {
        // Ensure any overlay/spinner is gone before interacting
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        Thread.sleep(300);

        String sectionXpath;
        WebElement targetField;
        if (section.equalsIgnoreCase("Contact")) {
            sectionXpath = "//div[@data-aura-class='cWDLeadConversionContactScreen']//a";
            targetField = getContactFirstNameInput();
        } else if (section.equalsIgnoreCase("Opportunity")) {
            sectionXpath = "//div[@data-aura-class='cWDLeadConversionOpportunityScreen']//a";
            targetField = opportunityNameInput;
        } else {
            throw new IllegalArgumentException("Unknown section: " + section);
        }

        // If already expanded/visible, skip
        if (testContext.getCommonUtils().isElementDisplayed(targetField)) {
            testContext.getLogger().info("{} section already expanded", section);
            return;
        }

        WebElement expandLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sectionXpath)));
        try {
            testContext.getCommonUtils().jsScrollToElement(expandLink);
        } catch (Exception ignored) {}

        // Prefer JS click to avoid overlay/hitbox issues; fall back to retrying click
        try {
            testContext.getCommonUtils().jsClickToElement(expandLink);
        } catch (Exception e) {
            testContext.getCommonUtils().retryingClick(expandLink, 3);
        }

        // Wait for spinner/overlay to clear and target input to be visible
        testContext.getCommonUtils().waitForSpinnerToDisappear();
        wait.until(ExpectedConditions.visibilityOf(targetField));

        testContext.getLogger().info("Expanded '{}' section on Convert Lead dialog", section);
        Thread.sleep(300);
    }

    // For selected account
    public String getSelectedAccount() {
        String[] xpaths = new String[] {
                "//Span[normalize-space()='Selected Account']/following-sibling::div//input"
        };

        for (String xp : xpaths) {
            try {
                WebElement el = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.xpath(xp)));
                String val = el.getAttribute("value");
                if (val != null && !val.trim().isEmpty()) {
                    return val.trim();
                }
            } catch (Exception ignored) { }
        }

        try {
            WebElement textEl = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[normalize-space()='Selected Account']/parent::label/..//lightning-formatted-text | //span[normalize-space()='Account']/parent::label/..//lightning-formatted-text")
            ));
            return textEl.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Selected Account field not found on Convert Lead page using known locators");
        }
    }

    // For Future Opportunity Owner
    public String getFutureOpportunityOwner() {
        testContext.getLogger().info("Getting Future Opportunity Owner value - {}:",futureOpportunityOwner.getAttribute("value"));
        return futureOpportunityOwner.getAttribute("value");
        //return driver.findElement(By.xpath("//label[text()='Future Opportunity Owner']/following-sibling::div/input")).getAttribute("value");
    }

    // For Convert Lead dialog presence
    public boolean isConvertLeadDialogAvailable() throws InterruptedException {
        Thread.sleep(2000);
        return driver.findElement(By.xpath("//div[@class='quick-actions-panel']")).isDisplayed();
    }

    // For clicking Convert button
    public void clickConvertButton() throws InterruptedException {
        testContext.getCommonUtils().jsClickToElement(convertButton);
        Thread.sleep(2000);
    }

    public void setOpportunityDivision(String value) {
        new Select(opportunityDivisionDropdown).selectByVisibleText(value);
    }

    public void verifyContactSectionFields(List<Map<String, String>> fields) throws InterruptedException {
        String leadName = (String) testContext.getContextData("LeadName"); // e.g., "Test_Lead_D13WJN"
        String expectedFirstName = null;

        for (Map<String, String> field : fields) {
            String fieldName = field.get("Field Name");
            String expectedValue = field.get("Expected Value");

            switch (fieldName) {
                case "First Name":
                    expectedFirstName = expectedValue;
                    WebElement firstNameEl = getContactFirstNameInput();
                    String actualFirstName = firstNameEl.getAttribute("value");
                    if (!expectedFirstName.equals(actualFirstName)) {
                        throw new AssertionError("First Name mismatch: expected " + expectedFirstName + ", got " + actualFirstName);
                    }
                    testContext.getLogger().info("First Name verified: {}", actualFirstName);
                    break;
                case "Last Name":
                    // Remove FirstName and underscore to get randomized last name
                    String randomizedLastName = leadName.substring(expectedFirstName.length() + 1);
                    WebElement lastNameEl = getContactLastNameInput();
                    String actualLastName = lastNameEl.getAttribute("value");
                    if (!randomizedLastName.equals(actualLastName)) {
                        throw new AssertionError("Last Name mismatch: expected " + randomizedLastName + ", got " + actualLastName);
                    }
                    testContext.getLogger().info("Last Name verified: {}", actualLastName);
                    break;
                case "Middle Name":
                    WebElement middleNameEl = getContactMiddleNameInput();
                    String actualMiddleName = middleNameEl.getAttribute("value");
                    if (actualMiddleName != null && !actualMiddleName.isEmpty()) {
                        throw new AssertionError("Middle Name should be blank initially, but was: " + actualMiddleName);
                    }
                    testContext.getLogger().info("Middle Name is blank as expected");
                    wait.until(ExpectedConditions.elementToBeClickable(middleNameEl));
                    Thread.sleep(500);
                    testContext.getGenericWdsfPage().setValueToTextField(middleNameEl, expectedValue);
                    String[] parts = String.valueOf(leadName).split("\\s+", 2);
                    String LastName_rand = parts.length > 1 ? parts[1] : "";
                    String first = getContactFirstNameInput().getAttribute("value");
                    String fullContactName = (first + " " + expectedValue + " " + LastName_rand).trim();
                    testContext.setContextData("contactName", fullContactName);
                    testContext.getLogger().info("Full Contact Name set in context: {}", fullContactName);
                    Thread.sleep(1000);
                    break;
            }
        }
    }

    public void verifyOpportunitySectionFields(List<Map<String, String>> fields) throws InterruptedException {
        String expectedRegion = null;
        String expectedDivision = null;
        String expectedCloseDate = null;

        for (Map<String, String> field : fields) {
            String fieldName = field.get("Field Name");
            String expectedValue = field.get("Expected Value");

            switch (fieldName) {
                case "Opportunity Name":
                    String actualOppName = opportunityNameInput.getAttribute("value");
                    if (actualOppName != null && !actualOppName.isEmpty()) {
                        throw new AssertionError("Opportunity Name should be empty, but was: " + actualOppName);
                    }
                    testContext.getLogger().info("Opportunity Name is empty as expected");
                    /*String suffix = testContext.getCommonUtils().generateRandomChars(5);
                    String oppName = expectedValue + "_" + suffix;*/
                    Object sufObj = testContext.getContextData("randSuffix");
                    String suffix = sufObj == null ? testContext.getCommonUtils().generateRandomChars(6) : String.valueOf(sufObj);
                    String oppName = expectedValue + "_" + suffix;
                    opportunityNameInput.clear();
                    opportunityNameInput.sendKeys(oppName);
                    testContext.getLogger().info("Opportunity Name: {}", oppName);
                    testContext.setContextData("Opportunity Name", oppName);
                    break;

                case "Opportunity Close Date":
                    String actualCloseDate = opportunityCloseDateInput.getAttribute("value");
                    if (actualCloseDate != null && !actualCloseDate.isEmpty()) {
                        throw new AssertionError("Opportunity Close Date should be empty, but was: " + actualCloseDate);
                    }
                    testContext.getLogger().info("Opportunity Close Date is empty as expected");
                    // Set the close date to today + 2 days
                    String futureDate = testContext.getCommonUtils().getFutureDateFormatted(2);
                    opportunityCloseDateInput.clear();
                    opportunityCloseDateInput.sendKeys(futureDate);
                    testContext.getLogger().info("Set Opportunity Close Date to: {}", futureDate);
                    break;

                case "Region":
                    expectedRegion = expectedValue;
                    Select regionSelect = new Select(opportunityRegionDropdown);
                    String actualRegion = regionSelect.getFirstSelectedOption().getText().trim();
                    if (!expectedRegion.equals(actualRegion)) {
                        throw new AssertionError("Region mismatch: expected " + expectedRegion + ", got " + actualRegion);
                    }
                    if (opportunityRegionDropdown.isEnabled()) {
                        throw new AssertionError("Region dropdown should be disabled");
                    }
                    testContext.getLogger().info("Region is prepopulated and disabled as expected: {}", actualRegion);
                    break;

                case "Opportunity Division":
                    expectedDivision = expectedValue;
                    setOpportunityDivision(expectedDivision);
                    Select divisionSelect = new Select(opportunityDivisionDropdown);
                    String actualDivision = divisionSelect.getFirstSelectedOption().getText().trim();
                    if (!expectedDivision.equals(actualDivision)) {
                        throw new AssertionError("Opportunity Division mismatch: expected " + expectedDivision + ", got " + actualDivision);
                    }
                    testContext.getLogger().info("Opportunity Division set and verified: {}", actualDivision);
                    break;

                case "Opportunity Currency":
                    String actualCurrency = new Select(opportunityCurrencyDropDown).getFirstSelectedOption().getText().trim();
                    if (!expectedValue.equals(actualCurrency)) {
                        throw new AssertionError("Opportunity Currency mismatch: expected " + expectedValue + ", got " + actualCurrency);
                    }
                    testContext.getLogger().info("Opportunity Currency verified: {}", actualCurrency);
                    Thread.sleep(1000);
                    break;
            }
        }
    }

    public String getErrorMessage() {
        try {
            //WebElement errorElement = driver.findElement(By.xpath("//div[contains(@class,'toastMessage')] | //div[contains(@class,'forceToastMessage')] | //div[contains(@class,'slds-notify--toast')]//span"));
            testContext.getLogger().info("Getting error message from element: {}", errorMessage.getText());
            testContext.getLogger().info("Getting error message from element: {}", errorMessage.getText());
            return errorMessage.getText();
        } catch (Exception e) {
            testContext.getLogger().error("Error message element not found", e);
            return "";
        }
    }

    public void clickCancelButtonOnConvertLeadDialog() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cancelButtonOnLeadConvertDialog)).click();
            testContext.getLogger().info("Clicked Cancel button on Convert Lead dialog");
        } catch (Exception e) {
            testContext.getLogger().error("Cancel button not found or not clickable", e);
        }
    }

    public void fillPainBudgetDecisionProcessProcessFields(Map<String, String> data) throws InterruptedException {
        //click on Edit button on Lead Landing page
        Thread.sleep(3000);
        wait.until(ExpectedConditions.elementToBeClickable(EditButtonOnLeadLandingPage));
        if(EditButtonOnLeadLandingPage.isDisplayed()){
            Thread.sleep(500);
            testContext.getCommonUtils().jsScrollToElement(EditButtonOnLeadLandingPage);
            EditButtonOnLeadLandingPage.click();
        }
        else {
            Thread.sleep(500);
            testContext.getCommonUtils().jsScrollToElement(EditButtonOnLeadLandingPage);
            testContext.getCommonUtils().clickOnElementUsingKeys(EditButtonOnLeadLandingPage);
        }
        //testContext.getCommonUtils().jsScrollToElement(EditButtonOnLeadLandingPage);
        //testContext.getCommonUtils().clickOnElementUsingKeys(EditButtonOnLeadLandingPage);

        testContext.getLogger().info("Clicked Edit button on Lead Landing page");
        Thread.sleep(3000);

        // Wait for the Pain, Budget, & Decision Process fields to be visible
        testContext.getCommonUtils().jsScrollToElement(painTextArea);
        wait.until(ExpectedConditions.visibilityOf(painTextArea));
        testContext.getLogger().info("Pain, Budget, & Decision Process fields are visible");

        // Fill in the Pain field
        painTextArea.clear();
        painTextArea.sendKeys(data.get("Pain"));
        testContext.getLogger().info("Pain field filled with: {}", data.get("Pain"));
        Thread.sleep(500);

        // Fill in the Budget field
        budgetTextArea.clear();
        budgetTextArea.sendKeys(data.get("Budget"));
        testContext.getLogger().info("Budget field filled with: {}", data.get("Budget"));
        Thread.sleep(500);

        // Fill in the Decision Process field
        decisionProcessTextArea.clear();
        decisionProcessTextArea.sendKeys(data.get("Decision Process"));
        testContext.getLogger().info("Decision Process field filled with: {}", data.get("Decision Process"));
        Thread.sleep(1000);

        // Click Save to apply changes
        WebElement saveButton = driver.findElement(By.xpath("//li//button[text()='Save']"));
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        //testContext.getCommonUtils().jsClickToElement(saveButton);
        testContext.getCommonUtils().clickUsingKeys(saveButton);
        testContext.getLogger().info("Clicked Save button to apply changes");
        Thread.sleep(2000);

        //navigateToLeadRecordUsingSearch();
    }

    // Resolve inputs based on record type
    private WebElement getContactFirstNameInput() {
        Object rt = testContext.getContextData("currentRecordType");
        String recordType = rt == null ? "" : rt.toString();
        if (recordType.toLowerCase().contains("intuit")) {
            return contactFirstNameInputIntuit;
        }
        return contactFirstNameInputGlobal;
    }

    private WebElement getContactLastNameInput() {
        Object rt = testContext.getContextData("currentRecordType");
        String recordType = rt == null ? "" : rt.toString();
        if (recordType.toLowerCase().contains("intuit")) {
            return contactLastNameInputIntuit;
        }
        return contactLastNameInputGlobal;
    }

    private WebElement getContactMiddleNameInput() {
        Object rt = testContext.getContextData("currentRecordType");
        String recordType = rt == null ? "" : rt.toString();
        if (recordType.toLowerCase().contains("intuit")) {
            return contactMiddleNameInputIntuit;
        }
        return contactMiddleNameInputGlobal;
    }

    public void waitForLeadConvertedLanding() throws InterruptedException {
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOf(leadConvertedHeading));
        testContext.getLogger().info("Lead Converted landing page is visible");
    }

    public void verifyConvertedCardsOnLanding() {
        wait.until(ExpectedConditions.visibilityOfAllElements(conversionCardLinks));

        Object rt = testContext.getContextData("currentRecordType");
        String recordType = rt == null ? "" : rt.toString();
        boolean isIntuit = recordType.toLowerCase().contains("intuit");

        int expectedCards = isIntuit ? 2 : 3;
        if (conversionCardLinks.size() < expectedCards) {
            throw new AssertionError("Expected " + expectedCards + " cards on conversion landing, but found: " + conversionCardLinks.size());
        }

        String accountUi = conversionCardLinks.get(0).getText().trim();
        String contactUi = conversionCardLinks.get(1).getText().trim();
        testContext.getLogger().info("Account on UI: {}", accountUi);
        testContext.getLogger().info("Contact on UI: {}", contactUi);

        String expectedAccount = String.valueOf(testContext.getContextData("accountName"));
        String expectedContact = String.valueOf(testContext.getContextData("contactName"));
        if (expectedContact == null || "null".equals(expectedContact) || expectedContact.isEmpty()) {
            String leadName = String.valueOf(testContext.getContextData("LeadName"));
            String[] parts = leadName.split("\\s+", 2);
            String first = parts.length > 0 ? parts[0] : "";
            String last  = parts.length > 1 ? parts[1] : "";
            Object middleObj = testContext.getContextData("ContactMiddleName");
            String middle = middleObj == null ? "Contact" : middleObj.toString();
            expectedContact = (first + " " + middle + " " + last).trim();
            testContext.getLogger().info("Constructed expected contact name as: {}", expectedContact);
        }

        if (!accountUi.equals(expectedAccount)) {
            throw new AssertionError("Account name mismatch on conversion landing. Expected: " + expectedAccount + ", Actual: " + accountUi);
        }
        if (!contactUi.equals(expectedContact)) {
            throw new AssertionError("Contact name mismatch on conversion landing. Expected: " + expectedContact + ", Actual: " + contactUi);
        }

        if (!isIntuit) {
            String oppUi = conversionCardLinks.get(2).getText().trim();
            testContext.getLogger().info("Opportunity on UI: {}", oppUi);
            String expectedOpp = String.valueOf(testContext.getContextData("OpportunityName"));
            if (expectedOpp == null || "null".equals(expectedOpp) || expectedOpp.isEmpty()) {
                expectedOpp = String.valueOf(testContext.getContextData("Opportunity Name"));
            }
            if (!oppUi.equals(expectedOpp)) {
                throw new AssertionError("Opportunity name mismatch on conversion landing. Expected: " + expectedOpp + ", Actual: " + oppUi);
            }
        }
        testContext.getLogger().info("Cards verified successfully ({}).", isIntuit ? "Account, Contact" : "Account, Contact, Opportunity");
    }

    public void clickGoToLeadsButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(goToLeadsButton)).click();
        testContext.getLogger().info("Clicked on Go to Leads button");
        Thread.sleep(500);
    }
}
