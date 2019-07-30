package BaseUtil;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BaseClass {

	private WebDriver driver;

	public BaseClass(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
}