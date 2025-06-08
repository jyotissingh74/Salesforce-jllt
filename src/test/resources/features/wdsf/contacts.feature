@contact
Feature: Contact Creation in Salesforce

  Scenario Outline: Create a new Contact
    Given I am logged in to Salesforce
    Then I should be logged in successfully
    When I log in as user "<Username>"

    # Account Creation
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Contact Creation
    Then I navigate to the Contacts page
    When I click on the New Contact button And I fill in the following Contact details:
      | First Name | Last Name | Contact Job Role | Company Name | Phone            | Email           | Mobile           |
      | Test       | Contact   | Lead             |              | +1 (555) 123-4567| john@example.com| +1 (555) 987-6543|
    Then I should be redirected to the contact landing page
    #Then I should see the confirmation message
    Examples:
      | Username             | Account Record Type | Search Account Name |
      | TestAutomation1 jllt | Account Request     | Test Account        |