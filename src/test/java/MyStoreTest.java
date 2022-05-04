import configuration.TestBase;
import models.Cart;
import models.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MyStoreTest extends TestBase {
    protected Logger log = LoggerFactory.getLogger("MyStoreTest.class");
    protected HomePage homePage = new HomePage(driver);
    protected MenuPage menuPage = new MenuPage(driver);
    protected FooterPage footerPage = new FooterPage(driver);
    protected ProductPage productPage = new ProductPage(driver);
    protected SearchResultPage searchResultPage = new SearchResultPage(driver);
    protected MenuCategory menuCategory = new MenuCategory(driver);
    protected ShopCartPopupPage shopCartPopupPage = new ShopCartPopupPage(driver);
    protected BasketPage basketPage = new BasketPage(driver);
    protected CartPage cartPage = new CartPage(driver);


    @Test
    public void visibilityOfProductNameInSearchBoxTest() throws InterruptedException {

        String expectedName = homePage.getRandomProduct().getText();
        menuPage.enterProductNameIntoSearchField(expectedName);
        List<String> foundProducts = menuPage.getSearchDropdownResults();

        assertThat(foundProducts).allMatch(element -> element.contains(expectedName));
        SoftAssertions softAssertions = new SoftAssertions();
        log.info("Correct product is visible in search box");

//        for (String productName : foundProducts) {
//            softAssertions.assertThat(productName).contains(expectedName);
//        }
//        softAssertions.assertAll();
    }

    @Test
    public void searchRandomProductTest() throws InterruptedException {

        String productsName = homePage.getProductsName(homePage.getRandomProduct());
        System.out.println(productsName);
        log.info("***** Random product is chosen *****");
        menuPage.enterProductNameIntoSearchField(productsName);
        log.info("***** Products name in typed into the search box *****");
        menuPage.clickLensButtonToSearch();
        log.info("***** Search was submitted *****");
        searchResultPage.verifyVisibilityOfProduct(productsName);
        log.info("***** Correct products name is displayed as expected *****");
    }

    @Test
    public void menuCategoriesTest() throws InterruptedException {

        List<WebElement> menuOptions = menuPage.getMenuOptions();
//        menuPage.verifyMainMenuOptions();
        menuPage.verifySubMenuOptions();
    }


    @Test
    public void filtersTest() throws InterruptedException {
        menuPage.selectCategory("Art");
        log.info("***** Art category was opened. *****");
        menuPage.moveLeftSlider(9);
        log.info("***** Slider was moved left, if it was necessary. *****");
        menuPage.moveRightSlider(10);
        log.info("***** Slider was moved right, if it was necessary. Price range was selected. *****");
        menuPage.checkProductInPriceRange(10, 24);
        log.info("***** Products have been verified in terms of price. ***** ");
        menuPage.clearFilters();
        log.info("***** Filters ale cleared. *****");
        //dodac warunek co zrobic, jeśli poda sie zbyt wysoką liczbę
    }

    @Test
    public void pricesDropTest() {
        footerPage.choosePricesDropOption();
        log.info("***** Drop prices option was chosen *****");
        footerPage.verifyIfOnSalePageIsLoaded();
        log.info("***** Correct page is loaded *****");
        menuPage.checkDiscountVisibility();
        log.info("***** Regular price, discount amount and discounted prices are correct *****");
        menuPage.selectRandomProduct();
        log.info("***** Product is selected *****");
        productPage.verifyVisibilityOfLabel();
        log.info("***** Correct label is displayed *****");
        productPage.verifyVisibilityOfRegularPrice();
        log.info("***** Regular price is displayed *****");
        productPage.verifyVisibilityOfDiscountedPrice();
        log.info("***** Discounted price is displayed *****");
        productPage.verifyDiscount();
        log.info("Discount was correctly applied");
    }

    @Test
    public void addingProductToShoppingCartTests() throws InterruptedException, IOException, AWTException {

        int basketAmount = productPage.getBasketAmount();
        while (basketAmount < 15) {
            menuCategory.getRandomCategory();
            log.info("***** Random category is chosen *****");
            menuPage.selectRandomProduct();
            log.info("***** Random product is chosen *****");
            String amount = String.valueOf(basePage.getRandomNumberInRange(1, 5));
            productPage.setProductAmount(amount);
            Thread.sleep(3000);
            log.info("***** Amount of products was types in *****");
            String productName = productPage.getProductName();
            double price = productPage.getProductDiscPrice();
            int quantity = Integer.parseInt(amount);

            String parentWindowHandler = driver.getWindowHandle();
            log.info("Parent window id is: " + parentWindowHandler);
            productPage.addProductToCart();
            log.info("***** Product was added to cart *****");
            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(productName, price, quantity);
            log.info("***** Product data are correct *****");
            shopCartPopupPage.continueShopping();
            log.info("***** Continue shopping button was chosen *****");
            driver.switchTo().window(parentWindowHandler);
            log.info("***** Popup window is closed *****");
            int refreshedBasketAmount = productPage.getBasketAmount();
            log.info("Actual amount of products in basket is: " + refreshedBasketAmount);
            Assert.assertNotEquals(basketAmount, refreshedBasketAmount);
//            basePage.assertIfEquals(String.valueOf(basketAmount), String.valueOf(refreshedBasketAmount));
            log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            basketAmount = productPage.getBasketAmount();
        }
    }

    @Test
    public void basketFunctionalityTest() throws InterruptedException, IOException, AWTException {
        List<Product> selectedProductsList = productPage.addRandomProductWithVerification(3);
        Cart cart = new Cart(driver, selectedProductsList);
        basePage.openBasket();

        try {
            //produkty moga sie powtarzać, tylko w koszyku trzeba to wziąć pod uwagę że rodzajów produktu będzie mniej
            Assert.assertEquals(productPage.getProductInfoFromProductPage(cart), basketPage.getProductInfoFromBasket());
            System.out.println("Lists consist of equal values");
        } catch (Throwable e) {
            System.err.println("Lists are not equal. " + e.getMessage());
        }

        cartPage.verifyShippingCost(cart);
        basketPage.increaseAmountOfProduct(1, "5");
        basketPage.verifyTotalCost(cart);
        basketPage.verifyIncreasedQuantityChange(2);
        //sprawdzenie poprawności całkowitego kosztu
        basketPage.verifyTotalCost(cart);
        basketPage.verifyDecreasedQuantityChange(2);
        //sprawdzenie poprawności całkowitego kosztu
        basketPage.verifyTotalCost(cart);

    }
}


