package models;

import org.openqa.selenium.WebDriver;
import pages.BasePage;
import pages.BasketPage;

import java.util.List;

public class Cart extends BasePage {


    private List<Product> productsList;

    public Cart(WebDriver driver, List<Product> productsList) {
        super(driver);
        this.productsList = productsList;
    }

    BasketPage basketPage = new BasketPage(driver);

    public List<Product> getProductsList() {
        return productsList;
    }

    public Double sumProductValuesInCart(Cart elementList){
        double sum = 0;
        for (Product product : elementList.getProductsList()) {
            sum += product.getPrice() * product.getQuantity();
        }
        return sum;
    }

}
