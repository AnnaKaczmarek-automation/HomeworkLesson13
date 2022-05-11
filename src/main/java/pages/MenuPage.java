package pages;

import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuPage extends BasePage {
    public MenuPage(WebDriver driver) {
        super(driver);
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");
//    MenuCategory menuCategory = new MenuCategory(driver);

    @FindBy(css = ".material-icons.search")
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

    @FindBy(xpath = "//div[@class='product-price-and-shipping']/span[1]")
    private WebElement regularPrice;

    @FindBy(xpath = "//div[@class='product-price-and-shipping']/span[3]")
    private WebElement discountPrice;

    @FindBy(xpath = "//div[@class='product-description']/h2/a")
    private WebElement productName;


    Logger log = LoggerFactory.getLogger("MenuPage.class");

    public  void chooseRandomCategoryAndProduct() throws InterruptedException {
        getRandomCategory();
        log.info("***** Random category is chosen *****");
        WebElement randomProduct = getRandomElement(displayedProducts);
        randomProduct.click();
//        clickOnElement(randomProduct);

        log.info("***** Random product is chosen *****");
        Thread.sleep(3000);
    }
    public void getRandomCategory() {
        clickOnElement(getRandomElement(categoriesList));
    }

    public void clickLensButtonToSearch() {
        waitUntilVisibilityOfElement(lensButton);
        clickOnElement(lensButton);
        log.info("***** Search was submitted *****");
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
        log.info("***** Products name is typed into the search box *****");
        return this;
    }

    public List<WebElement> getMenuOptions() {
        return categoriesList;
    }

    public void verifyMainMenuOptions() {
        List<WebElement> menuOptions = getMenuOptions();
        waitUntilVisibilityOfAllElements(menuOptions);
//        wait.until(ExpectedConditions.visibilityOfAllElements(categoriesList));
        for (int i = 0; i < menuOptions.size(); i++) {
            WebElement option = menuOptions.get(i);
            waitUntilElementIsClickable(option);
            highlightElements(option);
            String expectedName = option.getText();
            log.info("Expected category name in: " + expectedName);

            clickOnElement(option);
            log.info("category was chosen");

            highlightElements(displayedCategory);
            String actualOption = displayedCategory.getText().toUpperCase();
            assertIfEquals(expectedName, actualOption);
            log.info("Correct category was displayed");

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
            menuOptions = getMenuOptions();
        }
    }

    public void waitForMenuRefresh() {
        wait.until((ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ol/li/span")))));
    }

    public void verifySubMenuOptions() {
        SoftAssertions softAssertions = new SoftAssertions();
        List<WebElement> menuOptions = getMenuOptions();
        for (int i = 0; i < menuOptions.size(); i++) {
            clickOnElement(menuOptions.get(i));
            List<WebElement> subCategories = driver.findElements(By.cssSelector(".category-sub-menu li a"));
            if (subCategories.size() > 0) {
                for (int j = 0; j < subCategories.size(); j++) {
                    waitUntilVisibilityOfElement(subCategories.get(j));
                    String expectedName = subCategories.get(j).getText();
                    log.info("Expected subcategory name is: " + expectedName);

                    clickOnElement(subCategories.get(j));
                    log.info("SubOption was chosen");

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ol li:nth-child(3) ")));
                    String actualOption = displayedCategory.getText();
                    softAssertions.assertThat(actualOption).isEqualTo(expectedName);
                    log.info("Correct subOption was displayed");

                    Assert.assertTrue(filterMenu.isDisplayed());
                    log.info("FilterMenu was displayed");

                    displayedProducts = driver.findElements(By.xpath("//article[@class='product-miniature js-product-miniature']"));
                    Integer optionAmount = getAmountOfElements(displayedProducts);
                    String actualAmount = amountInfo.getText();
                    softAssertions.assertThat(actualAmount).contains(String.valueOf(optionAmount));
                    log.info("Amount of displayed products given in text information is correct");

                    driver.navigate().back();
                    subCategories = driver.findElements(By.cssSelector(".category-sub-menu li a"));
                    wait.until(ExpectedConditions.visibilityOfAllElements(subCategories));
                }
                softAssertions.assertAll();
                getMenuOptions();
            }
            if (subCategories.size() == 0) {
                log.info(menuOptions.get(i) + "Category does not have subcategories");
            }
        }
    }


    public void selectCategory(String categoryName) {
        for (WebElement category : categoriesList) {
            waitUntilVisibilityOfElement(category);
            if (category.getText().equals(categoryName.toUpperCase())) {
                highlightElements(category);
                clickOnElement(category);
                log.info("***** " + category + " category was opened. *****");
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
                highlightElements(driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[1]")));
                driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[1]")).sendKeys(Keys.ARROW_RIGHT);
                lowerPriceInt = Integer.parseInt(priceRange.getText().substring(1, priceRange.getText().length() - 12));
            }
            while (lowerPriceInt > number) {
                highlightElements(driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[1]")));
                driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[1]")).sendKeys(Keys.ARROW_LEFT);
                lowerPriceInt = Integer.parseInt(priceRange.getText().substring(1, priceRange.getText().length() - 12));
            }
            String priceAfterChange = priceRange.getText();
            String lowerPrice = priceAfterChange.substring(1, priceAfterChange.length() - 12);
            int lowerPriceInteger = Integer.parseInt(lowerPrice);
            Assert.assertEquals(number, lowerPriceInteger);
            log.info("Given value equals: " + lowerPriceInteger);
        }
        log.info("***** Slider was moved left, if it was necessary. *****");
    }

    public void moveRightSlider(int number) {
        waitUntilVisibilityOfElement(priceRange);
        if (number < 10) {
            String priceRangeText = priceRange.getText();
            String higherPriceString = priceRangeText.substring(9, priceRangeText.length() - 3);
            int higherPriceInt = Integer.parseInt(higherPriceString);
            while (higherPriceInt < number) {
                highlightElements(driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[2]")));
                driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[2]")).sendKeys(Keys.ARROW_RIGHT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(9, priceRange.getText().length() - 3));
            }
            while (higherPriceInt > number) {
                highlightElements(driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[2]")));
                driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[2]")).sendKeys(Keys.ARROW_LEFT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(9, priceRange.getText().length() - 3));
            }
            String priceAfterChange = priceRange.getText();
            String higherPrice = priceAfterChange.substring(9, priceAfterChange.length() - 3);
            int higherPriceInteger = Integer.parseInt(higherPrice);
            Assert.assertEquals(number, higherPriceInteger);
            log.info("Given value equals: " + higherPriceInteger);
        }


        if (number >= 10 && number < 30) {
            String priceRangeText = priceRange.getText();
            String higherPriceString = priceRangeText.substring(9, priceRangeText.length() - 3);
            int higherPriceInt = Integer.parseInt(higherPriceString);
            while (higherPriceInt < number) {
                highlightElements(driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[2]")));
                rightSlider.sendKeys(Keys.ARROW_RIGHT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(9, priceRange.getText().length() - 3));
            }
            while (higherPriceInt > number) {
                highlightElements(driver.findElement(By.xpath("//div[contains(@class,'ui-slider')]/a[2]")));
                rightSlider.sendKeys(Keys.ARROW_LEFT);
                higherPriceInt = Integer.parseInt(priceRange.getText().substring(9, priceRange.getText().length() - 3));
            }

            String priceAfterChange2 = priceRange.getText();
            String higherPrice2 = priceAfterChange2.substring(9, priceAfterChange2.length() - 3);
            int higherPriceInteger2 = Integer.parseInt(higherPrice2);
            Assert.assertEquals(number, higherPriceInteger2);
            log.info("Given value equals: " + higherPriceInteger2);
        }
        log.info("***** Slider was moved right, if it was necessary. Price range was selected. *****");
    }

    public void checkProductInPriceRange(int lowerNumber, int higherNumber) {
        List<WebElement> displayedProducts = driver.findElements(By.xpath("//article[@class='product-miniature js-product-miniature']"));
        for (WebElement product : displayedProducts) {
//            WebElement productPrice = driver.findElement(By.xpath("//span[@class='price']"));
//            String productName = driver.findElement(By.cssSelector(".h3.product-title a")).getText();
            double productPriceText = Double.parseDouble(driver.findElement(By.xpath("//span[@class='price']")).getText().replaceAll("[^0-9.]", ""));

            System.out.println(productPriceText);

            if (productPriceText <= higherNumber && productPriceText >= lowerNumber) {
                log.info("Product: " + driver.findElement(By.xpath(".//div[@class='product-description']/h2[@class='h3 product-title']/a")).getText() + " price belong to given price range.");
            } else {
                log.info("Product " + product.getText() + " price DOES NOT belong to given price range.");
            }
        }
        log.info("***** Products have been verified in terms of price. ***** ");
    }

    public void clearFilters() {
        waitUntilElementIsClickable(clearFiltersBtn);
        driver.findElement(By.xpath("//button[@class='btn btn-tertiary js-search-filters-clear-all']/i")).click();

        log.info("***** Filters ale cleared. *****");
    }

    public void checkDiscountVisibility() {
        wait.until(c -> displayedProducts.size() == 2);
        if (displayedProducts.size() > 0) {
            for (WebElement product : displayedProducts) {

                waitUntilVisibilityOfElement(product);
                WebElement discount = product.findElement(By.xpath(".//ul[@class='product-flags']/li"));
                String discountAmount = discount.getText();
                String actualDiscountAmount = discountAmount.substring(1, discountAmount.length() - 1);
                String expectedDiscountAmount = "20";

                String displayedProductName = product.findElement(By.xpath(".//div[@class='product-description']/h2/a")).getText();
                assertIfEquals(expectedDiscountAmount, actualDiscountAmount);
                log.info("Correct discount was given to the product " + displayedProductName);

                String regPriceText = regularPrice.getText();
                String regularPrice = regPriceText.substring(1);
                double regularPriceDouble = Double.parseDouble(regularPrice);
                log.info("Regular product prise is: " + regularPriceDouble);

                String discPriceText = discountPrice.getText();
                String discPrice = discPriceText.substring(1);
                double discPriceDouble = Double.parseDouble(discPrice);
                log.info("Discounted product price is: " + discPriceDouble);

                double discountValue = calculateDiscount(regularPriceDouble, Integer.parseInt(expectedDiscountAmount));
                double calculatedDiscountedPrice = Double.parseDouble(df.format(regularPriceDouble - discountValue));

                assertIfEquals(String.valueOf(calculatedDiscountedPrice), discPrice);
            }
        } else {
            log.info("Any product is displayed");
        }
        log.info("***** Regular price, discount amount and discounted prices are correct *****");
    }

    public void selectRandomProduct() {
//        waitUntilVisibilityOfAllElements(displayedProducts);
        WebElement randomProduct = getRandomElement(displayedProducts);
        waitUntilVisibilityOfElement(randomProduct);
        randomProduct.click();
        log.info("***** Product is selected *****");
    }

}


