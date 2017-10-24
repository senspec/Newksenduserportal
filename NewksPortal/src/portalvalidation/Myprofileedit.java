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

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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



																
public class Myprofileedit {
private WebDriver driver;
private static final Logger log = LogManager.getLogger(Myprofileedit.class.getName());

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
																
	@DataProvider(name = "EditProfile")
	public Object[][] dataProvider() 
	{
		Object[][] testData = ExcelUtility.getTestData("Invalid_EditProfile");
		return testData;
	}
																	
//	@AfterTest
//	public void tearDown(ITestResult testResult) throws IOException {
//			Screenshots.takeScreenshot(driver, testResult.getName());
//		
//		//driver.quit();
//	}


	@AfterClass
	public void shutDownSelenium(){
		 driver.close();
	
	}
	
	@Test(dataProvider="EditProfile")
	public void testUsingExcel(String usrname, String usrpwd, String fnmprof, String lnmprof, String lmailprof, String lmobileprof, String lbrthdayprof, String lfavlocationprof, String lzipcodeprof,String fname, String lname, String zipcd, String favlc, String favlocdesc, String tcno) throws Exception 
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
		System.out.println("textuname :" + textuname);
		System.out.println("Password text is :" + textpwd);

		
		driver.findElement(By.xpath(property.getProperty("loginbtn"))).click();
		
		Thread.sleep(6000);
		
		WebElement firstnameprof = driver.findElement(By.xpath(property.getProperty("proffname")));
		String fnnameprof = firstnameprof.getText();
		System.out.println("First Name Prof :" + fnnameprof);
		System.out.println("First Name input :" + fnmprof);
		try
		{
			assertEquals(fnnameprof,fnmprof);
		}catch (AssertionError e)
		{
			log.error("First name is not same as provided during registration:");
		}
		
		
		WebElement lastnameprof = driver.findElement(By.xpath(property.getProperty("proflname")));
		String lnnameprof = lastnameprof.getText();
		
		try
		{
			assertEquals(lnnameprof,lnmprof);
		}catch (AssertionError e)
		{
			log.error("Last name is not same as provided during registration:");
		}
		
		WebElement emailprof = driver.findElement(By.xpath(property.getProperty("profemail")));
		String lemailprof = emailprof.getText();
		
		
		try
		{
			assertEquals(lemailprof,lmailprof);
		}catch (AssertionError e)
		{
			log.error("Email ID is not same as provided during registration:");
		}
		
		WebElement mobprof = driver.findElement(By.xpath(property.getProperty("profmob")));
		String lmobprof = mobprof.getText();
		
		try
		{
			assertEquals(lmobprof,lmobileprof);
		}catch (AssertionError e)
		{
			log.error("Mobile Number is not same as provided during registration:");
		}
		
		WebElement birthprof = driver.findElement(By.xpath(property.getProperty("profbirth")));
		String lbirthprof = birthprof.getText();
		
		try
		{
			assertEquals(lbirthprof,lbrthdayprof);
		}catch (AssertionError e)
		{
			log.error("Birthdate is not same as provided during registration:");
		}
		
		WebElement favlocprof = driver.findElement(By.xpath(property.getProperty("proffavloc")));
		String lfavlocprof = favlocprof.getText();
		
		
		try
		{
			assertEquals(lfavlocprof,lfavlocationprof);
		}catch (AssertionError e)
		{
			log.error("Location is not same as expected:");
		}
		
		WebElement zipprof = driver.findElement(By.xpath(property.getProperty("profzip")));
		String lzipprof = zipprof.getText();
		
		try
		{
			assertEquals(lzipprof,lzipcodeprof);
		}catch (AssertionError e)
		{
			log.error("Zipcode is not same as expected:");
		}
		
		
		driver.findElement(By.xpath(property.getProperty("editbtn"))).click();
		
		WebElement firstnm = driver.findElement(By.xpath(property.getProperty("firstnamedite")));
		firstnm.clear();
		firstnm.sendKeys(fname);
		String fnname = firstnm.getAttribute("value");
			
		WebElement lastnm = driver.findElement(By.xpath(property.getProperty("lastnameedit")));
		lastnm.clear();
		lastnm.sendKeys(lname);
		String lnname = lastnm.getAttribute("value");
		
		WebElement zipus = driver.findElement(By.xpath(property.getProperty("zipcodeedit")));
		zipus.clear();
		zipus.sendKeys(zipcd);
		String zipusa = zipus.getAttribute("value");
		zipus.sendKeys(Keys.TAB);
		Thread.sleep(6000);
				
		boolean value = true;
		boolean zipvalid = true;
		//boolean favloc = true;
		
		if (zipusa.isEmpty())
		{
			Thread.sleep(3000);
			log.info("zip is empty on TAB:" + textuname + " " + tcno);
			Thread.sleep(3000);
			WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
			List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
			String TXT1 = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_tab.png"));
			value = false;
			assertEquals("Please enter valid zip code to view the stores in nearby locations.", TXT1);
			WebDriverWait okbtn = new WebDriverWait(driver, 60);
			okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
			driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
		}	
		else
		{
			if (zipusa.length() <=4 || (zipusa.length() >=7)) 					
			{
				log.info("Zip length mismatch on TAB:" + textuname + " " + tcno);
				zipvalid = false;
				Thread.sleep(3000);
				WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
				List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
				String TXT = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_tab.png"));
				assertEquals("Zip code is not valid.", TXT);
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
			if (zipusa.length() >=5 && (zipusa.length() <=6))
			{
			
				Thread.sleep(3000);
				if (!favlc.equals("N"))
				{
					zipus.sendKeys(Keys.SHIFT);
					zipus.sendKeys(Keys.TAB);
					WebElement storessrch = driver.findElement(By.xpath(property.getProperty("storesrchedit")));
					storessrch.click();
				
					Thread.sleep(3000);
					WebElement strtxt = driver.findElement(By.xpath(property.getProperty("storesrchtxtedit")));
					strtxt.sendKeys(favlc);
					Thread.sleep(3000);
				
					//String xp_start = ".//*[@id='profile-container']/div[1]/div/div[4]/div/div/div/div[2]/table[1]/tbody[1]/tr[6]/td[2]/ul/li[";
					//String xp_end = "]/a";
					String xp_start = property.getProperty("xp_startedit");
					String xp_end = property.getProperty("xp_endedit");
					
//					List<WebElement> storeloc = driver.findElements(By.xpath(xp_start + xp_end));
//					int j = storeloc.size();
//					System.out.println("The size is " + storeloc.size());
//					System.out.println("xp start is: " + xp_start);
//					System.out.println("xp end is: " + xp_end);
//					
					
					for(int i=3; i<=52;i++)
					{
						String x = driver.findElement(By.xpath(xp_start + i + xp_end)).getText();
						
						System.out.println("The value of x is: " + x );
						System.out.println("The fav loc desc: " + favlocdesc);
						if (x.contains(favlocdesc))
						{
							driver.findElement(By.xpath(xp_start + i + xp_end)).click();
							break;
						}	
					}
				}
			}
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath(property.getProperty("savebtnedit"))).click();
		
		if (fnname.isEmpty())
		{
			log.info("First name is empty on Submit:" + textuname + " " + tcno);
			WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
			List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
			String TXT2 = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_submit.png"));
			assertEquals("First Name is required.", TXT2);
			WebDriverWait okbtn = new WebDriverWait(driver, 60);
			okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
			driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
		}
		else
		{
			if (fnname.length() >=1 && fnname.length() <2 ) 
			{
				log.info("First name less than 2 chars on Submit:" + textuname + " " + tcno);
				WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
				List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
				String TXT1 = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_submit.png"));
				assertEquals("First name should have minimum 2 characters.", TXT1);
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}	
		}
		if (lnname.isEmpty())
		{
			System.out.println("Last name empty :" + lnname);
			log.info("Last name is empty on Submit:" + textuname + " " + tcno);
			WebElement dialog = driver.findElement(By.cssSelector(property.getProperty("popup")));
			List<WebElement> RowCount = dialog.findElements(By.tagName("p"));
			String TXT3 = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_submit.png"));
			assertEquals("Last name is required.", TXT3);
			WebDriverWait okbtn = new WebDriverWait(driver, 60);
			okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
			driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
		}
		
		if (!zipusa.isEmpty() )
		{		
			if (!zipvalid) 
			{
				log.info("Zip  invalid and favloc empty on Submit:" + textuname + " " + tcno);
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String TXT = RowCount.get(0).getText();
				String TXT1 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_submit.png"));
				assertEquals("Zipcode is not valid.", TXT);
				assertEquals("Please select Favorite Location.", TXT1);
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
		}
		
		if (zipusa.isEmpty())
		{
				log.info("Zip empty and favloc empty on Submit:" + textuname + " " + tcno);
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String TXT = RowCount.get(0).getText();
				String TXT1 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_submit.png"));
				assertEquals("Zipcode is is required.", TXT);
				assertEquals("Please select Favorite Location.", TXT1);
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
		}

		if (favlc.equals("N") )
		{	
			if (zipvalid && value)
			{
				System.out.println("favloc is empty :" + fnname);
				log.info("Zip valid and favloc empty on Submit:" + textuname + " " + tcno);
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String TXT10 = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\EditProfile\\"+tcno+"_submit.png"));
				assertEquals("Please select Favorite Location.", TXT10);
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
		}
		
		
		
		
		
	}
}





