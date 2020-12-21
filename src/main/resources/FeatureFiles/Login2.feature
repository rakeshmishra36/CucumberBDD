@Login2
Feature: Flight Search Feature Duplicate 
		As a Automation tester
  		I want to test flight search functionality
  		So that I can book flight

Background: User navigates to Company home page 
	Given Open Browser and navigate to Home page 
	
@Basic2
Scenario: Flight Search scenario two
	When valid Depart and arrival pair is entered 
	And Search button clicked
	Then user should able to navigate to Select Flight Page 
	And Verify Application is closed
	
@DateTables2
Scenario Outline: search for multiple combinations scenario two
	When valid <Depart> and <Arrival> location is entered
	And Search button clicked
	Then user should able to navigate to Select Flight Page
	And Verify Application is closed
	
	Examples: 
		| Depart   	 | Arrival    |
		| MCO        | ATL        |
		| DAL        | ATL        |