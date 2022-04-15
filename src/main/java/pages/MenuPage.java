package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuPage extends BasePage {
    public MenuPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@id='search_widget']/form/button[@type='submit']")
    private WebElement lensButton;

    @FindBy(xpath = "//input[@class='ui-autocomplete-input']")
    private WebElement searchField;

    @FindBy(xpath = "//ul[@id='ui-id-1']/li/a/span[@class='product']")
    private List<WebElement> searchResultsList;

    @FindBy(xpath = "//ul[@id='top-menu']/li/a")
    private List<WebElement> categoriesList;

    @FindBy(xpath = "//ol/li/span")
    private WebElement displayedCategory;

    @FindBy(xpath = "//div[@id='search_filters_wrapper']")
    private WebElement filterMenu;

    @FindBy(xpath = "//article[@class='product-miniature js-product-miniature']")
    private List<WebElement> displayedOptions;

    @FindBy(xpath = "//div[@class='col-md-6 hidden-sm-down total-products']/p")
    private WebElement amountInfo;

    Logger log = LoggerFactory.getLogger("MenuPage.class");


    public void clickLensButtonToSearch() {
        waitUntilVisibilityOfElement(lensButton);
//        hooverOnElement(lensButton);
        clickOnElement(lensButton);
    }


    public List<String> getSearchDropdownResults() {
        List<String> resultNamesList = new ArrayList<>();
        for (WebElement product : searchResultsList) {
            resultNamesList.add(product.getText());
        }
        return resultNamesList;
    }

    public MenuPage enterProductNameIntoSearchField(String productName) throws InterruptedException {
        searchField.sendKeys(productName);
        Thread.sleep(5000);
        return this;
    }

    public List<WebElement> getMenuOptions() {
        return categoriesList;
    }


    public void verifyMainMenuOptions(List<WebElement> categoriesList) {
//        List<WebElement> subOptionsList = new ArrayList<>();
        for (WebElement category : categoriesList) {
//            subOptionsList = category.findElements(By.xpath("//div[@class='popover sub-menu js-sub-menu collapse']/ul[@class='top-menu']/li/a"));
//            for (WebElement subOption : subOptionsList) {
            String expectedName = category.getText();
            log.info("Expected category name in: " + expectedName);

            clickOnElement(category);
            log.info("category was chosen");

            //zrobic z tego metodę
            String actualOption = displayedCategory.getText().toUpperCase();
            assertIfEquals(expectedName, actualOption);
            log.info("Correct category was displayed");


            //zrobic z tego nowa metodę
            Assert.assertTrue(filterMenu.isDisplayed());
            log.info("FilterMenu was displayed");


            //zroic z tego metodę
            Integer optionAmount = getAmountOfElements(displayedOptions);
            String actualAmount = amountInfo.getText();
            assertThat(actualAmount).contains(String.valueOf(optionAmount));
        }
    }
//    }

    public void verifySubMenuOptions(List<WebElement> categoriesList) {
        List<WebElement> subOptionsList = new ArrayList<>();
        for (WebElement category : categoriesList) {

            //tutaj dodac if'a, że jesli lista podkategorii nie jest pusta to wtedy przeiteruj. Jeśli jest pusta to zwróc loga, że głowna kategoria nie ma podkategorii
            subOptionsList = category.findElements(By.xpath("//div[@class='popover sub-menu js-sub-menu collapse']/ul[@class='top-menu']/li/a"));
            for (WebElement subOption : subOptionsList) {
                String expectedName = subOption.getText();
                log.info("Expected option name in: " + expectedName);

                subOption.click();
                log.info("SubOption was chosen");

                String actualOption = displayedCategory.getText();
                assertIfEquals(expectedName, actualOption);
                log.info("Correct subOption was displayed");

                Assert.assertTrue(filterMenu.isDisplayed());
                log.info("FilterMenu was displayed");

                Integer optionAmount = getAmountOfElements(displayedOptions);
                String actualAmount = amountInfo.getText();
                assertThat(actualAmount).contains(String.valueOf(optionAmount));
            }
        }

    }


}



