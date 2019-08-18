package StepDefinition;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;

import BaseUtil.CommonMethod;
import PageObjects.LoginPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class StepDefinition extends CommonMethod {

	public LoginPage loginToPage;

	public StepDefinition() {
		Setup();
		loginToPage = new LoginPage(driver);
	}

	@Given("^Open Browser and navigate to Home page$")
	public void openApplication() throws Throwable {
		driver.get(prop.getProperty("url"));
		driver.manage().window().maximize();
		Thread.sleep(5000);
		logger.info("Application opened in given Browser");
	}

	@When("^valid Depart and arrival pair is entered$")
	public void enterValidLocations() throws Throwable {
		clearAndSendKeysToElement(loginToPage.departureField(), "MCO");
		clearAndSendKeysToElement(loginToPage.arrivalField(), "DAL");
		logger.info("DEST and ARR location code entered");
		click(loginToPage.searchButton());
		logger.info("Search button clicked");
	}

	@When("^valid (.*) and (.*) location is entered$")
	public void enterDataTablesLocations(String Depart, String Arrival) throws Throwable {
		clearAndSendKeysToElement(loginToPage.departureField(), Depart);
		clearAndSendKeysToElement(loginToPage.arrivalField(), Arrival);
		logger.info("DEST and ARR location code entered");
		click(loginToPage.searchButton());
		logger.info("Search button clicked");
	}

	@Then("^user should able to navigate to Select Flight Page$")
	public void selectFlightPage() throws InterruptedException, IOException {
		Thread.sleep(8000);
		String pageTitle = driver.getTitle();
		Assert.assertEquals(pageTitle, "Southwest Airlines - Select Flight");
		logger.info("Page title matched");
	}
	
	@Then("Verify Application is closed")
	public void verify_Application_is_closed() throws IOException {
		TearDown();
		logger.info("Browser closed");
	}
	
	@When("^Valid Depart and arrival is entered from excelsheet placed at excel sheet$")
	public void valid_Depart_and_arrival_is_entered_from_excelsheet_placed_at_excel_shhet() throws IOException, InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		List<List<String>> raw = readExcel("./src/main/resources/", "FeatureConfiguration.xlsx", "Datatable");
		List<Map<String, String>> list = DataTable.create(raw).asMaps();
		
		System.out.println("Tables data as >>>>>>>>> " + list);
		
		//for (Map<String, String> location :  list) {
			clearAndSendKeysToElement(loginToPage.departureField(), list.get(0).get("Source"));
			clearAndSendKeysToElement(loginToPage.arrivalField(), list.get(0).get("Destination"));
			click(loginToPage.searchButton());			
		//}	
	}

}