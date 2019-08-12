package BaseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

import RunnerClass.TestRunner;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CommonMethod extends TestRunner{

	public static WebDriver driver;
	public static InputStream inputStream;
	public List<String> list;
	public List<List<String>> excelSheetRows;
	public Workbook workBook = null;
	public File file;
	public static Logger logger;

	public void Setup() {
		logger = Logger.getLogger("ApplicationLog");
		openBrowser(prop.getProperty("browser"));
	}

	public void TearDown() {		
		try {
			inputStream.close();
		} catch (IOException e) {
			System.out.println("Exception >> " + e.getMessage());
		} finally {			
			driver.quit();
		}		
	}

	public Properties getPropValues() {
		try {
			prop = new Properties();
			String propFileName = "configuration.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}
		return prop;
	}


	public void getCellType(List<String> innerList, Row row, int j) {
		switch (row.getCell(j).getCellType()) {
		case BLANK:
			innerList.add("");
			break;

		case STRING:
			innerList.add(row.getCell(j).getStringCellValue());
			break;

		case NUMERIC:
			innerList.add(row.getCell(j).getNumericCellValue() + "");
			break;

		case BOOLEAN:
			innerList.add(row.getCell(j).getBooleanCellValue() + "");
			break;

		default:
			throw new IllegalArgumentException("Cannot read the column : " + j);
		}
	}

	public Workbook fileType(String filePath, String fileName) throws IOException {
		file = new File(filePath + "/" + fileName);
		inputStream = new FileInputStream(file);
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		if (fileExtensionName.equals(".xlsx")) {
			workBook = new XSSFWorkbook(inputStream);
		} else if (fileExtensionName.equals(".xls")) {
			workBook = new HSSFWorkbook(inputStream);
		}
		return workBook;
	}

	public List<List<String>> readExcel(String filePath, String fileName, String sheetName) throws IOException {
		workBook = fileType(filePath, fileName);
		Sheet sheet = workBook.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum() + 1;
		int columnCount = sheet.getRow(0).getLastCellNum();
		excelSheetRows = new LinkedList<>();
		for (int i = 1; i < rowCount; i++) {
			Row row = sheet.getRow(i);
			list = new LinkedList<>();
			for (int j = 0; j < columnCount; j++) {
				getCellType(list, row, j);
			}
			excelSheetRows.add(list);
		}
		inputStream.close();
		return excelSheetRows;
	}

	public void listToMap(String filePath, String fileName, String sheetName) throws IOException {
		List<List<String>> myList = readExcel(filePath, fileName, sheetName);
		System.out.println("List data >> \n" + myList);

		int size = myList.size();

		Map<String, String> testCaseMap = new HashMap<String, String>();

		for (int i = 0; i < size; i++) {
			String key = myList.get(i).get(0).toString();
			String value = myList.get(i).get(1).toString();
			testCaseMap.put(key, value);
			System.out.println("Value of " + key + " : " + testCaseMap.get(key));
		}
		System.out.println("Test case Map " + "\n" + testCaseMap);
	}

	public void takeScreenShot(WebDriver driver) throws IOException {
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile = new File("target/screenShots");
		FileUtils.copyFile(SrcFile, DestFile);
	}

	public WebDriver openBrowser(String Browser) {
		if (Browser.equals("FireFox")) {
			WebDriverManager.firefoxdriver().setup();
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "target/Driverlogs.txt");
			driver = new FirefoxDriver();
		} else if (Browser.equals("Chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			String userProfile = System.getenv("LOCALAPPDATA");
			options.addArguments("--user-data-dir=" + userProfile + "\\Google\\Chrome\\User Data\\");
			options.addArguments("--profile-directory=Profile 1");
			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
		} else if (Browser.equals("IE")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		}
		return driver;
	}

	public void click(WebElement webElement) {
		if (webElement != null) {
			webElement.click();
		}
	}

	public void clickJS(WebElement webElement) {
		if (webElement != null) {
			String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
			((JavascriptExecutor) driver).executeScript(js, webElement);
			webElement.click();
		}
	}

	public boolean isElementDisplayed(WebElement webElement) {
		if (webElement != null) {
			webElement.isDisplayed();
		}
		return false;
	}

	public void clearAndSendKeysToElement(WebElement webElement, String text) throws InterruptedException {
		if (webElement != null) {
			Thread.sleep(3000);
			clearInputField(webElement);
			webElement.sendKeys(text);
		}
	}

	public void clearInputField(WebElement webElement) {
		if (webElement != null) {
			webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			webElement.sendKeys(Keys.DELETE);
		}
	}

	public void SelectDropDownValue(WebElement webElement, String Value) {
		if (webElement != null) {
			Select select = new Select(webElement);
			select.selectByVisibleText(Value);
		}
	}
}