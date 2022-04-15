import configuration.TestBase;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.HomePage;
import pages.MenuPage;
import pages.SearchResultPage;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;


public class MyStoreTest extends TestBase {
    Logger log = LoggerFactory.getLogger("MyStoreTest.class");
    HomePage homePage = new HomePage(driver);
    MenuPage menuPage = new MenuPage(driver);
    SearchResultPage searchResultPage = new SearchResultPage(driver);

    @Test
    public void visibilityOfProductNameInSearchBoxTest() throws InterruptedException {

        String expectedName = homePage.getRandomProductName();
        menuPage.enterProductNameIntoSearchField(expectedName);
        List<String> foundProducts = menuPage.getSearchDropdownResults();

        assertThat(foundProducts).allMatch(element -> element.contains(expectedName));
        SoftAssertions softAssertions = new SoftAssertions();

//        for (String productName : foundProducts) {
//            softAssertions.assertThat(productName).contains(expectedName);
//        }
//        softAssertions.assertAll();
    }

    @Test
    public void searchRandomProductTest() throws InterruptedException {
        String expectedName = homePage.getRandomProductName();
        menuPage.enterProductNameIntoSearchField(expectedName);
        menuPage.clickLensButtonToSearch();
        searchResultPage.verifyVisibilityOfProduct(expectedName);
    }

    @Test
    public void menuCategoriesTest(){

        List<WebElement> menuOptions = menuPage.getMenuOptions();
        menuPage.verifyMainMenuOptions(menuOptions);
        menuPage.verifySubMenuOptions(menuOptions);



    }

}


