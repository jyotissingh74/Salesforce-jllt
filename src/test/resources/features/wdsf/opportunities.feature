@opportunity
Feature: Opportunity Creation in Salesforce

  Background:
    Given I am logged in to Salesforce
    Then I should be logged in successfully

  Scenario Outline: Create a new Opportunity using New Opportunity Form
    When I log in as user "<Username>"
    Then I navigate to the Opportunities page
    When I click on the New Opportunity button
    Then I should see the New Opportunity form
    When I fill in the following opportunity details:
      | Name                  | Account Name  | Opportunity Region | Close Date   | Country       | Opportunity Currency | Opportunity Division | Client Value & Growth Primary Vertical |
      | Test Wdsf Opportunity |               | Americas           | <CloseDate>  | United States | USD - U.S. Dollar    | Healthcare           | AMR Healthcare                         |
    And I click the Save button
    Then I should see the new opportunity record
    Examples:
      | Username             |
      | TestAutomation1 jllt |