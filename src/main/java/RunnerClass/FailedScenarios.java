package RunnerClass;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
		monochrome = true, 
		features = "@target/rerun.txt", // Cucumber picks the failed scenarios from this file
		glue = { "StepDefinition" }, 
		plugin = { 
				"pretty", "html:target/site/cucumber-pretty", 
				"json:target/cucumber.json"
				}		
		)

public class FailedScenarios extends AbstractTestNGCucumberTests{

}
