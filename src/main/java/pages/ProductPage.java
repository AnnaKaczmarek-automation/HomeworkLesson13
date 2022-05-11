package pages;

import models.Product;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.assertj.core.api.Assertions.assertThat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.rmi.server.ExportException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductPage extends BasePage {
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    private Logger log = LoggerFactory.getLogger("ProductPage.class");
    private MenuPage menuPage = new MenuPage(driver);
    private ShopCartPopupPage shopCartPopupPage = new ShopCartPopupPage(driver);
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private List<Product> selectedProductsList = new ArrayList<>();
//    Dimension currentDimension = driver.manage().window().getSize();

    @FindBy(xpath = "//div[@class='col-md-6']/h1")
    private WebElement displayedProduct;

    @FindBy(xpath = "//div[@class='current-price']/span[2]")
    private WebElement discountLabel;

    @FindBy(xpath = "//div[@class='product-discount']/span")
    private WebElement regularPrice;

    @FindBy(xpath = "//div[@class='current-price']/span[1]")
    private WebElement discountedPrice;

    @FindBy(css = "#quantity_wanted")
    private WebElement amountInput;

    @FindBy(xpath = "//button[@class='btn btn-primary add-to-cart']")
    private WebElement addToCartBtn;

    public void verifyVisibilityOfLabel() {
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".discount.discount-percentage"))));
        String displayedDiscount = discountLabel.getText().replaceAll("[^0-9]", "");
        Assert.assertEquals(displayedDiscount, "20");
        assertThat(displayedDiscount).isEqualTo("20");
        log.info("***** Correct label is displayed *****");
    }

    public void verifyVisibilityOfRegularPrice() {
        assertVisibilityOfElement(regularPrice);
        log.info("***** Regular price is displayed *****");
    }

    public void verifyVisibilityOfDiscountedPrice() {
        assertVisibilityOfElement(discountedPrice);
        log.info("***** Discounted price is displayed *****");
    }

    public void verifyDiscount() {
        double regularPriceDouble = Double.parseDouble(regularPrice.getText().substring(1));
        double discountedPriceDisplayed = Double.parseDouble(discountedPrice.getText().substring(1));
        int discountPercentage = Integer.parseInt(discountLabel.getText().substring(5, discountLabel.getText().length() - 1));
        double valueOfDiscount = calculateDiscount(regularPriceDouble, discountPercentage);
        double priceAfterDiscount = Double.parseDouble(df.format(regularPriceDouble - valueOfDiscount));
        assertIfEquals(String.valueOf(discountedPriceDisplayed), String.valueOf(priceAfterDiscount));
        log.info("Discount was correctly applied");
    }

    public void setProductAmount(String amount) {
//        waitUntilVisibilityOfElement(amountInput);
//        amountInput.clear();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-add-to-cart")));
        driver.findElement(By.cssSelector("#quantity_wanted")).clear();
//        amountInput.sendKeys(amount);
        driver.findElement(By.cssSelector("#quantity_wanted")).sendKeys(amount);
        log.info("***** Amount of products was types in *****");
    }

    public void addProductToCart() throws InterruptedException {
        try {
            clickOnElement(addToCartBtn);
            Thread.sleep(5000);
            log.info("***** Product was added to cart *****");

        } catch (TimeoutException e) {
            JFrame frame = new JFrame();
            java.awt.Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(dim.width / 2, dim.height / 2);
            waitUntilVisibilityOfElement(addToCartBtn);
            clickOnElement(addToCartBtn);
            log.info("***** Product was added to cart *****");
        }
    }

    public String getProductName() {
        String productName = displayedProduct.getText();
        return productName;
    }

    public Double getProductDiscPrice() {
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        double discPrice = Double.parseDouble(discountedPrice.getText().substring(1));
        double price = Double.parseDouble(dFormat.format(discPrice));
        return price;
    }

    public Integer getQuantity() {
        int quantity = Integer.parseInt(amountInput.getText());
        return quantity;
    }
}
