package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InventoryPage {

    private final WebDriver driver;

    private final By title       = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By cartBadge   = By.className("shopping_cart_badge");
    private final By menuButton  = By.id("react-burger-menu-btn");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.findElement(title).getText();
    }

    public int getItemCount() {
        List<WebElement> items = driver.findElements(inventoryItems);
        return items.size();
    }

    public boolean isMenuDisplayed() {
        return driver.findElement(menuButton).isDisplayed();
    }

    public String getCartBadgeText() {
        return driver.findElement(cartBadge).getText();
    }
}