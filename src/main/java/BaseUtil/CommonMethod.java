package BaseUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
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
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.Select;

import RunnerClass.TestRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class CommonMethod extends TestRunner{

	public static WebDriver driver;
	public static InputStream inputStream;
	public static SessionId session;
	public List<String> list;
	public List<List<String>> excelSheetRows;
	public Workbook workBook = null;
	public File file;
	public static Logger logger;
	public static byte[] SrcFile;
	public static boolean driverClosed;

	public void Setup() {
		logger = Logger.getLogger("ApplicationLog");
		openBrowser(prop.getProperty("browser"));
		driverClosed = false;
	}

	public void TearDown() {		
		try {
			inputStream.close();
		} catch (IOException e) {
			System.out.println("Exception >> " + e.getMessage());
		} finally {			
			driver.quit();
			driverClosed = true;
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

	public byte[] visiblePageScreenshot(){
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		SrcFile = scrShot.getScreenshotAs(OutputType.BYTES);		
		return SrcFile;
	}
	
	public byte[] fullPageScreenshot() throws IOException{
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(1.5f), 1500)).takeScreenshot(driver);
		BufferedImage image = screenshot.getImage();
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ImageIO.write( image, "jpg", byteArray );
		byteArray.flush();
		byte[] imageInByte = byteArray.toByteArray();
		byteArray.close();
		return imageInByte;
	}
	
	public File writeByte(byte[] bytes) throws IOException {
		FileOutputStream fileOuputStream = new FileOutputStream("filename");
		fileOuputStream.write(bytes);
		fileOuputStream.close();
		return file;		
	}
	
	public WebElement highlightElement(WebElement webElement) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;');", webElement);
		return webElement;
	}

	public WebDriver openBrowser(String Browser) {
		if (Browser.equals("FireFox")) {
			WebDriverManager.firefoxdriver().setup();
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "target/Driverlogs.txt");
			driver = new FirefoxDriver();
			session = ((FirefoxDriver)driver).getSessionId();
			System.out.println("Session id is >>>>> " +session.toString());
		} else if (Browser.equals("Chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			String userProfile = System.getenv("LOCALAPPDATA");
			options.addArguments("--user-data-dir=" + userProfile + "\\Google\\Chrome\\User Data\\");
			options.addArguments("--profile-directory=Profile 1");
			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
			session = ((ChromeDriver)driver).getSessionId();
		} else if (Browser.equals("IE")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			session = ((InternetExplorerDriver)driver).getSessionId();
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

	public void SelectDropDownValue(WebElement webElement, String Value) throws InterruptedException {
		if (webElement != null) {
			Select select = new Select(webElement);
			select.selectByVisibleText(Value);
		}
	}
	
	public boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty())
			return false;
		return true;
	}
	
	public void waitPageLoad() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int i = 0; i < 25; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.info("Page Not Loaded within 25 seconds");
			}
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				Thread.sleep(4000);
				logger.info("Page Loaded successfully");
				break;
			}
		}
	}
	
}