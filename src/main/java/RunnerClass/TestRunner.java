package RunnerClass;


import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.codehaus.plexus.util.FileUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import BaseUtil.CommonMethod;
import BaseUtil.cucumber.api.testng.AbstractTestNGCucumberTests;

//import java.io.File;
//import java.util.Properties;
//import org.testng.annotations.AfterClass;
//import com.vimalselvam.cucumber.listener.*;

import cucumber.api.CucumberOptions;

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
	
	public static CommonMethod commonMethod = new CommonMethod();;
	public static Properties prop;
	
	@BeforeSuite
	public static void loggerProperties() {
		String configFileName = "./src/main/resources/log4j.properties";
		PropertyConfigurator.configure(configFileName);
		prop = commonMethod.getPropValues();
	}
	
	@AfterSuite
	public static void fileDeleted() throws IOException {
		FileUtils.deleteDirectory("./src/main/resources/FeatureFiles/temp/");
	}

}