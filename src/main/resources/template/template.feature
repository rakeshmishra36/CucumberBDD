@Login
Feature: Flight Search Feature two
  As a Automation tester
  		I want to test flight search functionality using excelsheet data
  		So that I can book flight
  		
  Background: User navigates to Company home page 
	Given Open Browser and navigate to Home page 

  @DateTables
  Scenario Outline: search for multiple combinations scenario three
    When valid <Depart> and <Arrival> location is entered
    And Search button clicked
    Then user should able to navigate to Select Flight Page
    And Verify Application is closed

    Examples: 
      | Data: | src/main/resources/FeatureConfiguration.xlsx | Datatable | A1:B4 |