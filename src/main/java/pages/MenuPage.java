package pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuPage extends BasePage {
    public MenuPage(WebDriver driver) {
        super(driver);
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
    private List<WebElement> displayedProducts;

    @FindBy(xpath = "//div[@class='col-md-6 hidden-sm-down total-products']/p")
    private WebElement amountInfo;

    @FindBy(xpath = "//div[contains(@class,'ui-slider')]/a[1]")
    private WebElement leftSlider;

    @FindBy(xpath = "//div[contains(@class,'ui-slider')]/a[2]")
    private WebElement rightSlider;

    @FindBy(xpath = "//li/p[1]")
    private WebElement priceRange;

    @FindBy(xpath = "//button[@class='btn btn-tertiary js-search-filters-clear-all']/i")
    private WebElement clearFiltersBtn;

    @FindBy(xpath = "//ul[@class='product-flags']/li")
    private WebElement discountLabel;

    Logger log = LoggerFactory.getLogger("MenuPage.class");


    public void clickLensButtonToSearch() {
        waitUntilVisibilityOfElement(lensButton);
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

    public void verifyMainMenuOptions() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfAllElements(categoriesList));
        List<WebElement> menuOptions = getMenuOptions();
        Iterator<WebElement> category = menuOptions.iterator();
        while (category.hasNext()) {
            waitForMenuRefresh();
            for (WebElement option : menuOptions) {
                waitUntilElementIsClickable(option);
                highlightElements(option);
                String expectedName = option.getText();
                log.info("Expected category name in: " + expectedName);
                Thread.sleep(3000);

                clickOnElement(option);
                log.info("category was chosen");
                Thread.sleep(3000);

                highlightElements(displayedCategory);
                String actualOption = displayedCategory.getText().toUpperCase();
                assertIfEquals(expectedName, actualOption);
                log.info("Correct category was displayed");
                Thread.sleep(3000);

                highlightElements(filterMenu);
                Assert.assertTrue(filterMenu.isDisplayed());
                log.info("FilterMenu was displayed");

                Integer optionAmount = getAmountOfElements(displayedProducts);
                log.info("Amount of displayed products is: " + optionAmount);
                highlightElements(amountInfo);
                String actualAmount = amountInfo.getText();
                log.info("Label information is: " + "'" + actualAmount + "'");
                assertThat(actualAmount).contains(String.valueOf(optionAmount));
                log.info("Amount of available options is correct");
                driver.navigate().back();
            }

        }
    }

    public void waitForMenuRefresh() {
        wait.until((ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ol/li/span")))));

    }

    public void verifySubMenuOptions(List<WebElement> categoriesList) {
        List<WebElement> subOptionsList = new ArrayList<>();
        for (WebElement category : categoriesList) {
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

                Integer optionAmount = getAmountOfElements(displayedProducts);
                String actualAmount = amountInfo.getText();
                assertThat(actualAmount).contains(String.valueOf(optionAmount));
            }
        }
    }

    public void selectCategory(String categoryName) {
        for (WebElement category : categoriesList) {
            waitUntilVisibilityOfElement(category);
            if (category.getText().equals(categoryName.toUpperCase())) {
                highlightElements(category);
                clickOnElement(category);
            }
        }
    }

    public void moveLeftSlider(int number) throws InterruptedException {
        waitUntilVisibilityOfElement(priceRange);
        String priceRangeText = priceRange.getText();
        String lowerPriceString = priceRangeText.substring(1, priceRangeText.length() - 12);
        int lowerPriceInt = Integer.parseInt(lowerPriceString);

        if (number > 8 && number < 30) {
            while (lowerPriceInt < number) {
                highlightElements(leftSlider);
                leftSlider.sendKeys(Keys.ARROW_RIGHT);
                lowerPriceInt = Integer.parseInt(priceRange.getText().substring(1, priceRange.getText().length() - 12));
            }
            while (lowerPriceInt > number) {
                highlightElements(leftSlider);
                leftSlider.sendKeys(Keys.ARROW_LEFT);
                lowerPriceInt = Integer.parseInt(priceRange.getText().substring(1, priceRange.getText().length() - 12));
            }
            String priceAfterChange = priceRange.getText();
            String lowerPrice = priceAfterChange.substring(1, priceAfterChange.length() - 12);
            int lowerPriceInteger = Integer.parseInt(lowerPrice);
            Assert.assertEquals(number, lowerPriceInteger);
            log.info("Given value equals: " + lowerPriceInteger);
        }
    }

    public void moveRightSlider(int number) {
        waitUntilVisibilityOfElement(priceRange);
        if (number < 10) {
            String priceRangeText = priceRange.getText();
            String higherPriceString = priceRangeText.substring(9, priceRangeText.length() - 3);
            int higherPriceInt = Integer.parseInt(higherPriceString);
            while (higherPriceInt < number) {
                highlightElements(rightSlider);
                rightSlider.sendKeys(Keys.ARROW_RIGHT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(9, priceRange.getText().length() - 3));
            }
            while (higherPriceInt > number) {
                highlightElements(rightSlider);
                rightSlider.sendKeys(Keys.ARROW_LEFT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(9, priceRange.getText().length() - 3));
            }
            String priceAfterChange = priceRange.getText();
            String higherPrice = priceAfterChange.substring(9, priceAfterChange.length() - 3);
            int higherPriceInteger = Integer.parseInt(higherPrice);
            Assert.assertEquals(number, higherPriceInteger);
            log.info("Given value equals: " + higherPriceInteger);
        }


        if (number > 9 && number < 30) {
            String priceRangeText = priceRange.getText();
            String higherPriceString = priceRangeText.substring(10, priceRangeText.length() - 3);
            int higherPriceInt = Integer.parseInt(higherPriceString);
            while (higherPriceInt < number) {
                highlightElements(rightSlider);
                rightSlider.sendKeys(Keys.ARROW_RIGHT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(10, priceRange.getText().length() - 3));
            }
            while (higherPriceInt > number) {
                highlightElements(rightSlider);
                rightSlider.sendKeys(Keys.ARROW_LEFT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(10, priceRange.getText().length() - 3));
            }

            String priceAfterChange2 = priceRange.getText();
            String higherPrice2 = priceAfterChange2.substring(10, priceAfterChange2.length() - 3);
            int higherPriceInteger2 = Integer.parseInt(higherPrice2);
            Assert.assertEquals(number, higherPriceInteger2);
            log.info("Given value equals: " + higherPriceInteger2);
        }
    }

    public void checkProductInPriceRange(int lowerNumber, int higherNumber) {
        for (WebElement product : displayedProducts) {
            WebElement productPrice = driver.findElement(By.xpath("//span[@class='price']"));
            String productText = productPrice.getText();
            String productPriceString = productText.substring(1, productText.length() - 3);
            int productPriceInt = Integer.parseInt(productPriceString);
            System.out.println(productPriceInt);
            if (productPriceInt <= higherNumber && productPriceInt >= lowerNumber) {
                log.info("Product: " + product.getText() + " price belong to given price range.");
            } else {
                log.info("Product " + product.getText() + " price DOES NOT belong to given price range.");
            }
        }
    }

    public void clearFilters() {
        waitUntilElementIsClickable(clearFiltersBtn);
        clickOnElement(clearFiltersBtn);
    }

    public void checkDiscountVisibility() {
        for (WebElement product : displayedProducts) {
            waitUntilVisibilityOfElement(product);
            WebElement discount = driver.findElement(By.xpath("//ul[@class='product-flags']/li"));
            String discountAmount = discount.getText();
            String actualDiscountAmount = discountAmount.substring(1, discountAmount.length() - 1);
            String expectedDiscountAmount = "20";

            WebElement name = driver.findElement(By.xpath("//div[@class='product-description']/h2"));
            String productName = name.getText();
            assertIfEquals(expectedDiscountAmount, actualDiscountAmount);
            log.info("Correct discount was given to the product " + productName);

            WebElement productRegPrice = driver.findElement(By.xpath("//div[@class='product-price-and-shipping']/span[1]"));
            String regPriceText = productRegPrice.getText();
            String regularPrice = regPriceText.substring(1);
            double regularPriceDouble = Double.parseDouble(regularPrice);
            log.info("Regular product prise is: " + regularPriceDouble);

            WebElement productDiscPrice = driver.findElement(By.xpath("//div[@class='product-price-and-shipping']/span[3]"));
            String discPriceText = productDiscPrice.getText();
            String discPrice = discPriceText.substring(1);
            double discPriceDouble = Double.parseDouble(discPrice);
            log.info("Discounted product price is: " + discPriceDouble);

            double discountValue = countDiscount(regularPriceDouble, Integer.parseInt(expectedDiscountAmount));
            double displayedDiscountedPrice = regularPriceDouble - discountValue;

            assertIfEquals(String.valueOf(displayedDiscountedPrice), discPrice);
        }
    }

    public void selectProduct() {
        clickOnElement(getRandomElement(displayedProducts));
    }
}


