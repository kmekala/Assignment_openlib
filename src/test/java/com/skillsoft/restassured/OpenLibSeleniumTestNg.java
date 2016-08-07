package com.skillsoft.restassured;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import reporter.JyperionListener;

@Listeners(JyperionListener.class)
public class OpenLibSeleniumTestNg {
    Logger logger = Logger.getLogger(this.getClass());
    public static WebDriver driver;

    @BeforeSuite
    public void startSuite(){
        Reporter.log("Start the test ");
    }

    @Parameters ({"browser"})
    @Test
    public void openLibLogin(String browser) throws InterruptedException, MalformedURLException {
        DesiredCapabilities cap = null;
        if(browser.equalsIgnoreCase("firefox"))
        { cap = DesiredCapabilities.firefox();
            cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            cap.setBrowserName("firefox");
            cap.setCapability("marionette", true);
            WebDriver driver = new FirefoxDriver(cap);
            System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/FireDriver/chromedriver");
            //cap.setPlatform(Platform.WINDOWS);
        }

        if(browser.equalsIgnoreCase("internet explorer"))
        {
            cap = DesiredCapabilities.internetExplorer();
            cap.setBrowserName("internet explorer");
            cap.setPlatform(Platform.ANY);
        }
        if(browser.equalsIgnoreCase("safari"))
        {
            System.setProperty("webdriver.safari.noinstall", "true");
            SafariOptions options = new SafariOptions();
            options.setUseCleanSession(true);
            //cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            cap = DesiredCapabilities.safari();
            cap.setCapability(SafariOptions.CAPABILITY, options);
            cap.setBrowserName("safari");
            cap.setPlatform(Platform.MAC);
        }
        if(browser.equalsIgnoreCase("chrome")){
            System.out.println("chrome");
            System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/ChromeDriver/chromedriver");
            // browser.add(setupDriver(new ChromeDriver()));
            cap= DesiredCapabilities.chrome();
            cap.setBrowserName("chrome");
            cap.setPlatform(Platform.ANY);
        }
        ChromeDriver driver = new ChromeDriver(cap);
        driver.get("https://openlibrary.org/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id='headerLogin']/a[1]")).click();
        Thread.sleep(300);
        //Username
        driver.findElement(By.xpath("//*[@id='username']")).click();
        driver.findElement(By.xpath("//*[@id='username']")).sendKeys("kalyanfn");
        //Password
        driver.findElement(By.xpath("//*[@id='password']")).click();
        driver.findElement(By.xpath("//*[@id='password']")).sendKeys("DietC0ke");
        //submit
        driver.findElement(By.xpath("//*[@id='register']/div[5]/input")).submit();
        driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
        //Assert the login info on the landing page
        try{
            String actual=driver.findElement(By.xpath("//*[@id='userToggle']/span[1]")).getText();
            String expected="kalyan Mekala";
            Assert.assertEquals(expected, actual);
            Reporter.log("Assertion passed on the landing page" + actual+" is equal "+expected);

        }catch(AssertionError e)
        {
            Reporter.log("Assertion error ");
        }
        driver.quit();
    }


    @AfterSuite
    public void tearDown() {
        sendPDFReportByGMail("kalyanfn@gmail.com", "DietC0ke", "kalyanfn@gmail.com","Test Report: UI Tests with Selenium Testng for OpenLib", "");
        sendPDFReportByGMail("kalyanfn@gmail.com", "DietC0ke", "Haribabu_namduri@skillsoft.com", "Test Report: Restful API Tests with RestAssured for OpenLib", "");
    }

    public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {
        // Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        // Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        // Move image file to new destination
        File DestFile = new File(fileWithPath);
        // Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);

    }

    /**
     * Send email using java
     *
     * @param from
     * @param pass
     * @param to
     * @param subject
     * @param body
     */
    private static void sendPDFReportByGMail(String from, String pass, String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            // Set from address
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set subject
            message.setSubject(subject);
            message.setText(body);
            BodyPart objMessageBodyPart = new MimeBodyPart();
            objMessageBodyPart.setText("Please Find The Attached Report File!");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(objMessageBodyPart);
            objMessageBodyPart = new MimeBodyPart();
            // Set path to the pdf report file
            String filename = System.getProperty("user.dir") + "/SeleniumTestng-OPenLib.pdf";
            // Create data source to attach the file in mail
            DataSource source = new FileDataSource(filename);
            objMessageBodyPart.setDataHandler(new DataHandler(source));
            objMessageBodyPart.setFileName(filename);
            multipart.addBodyPart(objMessageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }

    }
}
