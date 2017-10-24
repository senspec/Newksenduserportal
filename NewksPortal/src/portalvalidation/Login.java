package portalvalidation;
																
import static org.testng.Assert.assertEquals;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;		
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
															
import utilities.Configuration;
import utilities.ExcelUtility;
//import utilities.Screenshots;



																
public class Login {
private WebDriver driver;

	private static final Logger log = LogManager.getLogger(Login.class.getName());
	
	@BeforeClass
	public void setUp() throws Exception 
	{
																		
		ProfilesIni prof = new ProfilesIni();
		FirefoxProfile ffProfile= prof.getProfile ("Senthil");
		ffProfile.setAcceptUntrustedCertificates(true);
		ffProfile.setAssumeUntrustedCertificateIssuer(true);
		//System.setProperty("webdriver.gecko.driver", "E:\\geckodriver-v0.14.0-win64\\geckodriver.exe");
		driver = new FirefoxDriver(ffProfile);						
						// Maximize the browser's window
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		ExcelUtility.setExcelFile(Configuration.File_Path + Configuration.File_Name, "RegistrationTests");
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get(Configuration.URL);
		
	}
																
	@DataProvider(name = "LoginData")
	public Object[][] dataProvider() 
	{
		Object[][] testData = ExcelUtility.getTestData("Invalid_Login");
		return testData;
	}
																	
	@AfterClass
	public void shutDownSelenium(){
		 driver.close();
	
	}
	
	@Test(dataProvider="LoginData")
	public void testUsingExcel(String usrname, String usrpwd, String tcno) throws Exception 
	{
		Properties property = new Properties();
		FileInputStream objfile = new FileInputStream("E:\\GitWebDriverMaven\\NewksPortal\\src\\utilities\\OR.properties");
	   	property.load(objfile);

		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get(Configuration.URL);
		Thread.sleep(6000);
		WebElement Uname = driver.findElement(By.xpath(property.getProperty("emailmob")));
		Uname.clear();
		Uname.sendKeys(usrname);
		String textuname = Uname.getAttribute("value");
		
		WebElement Upwd = driver.findElement(By.xpath(property.getProperty("emailmobpwd")));
		Upwd.clear();
		Upwd.sendKeys(usrpwd);
		String textpwd = Upwd.getAttribute("value");
		
		driver.findElement(By.xpath(property.getProperty("loginbtn"))).click();
		
		WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
		List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
		if (!textuname.isEmpty())
		{
		Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}|^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		Matcher m = pattern.matcher(textuname);
		
			if (!m.find())
			{
				if (textpwd.length() >0 )
				{
					log.info("Email/Mobile is invalid and Password not empty:" + textuname + " " + tcno);
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\Login\\"+tcno+".png"));
					String alert1 = RowCount.get(0).getText();
					assertEquals(alert1,"Please enter valid email / phone number.");
				}
				if (textpwd.isEmpty() )
				{
					log.info("Email/Mobile is invalid and Password empty :" + textuname + " " + tcno);
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\Login\\"+tcno+".png"));
					String alert1 = RowCount.get(0).getText();
					String alert2 = RowCount.get(1).getText();
					assertEquals(alert1,"Please enter valid email / phone number.");
					assertEquals(alert2,"Password field is empty.");
				}	
			}
			else
			{
				if (textpwd.isEmpty())
				{
					log.info("Email/Mobile is valid but Password is empty :" + textuname + " " + tcno);
					String alert3 = RowCount.get(0).getText();
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\Login\\"+tcno+".png"));
					assertEquals(alert3,"Password field is empty.");
				}
				if (!textpwd.isEmpty())
				{
					log.info("Email/Mobile is valid but Password is incorrect :" + textuname + " " + tcno);	
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\Login\\"+tcno+".png"));
					String alert4 = RowCount.get(0).getText();
					assertEquals(alert4,"Invalid Email/Phone Number or Password");
				}
			}
		}
		if (textuname.isEmpty())			
		{
			if (textpwd.isEmpty())
			{
				log.info("Email/Mobile and Password are empty :" + textuname + " " +tcno);		
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\Login\\"+tcno+".png"));
				String alert5 = RowCount.get(0).getText();
				String alert6 = RowCount.get(1).getText();
				assertEquals(alert5,"Please enter valid email / phone number.");
				assertEquals(alert6,"Password field is empty.");
			}
		}
		WebDriverWait okbtn = new WebDriverWait(driver, 60);
		okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
		driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
	}
}
