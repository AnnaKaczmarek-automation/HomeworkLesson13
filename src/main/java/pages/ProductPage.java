package pages;

import models.Cart;
import models.Product;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductPage extends BasePage {
    public ProductPage(WebDriver driver) {
        super(driver);
    }
//    private ShopCartPopupPage shopCartPopupPage;
    private Logger log = LoggerFactory.getLogger("ProductPage.class");
    private MenuCategory menuCategory = new MenuCategory(driver);
    private  MenuPage menuPage = new MenuPage(driver);
    private ShopCartPopupPage shopCartPopupPage= new ShopCartPopupPage(driver);

    private static final DecimalFormat df = new DecimalFormat("0.00");

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
        verifyVisibilityOfElement("SAVE 20%", discountLabel);
    }

    public void verifyVisibilityOfRegularPrice() {
        assertVisibilityOfElement(regularPrice);
    }


    public void verifyVisibilityOfDiscountedPrice() {
        assertVisibilityOfElement(discountedPrice);
    }

    public void verifyDiscount() {
        double regularPriceDouble = Double.parseDouble(regularPrice.getText().substring(1));
        double discountedPriceDisplayed = Double.parseDouble(discountedPrice.getText().substring(1));
        int discountPercentage = Integer.parseInt(discountLabel.getText().substring(5, discountLabel.getText().length() - 1));
        double valueOfDiscount = calculateDiscount(regularPriceDouble, discountPercentage);
        double priceAfterDiscount = Double.parseDouble(df.format(regularPriceDouble - valueOfDiscount));
        assertIfEquals(String.valueOf(discountedPriceDisplayed), String.valueOf(priceAfterDiscount));

    }

    public void setProductAmount(String amount) {
        amountInput.clear();
        amountInput.sendKeys(amount);
    }

    public void addProductToCart() throws InterruptedException {
        clickOnElement(addToCartBtn);
        Thread.sleep(5000);
    }


    public String getProductName() {
        String productName = displayedProduct.getText();
        return productName;
    }

    public Double getProductDiscPrice() {
        double discPrice = Double.parseDouble(discountedPrice.getText().substring(1));
        return discPrice;
    }

    public Integer getQuantity() {
        int quantity = Integer.parseInt(amountInput.getText());
        return quantity;
    }

    public List<String> getProductInfoFromProductPage(Cart cart) {
        List<String> productPageValues = new ArrayList<>();
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        for (Product product : cart.getProductsList()) {
            productPageValues.add(product.getName());
            double productPrice = Double.parseDouble(dFormat.format(product.getPrice()));
            productPageValues.add(String.valueOf(productPrice));
            productPageValues.add(String.valueOf(product.getQuantity()));
            double totalPriceDouble = Double.parseDouble(dFormat.format(product.getTotalPrice()));
            productPageValues.add(String.valueOf(totalPriceDouble));
        }
        return productPageValues;
    }

    public List<Product> addRandomProductWithVerification(int productAmount) throws InterruptedException {
        int basketAmount = getBasketAmount();

        String productName = null;
        int quantity = 0;
        double price = 0;
        double totalPrice = 0;
        List<Product> selectedProductsList = null;
        WebElement randomProduct = null;

        while (basketAmount < productAmount) {
            selectedProductsList = new ArrayList<>();
            menuCategory.getRandomCategory();
            log.info("***** Random category is chosen *****");
            randomProduct = menuPage.selectRandomProduct();

            if (!selectedProductsList.isEmpty()) {
                for (Product product : selectedProductsList) {
                    if (product.getName() == randomProduct.findElement(By.cssSelector(".h3.product-title a")).getText()) {
                        menuCategory.getRandomCategory();
                        randomProduct = menuPage.selectRandomProduct();
                        log.info("***** This product is already on the list *****");
                    }
                }
            }
            clickOnElement(randomProduct);
            log.info("***** Random product is chosen *****");

            String amount = "1";
            setProductAmount(amount);
            log.info("***** Amount of products was types in *****");
            productName = getProductName();
            price = getProductDiscPrice();
            quantity = Integer.parseInt(amount);
            totalPrice = price * quantity;

            Product product = new Product(productName, price, quantity, totalPrice, driver);
            List<Product> products = new ArrayList<>();
            products.add(product);
//            selectedProductsList.add(product);

            String parentWindowHandler = driver.getWindowHandle();
            log.info("Parent window id is: " + parentWindowHandler);
            addProductToCart();
            log.info("***** Product was added to cart *****");

            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(productName, price, quantity);
            log.info("***** Product data are correct *****");
            shopCartPopupPage.continueShopping();
            log.info("***** Continue shopping button was chosen *****");
            driver.switchTo().window(parentWindowHandler);
            log.info("***** Popup window is closed *****");

            int refreshedBasketAmount = getBasketAmount();
            log.info("Actual amount of products in basket is: " + refreshedBasketAmount);
            Assert.assertNotEquals(basketAmount, refreshedBasketAmount);
            log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            basketAmount = getBasketAmount();
            selectedProductsList = products;
            System.out.println("size of product list on product page equals: " + selectedProductsList.size());


        }
        return selectedProductsList;
    }


}
