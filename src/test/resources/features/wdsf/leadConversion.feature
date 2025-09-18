@leadConversion
Feature: Lead Creation in Salesforce

  Background:
    Given I am logged in to Salesforce
    Then I should be logged in successfully

  Scenario Outline: Convert a Lead where Lead Record Type is 'Global Lead'  with 'Is New Account'
    When I log in as user "<Username>"
    # Lead Creation
    Given A lead is created with the following details:
      | First Name   | Last Name   | Contact Job Role | Account Name | Lead Source   | Phone | Email                          | Mobile | Region   | Lead Type   | Lead Record Type   |
      | <First Name> | <Last Name> | Lead             | Lead Account | <Lead Source> |       | GlobalLead.164test@example.com |        | <Region> | <Lead Type> | <Lead Record Type> |
    # Lead Conversion
    And I fill in the Pain, Budget, & Decision Process Process fields in order to convert the Lead:
      | Pain   | Budget   | Decision Process   |
      | <Pain> | <Budget> | <Decision Process> |
    When I click on the Convert Lead button
    Then I should see the Convert Lead dialog
    And I verify the following fields on Convert Lead dialog:
      | Field Name                           | Expected Value |
      | Converted Status                     | Qualified      |
      | Converted Opportunity Region         | <Region>       |
      | Converted Opportunity Business Group | Business Lines |
    And I click on the Next button on Convert Lead dialog
    # creating Account using D&B Search
    Then I search for existing account with name "<Search Account Name>"
    And I create a new account from D&B search results with details:
      | Account Name | Region   | Currency    | Industry   |
      | Lead Account | <Region> | U.S. Dollar | <Industry> |
    # Convert Lead page
    Then I navigate to the Convert Lead dialog
    And the selected account should be "<Account Name>"
    When I expand the "Contact" section on convert lead dialog
    Then the "Contact" radio button "Create New" should be selected
    And I verify fields in contact section:
      | Field Name  | Expected Value |
      | First Name  | <First Name>   |
      | Last Name   | <Last Name>    |
      | Middle Name | Contact        |
   # Then the "Opportunity" radio button "Create New" should be selected
    And I verify fields in Opportunity section on convert lead dialog:
      | Field Name             | Expected Value         |
      | Opportunity Name       | Lead Opportunity       |
      | Opportunity Close Date |                        |
      | Region                 | <Region>               |
      | Opportunity Division   | <Opportunity Division> |
      | Opportunity Currency   | <Currency>             |
    And the Future Opportunity Owner should be "<Lead Owner>"
    When I click the Convert button
    Then I should see the Converted Lead success page
    And I verify Account, Contact and Opportunity Cards on converted landing page
    And I click on Go to Leads button
    When I go to the "Accounts" tab
    Then I search the record on the recently viewed page with "Record Name"
    Then I should be redirected to the account landing page
    And I should verify the fields on the account record page:
      | Field Name          | Expected Value        |
      | Account             |                       |
      | Account Region      | <Region>              |
      | Industry            | <Industry>            |
      | Account Source      | <Lead Source>         |
      | Account Record Type | Work Dynamics Account |
    When I go to the "Contacts" tab
    Then I search the record on the recently viewed page with "Record Name"
    Then I should be redirected to the contact landing page
    And I should verify the fields on the contact record page:
      | Field Name   | Expected Value |
      | Name         |                |
      | Account Name |                |
      | Lead Source  | <Lead Source>  |
    When I go to the "Opportunities" tab
    Then I search the record on the recently viewed page with "Record Name"
    Then I should be redirected to the opportunity landing page
    And I should verify the fields on the Opportunity record page:
      | Field Name           | Expected Value         |
      | Opportunity Name     |                        |
      | Opportunity Owner    | <Lead Owner>           |
      | Opportunity Region   | <Region>               |
      | Opportunity Division | <Opportunity Division> |
      | Account Name         |                        |
      | Lead Source          | <Lead Source>          |
      | Lead                 |                        |
      | Pain                 | <Pain>                 |
      | Budget               | <Budget>               |
      | Decision Process     | <Decision Process>     |
    Examples:
      | Username             | Lead Type      | Lead Record Type | Search Account Name | Account Name | First Name | Last Name | Region   | Currency          | Lead Owner           | Industry   | Lead Source           | Pain      | Budget | Decision Process      | Opportunity Division |
      | TestAutomation1 jllt | Is New Account | Global Lead      | JLL                 | Lead Account | Test       | Lead      | Americas | USD - U.S. Dollar | TestAutomation1 jllt | Technology | Referral: JLL Markets | Test Pain | 748592 | Test Decision Process | AM - US Central      |


  Scenario Outline: Convert a Lead where Lead Record Type is 'Intuit Lead'  with 'Is New Account'
    When I log in as user "<Username>"
    # Lead Creation
    Given A lead is created with the following details:
      | First Name   | Last Name   | Contact Job Role | Account Name   | Lead Source   | Lead Sub Source   | Phone             | Email                       | Mobile            | Region   | Lead Type   | Lead Record Type   |
      | <First Name> | <Last Name> | Lead             | <Account Name> | <Lead Source> | Inside Sales Team | +1 (555) 123-4567 | IntuitLead.test@example.com | +1 (555) 987-6543 | <Region> | <Lead Type> | <Lead Record Type> |
    # Lead Conversion
    When I click on the Convert Lead button
    # creating Account using D&B Search
    Then I search for existing account with name "<Search Account Name>"
    And I create a new account from D&B search results with details:
      | Account Name | Region   | Currency    | Industry   |
      | Lead Account | <Region> | U.S. Dollar | Technology |
    # Convert Lead page
    Then I navigate to the Convert Lead dialog
    And the selected account should be "<Account Name>"
    When I expand the "Contact" section on convert lead dialog
    Then the "Contact" radio button "Create New" should be selected
    And I verify fields in contact section:
      | Field Name  | Expected Value |
      | First Name  | <First Name>   |
      | Last Name   | <Last Name>    |
      | Middle Name | Contact        |
    And the Future Opportunity Owner should be "<Lead Owner>"
    When I click the Convert button
    Then I should see the Converted Lead success page
    And I verify Account, Contact and Opportunity Cards on converted landing page
    And I click on Go to Leads button
    When I go to the "Accounts" tab
    Then I search the record on the recently viewed page with "Record Name"
    Then I should be redirected to the account landing page
    And I should verify the fields on the account record page:
      | Field Name          | Expected Value        |
      | Account             |                       |
      | Account Region      | <Region>              |
      | Account Source      | <Lead Source>         |
      | Account Record Type | Work Dynamics Account |
    When I go to the "Contacts" tab
    Then I search the record on the recently viewed page with "Record Name"
    Then I should be redirected to the contact landing page
    And I should verify the fields on the contact record page:
      | Field Name   | Expected Value |
      | Name         |                |
      | Account Name |                |
      | Lead Source  | <Lead Source>  |
    Examples:
      | Username             | Lead Type      | Lead Record Type | Search Account Name | Account Name | First Name | Last Name | Region   | Currency          | Lead Owner           | Lead Source |
      | TestAutomation1 jllt | Is New Account | Intuit FSM       | JLL                 | Lead Account | Test       | Lead      | Americas | USD - U.S. Dollar | TestAutomation1 jllt | Intuit FSM  |


  Scenario Outline: Convert a Lead where Lead Record Type is 'Global Lead'  with 'Is Existing Account'
    When I log in as user "<Username>"
    # Account Creation
    Given An account is created with the following details:
      | Account Record Type | Search Account Name   | Account Name | Region   | Currency    | Industry   | Street                   | City    | State/Province | Country       | Zip/PostalCode |
      | Global Account      | <Search Account Name> | Test Account | <Region> | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham | Massachusetts  | United States | 21920          |
    # Lead Creation
    Given A lead is created with the following details:
      | First Name   | Last Name   | Contact Job Role | Account Name | Lead Source           | Phone             | Email                         | Mobile            | Region   | Lead Type   | Lead Record Type   |
      | <First Name> | <Last Name> | Lead             | Lead Account | Referral: JLL Markets | +1 (555) 123-9870 | GlobalLead.test12@example.com | +1 (555) 987-6320 | <Region> | <Lead Type> | <Lead Record Type> |
    # Lead Conversion
    And I fill in the Pain, Budget, & Decision Process Process fields in order to convert the Lead:
      | Pain      | Budget | Decision Process      |
      | Test Pain | 748592 | Test Decision Process |
    When I click on the Convert Lead button
    Then I should see the Convert Lead dialog
    And I verify the following fields on Convert Lead dialog:
      | Field Name                           | Expected Value |
      | Converted Status                     | Qualified      |
      | Converted Opportunity Region         | <Region>       |
      | Converted Opportunity Business Group | Business Lines |
    And I click on the Next button on Convert Lead dialog
    # Convert Lead page
    Then I navigate to the Convert Lead dialog
    And the selected account should be "<Account Name>"
    When I expand the "Contact" section on convert lead dialog
    Then the "Contact" radio button "Create New" should be selected
    And I verify fields in contact section:
      | Field Name  | Expected Value |
      | First Name  | <First Name>   |
      | Last Name   | <Last Name>    |
      | Middle Name | Contact        |
    And I verify fields in Opportunity section on convert lead dialog:
      | Field Name             | Expected Value   |
      | Opportunity Name       | Lead Opportunity |
      | Opportunity Close Date |                  |
      | Region                 | <Region>         |
      | Opportunity Division   | AM - US Central  |
      | Opportunity Currency   | <Currency>       |
    And the Future Opportunity Owner should be "<Lead Owner>"
    When I click the Convert button
    Then I should see the Converted Lead success page
    And I verify Account, Contact and Opportunity Cards on converted landing page
    And I click on Go to Leads button
    Examples:
      | Username             | Lead Type           | Lead Record Type | Search Account Name | First Name | Last Name | Region   | Currency          | Lead Owner           |
      | TestAutomation1 jllt | Is Existing Account | Global Lead      | Test Account        | Test       | Lead      | Americas | USD - U.S. Dollar | TestAutomation1 jllt |