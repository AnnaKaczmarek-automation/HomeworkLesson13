import configuration.TestBase;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.*;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;


public class MyStoreTest extends TestBase {
    protected Logger log = LoggerFactory.getLogger("MyStoreTest.class");
    protected HomePage homePage = new HomePage(driver);
    protected MenuPage menuPage = new MenuPage(driver);
    protected FooterPage footerPage = new FooterPage(driver);
    protected ProductPage productPage = new ProductPage(driver);
    protected SearchResultPage searchResultPage = new SearchResultPage(driver);

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
        menuPage.verifyMainMenuOptions();
        menuPage.verifySubMenuOptions(menuOptions);
    }


    @Test
    public void filtersTest() throws InterruptedException {
        menuPage.selectCategory("Art");
        log.info("***** Art category was opened. *****");
        menuPage.moveLeftSlider(10);
        log.info("***** Slider was moved left, if it was necessary. *****");
        menuPage.moveRightSlider(24);
        log.info("***** Slider was moved right, if it was necessary. Price range was selected. *****");
        menuPage.checkProductInPriceRange(10, 24);
        log.info("***** Products have been verified in terms of price. ***** ");
        menuPage.clearFilters();
        log.info("***** Filters ale cleared. *****");
    }

    @Test
    public void pricesDropTest(){
        footerPage.choosePricesDropOption();
        log.info("***** Drop prices option was chosen *****");
        footerPage.verifyIfOnSalePageIsLoaded();
        log.info("***** Correct page is loaded *****");
        menuPage.checkDiscountVisibility();
        log.info("***** Regular price, discount amount and discounted prices are correct *****");
        menuPage.selectProduct();
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
    public void basketAndCheckoutTests(){


    }



}


