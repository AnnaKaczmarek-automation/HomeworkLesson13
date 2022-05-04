package pages;

import helpers.StringConverter;
import models.Cart;
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

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    private Logger log = LoggerFactory.getLogger("BasketPage.class");
    DecimalFormat df = new DecimalFormat("#,###.##");

    @FindBy(xpath = "//ul[@class='cart-items']/li")
    private List<WebElement> productsList;

    @FindBy(css = "#cart-subtotal-shipping span.value")
    private WebElement shippingPrice;

    //wyszukuje tyle ile jest produktów w koszyku
    @FindBy(css = ".material-icons.float-xs-left")
    private WebElement trashBtn;

    @FindBy(css = ".cart-summary-line.cart-total")
    private WebElement totalAmount;


    public double getShippingPrice() {
        dFormat = new DecimalFormat("#,###.##");
        double price = Double.parseDouble(shippingPrice.getText().substring(1));
        System.out.println(price);
//        double priceDouble = Double.parseDouble(dFormat.format(price));
        return price;
    }

    public List<WebElement> getListOfProducts() {
        return productsList;
    }

    public List<String> getProductInfoFromBasket() {
        List<WebElement> productsList = getListOfProducts();
        System.out.println("size of product list in basket equalt: " + productsList.size());
        List<String> productBasketValues = new ArrayList<>();

        for (int i = 0; i < productsList.size(); i++) {
            productBasketValues.add(productsList.get(i).findElement(By.xpath(".//div[@class='product-line-info']/a")).getText());
            String productBasketPrice = productsList.get(i).findElement(By.cssSelector("div .current-price .price")).getText().substring(1);
            double priceDouble = Double.parseDouble(productBasketPrice);
            double priceDoubleFormatted = Double.parseDouble(df.format(priceDouble));
            productBasketValues.add(String.valueOf(priceDoubleFormatted));
            productBasketValues.add(productsList.get(i).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
            String totalPriceBasket = productsList.get(i).findElement(By.cssSelector(".product-price strong")).getText().substring(1);
            double totalPriceDouble = Double.parseDouble(totalPriceBasket);
            double totalPriceFormatted = Double.parseDouble(df.format(totalPriceDouble));
            productBasketValues.add(String.valueOf(totalPriceFormatted));
        }
        return productBasketValues;
    }

    public void increaseAmountOfProduct(int productNumber, String amount) {
        int index = productNumber - 1;
        WebElement product = productsList.get(index);
        WebElement quantityInput = product.findElement(By.xpath("//input[@class='js-cart-line-product-quantity form-control']"));

        int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
        while (quantity < Integer.parseInt(amount)) {
            increaseProductAmount(productNumber);
            quantity = Integer.parseInt(quantityInput.getAttribute("value"));
        }

        System.out.println("Actual quantity is: " + quantityInput.getAttribute("value"));
//        setValueIntoInputBox(quantityInput, amount);
        log.info("Product quantity was changed ");
    }

    public void verifyTotalCost(Cart cart) {
        double totalValue2 = cart.sumProductValuesInCart(cart) + getShippingPrice();
        double totalDisplayedValue2 = cartPage.getTotalPrice();
        Assertions.assertThat(dFormat.format(totalValue2)).isEqualTo(dFormat.format(totalDisplayedValue2));
        log.info("***** Displayed total cost is correct *****");
    }

    public void increaseProductAmount(int productNumber) {
        int index = productNumber - 1;
        WebElement increaseBtn = productsList.get(index).findElement(By.cssSelector(".btn.btn-touchspin.js-touchspin.js-increase-product-quantity.bootstrap-touchspin-up"));
        clickOnElement(increaseBtn);
    }

    public void verifyIncreasedQuantityChange(int productNumber) {
        int index = productNumber - 1;
        int quantityBefore = Integer.parseInt(productsList.get(index).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        System.out.println(quantityBefore);
        increaseProductAmount(productNumber);
        int quantityAfter = Integer.parseInt(productsList.get(index).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        System.out.println(quantityAfter);
        int expectedQuantity = quantityBefore + 1;
        Assertions.assertThat(quantityAfter).isEqualTo(expectedQuantity);
    }

    public void verifyDecreasedQuantityChange(int productNumber) {
        int index = productNumber - 1;
        int quantityBefore = Integer.parseInt(productsList.get(index).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        System.out.println(quantityBefore);
        decreaseProductAmount(productNumber);
        int quantityAfter = Integer.parseInt(productsList.get(index).findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value"));
        System.out.println(quantityAfter);
        int expectedQuantity = quantityBefore + 1;
        Assertions.assertThat(quantityAfter).isEqualTo(expectedQuantity);
    }

    public void decreaseProductAmount(int productNumber) {
        int index = productNumber - 1;
        WebElement decreaseBtn = productsList.get(index).findElement(By.cssSelector(".btn.btn-touchspin.js-touchspin.js-decrease-product-quantity.bootstrap-touchspin-down"));
        clickOnElement(decreaseBtn);
    }

    public Integer getQuantity(WebElement element) {
        String quantity = element.findElement(By.cssSelector(".js-cart-line-product-quantity.form-control")).getAttribute("value");
        int quantityInt = Integer.parseInt(quantity);
        return quantityInt;

    }

    public void removeProduct(WebElement element) {
        WebElement trashIcon = element.findElement(By.cssSelector(".material-icons.float-xs-left"));
        clickOnElement(trashIcon);

    }


    public void removeAllProducts() {
        for (WebElement product : productsList) {
            double amountBefore = StringConverter.covertStringIntoDouble(totalAmount.getText());
            removeProduct(product);
            double amountAfter = StringConverter.covertStringIntoDouble(totalAmount.getText());
            Assertions.assertThat(amountAfter).isEqualTo(amountBefore);

        }
    }
}
