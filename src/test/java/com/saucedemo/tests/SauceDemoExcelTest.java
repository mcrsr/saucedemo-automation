package com.saucedemo.tests;

import com.saucedemo.models.LoginData;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.LoginDataProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SauceDemoExcelTest {

    private static final String BASE_URL = "https://www.saucedemo.com/";

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        loginPage = new LoginPage(driver).open(BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test(dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class,
            description = "Login using data from Excel")
    public void loginFromExcelTest(LoginData data) {

        loginPage.enterUsername(data.getUsername())
                .enterPassword(data.getPassword());
        loginPage.clickLogin();

        String expected = data.getExpectedResult();

        if ("success".equalsIgnoreCase(expected)) {
            InventoryPage inventory = new InventoryPage(driver);
            Assert.assertEquals(inventory.getTitle(), "Products",
                    "Login failed for user: " + data.getUsername());
        } else {
            String actualError = loginPage.getErrorMessage();
            Assert.assertTrue(actualError.contains(expected),
                    "Error mismatch for user " + data.getUsername()
                            + "\nExpected to contain: " + expected
                            + "\nActual: " + actualError);
        }
    }
}