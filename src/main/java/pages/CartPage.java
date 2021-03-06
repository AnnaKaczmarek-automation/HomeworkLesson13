package pages;

import models.Cart;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class CartPage extends BasePage{
    public CartPage(WebDriver driver) {
        super(driver);
    }
    DecimalFormat dFormat;

    private Logger log = LoggerFactory.getLogger("CartPage.class");

    @FindBy(css = ".cart-summary-line.cart-total span:nth-child(2)")
    private WebElement totalPrice;

    @FindBy(css = "#cart-subtotal-shipping span.value:nth-child(2)")
    private WebElement shippingCost;

    @FindBy(css = ".cart-summary-line.cart-total span.value:nth-child(2)")
    private WebElement totalCost;

    public Double getTotalPrice(){
         dFormat = new DecimalFormat("#,###.##");

        double total= Double.parseDouble(totalPrice.getText().substring(1));
        double totalCost = Double.parseDouble(dFormat.format(total));
        return totalCost;
    }

    public WebElement getTotalCost() {
        return totalCost;
    }

    public WebElement getShippingCost() {
        return shippingCost;
    }

    public void verifyShippingCost(Cart cart){
        dFormat = new DecimalFormat("#,###.##");
        double shippingCost = Double.parseDouble(getShippingCost().getText().substring(1));
        double totalValue = Double.parseDouble(dFormat.format(cart.sumProductValuesInCart(cart) + shippingCost));
        double totalDisplayedValue = Double.parseDouble(dFormat.format(getTotalPrice()));

        Assertions.assertThat(totalValue).isEqualTo(totalDisplayedValue);
        log.info("***** Displayed total cost is correct *****");
    }
}
