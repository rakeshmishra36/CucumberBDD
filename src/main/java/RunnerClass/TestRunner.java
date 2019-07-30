package RunnerClass;


//import java.io.File;
//import java.util.Properties;
//import org.testng.annotations.AfterClass;
//import com.vimalselvam.cucumber.listener.*;

import cucumber.api.CucumberOptions;
import BaseUtil.cucumber.api.testng.AbstractTestNGCucumberTests;

//@RunWith(Cucumber.clase)  - if want to run using JUnit , and remove extends class
@CucumberOptions(
			features = "src/main/resources/FeatureFiles", 
			glue = { "StepDefinition" }, 
			plugin = { 
						"html:target/cucumber-reports/htmlReport",
						"json:target/cucumber-reports/JsonReport/CucumberTestReport.json", 
						"testng:target/cucumber-reports/xmlReport/CucumberTestReport.xml",
						"rerun:target/rerun.txt"
					 },
			monochrome = true
			//tags = {"@Basic,@Login"}  // - Logical OR
			//tags = {"@Basic","@Login"}   //- Logical AND
			//if want to ignore tag execution, Use ~ sign in front of tag
		)
public class TestRunner extends AbstractTestNGCucumberTests{
	

}
