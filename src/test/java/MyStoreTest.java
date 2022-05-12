import configuration.TestBase;

import models.Cart;
import models.Order;
import models.Product;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@Execution(ExecutionMode.CONCURRENT)
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
    protected OrderHistoryPage orderHistoryPage;
    protected TopMenuPage topMenuPage;
    protected OrderDetailsPage orderDetailsPage;

    @DisplayName("Search box test")
    @Test
    public void visibilityOfProductNameInSearchBoxTest() throws InterruptedException {
        menuPage = new MenuPage(driver);
        homePage = new HomePage(driver);
        String expectedName = homePage.getRandomProduct().getText();
        menuPage.enterProductNameIntoSearchField(expectedName);
        List<String> foundProducts = menuPage.getSearchDropdownResults();

        assertThat(foundProducts).allMatch(element -> element.contains(expectedName));
        log.info("Correct product is visible in search box");
    }

    @DisplayName("Searching random product test")
    @Test
    public void searchRandomProductTest() throws InterruptedException {
        menuPage = new MenuPage(driver);
        homePage = new HomePage(driver);
        searchResultPage = new SearchResultPage(driver);

        String productsName = homePage.getProductsName(homePage.getRandomProduct());
        menuPage.enterProductNameIntoSearchField(productsName);
        menuPage.clickLensButtonToSearch();
        searchResultPage.verifyVisibilityOfProduct(productsName);
    }

    @DisplayName("Walking through all categories and subcategories test ")
    @Test
    public void menuCategoriesTest() {
        menuPage = new MenuPage(driver);
        menuPage.verifyMainMenuOptions();
        menuPage.verifySubMenuOptions();
    }

    @DisplayName("Filters test")
    @Test
    public void filtersTest() throws InterruptedException {
        menuPage = new MenuPage(driver);
        menuPage.selectCategory("Art");
        menuPage.moveLeftSlider(9.00);
        menuPage.moveRightSlider(10.00);
        menuPage.checkProductInPriceRange(9.00, 10.00);
        menuPage.clearFilters();
    }

    @DisplayName("Prices drop test")
    @Test
    public void pricesDropTest() {
        menuPage = new MenuPage(driver);
        footerPage = new FooterPage(driver);
        productPage = new ProductPage(driver);
        footerPage.choosePricesDropOption();
        footerPage.verifyIfOnSalePageIsLoaded();
        menuPage.checkDiscountVisibility();
        menuPage.selectRandomProduct();
        productPage.verifyVisibilityOfLabel();
        productPage.verifyVisibilityOfRegularPrice();
        productPage.verifyVisibilityOfDiscountedPrice();
        productPage.verifyDiscount();
    }

    @DisplayName("Shopping Cart test")
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
            while (maxvalue > 15) {
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
                int refreshedBasketAmount = productPage.getBasketAmount();
                Assert.assertNotEquals(basketListSize, refreshedBasketAmount);
                log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            }
            basketListSize = productPage.getBasketAmount();
        }
    }

    @DisplayName("Basket functionality test")
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
            cart.addNewProduct(product);
            productPage.addProductToCart();

            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(product);

            if (cart.getProductsList().size() == 5) {
                shopCartPopupPage.proceedToCheckoutBtn();
            } else {
                shopCartPopupPage.continueShopping();
                int refreshedBasketAmount = basketPage.getBasketAmount();
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
        assertThat(basketPage.getTotalOrderCost()).isEqualTo(cart.getTotalOrderCost());//chyba bede musiała tutaj dodać wartośc dostawy. ale nie w metodzie tylko tutaj
        assertThat(basketPage.getNoItemNotification()).isEqualTo("There are no more items in your cart");
    }

    @DisplayName("Checkout test")
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
        orderHistoryPage = new OrderHistoryPage(driver);
        topMenuPage = new TopMenuPage(driver);
        orderDetailsPage = new OrderDetailsPage(driver);

        homePage.choseSignInOption();
        homePage.choseRegisterOption();
        createAccountPage.fillInTheForm(System.getProperty("gender"), System.getProperty("firstName"), System.getProperty("secondName"), basePage.createRandomMailAddress());

        menuPage.waitForMenuRefresh();
        int basketAmount = basketPage.getBasketAmount();
        Cart cart = new Cart();
        String amount = String.valueOf(basePage.getRandomNumberInRange(1, 3));
        while (basketAmount < 5) {
            menuPage.chooseRandomCategoryAndProduct();
            int maxvalue = basketAmount + Integer.parseInt(amount);
            while (maxvalue > 5) {
                amount = String.valueOf(basePage.getRandomNumberInRange(1, 3));
                maxvalue = basketAmount + Integer.parseInt(amount);
            }
            productPage.setProductAmount(amount);
            System.out.println("typed amount was: " + amount);
            Thread.sleep(3000);
            DecimalFormat dFormat = new DecimalFormat("#,###.##");
            double totalPriceFormatted = Double.parseDouble(dFormat.format(productPage.getProductDiscPrice() * Integer.parseInt(amount)));
            Product product = new Product(productPage.getProductName(), productPage.getProductDiscPrice(), Integer.parseInt(amount), totalPriceFormatted );
            cart.addNewProduct(product);
            productPage.addProductToCart();

            shopCartPopupPage.switchToLastOpenedWindow();
            shopCartPopupPage.verifyShopCartData(product);
            if (shopCartPopupPage.getNumberFromItemsAmountInfo() == 5) {
                shopCartPopupPage.proceedToCheckoutBtn();
            } else {
                shopCartPopupPage.continueShopping();
                int refreshedBasketAmount = basketPage.getBasketAmount();
                Assert.assertNotEquals(basketAmount, refreshedBasketAmount);
                log.info("***** Amount of products in basket correctly differentiate from the initial one *****");
            }
            basketAmount = basketPage.getBasketAmount();
        }


        basketPage.proceedToCheckOut();
        double shippingCost = checkOutPage.getShippingValue();
        double totalCost = checkOutPage.getTotalCost();
        checkOutPage.fillAddressForm();
        checkOutPage.chooseShippingMethod();
        String chosenShipping = checkOutPage.getChosenShippingInfo();

        if (System.getProperty("shippingMethod").equals("shop")) {
            totalCost = totalCost - 7.00;
        }
        checkOutPage.choosePaymentOption();
        String chosenPayment = checkOutPage.getChosenPaymentOption();
        checkOutPage.openTermsOfUseInfo();
        checkOutPage.switchToLastOpenedWindow();
        checkOutPage.verifyTermsOfUsePopupInfo();
        checkOutPage.closeTermsOfUsePopup();
        checkOutPage.confirmTermsOfService();
        checkOutPage.choosePlaceOrderButton();

        basePage.waitUntilVisibilityOfElement(confirmationPage.getConfirmationContent());
        Cart confirmationCart = new Cart(confirmationPage.getProductInfoFromConfirmationPage());
        //tutaj cos jest xle z produktami.nie ma ich nazw w asercji tylko miejsca w pamięci a expected lista jest pusta = []
        try {
            assertThat(confirmationCart).usingRecursiveComparison().isEqualTo(cart);

            log.info("Lists consist of equal values");
        } catch (Throwable e) {
            log.info("Lists are not equal. " + e.getMessage());
        }

        assertThat(chosenShipping).isEqualTo(confirmationPage.getDisplayedShippingInfo());
        log.info("Shipping method match chosen option in previous steps");
        assertThat(confirmationPage.getDisplayedPaymentInfo()).contains(chosenPayment);
        log.info("Payment method match chosen option in previous steps");


        String refOrderNumber = confirmationPage.getOrderNumber();
        topMenuPage.openUserAccount();
        topMenuPage.openOrderHistory();

        assertThat(refOrderNumber).isEqualTo(orderHistoryPage.findOrder());
        log.info("***** Correct order number is displayed *****");

        String currentDay = basePage.getCurrentDate();
        Order actualDetails = orderHistoryPage.getDisplayedOrderDetails();
        Order expectedDetails = new Order(currentDay, totalCost, chosenPayment, confirmationPage.getExpectedOrderStatus());

        assertThat(actualDetails.getDate()).isEqualTo(expectedDetails.getDate());
        log.info("***** Displayed date is correct ****");
        assertThat(actualDetails.getPayment()).contains(expectedDetails.getPayment());
        log.info("***** Displayed payment option is correct ****");
        assertThat(actualDetails.getStatus()).isEqualTo(expectedDetails.getStatus());
        log.info("***** Displayed status option is correct ****");
        assertThat(actualDetails.getPrice()).isEqualTo(expectedDetails.getPrice());
        log.info("***** Displayed price is correct ****");

        orderHistoryPage.chooseDetailsOption();

        Cart detailsCart = new Cart(orderDetailsPage.getProductsInfoFromDetailsPage());
        try {
            assertThat(detailsCart).usingRecursiveComparison().isEqualTo(cart);
            log.info("Lists consist of equal values");
        } catch (Throwable e) {
            log.info("Lists are not equal. " + e.getMessage());
        }
        log.info("***** Displayed data od details page are correct *****");

        String deliveryAddress = orderDetailsPage.getDeliveryAddressDetails();
        String invoiceAddress = orderDetailsPage.getInvoiceAddressDetails();
        assertThat(invoiceAddress).isEqualTo(deliveryAddress);
        log.info("Displayed details of delivery and invoice address are correct and equals.");
    }
}




