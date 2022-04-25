package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class BasePage {
    protected WebDriverWait wait;
    protected Actions actions;
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = new WebDriverWait(driver, 15);
        actions = new Actions(driver);
    }


    public WebElement getRandomElement(List<WebElement> elements) {
        Random random = new Random();
        WebElement randomProduct = elements.get(random.nextInt(elements.size()));
        return randomProduct;

    }

    public void clickOnElement(WebElement element) {
        waitUntilElementIsClickable(element);
        highlightElements(element);
        element.click();
    }

    public void waitUntilVisibilityOfElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));

    }

    public void waitUntilElementIsClickable(WebElement element){
        highlightElements(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void highlightElements(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background:orange; border:5px solid red;')", element);
        try{
            Thread.sleep(1500);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }



//    public void hooverOnElement(WebElement element) {
//
//        actions = new Actions(this.driver);
//        actions.moveToElement(element).click().perform();
//    }

    public void verifyVisibilityOfElement(String selectedElement, WebElement element)
    {
        String expectedProductName = selectedElement;
        String actualProductName = element.getText();
        Assert.assertEquals(expectedProductName, actualProductName);
    }

    public void findElementsByXpath(String locator){
        driver.findElements(By.xpath(locator));
    }


    public void assertVisibilityOfElement(WebElement element){
        Assert.assertTrue(element.isDisplayed());
    }

    public void assertIfEquals(String expected, String actual){
        Assert.assertEquals(expected,actual);
    }
    public Integer getAmountOfElements(List<WebElement> elementsList){
        return elementsList.size();
    }

    public void openCategory(List<WebElement> element, String name){
        for (WebElement el : element) {
            waitUntilVisibilityOfElement(el);
            if(el.getText().equals(name)){
                highlightElements(el);
                el.click();
            }
        }
    }

    public Double countDiscount(double price, int discount){
        return (price * discount)/100;
    }
}
