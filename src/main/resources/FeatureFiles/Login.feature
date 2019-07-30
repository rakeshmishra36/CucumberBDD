#Author: rakeshmishra36@gmail.com
#Keywords Summary : Cucumber-TestNG-POC
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@Login
Feature: Flight Search Feature 
		As a Automation tester
  		I want to test flight search functionality
  		So that I can book flight

Background: User navigates to Company home page 
	Given Open Browser and navigate to Home page 
	
@Basic
Scenario: Flight Search scenario
	When valid Depart and arrival pair is entered 
	Then user should able to navigate to Select Flight Page 
	And Verify Application is closed
	
@DateTables
Scenario Outline: search for multiple combinations scenario
	When valid <Depart> and <Arrival> location is entered
	Then user should able to navigate to Select Flight Page
	And Verify Application is closed
	
	Examples: 
		| Depart   	 | Arrival    |
		| MCO        | ATL        |
		| DAL        | ATL        |
		
		
@ExcelSheet 
	Scenario: Flight Search using excel sheet 
	When Valid Depart and arrival is entered from excelsheet placed at excel shhet
	Then user should able to navigate to Select Flight Page
	And Verify Application is closed