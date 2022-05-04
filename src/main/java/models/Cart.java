package models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.List;

public class Cart extends BasePage {


    private List<Product> productsList;

    public Cart(WebDriver driver, List<Product> productsList) {
        super(driver);
        this.productsList = productsList;
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public Double sumListValuesInCart(Cart elementList){
        double sum = 0;
        for (Product product : elementList.getProductsList()) {
            sum += product.getPrice();
        }
        return sum;
    }

//    public Cart(WebDriver driver) {
//        super(driver);
//    }
}
