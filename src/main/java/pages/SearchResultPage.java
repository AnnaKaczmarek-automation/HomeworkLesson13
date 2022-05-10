package pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResultPage extends BasePage {
    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    Logger log = LoggerFactory.getLogger("SearchResultPage.class");

    @FindBy(xpath = "//div[@class='product-description']/h2")
    private WebElement displayedProduct;


    public void verifyVisibilityOfProduct(String selectedProduct) {
        waitUntilVisibilityOfElement(displayedProduct);
        highlightElements(displayedProduct);
        verifyVisibilityOfElement(selectedProduct, displayedProduct);
        log.info("***** Correct products name is displayed as expected *****");
    }
}
