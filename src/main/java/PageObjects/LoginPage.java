package PageObjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import BaseUtil.BaseClass;

public class LoginPage extends BaseClass{
	
	public WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}	
	
	
	@FindBy(xpath="//div[@class='air-search-form--fields-airports']/div[1]/label/div//input")
	WebElement departure;
	
	@FindBy(xpath="//div[@class='air-search-form--fields-airports']/div[2]/label/div//input")
	WebElement arrival;
	
	@FindBy(id="LandingPageAirSearchForm_submit-button")
	WebElement searchButton;	
	
	@FindAll(@FindBy(how = How.CSS, using = ".noo-product-inner"))
	List<WebElement> prd_List;
	
	public WebElement departureField() {
		return departure;
	}
	
	public WebElement arrivalField() {
		return arrival;
	}
	
	public WebElement searchButton() {
		return searchButton;
	}
	
	
	public List<WebElement> select_Product() {
		return prd_List;
	}
}