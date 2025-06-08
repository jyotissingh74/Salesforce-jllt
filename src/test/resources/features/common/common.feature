@account
Feature: Account Creation in Salesforce

  Background:
    Given I am logged in to Salesforce
    Then I should be logged in successfully

  Scenario Outline: Create a new account using Create Account Button
    When I log in as user "<Username>"
    Examples:
      | Username |
      | TestAutomation1 jllt |