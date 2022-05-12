package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FooterPage extends BasePage {
    public FooterPage(WebDriver driver) {
        super(driver);
    }

    Logger log = LoggerFactory.getLogger("FooterPage");

    @FindBy(css = "#link-product-page-prices-drop-1")
    private WebElement pricesDropBtn;

    @FindBy(xpath = "//li/span")
    private WebElement chosenCategoryName;

    public void choosePricesDropOption() {
        clickOnElement(pricesDropBtn);
        log.info("***** Drop prices option was chosen *****");
    }

    public void verifyIfOnSalePageIsLoaded() {
        verifyVisibilityOfElement("On sale", chosenCategoryName);
        log.info("***** Correct page is loaded *****");
    }
}
