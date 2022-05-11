package pages;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.internal.EventFiringMouse;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webListener.WebListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BasePage {

    protected Actions actions;
    protected WebDriver driver;
    protected WebDriverWait wait;
    private EventFiringMouse eventFiringMouse;
    private ProductPage productPage;
    //    private MenuCategory menuCategory;
    private MenuPage menuPage;
    private ShopCartPopupPage shopCartPopupPage;
    private Logger log = LoggerFactory.getLogger("BasePage.class");

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = new WebDriverWait(driver, 40);
        actions = new Actions(driver);
    }

    @FindBy(xpath = "//span[@class='cart-products-count']")
    private WebElement basketAmount;

    public void mouseHover(WebElement element) {
        eventFiringMouse = new EventFiringMouse(driver, new WebListener());
        Locatable item = (Locatable) element;
        Coordinates coordinates = item.getCoordinates();
        eventFiringMouse.mouseMove(coordinates);
    }

    public WebElement getRandomElement(List<WebElement> elements) {
        Random random = new Random();
        WebElement randomElement = elements.get(random.nextInt(elements.size()));
        return randomElement;
    }

    public int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public void clickOnElement(WebElement element) {
        waitUntilElementIsClickable(element);
        highlightElements(element);
        element.click();
    }

    public void waitUntilVisibilityOfElement(WebElement element) {
        highlightElements(element);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitUntilVisibilityOfAllElements(List<WebElement> elements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void waitUntilElementIsClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public String createRandomMailAddress() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        String email = fakeValuesService.bothify("????##@gmail.com");
        return email;
    }

    public void waitUntilPageRefresh(WebElement element) {
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
    }

    public void highlightElements(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background:orange; border:5px solid red;')", element);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void verifyVisibilityOfElement(String selectedElement, WebElement element) {
        String expectedProductName = selectedElement;
        String actualProductName = element.getText();
        Assert.assertEquals(expectedProductName, actualProductName);
    }

    public void assertVisibilityOfElement(WebElement element) {
        Assert.assertTrue(element.isDisplayed());
    }

    public void assertIfEquals(String expected, String actual) {
        Assert.assertEquals(expected, actual);
    }

    public Integer getAmountOfElements(List<WebElement> elementsList) {
        return elementsList.size();
    }

    public Double calculateDiscount(double price, int discount) {
        return (price * discount) / 100;
    }

    public Integer getBasketAmount() {
        waitUntilVisibilityOfElement(basketAmount);
        String number = basketAmount.getText();
        int numberInBasket = Integer.parseInt(number.replaceAll("[^0-9.]", ""));
        log.info("Actual amount of products in basket is: " + numberInBasket);
        return numberInBasket;
    }

    public void openBasket() {
        clickOnElement(basketAmount);
    }

    public void setValueIntoInputBox(WebElement element, String input) {
        waitUntilVisibilityOfElement(element);
        element.clear();
        element.sendKeys(input);
    }

    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String currentDay = (dateFormat.format(date));
        return currentDay;
    }
}
