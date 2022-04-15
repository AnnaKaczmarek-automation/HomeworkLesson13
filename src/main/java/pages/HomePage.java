package pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//div[@class='product']/article[@class='product-miniature js-product-miniature']/div[@class='thumbnail-container reviews-loaded']/div[@class='product-description']/h3[@class='h3 product-title']/a")
    private List<WebElement> ListOfProducts;

    @FindBy(xpath = "//ul[@id='ui-id-1']/li[@class='ui-menu-item']/a[@id='ui-id-6']/span[@class='product']")
    private WebElement dropdownResult;


    public String getRandomProductName() {
        int sizeOfList = ListOfProducts.size();
//        System.out.println(sizeOfList);
//        int randomProductIndex = ThreadLocalRandom.current().nextInt(sizeOfList) % ListOfProducts.size();
//        WebElement randomProduct = ListOfProducts.get(randomProductIndex);
//        return randomProduct;
        String randomProductName = getRandomElement(ListOfProducts).getText();
        return randomProductName;

    }





    public void verifyProductInSearchDropdown(WebElement selectedElement) {

        String actualProductName = dropdownResult.getText();
        String expectedProductName = selectedElement.getText();
        Assert.assertEquals(expectedProductName, actualProductName);

    }


}
