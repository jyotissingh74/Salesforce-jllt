@lead
Feature: Lead Creation in Salesforce

  Background:
    Given I am logged in to Salesforce
    Then I should be logged in successfully

  Scenario Outline: Create a new Lead for 'Global Lead' Record Type with 'Is New Account'
    When I log in as user "<Username>"
    Then I navigate to the Leads page
    When I click on the New Lead button with record type "<Lead Record Type>"
    And I select "<Lead Type>" account option
    When I fill in the following Lead details:
      | First Name | Last Name | Contact Job Role | Account Name | Lead Source           | Phone | Email                       | Mobile | Region   |
      | Test       | Lead      | Lead             | Lead Account | Referral: JLL Markets |       | GlobalLead.test@example.com |        | Americas |
    Then I should verify "<Lead Type>" text box is enabled
    When I save the lead
    Then I should be redirected to the lead landing page
    And I should verify the fields on the lead record page:
      | Field Name  | Expected Value              |
      | Account     |                             |
      | Name        |                             |
      | Phone       |                             |
      | Email       | GlobalLead.test@example.com |
      | Region      | Americas                    |
      | Lead Status | New                         |
      | Lead Source | Referral: JLL Markets       |
    Then I edit Business Group to "Business Lines" and verify Sub Business Group has option "Energy Advisory"
    And I select Lead Source as "<Lead Source Value>" and should see Lead Sub Source options:
    """
    --None-- | Eptura | FM:Systems | IBM | MSA Parasense | Nuvolo | Sclera | ServiceNow | Spark | Vergesense
    """
    Examples:
      | Username             | Lead Type      | Lead Record Type | Lead Source Value                 |
      | TestAutomation1 jllt | Is New Account | Global Lead      | Referral: Tech Sales Partnerships |


  Scenario Outline: Create a new Lead for 'Global Lead' Record Type with 'Is Existing Account'
    When I log in as user "<Username>"
   # Account Creation
    Given An account is created with the following details:
      | Account Record Type | Search Account Name   | Account Name | Region   | Currency    | Industry   | Street                   | City    | State/Province | Country       | Zip/PostalCode |
      | Global Account      | <Search Account Name> | Test Account | <Region> | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham | Massachusetts  | United States | 21920          |
    # Lead Creation
    Then I navigate to the Leads page
    When I click on the New Lead button with record type "<Lead Record Type>"
    And I select "<Lead Type>" account option
    When I fill in the following Lead details:
      | First Name | Last Name | Contact Job Role | Account Name | Lead Source           | Phone             | Email                | Mobile            | Region   |
      | Test       | Lead      | Lead             |              | Referral: JLL Markets | +1 (555) 123-4567 | TestLead@example.com | +1 (555) 987-6543 | <Region> |
    Then I should verify "<Lead Type>" text box is enabled
    When I save the lead
    Then I should be redirected to the lead landing page
    Examples:
      | Username             | Lead Type           | Lead Record Type | Search Account Name | Region   |
      | TestAutomation1 jllt | Is Existing Account | Global Lead      | Test Account        | Americas |


  Scenario Outline: Create a new Lead for 'Intuit FSM' Record Type
    When I log in as user "<Username>"
    Then I navigate to the Leads page
    When I click on the New Lead button with record type "<Lead Record Type>"
    And I select "<Lead Type>" account option
    When I fill in the following Lead details:
      | First Name | Last Name       | Account Name | Lead Source | Lead Sub Source   | Phone             | Email                      | Mobile            | Region   |
      | Test       | Intuit FSM Lead | Lead Account | Intuit FSM  | Inside Sales Team | +1 (555) 123-4567 | IntuitFSM.Lead@example.com | +1 (555) 987-6543 | Americas |
    When I save the lead
    Then I should be redirected to the lead landing page
    And I should verify fields on highlights panel:
    """
    Account | Email | Title | Lead Owner
    """
    And I should verify Status Path on lead record page:
    """
    New | Assigned | Working | Lost | Converted
    """
    And I should verify the fields on the lead record page:
      | Field Name      | Expected Value             |
      | Account         |                            |
      | Name            |                            |
      | Lead Source     | Intuit FSM                 |
      | Lead Sub Source | Inside Sales Team          |
      | Phone           | +1 (555) 123-4567          |
      | Email           | IntuitFSM.Lead@example.com |
      | Lead Status     | New                        |
      | Lead Owner      | TestAutomation1 jllt       |
    And I select Lead Source as "<Lead Source Value>" and should see Lead Sub Source options:
    """
    --None-- | 7 Day Test Drive | Inside Sales Team | Intuit QSP | Intuit Other
    """
    Examples:
      | Username             | Lead Source Value | Lead Record Type |
      | TestAutomation1 jllt | Intuit FSM        | Intuit FSM       |


  Scenario Outline: Create a new Lead for 'Intuit FSM' Record Type with System Admin
    Then I click on App Launcher and search "<AppName>"
    Then I navigate to the Leads page
    When I click on the New Lead button with record type "<Lead Record Type>"
    And I select "<Lead Type>" account option
    When I fill in the following Lead details:
      | First Name | Last Name       | Account Name | Lead Source | Lead Sub Source   | Phone             | Email                      | Mobile            | Region   |
      | Test       | Intuit FSM Lead | Lead Account | Intuit FSM  | Inside Sales Team | +1 (555) 123-4567 | IntuitFSM.Lead@example.com | +1 (555) 987-6543 | Americas |
    When I save the lead
    Then I should be redirected to the lead landing page
    And I should verify the fields on the lead record page:
      | Field Name      | Expected Value             |
      | Account         |                            |
      | Name            |                            |
      | Lead Source     | Intuit FSM                 |
      | Lead Sub Source | Inside Sales Team          |
      | Phone           | +1 (555) 123-4567          |
      | Email           | IntuitFSM.Lead@example.com |
      | Lead Status     | New                        |
      | Lead Owner      | Test Automation User       |
    And I select Lead Source as "<Lead Source Value>" and should see Lead Sub Source options:
    """
    --None-- | 7 Day Test Drive | Inside Sales Team | Intuit QSP | Intuit Other
    """
    Examples:
      | Username             | Lead Source Value | Lead Record Type | AppName          |
      | TestAutomation1 jllt | Intuit FSM        | Intuit FSM       | Sales Management |