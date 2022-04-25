package pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage {
    public ProductPage(WebDriver driver) {
        super(driver);
    }


    @FindBy(xpath = "//div[@class='col-md-6']/h1")
    private WebElement displayedProduct;

    @FindBy(xpath = "//div[@class='current-price']/span[2]")
    private WebElement discountLabel;

    @FindBy(xpath = "//div[@class='product-discount']/span")
    private WebElement regularPrice;


    @FindBy(xpath = "//div[@class='current-price']/span[1]")
    private WebElement discountedPrice;

    public void verifyVisibilityOfLabel(){
        verifyVisibilityOfElement("SAVE 20%", discountLabel);
    }

    public void verifyVisibilityOfRegularPrice(){
        assertVisibilityOfElement(regularPrice);
    }


    public void verifyVisibilityOfDiscountedPrice(){
        assertVisibilityOfElement(discountedPrice);
    }

    public void verifyDiscount(){
        double regularPriceDouble = Double.parseDouble(regularPrice.getText().substring(1));
        double discountedPriceDisplayed = Double.parseDouble(discountedPrice.getText().substring(1));
        int discountPercentage = Integer.parseInt(discountLabel.getText().substring(5,discountLabel.getText().length()-1 ));
        double valueOfDiscount = countDiscount(regularPriceDouble, discountPercentage);
        double priceAfterDiscount = regularPriceDouble-valueOfDiscount;
        assertIfEquals(String.valueOf(discountedPriceDisplayed),String.valueOf(priceAfterDiscount));

    }



}
