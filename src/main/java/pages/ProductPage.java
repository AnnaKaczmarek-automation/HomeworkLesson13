package pages;

import helpers.StringConverter;
import models.Cart;
import models.Product;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductPage extends BasePage {
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    //    private ShopCartPopupPage shopCartPopupPage;
    private Logger log = LoggerFactory.getLogger("ProductPage.class");
//    private MenuCategory menuCategory = new MenuCategory(driver);
    private MenuPage menuPage = new MenuPage(driver);
    private ShopCartPopupPage shopCartPopupPage = new ShopCartPopupPage(driver);
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private List<Product> selectedProductsList = new ArrayList<>();

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
        int quantity = 0;
        for (Product product : cart.getProductsList()) {
            //jesli product jest na liście to tylko zwiększ quantity i weź nową totalPrice
            if(cart.getProductsList().contains(product.getName())){
                quantity = quantity +product.getQuantity();
                log.info("quantity number is: " + quantity);
                productPageValues.add(String.valueOf(quantity));
                double totalPriceDouble = Double.parseDouble(dFormat.format(product.getTotalPrice()*quantity));
                productPageValues.add(String.valueOf(totalPriceDouble));
            }
            if(!cart.getProductsList().contains(product.getName())) {
                //jesli produkt nie jest na liście to zrób wszystkie akcje
                productPageValues.add(product.getName());
                log.info("Product price before formatting is :" + product.getPrice());
                double productPrice = Double.parseDouble(dFormat.format(product.getPrice()));
//            double productPrice = StringConverter.covertStringIntoDouble(product.getPrice());
                log.info("Product price is " + productPrice);
                productPageValues.add(String.valueOf(productPrice));
                log.info("quantity number is: " + product.getQuantity());
                productPageValues.add(String.valueOf(product.getQuantity()));
                double totalPriceDouble = Double.parseDouble(dFormat.format(product.getTotalPrice()));
                productPageValues.add(String.valueOf(totalPriceDouble));
            }
        }
        return productPageValues;
    }

    public void getProductInfoWithRepeatedProducts(Cart cart){
        List<String> productInfo = getProductInfoFromProductPage(cart);
        for(int i =0; i < productInfo.size(); i++) {
            if (productInfo.contains(productInfo.get(i))) {
                System.out.println(productInfo.get(i)+" is duplicated");
            }

        }

//        for(int i=0 ; i < productInfo.size() ; i++){
//            if (!productInfo.contains(productInfo.get(i))){
//                productInfo.get(i).
//            }
        }



    public List<Product> addRandomProductWithVerification(int productAmount) throws InterruptedException, IOException, AWTException {
        int basketAmount = getBasketAmount();

        String productName = null;
        int quantity = 0;
        double price = 0;
        double totalPrice = 0;
        List<Product> selectedProductsList = new ArrayList<>();
        WebElement randomProduct = null;

        while (selectedProductsList.size() < productAmount) {
            menuPage.getRandomCategory();
            log.info("***** Random category is chosen *****");
            randomProduct = menuPage.selectRandomProduct();
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
            if(!selectedProductsList.contains(product.getName())){
                selectedProductsList.add(product);
            }
            if(selectedProductsList.contains(product.getName())){
                quantity = product.getQuantity()+quantity;
                totalPrice = product.getPrice()*quantity;
            }
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
//            driver.switchTo().window(parentWindowHandler);
//            log.info("***** Popup window is closed *****");

            int refreshedBasketAmount = getBasketAmount();
            log.info("Actual amount of products in basket is: " + refreshedBasketAmount);
            Assert.assertNotEquals(basketAmount, refreshedBasketAmount);
            log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            basketAmount = getBasketAmount();
        }
        return selectedProductsList;
    }


}
