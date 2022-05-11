package pages;

import models.Product;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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
            String name = productsList.get(i).findElement(By.cssSelector(".col-sm-4.col-xs-9.details")).getText();
            String nameFormatted = StringUtils.substringBefore(name, " - ");
            String productPrice = productsList.get(i).findElement(By.cssSelector(".col-xs-4.text-sm-center.text-xs-left")).getText().substring(1);
            double priceDouble = Double.parseDouble(productPrice);
            double productPriceFormatted = Double.parseDouble(df.format(priceDouble));

            waitUntilVisibilityOfElement(quantity);
            int quantity = Integer.parseInt(productsList.get(i).findElement(By.xpath("//div[@class='col-sm-6 col-xs-12 qty']/div/div[@class='col-xs-4 text-sm-center']")).getText());

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
        String reference = driver.findElement(By.xpath("//li[contains(text(),'Order reference')]")).getText();
        String orderReference = StringUtils.removeStart(reference,"Order reference: ");
        log.info("Order reference: " + orderReference);
        return orderReference;
    }

    public String getDisplayedShippingInfo(){
        waitUntilVisibilityOfElement(shippingDetails);
        String shippingInfo = shippingDetails.getText();
        String formattedInfo = StringUtils.substringBefore(shippingInfo, "");
        return formattedInfo;
    }

    public String getDisplayedPaymentInfo(){
        waitUntilVisibilityOfElement(paymentDetails);
        String paymentInfo = paymentDetails.getText();
        String formattedInfo = StringUtils.removeEnd(paymentInfo, "");
        return formattedInfo;
    }

    public String getExpectedOrderStatus(){
        String status = "Awaiting bank wire payment";
        return status;
    }

    public void getTotalOrderPrice(){

    }
}
