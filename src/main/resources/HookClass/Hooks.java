package BaseUtil.*;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebDriver;

import BaseUtil.CommonMethod;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

public class Hooks implements Formatter, Reporter {

	public WebDriver driver;
	public CommonMethod commonMethod = new CommonMethod();

	@Override
	public void before(Match match, Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void result(Result result) {
		try {
			 if (result.getStatus().equalsIgnoreCase("failed")) {			 
				commonMethod.takeScreenShot(driver);
			 }else if (result.getStatus().equalsIgnoreCase("skipped")) {
		        commonMethod.takeScreenShot(driver);
		     }else {
		    	commonMethod.takeScreenShot(driver);  
		     }
		}catch(IOException e){
			System.out.println("Exception >> " + e.getMessage());
		}
	}

	@Override
	public void after(Match match, Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void match(Match match) {
		// TODO Auto-generated method stub

	}

	@Override
	public void embedding(String mimeType, byte[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uri(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void feature(Feature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scenarioOutline(ScenarioOutline scenarioOutline) {
		// TODO Auto-generated method stub

	}

	@Override
	public void examples(Examples examples) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {
		// TODO Auto-generated method stub

	}

	@Override
	public void background(Background background) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scenario(Scenario scenario) {
		// TODO Auto-generated method stub

	}

	@Override
	public void step(Step step) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {
		// TODO Auto-generated method stub

	}

	@Override
	public void done() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void eof() {
		// TODO Auto-generated method stub

	}

}
