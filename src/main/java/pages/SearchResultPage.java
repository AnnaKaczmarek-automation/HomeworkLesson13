package pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchResultPage extends BasePage {
    public SearchResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

        @FindBy(xpath = "//div[@class='product']/article[@class='product-miniature js-product-miniature']/div[@class='thumbnail-container reviews-loaded']/div[@class='product-description']/h3[@class='h3 product-title']/a")
        private WebElement displayedProduct;



    public void verifyVisibilityOfProduct(String selectedProduct)
    {
            verifyVisibilityOfElement(selectedProduct, displayedProduct);
//            String expectedProductName = selectedProduct;
//            String actualProductName = displayedProduct.getText();
//            Assert.assertEquals(expectedProductName, actualProductName);
    }
}
