package org.duckdns.valci.opensongbook.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SeleniumTests {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeTest
    public void setUp() throws Exception {
        // File pathToBinary = new File("D:\\PROGRAMS\\Firefox\\firefox.exe");
        // FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        // FirefoxProfile firefoxProfile = new FirefoxProfile();
        // WebDriver driver = new FirefoxDriver(ffBinary,firefoxProfile);
        driver = new FirefoxDriver();
        // WebDriver driver = new HtmlUnitDriver(true);
        baseUrl = "http://localhost:8080/opensongbook/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void simpleLoginTest() throws Exception {
        // Alert alert = driver.switchTo().alert();
        // alert.accept();
        driver.get(baseUrl);
        // driver.wait(500000);
        // Sleep until the div we want is visible or 5 seconds is over
        /*
         * long end = System.currentTimeMillis() + 5000; while
         * (System.currentTimeMillis() < end) { WebElement resultsDiv =
         * driver.findElement(By.id("usernameTextBoxLoginView")); // If results
         * have been returned, the results are displayed in a drop down. if
         * (resultsDiv.isDisplayed()) { break; } }
         */
        driver.findElement(By.id("usernameTextBoxLoginView")).clear();
        driver.findElement(By.id("usernameTextBoxLoginView")).sendKeys("test");
        driver.findElement(By.id("passwordTextBoxLoginView")).clear();
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.id("songEditorButton")).click();
        driver.findElement(By.xpath("//div[@id='songListTable']/div[2]/div/table/tbody/tr[4]/td/div")).click();
        String song = driver.findElement(By.id("songTextInput")).getAttribute("value");
        System.out.println("####################");
        System.out.println(song.toString());
        System.out.println("####################");
    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            // fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
