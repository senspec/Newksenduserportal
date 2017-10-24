package portalvalidation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
															
import utilities.Configuration;
import utilities.ExcelUtility;

public class ProfileLink {
	
	private WebDriver driver;
	public String expectedurl;
	
	private static final Logger log = LogManager.getLogger(ProfileLink.class.getName());

		@BeforeClass
		public void setUp() throws Exception 
		{
																			
			ProfilesIni prof = new ProfilesIni();
			FirefoxProfile ffProfile= prof.getProfile ("Senthil");
			ffProfile.setAcceptUntrustedCertificates(true);
			ffProfile.setAssumeUntrustedCertificateIssuer(true);
			System.setProperty("webdriver.gecko.driver", "E:\\geckodriver-v0.14.0-win64\\geckodriver.exe");
			driver = new FirefoxDriver(ffProfile);	
			
							// Maximize the browser's window
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			ExcelUtility.setExcelFile(Configuration.File_Path + Configuration.File_Name, "RegistrationTests");
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.get(Configuration.URL);
			
		}
																	
		@DataProvider(name = "LinkValidate")
		public Object[][] dataProvider() 
		{
			Object[][] testData = ExcelUtility.getTestData("Linkvalidation");
			return testData;
		}
																		
//		@AfterTest
//		public void tearDown(ITestResult testResult) throws IOException {
//				Screenshots.takeScreenshot(driver, testResult.getName());
//			
//			//driver.quit();
//		}


		@AfterClass
		public void shutDownSelenium(){
			 driver.close();
		
		}
		
		@Test(dataProvider="LinkValidate")
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
			
			WebElement Upwd = driver.findElement(By.xpath(property.getProperty("emailmobpwd")));
			Upwd.clear();
			Upwd.sendKeys(usrpwd);
			
			driver.findElement(By.xpath(property.getProperty("loginbtn"))).click();
			
				Thread.sleep(6000);
				String parentHandle = driver.getWindowHandle();

				
				driver.switchTo().frame(0);
				
				driver.findElement(By.xpath("html/body/header/nav/ul/li[1]/a")).click();
				expectedurl = "https://order.newks.com/vendor/search";
				Thread.sleep(3000);
				Set<String> handles = driver.getWindowHandles();
				for (String handle: handles) 
				{
					
					if (!handle.equals(parentHandle)) 
					{
						
						driver.switchTo().window(handle);
						Thread.sleep(6000);
						String navigatedurl = driver.getCurrentUrl();
						
						File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ProfileLink\\"+tcno+"_Order.png"));

						Assert.assertEquals(navigatedurl, expectedurl);	
						log.info("Navigated to Order Page: " + tcno);
						break;
								
					}
				
				}
				driver.close();
				driver.switchTo().window(parentHandle);
				driver.switchTo().defaultContent();
				
				
				driver.switchTo().frame(0);
				driver.findElement(By.xpath("html/body/header/nav/ul/li[2]/a")).click();
				expectedurl = "http://www.newks.com/locations/";
				Thread.sleep(3000);
				Set<String> handles1 = driver.getWindowHandles();
				for (String handle: handles1) 
				{
					System.out.println(handle);
					if (!handle.equals(parentHandle)) 
					{
						driver.switchTo().window(handle);
						Thread.sleep(6000);
						String navigatedurl = driver.getCurrentUrl();
						File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ProfileLink\\"+tcno+"_Locations.png"));

						Assert.assertEquals(navigatedurl, expectedurl);
						log.info("Navigated to Locations Page: " + tcno);
						break;
					}
				
				}
				driver.close();
				driver.switchTo().window(parentHandle);
				driver.switchTo().defaultContent();
				
			
				driver.switchTo().frame(0);
				driver.findElement(By.xpath("html/body/header/nav/ul/li[3]/a")).click();
				expectedurl = "http://www.newks.com/inside-the-eatery/";
				Thread.sleep(3000);
				Set<String> handles2 = driver.getWindowHandles();
				for (String handle: handles2) 
				{
					if (!handle.equals(parentHandle)) 
					{
						driver.switchTo().window(handle);
						Thread.sleep(6000);
						String navigatedurl = driver.getCurrentUrl();
						File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ProfileLink\\"+tcno+"_Eatery.png"));

						Assert.assertEquals(navigatedurl, expectedurl);
						log.info("Navigated to Eatery Page: " + tcno);
						break;
					}
				
				}
				driver.close();
				driver.switchTo().window(parentHandle);
				driver.switchTo().defaultContent();
				
				
				driver.switchTo().frame(0);
				driver.findElement(By.xpath("html/body/header/nav/ul/li[4]/a")).click();
				expectedurl = "http://www.newks.com/catering/";
				Thread.sleep(3000);
				Set<String> handles3 = driver.getWindowHandles();
				for (String handle: handles3) 
				{
					System.out.println(handle);
					if (!handle.equals(parentHandle)) 
					{
						driver.switchTo().window(handle);
						Thread.sleep(6000);
						String navigatedurl = driver.getCurrentUrl();
						File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ProfileLink\\"+tcno+"_Catering.png"));

						Assert.assertEquals(navigatedurl, expectedurl);
						log.info("Navigated to Catering Page: " + tcno);
						break;
					}
				
				}
				driver.close();
				driver.switchTo().window(parentHandle);
				driver.switchTo().defaultContent();
				
				
				driver.switchTo().frame(0);
				driver.findElement(By.xpath("html/body/header/nav/ul/li[5]/a")).click();
				expectedurl = "https://newks.fishbowlcloud.com/enduserportal/index.html#/registration";
				Thread.sleep(3000);
				Set<String> handles4 = driver.getWindowHandles();
				for (String handle: handles4) 
				{
					if (!handle.equals(parentHandle)) 
					{
						driver.switchTo().window(handle);
						Thread.sleep(6000);
						String navigatedurl = driver.getCurrentUrl();
						File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\ProfileLink\\"+tcno+"_RoundTable.png"));

						Assert.assertEquals(navigatedurl, expectedurl);
						log.info("Navigated to RoundTable Page: " + tcno);
						break;
					}
				
				}
				driver.close();
				driver.switchTo().window(parentHandle);
				driver.switchTo().defaultContent();
	

		}
}

