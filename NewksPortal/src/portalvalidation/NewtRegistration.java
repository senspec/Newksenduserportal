package portalvalidation;
																																					
import static org.testng.Assert.assertEquals;
import java.io.File;																					
import java.io.FileInputStream;
import java.util.List;																					
import java.util.Properties;																					
import java.util.concurrent.TimeUnit;																					
import java.util.regex.Matcher;																					
import java.util.regex.Pattern;																					
																					
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;																					
import org.openqa.selenium.Keys;																					
import org.openqa.selenium.OutputType;																					
import org.openqa.selenium.TakesScreenshot;																					
import org.openqa.selenium.WebDriver;																					
import org.openqa.selenium.WebElement;																					
import org.openqa.selenium.firefox.FirefoxDriver;																					
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;																					
import org.openqa.selenium.support.ui.WebDriverWait;																					
import org.testng.annotations.AfterTest;																					
import org.testng.annotations.BeforeClass;																					
import org.testng.annotations.DataProvider;																					
import org.testng.annotations.Test;																					
																																				
import utilities.Configuration;
import utilities.ExcelUtility;
public class NewtRegistration {
private WebDriver driver;
private static final Logger log = LogManager.getLogger(NewtRegistration.class.getName());

public String cpwdalert;
public String zipusa;
public String birthalert;
public String mobilealert;
public boolean noerror = true;
public static String day;
public static String dtalert;
public static String year_str;
public static String dateTime;

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
}

@DataProvider(name = "RegistrationloginData")

public Object[][] dataProvider() 
{
	Object[][] testData = ExcelUtility.getTestData("Invalid_Registration");
	return testData;
}

@AfterTest
public void shutDownSelenium() 
{
	driver.close();
}
																													
@Test(dataProvider="RegistrationloginData")
	public void testUsingExcel(String fname, String lname, String email,String cfemail,String password, String cpwd, String bdate, String mobno, String zipcd, String favlc, String rec, String agree,	String emailmob,String tcno) throws Exception 

	{
		//Robot robot = new Robot();
		Properties property = new Properties();																						
		FileInputStream objfile = new FileInputStream("E:\\GitWebDriverMaven\\NewksPortal\\src\\utilities\\OR.properties");
		property.load(objfile);																					   	
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.get(Configuration.URL);																						
		Thread.sleep(10000);																						
		driver.findElement(By.xpath(property.getProperty("createone"))).click();
		Thread.sleep(10000);
		WebElement firstnm = driver.findElement(By.cssSelector(property.getProperty("firstname")));
		firstnm.sendKeys(fname);
		String fnname = firstnm.getAttribute("value");
		WebElement lastnm = driver.findElement(By.cssSelector(property.getProperty("lastname")));
		lastnm.sendKeys(lname);
		String lnname = lastnm.getAttribute("value");
		WebElement emails = driver.findElement(By.cssSelector(property.getProperty("emailid")));
		emails.sendKeys(email);
		String emailalert = emails.getAttribute("value");
		Pattern emailpattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		Matcher m = emailpattern.matcher(emailalert);
		WebElement cfemails = driver.findElement(By.cssSelector(property.getProperty("confirmemailid")));
		cfemails.sendKeys(cfemail);
		String cfemailalert = cfemails.getAttribute("value");
		cfemails.sendKeys(Keys.TAB);
		boolean passwd = true;
		if (!emailalert.equals(cfemailalert))																					
		{
			if (!cfemailalert.isEmpty())
			{
				log.info("Email and Conf email mismatch on tab :" + fnname +" "+tcno);
				noerror = false;
				Thread.sleep(6000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
				assertEquals(alert,"Your email and confirmation email do not match.");
			    WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
				cfemailalert = "";
				emailalert="";
			}
			else
			{
				//System.out.println("Email not empty and Conf email is empty :" + fnname +" "+tcno);
			}
		}
	
		WebElement pwd = driver.findElement(By.cssSelector(property.getProperty("password")));
		pwd.sendKeys(password);
		String pwddalert= pwd.getAttribute("value");
		pwd.sendKeys(Keys.TAB);
		
		if (pwddalert.isEmpty())
		{						
			WebElement cpwdnoemp = driver.findElement(By.cssSelector(property.getProperty("confirmpassword")));		
			cpwdnoemp.sendKeys(cpwd);		
			cpwdalert = cpwdnoemp.getAttribute("value");		
			cpwdnoemp.sendKeys(Keys.TAB);			
			if (!cpwdalert.isEmpty())		
			{		
				log.info("Password Empty on TAB: " + fnname + " " + tcno);
				noerror = false;
				WebElement T = 	driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));														
				List<WebElement> RowCount = T.findElements(By.tagName("p"));	
				String alert = RowCount.get(0).getText();		
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
				assertEquals(alert,"Please enter password.");		
				WebDriverWait okbtn = new WebDriverWait(driver, 60);		
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));		
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
				pwddalert="";
				cpwdalert="";
				passwd = false;
			}		
			else		
			{		
				//System.out.println("Password and Confirm Password is empty");		
			}		
		}
		else
		{
			if (pwddalert.length() <=6)
			{
				log.info("Password Length Error on TAB: " + fnname + " " + tcno);
				noerror = false;
//				WebElement cpwdnoemp = driver.findElement(By.cssSelector(property.getProperty("confirmpassword")));		
//				cpwdnoemp.sendKeys(cpwd);		
//				cpwdalert = cpwdnoemp.getAttribute("value");		
				Thread.sleep(6000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));		
				String alert = RowCount.get(0).getText();		
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
				//String alert = driver.findElement(By.xpath("//p[contains(.,'Password should have minimum 6 characters')]")).getText();		
				assertEquals(alert,"Password should have minimum 6 characters.");		
				WebDriverWait okbtn = new WebDriverWait(driver, 60);		
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));		
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();		
				pwddalert="";
				cpwdalert="";
				passwd = false;
			}
			else
			{
				WebElement cpwdnoemp = driver.findElement(By.cssSelector(property.getProperty("confirmpassword")));		
				cpwdnoemp.sendKeys(cpwd);		
				cpwdalert = cpwdnoemp.getAttribute("value");	
				cpwdnoemp.sendKeys(Keys.TAB);
				
			}
		}
		if (!pwddalert.isEmpty() && !cpwdalert.isEmpty() && passwd)	
		{																							
			if (!pwddalert.equals(cpwdalert))																										
			{
				//System.out.println("Password and Conf Password mismatch on TAB: " + fnname + " " + tcno );
				noerror = false;
				log.info("Password and Conf Password mismatch on TAB: " + fnname + " " + tcno);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
				assertEquals(alert,"Your password and confirmation password do not match.");
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
				pwddalert = "";
				cpwdalert="";
			}
		}
		if (!bdate.equals("N"))
		{
			//driver.findElement(By.xpath(".//*[@id='customerBirthdate']")).sendKeys(bdate);
			WebElement birthdt = driver.findElement(By.xpath(property.getProperty("birthdate")));
			birthdt.sendKeys(bdate);
			birthalert = birthdt.getAttribute("value");
			birthdt.sendKeys(Keys.TAB);
		}
		else
		{
			birthalert="";
		}
					
					//((JavascriptExecutor)driver).executeScript("scroll(0,500)");
					
		WebElement mobnoalert = driver.findElement(By.xpath(property.getProperty("mobilephone")));
		mobnoalert.sendKeys(mobno);
		mobilealert = mobnoalert.getAttribute("value");
		mobnoalert.sendKeys(Keys.TAB);
		WebElement zipus = driver.findElement(By.xpath(property.getProperty("zipcode")));
		zipus.sendKeys(zipcd);
		zipusa = zipus.getAttribute("value");
		zipus.sendKeys(Keys.TAB);
		Thread.sleep(6000);

		//boolean validzipcode = true;
		boolean zipvalid = true;
		
		if (!zipusa.isEmpty())
		{
			if (zipusa.length() <=4)
			{
				//System.out.println("Zip length mismatch on TAB: " + fnname + " " + tcno );
				log.info("Zip length mismatch on TAB: " + fnname + " " + tcno);
				noerror = false;
				zipvalid = false;
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
				//Thread.sleep(10000);
				//String alert = driver.findElement(By.xpath("//p[contains(.,'Your email and confirmation email do not match.')]")).getText();
				assertEquals(alert,"Zip code is not valid.");
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
			if (zipusa.length() >=7)
			{
				//System.out.println("Zip length mismatch on TAB: " + fnname + " " + tcno );
				log.info("Zip length mismatch on TAB: " + fnname + " " + tcno);
				noerror = false;
				zipvalid = false;
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
				//Thread.sleep(10000);
				//String alert = driver.findElement(By.xpath("//p[contains(.,'Your email and confirmation email do not match.')]")).getText();
				assertEquals(alert,"Zip code is not valid.");
				WebDriverWait okbtn = new WebDriverWait(driver, 60);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
		}	
		else
		{
			//System.out.println("Zip is empty on TAB: " + fnname + " " + tcno );
			log.info("Zip is empty on TAB: " + fnname + " " + tcno);
			noerror = false;
			zipvalid = false;
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));	
			String alert = RowCount.get(0).getText();	
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Tab Action.png"));
			assertEquals(alert,"Please enter your zip code.");	
			WebDriverWait okbtn = new WebDriverWait(driver, 60);
			okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
			driver.findElement(By.xpath(property.getProperty("okbtn"))).click();	
	
		}
	
		if(!favlc.equals("N") && zipvalid)
		{
			Select sel1 = new Select(driver.findElement(By.xpath(".//*[@id='scustom-append']/div/select")));										
			sel1.selectByValue(favlc);
		}	
			

		if(agree.equals("Y"))
		{
			driver.findElement(By.xpath(property.getProperty("agreement"))).click();
		}
	
		driver.findElement(By.xpath(property.getProperty("submit"))).click();
	
		if (fnname.isEmpty())
		{
			//System.out.println("fnname is empty on Submit: "+ fnname );
			noerror = false;
			log.info("fname is empty on Submit: " + fnname + " " + tcno);
			Thread.sleep(3000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"First Name is required.");
		}
		else
		{
			if (fnname.length() >=1 && fnname.length() <2 ) 
			{
				//System.out.println("fname less than 2 chars on Submit: " + fnname +" "+tcno);
				log.info("fname is less than 2 chars on Submit: " + fnname + " " + tcno);
				noerror = false;
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				assertEquals(alert,"First Name should have minimum 2 characters.");
			}
		}
		if (lnname.isEmpty())
		{
			//System.out.println("Last name empty on Submit:" + fnname +" "+tcno);
			log.info("Last name  is empty on Submit: " + fnname + " " + tcno);
			noerror = false;
			Thread.sleep(3000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"Last Name is required.");
		}
		boolean emailnoempty = true;
		if (emailalert.isEmpty())
		{
			if (cfemailalert.isEmpty())
			{
				//System.out.println("email and confirm email empty on Submit:" + fnname +" "+tcno);
				log.info("email and conf email are empty on Submit: " + fnname + " " + tcno);
				noerror = false;
				emailnoempty =false;
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert1 = RowCount.get(0).getText();
				String alert2 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));				
				assertEquals(alert1,"E-mail Address is required.");
				assertEquals(alert2,"Please enter Confirmation E-mail Address.");
			}
		}
		
		
		if (!m.find() && emailnoempty)   
		{
			if (cfemailalert.isEmpty())
			{
				//System.out.println("email formatt error on submit :" + fnname +" "+tcno);	
				log.info("email formatt error on Submit: " + fnname + " " + tcno);	
				noerror = false;	
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));	
				List<WebElement> RowCount = T.findElements(By.tagName("p"));	
				String alert1 = RowCount.get(0).getText();	
				String alert2 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);	
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_SubmitAction.png"));
				assertEquals(alert1,"E-mail Address is not valid.");	
				assertEquals(alert2,"Please enter Confirmation E-mail Address.");
			}
		}
		if (m.find() && emailnoempty)   
		{
			if (cfemailalert.isEmpty())
			{
				//System.out.println("email formatt error on submit :" + fnname +" "+tcno);	
				log.info("email formatt error on Submit: " + fnname + " " + tcno);	
				noerror = false;	
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));	
				List<WebElement> RowCount = T.findElements(By.tagName("p"));	
				String alert = RowCount.get(0).getText();	
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);	
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_SubmitAction.png"));
				assertEquals(alert,"Please enter Confirmation E-mail Address.");
			}
		}
		if (pwddalert.isEmpty())
		{
			if (cpwdalert.isEmpty())
			{
				//System.out.println("Password and confirm password empty on Submit:" + fnname +" "+tcno);
				log.info("Password and conf. Password are empty on Submit: " + fnname + " " + tcno);
				noerror = false;
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert1 = RowCount.get(0).getText();
				String alert2 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				assertEquals(alert1,"Password is required.");
				assertEquals(alert2,"Please enter Confirmation Password.");
			}
		}
		if (!pwddalert.isEmpty())
		{	
			if (cpwdalert.isEmpty())
			{
				//System.out.println("Password not empty and confirm email empty on Submit :" + fnname +" "+tcno);
				log.info("Password valid and confirm Password empty on Submit: " + fnname + " " + tcno);
				noerror = false;
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				assertEquals(alert,"Please enter Confirmation Password.");
			}
		}
		if (birthalert.isEmpty())
		{
			//System.out.println("Birthdate empty :" + fnname +" "+tcno);
			log.info("Birthdate is empty on Submit: " + fnname + " " + tcno);
			noerror = false;
			Thread.sleep(3000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"Please select Birth Date.");
			//Thread.sleep(6000);
		}
		if (!mobilealert.isEmpty())
		{
			Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
			Matcher n = pattern.matcher(mobilealert);
			if (!n.find())
			{
				//System.out.println("Mobile number invalid Submit:" + fnname +" "+tcno);
				log.info("Mobile number is not valid on Submit: " + fnname + " " + tcno);
				noerror = false;
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				assertEquals(alert,"The format of Mobile Phone should be XXX-XXX-XXXX");
			}
		}
		else
		{
			//System.out.println("Mobile number empty Submit :" + fnname +" "+tcno);
			log.info("Mobile number is empty: " + fnname + " " + tcno);
			noerror = false;
			Thread.sleep(2000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"Mobile Phone is required.");
			Thread.sleep(6000);
		}
		if (!zipusa.isEmpty())
		{
			if (zipusa.length() <=4)
			{
				//System.out.println("Zip length and Favourite Location not selected on Submit: " + fname + " " + tcno );
				log.info("Zip length error and favourite location not selected on Submit: " + fnname + " " + tcno);
				noerror = false;
				zipvalid=false;
				WebDriverWait pop = new WebDriverWait(driver, 120);
				pop.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".md-dialog-content.warning-pop")));
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				String alert1 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				//String alert = driver.findElement(By.xpath("//p[contains(.,'Your email and confirmation email do not match.')]")).getText();
				assertEquals(alert,"Zipcode is not valid.");
				assertEquals(alert1,"Please select Favorite Location.");
//				WebDriverWait okbtn = new WebDriverWait(driver, 60);
//				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
//				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
			if (zipusa.length() >=7)
			{
				//System.out.println("Zip length and Favourite Location not selected on Submit: " + fname + " " + tcno );
				log.info("Zip length Seven error and favourite location not selected on Submit: " + fnname + " " + tcno);
				noerror = false;
				zipvalid=false;
				WebDriverWait pop = new WebDriverWait(driver, 120);
				pop.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".md-dialog-content.warning-pop")));
				Thread.sleep(3000);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				String alert1 = RowCount.get(1).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				//String alert = driver.findElement(By.xpath("//p[contains(.,'Your email and confirmation email do not match.')]")).getText();
				assertEquals(alert,"Zipcode is not valid.");
				assertEquals(alert1,"Please select Favorite Location.");
//				WebDriverWait okbtn = new WebDriverWait(driver, 60);
//				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
//				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
			}
		}	
		else
		{
			//System.out.println("Zip and Location are empty Submit: " + fname + " " + tcno );
			log.info("Zip and location are empty on Submit: " + fnname + " " + tcno);
			noerror = false;
			WebDriverWait pop = new WebDriverWait(driver,120);
			pop.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".md-dialog-content.warning-pop")));
			Thread.sleep(3000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));	
			String alert = RowCount.get(0).getText();	
			String alert1 = RowCount.get(1).getText();	
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			
			assertEquals(alert,"Zipcode is is required.");
			assertEquals(alert1,"Please select Favorite Location.");
			
		}
		
		if (favlc.equals("N") && zipvalid)
		{	
			//System.out.println("Zip valid and favloc is empty on Submit :" + fnname +" "+tcno);
			log.info("Zip valid but favourite location not selected: " + fnname + " " + tcno);
			noerror = false;
			WebDriverWait pop = new WebDriverWait(driver,120);
			pop.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".md-dialog-content.warning-pop")));
			Thread.sleep(3000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"Please select Favorite Location.");

		}
		
		if (agree.equals("N"))
		{
			//System.out.println("Agreement not agreed :" + fnname +" "+tcno);
			log.info("Agreement not selected on Submit: " + fnname + " " + tcno);
			noerror = false;
			WebDriverWait pop = new WebDriverWait(driver,120);
			pop.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".md-dialog-content.warning-pop")));
			Thread.sleep(3000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"Please confirm you have read the Terms Of Use & Privacy Policy by selecting the appropriate box.");
		}
		if (emailmob.equals("E"))
		{
			//System.out.println("Email exists :" + fnname);
			log.info("Email Already Exists on Submit: " + fnname + " " + tcno);
			noerror = false;
			Thread.sleep(2000);
			WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
			List<WebElement> RowCount = T.findElements(By.tagName("p"));
			String alert = RowCount.get(0).getText();
			File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
			assertEquals(alert,"Email already exists");
		}
		else
		{
			if (emailmob.equals("M"))
			{
				noerror = false;
				//System.out.println("Mobile exists :" + fnname);
				Thread.sleep(2000);
				log.info("Phone Number already exists on Submit: " + fnname + " " + tcno);
				WebElement T = driver.findElement(By.cssSelector(".md-dialog-content.warning-pop"));
				List<WebElement> RowCount = T.findElements(By.tagName("p"));
				String alert = RowCount.get(0).getText();
				File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_Submit Action.png"));
				assertEquals(alert,"Phone number already exists");
			}
			
		}

			if (!noerror) 
			{
				//System.out.println("error found:" + fnname);
				Thread.sleep(2000);
				WebDriverWait okbtn = new WebDriverWait(driver, 120);
				okbtn.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property.getProperty("okbtn"))));
				driver.findElement(By.xpath(property.getProperty("okbtn"))).click();
				
			}
			else
			{
				if (noerror) 
				{
					//System.out.println("No error found:" + fname);
					Thread.sleep(10000);
					File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_SuccessLogin.png"));
					driver.findElement(By.xpath("//span[text()='LOG OUT']")).click();
					File src1 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(src1, new File("E:\\GitWebDriverMaven\\NewksPortal\\test-Screenshot\\NewtReg\\"+tcno+"_SuccessLogout.png"));
				}
			}
			
	}
}


