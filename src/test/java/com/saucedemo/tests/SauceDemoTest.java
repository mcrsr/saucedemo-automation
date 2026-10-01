package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SauceDemoTest {

    private static final String BASE_URL = "https://www.saucedemo.com/";
    private static final String VALID_USER = "standard_user";
    private static final String VALID_PASS = "secret_sauce";
    private static final String LOCKED_USER = "locked_out_user";

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
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Valid user can log in and see the inventory page")
    public void validLoginTest() {
        InventoryPage inventory = loginPage.login(VALID_USER, VALID_PASS);

        Assert.assertEquals(inventory.getTitle(), "Products",
                "Page title should be 'Products'");
        Assert.assertEquals(inventory.getItemCount(), 6,
                "Inventory should contain 6 items");
        Assert.assertTrue(inventory.isMenuDisplayed(),
                "Menu button should be visible after login");
    }

    @Test(description = "Locked out user sees an error message")
    public void lockedOutUserTest() {
        loginPage.enterUsername(LOCKED_USER)
                .enterPassword(VALID_PASS);
        loginPage.clickLogin();

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Sorry, this user has been locked out"),
                "Error message should mention locked out user");
    }

    @Test(description = "Invalid credentials show an error message")
    public void invalidCredentialsTest() {
        loginPage.enterUsername("wrong_user")
                .enterPassword("wrong_pass");
        loginPage.clickLogin();

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"),
                "Error message should indicate invalid credentials");
    }
}