package pages;

import models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationPage extends BasketPage{
    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".order-line.row")
    private List<WebElement> productsList;

    @FindBy(css = "div.col-md-4 ul li:nth-child(2)")
    private WebElement paymentDetails;

    @FindBy(css = "div.col-md-4 ul li:nth-child(3)")
    private WebElement shippingDetails;


    public List<WebElement> getListOfProducts() {
        return productsList;
    }

    public List<Product> getProductInfoFromConfirmationPage() {
        List<WebElement> productsList = getListOfProducts();
        List<Product> productValues = new ArrayList<>();

        for (int i = 0; i < productsList.size(); i++) {
            String name = productsList.get(i).findElement(By.cssSelector(".col-sm-4.col-xs-9.details")).getText();
            String nameFormatted = name;
            String productPrice = productsList.get(i).findElement(By.cssSelector(".col-xs-4.text-sm-center.text-xs-right.bold")).getText().substring(1);
            double priceDouble = Double.parseDouble(productPrice);
            double productPriceFormatted = Double.parseDouble(df.format(priceDouble));
            int quantity = Integer.parseInt(productsList.get(i).findElement(By.xpath("//div[@class='col-sm-6 col-xs-12 qty']/div/div[@class='col-xs-4 text-sm-center']")).getText());
            String totalPriceBasket = productsList.get(i).findElement(By.cssSelector(".total-value.font-weight-bold td")).getText().substring(1);
            double totalPriceDouble = Double.parseDouble(totalPriceBasket);
            double totalPriceFormatted = Double.parseDouble(df.format(totalPriceDouble));
            productValues.add(new Product(name, productPriceFormatted, quantity, totalPriceFormatted));
        }
        return productValues;
    }

}
