package pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchResultPage extends BasePage {


    @FindBy(xpath = "//div[@class='product-description']/h2")
    private WebElement displayedProduct;

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    public void verifyVisibilityOfProduct(String selectedProduct) {
        waitUntilVisibilityOfElement(displayedProduct);
        highlightElements(displayedProduct);
        verifyVisibilityOfElement(selectedProduct, displayedProduct);
//            String expectedProductName = selectedProduct;
//            String actualProductName = displayedProduct.getText();
//            Assert.assertEquals(expectedProductName, actualProductName);
    }
}
