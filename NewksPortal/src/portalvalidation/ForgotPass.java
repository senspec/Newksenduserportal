package portalvalidation;
																
import static org.testng.Assert.assertEquals;
import org.apache.commons.io.FileUtils;
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

//import org.apache.logging.log4j.core.util.FileUtils;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import utilities.Screenshots;

public class ForgotPass {
	
	private static final Logger log = LogManager.getLogger(ForgotPass.class.getName());

	private WebDriver driver;
	
		@BeforeClass
		public void setUp() throws Exception 
		{
	
			//Firefox browser:
			ProfilesIni prof = new ProfilesIni();
			FirefoxProfile ffProfile= prof.getProfile ("Senthil");
			ffProfile.setAcceptUntrustedCertificates(true);
			ffProfile.setAssumeUntrustedCertificateIssuer(true);
			driver = new FirefoxDriver(ffProfile);				
//			
//			//Chrome browser
//			DesiredCapabilities capability = DesiredCapabilities.chrome();
//			capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//			driver = new ChromeDriver(capability);
			
//			//IE Browser
//			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
//			caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//			caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
//			InternetExplorerDriver driver = new InternetExplorerDriver(caps);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			ExcelUtility.setExcelFile(Configuration.File_Path + Configuration.File_Name, "RegistrationTests");
		}
		
		
		@DataProvider(name = "ForgotnData")
		public Object[][] dataProvider() 
		{
			Object[][] testData = ExcelUtility.getTestData("Invalid_Forgot");
			return testData;
		}
			
		@AfterClass
		public void shutDownSelenium(){
			driver.close();
		}
		
		
	@Test(dataProvider="ForgotnData")
	public void testUsingExcel(String usrname,String alreadyexist,String tcno) throws Exception 
	{
		Properties property = new Properties();
		FileInputStream objfile = new FileInputStream("E:\\GitWebDriverMaven\\NewksPortal\\src\\utilities\\OR.properties");
	   	property.load(objfile);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get(Configuration.URL);
		
		Thread.sleep(6000);
		WebElement fgtLink = driver.findElement(By.xpath(property.getProperty("resetlink")));
		//WebElement fgtLink = driver.findElement(By.xpath(".//*[@id='login-fields']/div[1]/form/div[3]/div/div[1]/div/a[1]"));
		fgtLink.click();

		WebElement emailadd = driver.findElement(By.xpath(property.getProperty("emailbox")));
		emailadd.clear();
		emailadd.sendKeys(usrname);
		String textuname = emailadd.getAttribute("value");
		
		String emailregex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@{1}(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		
		driver.findElement(By.xpath(property.getProperty("sendemailbtn"))).click();

		
		
		if (!textuname.isEmpty())
		{
				Pattern pattern = Pattern.compile(emailregex);
				Matcher m = pattern.matcher(textuname);		

			if (!m.find())
			{
				log.info("Email Address not Valid :" + textuname+" " + tcno);
				WebDriverWait wait = new WebDriverWait(driver, 60);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(property.getProperty("popup"))));

				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ForgotPassword\\"+tcno+".png"));
				WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
				List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
				String alerttxt = RowCount.get(0).getText();
				
				assertEquals(alerttxt,"Email Address is not valid.");
			}
			else
			{
				if (alreadyexist.equals("N"))
				{
					log.info("Sorry, The Email Id is not registered with us. :" + textuname +" "+ tcno);
					WebDriverWait wait = new WebDriverWait(driver, 60);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(property.getProperty("popup"))));
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ForgotPassword\\"+tcno+".png"));
					WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
					List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
					String alerttxt = RowCount.get(0).getText();
					assertEquals(alerttxt,"Sorry, The Email Id is not registered with us.");
				}
				if (alreadyexist.equals("Y"))
				{
					log.info("Forgot Password email sent, please check your inbox.:" + textuname+" " + tcno);
					Thread.sleep(6000);
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ForgotPassword\\"+tcno+".png"));
					String alerttxt = driver.findElement(By.xpath("//p[contains(.,'Forgot Password email sent to "+textuname+", please check your inbox.')]")).getText();
					assertEquals(alerttxt,"Forgot Password email sent to "+textuname+", please check your inbox.");
				}
			}
		}
		else
		{
			log.info("Email Address is required. :" + textuname+" "+tcno);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(property.getProperty("popup"))));
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ForgotPassword\\"+tcno+".png"));
			WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
			List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
			String alerttxt = RowCount.get(0).getText();
			assertEquals(alerttxt,"Email Address is required.");
		}
		
			WebDriverWait okbtn = new WebDriverWait(driver, 60);
			okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
			driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
	}		
	
}	
			 
		

