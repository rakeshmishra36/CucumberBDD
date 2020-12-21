import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Keyboard;
import org.testng.annotations.Test;

import BaseUtil.CommonMethod;
import io.cucumber.datatable.DataTable;

public class NewTest extends CommonMethod{
  
  public void f() throws InterruptedException, AWTException {
	  
	  System.setProperty("webdriver.gecko.driver","C:\\Users\\rakmishra\\Desktop\\Selenium scripts\\geckodriver.exe");
	  
	  WebDriver driver = new FirefoxDriver();
	    driver.manage().window().maximize();

	    String baseUrl = "http://www.google.co.uk/";
	    driver.get(baseUrl);
	   // Thread.sleep(10000);
	    
	   //driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL,Keys.RETURN));
	    //driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, "t"));
	    driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, Keys.chord("t")));
	    
	    

	    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	    Thread.sleep(5000);
	    driver.switchTo().window(tabs.get(1)); //switches to new tab
	    driver.get("https://www.facebook.com");

	    driver.switchTo().window(tabs.get(0)); // switch back to main screen        
	    driver.get("https://www.news.google.com");
  }
  //@Test
  public void valid_Depart_and_arrival_is_entered_from_excelsheet_placed_at_excel_shhet() throws IOException, InterruptedException {
		List<List<String>> raw = readExcel("./src/main/resources/", "FeatureConfiguration.xlsx", "Datatable");
		List<Map<String, String>> list = DataTable.create(raw).asMaps();
		
		System.out.println("Tables data as >>>>>>>>> " + DataTable.create(raw));
		
		
	}
  
  @Test
  public static void main(String args[]) {
	    Date date = new Date();
	    String DATE_FORMAT = "MM/dd/yyyy";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    System.out.println("Today is " + sdf.format(date));
	  }
}
