package pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class BasePage {

//    public BasePage(WebDriver driver) {
//        PageFactory.initElements(driver, this);
//    }

    WebDriverWait wait;
    Actions actions;

    public WebElement getRandomElement(List<WebElement> elements) {
        Random random = new Random();
        WebElement randomProduct = elements.get(random.nextInt(elements.size()));
        return randomProduct;

    }

    public void clickOnElement(WebElement element) {
        element.click();
    }

    public void waitUntilVisibilityOfElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));

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

    public void assertIfEquals(String expected, String actual){
        Assert.assertEquals(expected,actual);
    }
    public Integer getAmountOfElements(List<WebElement> elementsList){
        return elementsList.size();
    }

}
