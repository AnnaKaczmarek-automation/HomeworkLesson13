package pages;

import models.Product;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsPage extends BasePage {

    public OrderDetailsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".box.hidden-sm-down")
    private WebElement productSection;

    @FindBy(css = "tbody tr td a")
    private List<WebElement> productsList;

    @FindBy(css = "#delivery-address address")
    private WebElement deliveryAddress;

    @FindBy(css = "#invoice-address address")
    private WebElement invoiceAddress;

    public List<Product> getProductsInfoFromDetailsPage() {
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        waitUntilVisibilityOfElement(productSection);
        List<Product> listOfProducts = new ArrayList<>();
        Product product;
        for (WebElement element : productsList) {
            String name = element.getText();
            String nameFormatted = StringUtils.substringBefore(name, " - ");
            int quantity = Integer.parseInt(driver.findElement(By.cssSelector("table#order-products tbody tr td:nth-child(2)")).getText());
            double price = Double.parseDouble(driver.findElement(By.cssSelector("table#order-products tbody tr td:nth-child(3)")).getText().substring(1));
            double priceFormatted = Double.parseDouble(dFormat.format(price));
            double totalPrice = Double.parseDouble(driver.findElement(By.cssSelector("table#order-products tbody tr td:nth-child(4)")).getText().substring(1));
            double totalPriceFormatted = Double.parseDouble(dFormat.format(totalPrice));
            product = new Product(nameFormatted, priceFormatted, quantity, totalPriceFormatted);
            listOfProducts.add(product);
        }
        return listOfProducts;
    }

    public String getDeliveryAddressDetails(){
        String addressDetails = deliveryAddress.getText();
        return addressDetails;
    }

    public String getInvoiceAddressDetails(){
        String addressDetails = invoiceAddress.getText();
        return addressDetails;
    }
}
