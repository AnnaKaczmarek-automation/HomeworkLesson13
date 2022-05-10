package pages;

import models.Product;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationPage extends BasketPage {
    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    Logger log = LoggerFactory.getLogger("ConfirmationPage.class");

    @FindBy(css = ".order-line.row")
    private List<WebElement> productsList;

    @FindBy(css = "div.col-md-4 ul li:nth-child(2)")
    private WebElement paymentDetails;

    @FindBy(css = "div.col-md-4 ul li:nth-child(3)")
    private WebElement shippingDetails;

    @FindBy(xpath = "//li[contains(text(),'Order reference')] ")
    private WebElement orderNumber;

    @FindBy(css = "#content .card-block")
    private WebElement confirmationContent;

    @FindBy(css = ".total-value.font-weight-bold td:nth-child(2)")
    private WebElement totalPrice;

    @FindBy(xpath = "//div[@class='col-sm-6 col-xs-12 qty']/div/div[@class='col-xs-4 text-sm-center']")
    private WebElement quantity;

    @FindBy(css = ".col-xs-4.text-sm-center.text-xs-right.bold")
    private WebElement productName;

    @FindBy(css = "#order-items")
    private WebElement orderItems;


    public List<WebElement> getListOfProducts() {
        return productsList;
    }
    public WebElement getConfirmationContent() {
        return confirmationContent;
    }

    public List<Product> getProductInfoFromConfirmationPage() {
        List<WebElement> productsList = getListOfProducts();
        List<Product> productValues = new ArrayList<>();

        for (int i = 0; i < productsList.size(); i++) {
            waitUntilVisibilityOfElement(productName);
            String name = productsList.get(i).findElement(By.cssSelector(".col-sm-4.col-xs-9.details")).getText().replaceAll(("[a-z]+"), "");
            String nameFormatted = name.substring(name.length()-26);

            String productPrice = productsList.get(i).findElement(By.cssSelector(".col-xs-4.text-sm-center.text-xs-left")).getText().substring(1);
            double priceDouble = Double.parseDouble(productPrice);
            double productPriceFormatted = Double.parseDouble(df.format(priceDouble));

            waitUntilVisibilityOfElement(quantity);
            int quantity = Integer.parseInt(productsList.get(i).findElement(By.xpath("//div[@class='col-sm-6 col-xs-12 qty']/div/div[@class='col-xs-4 text-sm-center']")).getText());

//            Actions actions = new Actions(driver);
//            actions.moveToElement(totalPrice);
//            waitUntilVisibilityOfElement(totalPrice);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".total-value.font-weight-bold td:nth-child(2)")));
//            String totalPriceBasket = productsList.get(i).findElement(By.cssSelector(".col-xs-4.text-sm-center.text-xs-right.bold")).getText().substring(1);
//            double totalPriceDouble = Double.parseDouble(totalPriceBasket);
//            double totalPriceFormatted = Double.parseDouble(df.format(totalPriceDouble));
            String shipping = driver.findElement(By.cssSelector("tbody tr:nth-child(2) td:nth-child(2)")).getText();
            double shippingCost = 0;
            if(StringUtils.isNumeric(shipping)){
                shippingCost = Double.parseDouble(shipping.substring(1));
            }
            if(StringUtils.isAlpha(shipping)){
                shippingCost = 0;
            }
            double totalPrice = (quantity * productPriceFormatted) + shippingCost;
            productValues.add(new Product(nameFormatted, productPriceFormatted, quantity, totalPrice));


        }
        return productValues;
    }

    public String getOrderNumber() {
        driver.navigate().refresh();
        waitUntilVisibilityOfElement(confirmationContent);
        waitUntilVisibilityOfElement(orderNumber);
        String orderReference = orderNumber.getText().replaceAll(("[a-z]+"), "");
        log.info("Order reference is " + orderReference);
        return orderReference;
    }

}
