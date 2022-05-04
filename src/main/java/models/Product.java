package models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.text.DecimalFormat;
import java.util.List;

public class Product extends BasePage {

    private String name;
    private double price;
    private int quantity;
    private double totalPrice;

    public Product(String name, double price, int quantity, double totalPrice, WebDriver driver) {
        super(driver);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

//
//    public Product(WebDriver driver) {
//        super(driver);
//    }

    public String getName() {
        return name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }


}
