@opportunity
Feature: Opportunity Creation in Salesforce

  Background:
    Given I am logged in to Salesforce
    Then I should be logged in successfully

  Scenario Outline: Create a new Opportunity using New Opportunity Form for Sustainability Consulting record type
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Opportunity Creation for Sustainability Consulting record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                 | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical |
      | Test SC Opportunity  |               | Americas           |              | United States | USD - U.S. Dollar   | Healthcare           | AMR Healthcare                         |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group        |
      | Business Lines | Sustainability Consulting |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username             | Opportunity Record Type   | Account Record Type   | Search Account Name |
      | TestAutomation1 jllt | Sustainability Consulting | Account Request       | Test Account        |


  Scenario Outline: Create a new Opportunity using New Opportunity Form for Generic WorkDynamics record type using Admin credentials
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Opportunity Creation for Generic WorkDynamics record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details for Generic WorkDynamics Opportunity:
      | Name                  | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical | Forecast Category | Stage                 |
      | Test Wdsf Opportunity |               | Americas           |              | United States | USD - U.S. Dollar    | Healthcare           | AMR Healthcare                         | Omitted           | Stage 0 - Prospecting |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group  |
      | Accounts       | Healthcare          |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username                    | Opportunity Record Type | Account Record Type   | Search Account Name |
      | Generic WD Automation User6 | Generic WorkDynamics    | Account Request       | Test Account        |


  Scenario Outline: Create a new Opportunity using New Opportunity Form for Generic WorkDynamics record type
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Opportunity Creation for Generic WorkDynamics record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                  | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical | Forecast Category | Stage                 |
      | Test GWD Opportunity  |               | Americas           |              | United States | USD - U.S. Dollar    | Healthcare           | AMR Healthcare                         | Omitted           | Stage 0 - Prospecting |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group |
      | Accounts       | Healthcare         |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username                    | Opportunity Record Type | Account Record Type   | Search Account Name |
      | Generic WD Automation User6 | Generic Work Dynamics   | Account Request       | Test Account        |


  Scenario Outline: Create a new Opportunity using New Opportunity Form for Consulting record type
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Opportunity Creation for Consulting record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                        | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical |
      | Test Consulting Opportunity |               | Americas           |              | United States | USD - U.S. Dollar    | Healthcare           | AMR Healthcare                         |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group |
      | Business Lines | Consulting         |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username                    | Opportunity Record Type | Account Record Type   | Search Account Name |
      | Consulting Automation User2 | Consulting              | Account Request       | Test Account        |


  Scenario Outline: Create a new Opportunity using New Opportunity Form for Solution Development record type
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Opportunity Creation for Solution Development record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical |
      | Test SD Opportunity |               | Americas           |              | United States | USD - U.S. Dollar    | Healthcare           | AMR Healthcare                         |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group    |
      | WD Sales       | Solutions Development |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username                              | Opportunity Record Type | Account Record Type   | Search Account Name |
      | Solution Development Automation USer4 | Solutions Development   | Account Request       | Test Account        |


  Scenario Outline: Create a new Opportunity using New Opportunity Form for PDS record type
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode | Sector    |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          | Corporate |
    Then I should be redirected to the account landing page

    # Opportunity Creation for PDS record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Source | Confidentiality |
      | Test SD Opportunity |               | Americas           |              | United States | USD - U.S. Dollar    | Billboard          | Public          |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group |
      | Business Lines | PDS                |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username              | Opportunity Record Type | Account Record Type   | Search Account Name |
      | PDS Automation User1  | PDS                     | Account Request       | Test Account        |


  Scenario Outline: Create a new Opportunity using New Opportunity Form for Client Growth & Technology Sales record type
    # Account Creation
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page

    # Opportunity Creation for Client Growth & Technology Sales record type
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                        | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical |Client Value & Growth Team |Client Value & Growth Sub Team |
      | Automation Test CGT Opportunity0619 |               | Americas           |              | United States | USD - U.S. Dollar    | Healthcare           | AMR Healthcare                         | CGT Healthcare            |CGT Healthcare         |
    And I click the Save button
    Then I should be redirected to the opportunity landing page
    And verify Business Group and Sub Business Group values on the opportunity record page:
      | Business Group | Sub Business Group |
      | Tech Sales | Tech Sales Advisory         |
    And verify opportunity record type "<Opportunity Record Type>"
    Examples:
      | Username                    | Opportunity Record Type | Account Record Type   | Search Account Name |
      | CGT Automation User3 | Client Growth & Technology Sales             | Account Request       | Test Account        |