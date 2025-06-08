@account
Feature: Account Creation in Salesforce

  Background:
    Given I am logged in to Salesforce
    Then I should be logged in successfully

  Scenario Outline: Create a new account using Create Account Button
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the New Account Request Form button And I fill in the following account details:
      | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
      | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    Then I should be redirected to the account landing page
    #Then I should see the confirmation message
    Examples:
      | Username             | Account Record Type   | Search Account Name |
      | TestAutomation1 jllt | Account Request       | Test Account        |


  Scenario Outline: Create an account using D&B Search
    When I log in as user "<Username>"
    Then I navigate to the Accounts page
    When I click on the New Account button with record type "<Account Record Type>"
    Then I should see the Account Search page
    Then I search for existing account with name "<Search Account Name>"
    When I click on the D&B Account Name from the searched results
    #Then I fill in the following account details:
    #  | Account Name   | Region   | Currency    | Industry   | Street                   | City     | State/Province| Country       | Zip/PostalCode |
    #  | Test Account   | Americas | U.S. Dollar | Technology | 3865 Stanton Hollow Road | Needham  | Massachusetts | United States | 21920          |
    #Then I should see the confirmation message
    Examples:
      | Username             | Account Record Type   | Search Account Name |
      | TestAutomation1 jllt | Work Dynamics Account | sds                 |