package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FooterPage extends BasePage {


    public FooterPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "#link-product-page-prices-drop-1")
    private WebElement pricesDropBtn;

    @FindBy(xpath = "//li/span")
    private WebElement chosenCategoryName;


    public void choosePricesDropOption(){
        clickOnElement(pricesDropBtn);
    }

    public void verifyIfOnSalePageIsLoaded(){
        verifyVisibilityOfElement("On sale", chosenCategoryName);
    }



}
