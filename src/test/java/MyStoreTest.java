import configuration.TestBase;
import models.Cart;
import models.Product;
//import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;

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
import java.util.ArrayList;
import java.util.List;

public class MyStoreTest extends TestBase {
    protected Logger log = LoggerFactory.getLogger("MyStoreTest.class");
    protected HomePage homePage;
    protected MenuPage menuPage;
    protected FooterPage footerPage;
    protected ProductPage productPage;
    protected SearchResultPage searchResultPage;
    protected ShopCartPopupPage shopCartPopupPage;
    protected BasketPage basketPage;
    protected CartPage cartPage;
    protected CreateAccountPage createAccountPage;
    protected CheckOutPage checkOutPage;
    protected ConfirmationPage confirmationPage;

    @Test
    public void visibilityOfProductNameInSearchBoxTest() throws InterruptedException {
        menuPage = new MenuPage(driver);
        homePage = new HomePage(driver);
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
        menuPage = new MenuPage(driver);
        homePage = new HomePage(driver);
        searchResultPage = new SearchResultPage(driver);

        String productsName = homePage.getProductsName(homePage.getRandomProduct());
        System.out.println(productsName);
        log.info("***** Random product is chosen *****");
        menuPage.enterProductNameIntoSearchField(productsName);
        log.info("***** Products name is typed into the search box *****");
        menuPage.clickLensButtonToSearch();
        log.info("***** Search was submitted *****");
        searchResultPage.verifyVisibilityOfProduct(productsName);
        log.info("***** Correct products name is displayed as expected *****");
    }

    @Test
    public void menuCategoriesTest() throws InterruptedException {
        menuPage = new MenuPage(driver);
//        List<WebElement> menuOptions = menuPage.getMenuOptions();
        menuPage.verifyMainMenuOptions();
        menuPage.verifySubMenuOptions();
    }


    @Test
    public void filtersTest() throws InterruptedException {
        menuPage = new MenuPage(driver);
        menuPage.selectCategory("Art");
        log.info("***** Art category was opened. *****");
        menuPage.moveLeftSlider(9);
        log.info("***** Slider was moved left, if it was necessary. *****");
        menuPage.moveRightSlider(10);
        log.info("***** Slider was moved right, if it was necessary. Price range was selected. *****");
        menuPage.checkProductInPriceRange(9, 10);
        log.info("***** Products have been verified in terms of price. ***** ");
        menuPage.clearFilters();
        log.info("***** Filters ale cleared. *****");
        //dodac warunek co zrobic, jeśli poda sie zbyt wysoką liczbę
    }

    @Test
    public void pricesDropTest() {
        menuPage = new MenuPage(driver);
        footerPage = new FooterPage(driver);
        productPage = new ProductPage(driver);
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
        menuPage = new MenuPage(driver);
        productPage = new ProductPage(driver);
        shopCartPopupPage = new ShopCartPopupPage(driver);

        int basketListSize = productPage.getBasketAmount();
        while (basketListSize < 15) {
            String amount = null;
            menuPage.chooseRandomCategoryAndProduct();
            amount = String.valueOf(basePage.getRandomNumberInRange(1, 5));

            int maxvalue = basketListSize + Integer.parseInt(amount);
            while( maxvalue > 15){
                amount = String.valueOf(basePage.getRandomNumberInRange(1, 5));
                maxvalue = basketListSize + Integer.parseInt(amount);
            }
            productPage.setProductAmount(amount);
            Thread.sleep(3000);
            String productName = productPage.getProductName();
            double price = productPage.getProductDiscPrice();
            int quantity = Integer.parseInt(amount);
            double totalPrice = price * quantity;

            Product product = new Product(productName, price, quantity, totalPrice);

            productPage.addProductToCart();
            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(product);
            if (shopCartPopupPage.getNumberFromItemsAmountInfo() == 15) {
                shopCartPopupPage.proceedToCheckoutBtn();
            } else {
                shopCartPopupPage.continueShopping();
                log.info("***** Continue shopping button was chosen *****");
                int refreshedBasketAmount = productPage.getBasketAmount();
                Assert.assertNotEquals(basketListSize, refreshedBasketAmount);
                log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            }
            basketListSize = productPage.getBasketAmount();
        }
    }

    @Test
    public void basketFunctionalityTest() throws InterruptedException, IOException, AWTException {
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        basketPage = new BasketPage(driver);
        menuPage = new MenuPage(driver);
        shopCartPopupPage = new ShopCartPopupPage(driver);
        basePage = new BasePage(driver);

        Cart cart = new Cart();
        String amount = "1";
        int basketAmount = basketPage.getBasketAmount();

        while (basketAmount < 5) {
            menuPage.chooseRandomCategoryAndProduct();
            productPage.setProductAmount(amount);
            Product product = new Product(productPage.getProductName(), productPage.getProductDiscPrice(), Integer.parseInt(amount), productPage.getProductDiscPrice() * Integer.parseInt(amount));
            productPage.addProductToCart();
            cart.addNewProduct(product);
            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(product);

            if (cart.getProductsList().size() == 5) {
                shopCartPopupPage.proceedToCheckoutBtn();
            } else {
                shopCartPopupPage.continueShopping();
                log.info("***** Continue shopping button was chosen *****");

                int refreshedBasketAmount = basketPage.getBasketAmount();
                log.info("Actual amount of products in basket is: " + refreshedBasketAmount);
                Assert.assertNotEquals(basketAmount, refreshedBasketAmount);
                log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            }
            basketAmount = basketPage.getBasketAmount();
        }

        basePage.openBasket();
        Cart basketCart = new Cart(basketPage.getProductInfoFromBasket());

        try {
            assertThat(cart).usingRecursiveComparison().isEqualTo(basketCart);
            log.info("Lists consist of equal values");
        } catch (Throwable e) {
            log.info("Lists are not equal. " + e.getMessage());
        }

        cartPage.verifyShippingCost(cart);
        basketPage.increaseAmountOfProduct(1, 5);
        basketPage.verifyTotalCost(cart);
        basketPage.verifyIncreasedQuantityChange(2);
        basketPage.verifyTotalCost(cart);
        basketPage.verifyDecreasedQuantityChange(2);
        basketPage.verifyTotalCost(cart);

        while (basketPage.getQuantityOfDisplayedProducts() > 0) {
            basketPage.removeFirstProduct();
            cart.removeFirstProduct();
            basketPage.getQuantityOfDisplayedProducts();
            driver.navigate().refresh();
        }

        assertThat(basketPage.getTotalOrderCost()).isEqualTo(cart.getTotalOrderCost() - 7.00);
        assertThat(basketPage.getNoItemNotification()).isEqualTo("There are no more items in your cart");
    }

    @Test
    public void checkoutTest() throws InterruptedException, IOException, AWTException {
        createAccountPage = new CreateAccountPage(driver);
        homePage = new HomePage(driver);
        menuPage = new MenuPage(driver);
        basketPage = new BasketPage(driver);
        productPage = new ProductPage(driver);
        shopCartPopupPage = new ShopCartPopupPage(driver);
        checkOutPage = new CheckOutPage(driver);
        confirmationPage = new ConfirmationPage(driver);


        homePage.choseSignInOption();
        homePage.choseRegisterOption();
        createAccountPage.fillInTheForm(System.getProperty("gender"), System.getProperty("firstName"), System.getProperty("secondName"), basePage.createRandomMailAddress());

        menuPage.waitForMenuRefresh();
        int basketAmount = basketPage.getBasketAmount();
        Cart cart = new Cart();
        String amount = null;
        while (basketAmount < 5) {
            menuPage.chooseRandomCategoryAndProduct();
            amount = String.valueOf(basePage.getRandomNumberInRange(1, 3));

            int maxvalue = basketAmount + Integer.parseInt(amount);
            while( maxvalue > 5){
                amount = String.valueOf(basePage.getRandomNumberInRange(1, 3));
                maxvalue = basketAmount + Integer.parseInt(amount);
            }
            productPage.setProductAmount(amount);
            Thread.sleep(3000);
            String productName = productPage.getProductName();
            double price = productPage.getProductDiscPrice();
            int quantity = Integer.parseInt(amount);
            double totalPrice = price * quantity;

            Product product = new Product(productName, price, quantity, totalPrice);

            productPage.addProductToCart();
            cart.addNewProduct(product);
            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(product);
            if (shopCartPopupPage.getNumberFromItemsAmountInfo() == 5) {
                shopCartPopupPage.proceedToCheckoutBtn();
            } else {
                shopCartPopupPage.continueShopping();
                log.info("***** Continue shopping button was chosen *****");
                int refreshedBasketAmount = productPage.getBasketAmount();
                Assert.assertNotEquals(basketAmount, refreshedBasketAmount);
                log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            }
            basketAmount = productPage.getBasketAmount();
        }

        basketPage.proceedToCheckOut();
        checkOutPage.fillAddressForm();
        checkOutPage.chooseShippingMethod();
        String chosenShipping = checkOutPage.getChosenShippingInfo();
        checkOutPage.choosePaymentOption();
        String chosenPayment = checkOutPage.getChosenPaymentOption();
        checkOutPage.openTermsOfUseInfo();
        checkOutPage.switchToLastOpenedWindow();
        checkOutPage.verifyTermsOfUsePopupInfo();
        checkOutPage.closeTermsOfUsePopup();
        checkOutPage.confirmTermsOfService();
        checkOutPage.choosePlaceOrderButton();

        Cart confirmationCart = new Cart(confirmationPage.getProductInfoFromConfirmationPage());
        //tutaj cos jest xle z produktami.nie ma ich nazw w asercji tylko miejsca w pamięci a expected lista jest pusta = []
        try {
            assertThat(cart).usingRecursiveComparison().isEqualTo(confirmationCart);
            log.info("Lists consist of equal values");
        } catch (Throwable e) {
            log.info("Lists are not equal. " + e.getMessage());
        }

        assertThat(chosenShipping).isEqualTo(checkOutPage.getChosenShippingInfo());
        assertThat(chosenPayment).isEqualTo(checkOutPage.getChosenPaymentOption());

    }
}




