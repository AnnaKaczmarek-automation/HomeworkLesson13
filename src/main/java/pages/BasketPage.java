package pages;

import models.Cart;
import models.Product;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BasketPage extends BasePage {
    private CartPage cartPage = new CartPage(driver);
    private DecimalFormat dFormat;
    private long timeout;

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    private Logger log = LoggerFactory.getLogger("BasketPage.class");
    DecimalFormat df = new DecimalFormat("#,###.##");

    @FindBy(xpath = "//ul[@class='cart-items']/li")
    private List<WebElement> productsList;

    @FindBy(css = "#cart-subtotal-shipping span.value")
    private WebElement shippingPrice;

    @FindBy(css = ".material-icons.float-xs-left")
    private WebElement trashBtn;

    @FindBy(css = ".cart-summary-line.cart-total")
    private WebElement totalAmount;

    @FindBy(css = ".card.cart-container")
    private WebElement cartContainer;

    @FindBy(css = ".cart-items")
    private WebElement cartItem;

    @FindBy(css = ".no-items")
    private WebElement noItemInfo;

    @FindBy(css = ".text-sm-center .btn.btn-primary")
    private WebElement proceedToCheckOutBtn;


    public double getShippingPrice() {
        dFormat = new DecimalFormat("#,###.##");
        double price = Double.parseDouble(shippingPrice.getText().substring(1));
        return price;
    }

    public List<WebElement> getListOfProducts() {
        return productsList;
    }

    public List<Product> getProductInfoFromBasket() {
        List<WebElement> productsList = getListOfProducts();
        List<Product> productBasketValues = new ArrayList<>();

        for (int i = 0; i < productsList.size(); i++) {
            String name = productsList.get(i).findElement(By.xpath(".//div[@class='product-line-info']/a")).getText();
            String productBasketPrice = productsList.get(i).findElement(By.cssSelector("div .current-price .price")).getText().substring(1);
            double priceDouble = Double.parseDouble(productBasketPrice);
            double productPriceFormatted = Double.parseDouble(df.format(priceDouble));
            int quantity = Integer.parseInt(productsList.get(i).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
            String totalPriceBasket = productsList.get(i).findElement(By.cssSelector(".product-price strong")).getText().substring(1);
            double totalPriceDouble = Double.parseDouble(totalPriceBasket);
            double totalPriceFormatted = Double.parseDouble(df.format(totalPriceDouble));
            productBasketValues.add(new Product(name, productPriceFormatted, quantity, totalPriceFormatted));
        }
        return productBasketValues;
    }

    public void increaseAmountOfProduct(int productNumber, int amount) {
        waitUntilVisibilityOfElement(cartItem);
        int index = productNumber - 1;
        WebElement product = productsList.get(index);
        WebElement quantityInput = product.findElement(By.xpath("//input[@class='js-cart-line-product-quantity form-control']"));
        highlightElements(quantityInput);
        int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
        while (quantity < amount) {
            product.findElement(By.cssSelector(".btn.btn-touchspin.js-touchspin.js-increase-product-quantity.bootstrap-touchspin-up")).click();
            quantity = Integer.parseInt(product.findElement(By.xpath("//input[@class='js-cart-line-product-quantity form-control']")).getAttribute("value"));
            log.info("new quantity equals: " + quantity);
        }
        log.info("Product quantity was changed and actual quantity is: " + quantityInput.getAttribute("value"));
    }

    public void verifyTotalCost(Cart cart) {
        double totalValue2 = cart.sumProductValuesInCart(cart) + getShippingPrice();
        double totalDisplayedValue2 = cartPage.getTotalPrice();
        Assertions.assertThat(dFormat.format(totalValue2)).isEqualTo(dFormat.format(totalDisplayedValue2));
        log.info("***** Displayed total cost is correct *****");
    }

    public void verifyIncreasedQuantityChange(int productNumber) {
        int index = productNumber - 1;
        WebElement product = productsList.get(index);
        int quantityBefore = Integer.parseInt(product.findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        product.findElement(By.cssSelector(".btn.btn-touchspin.js-touchspin.js-increase-product-quantity.bootstrap-touchspin-up")).click();
        int quantityAfter = Integer.parseInt(productsList.get(index).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        int expectedQuantity = quantityBefore + 1;
        Assertions.assertThat(quantityAfter).isEqualTo(expectedQuantity);
    }

    public void verifyDecreasedQuantityChange(int productNumber) {
        int index = productNumber - 1;
        WebElement product = productsList.get(index);
        int quantityBefore = Integer.parseInt(product.findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        product.findElement(By.cssSelector(".btn.btn-touchspin.js-touchspin.js-decrease-product-quantity.bootstrap-touchspin-down")).click();
        int quantityAfter = Integer.parseInt(product.findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        int expectedQuantity = quantityBefore - 1;
        Assertions.assertThat(quantityAfter).isEqualTo(expectedQuantity);
    }

    public void removeFirstProduct() {
        int productBeforeDeleting = getQuantityOfDisplayedProducts();
        getListOfProducts().get(0).findElement(By.cssSelector(".material-icons.float-xs-left")).click();
        wait.until(c -> getQuantityOfDisplayedProducts() == productBeforeDeleting - 1);
    }

    public int getQuantityOfDisplayedProducts() {
        return getListOfProducts().size();
    }

    public double getTotalOrderCost() {
        return Double.parseDouble(totalAmount.getText().replaceAll("[^0-9.]", ""));
    }

    public String getNoItemNotification(){
        String  notification = noItemInfo.getText();
        return notification;
    }

    public void proceedToCheckOut(){
        waitUntilVisibilityOfElement(proceedToCheckOutBtn);
        driver.findElement(By.cssSelector(".text-sm-center .btn.btn-primary")).click();
        log.info("Button 'Proceed to checkout' was chosen");
    }


}

