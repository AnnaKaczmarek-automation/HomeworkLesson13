package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    Logger log= LoggerFactory.getLogger("HomePage.class");

//    @FindBy(xpath = "//div[@class='product']/article[@class='product-miniature js-product-miniature']/div[@class='thumbnail-container reviews-loaded']/div[@class='product-description']/h3[@class='h3 product-title']/a")
//    private List<WebElement> ListOfProducts;

    @FindBy(css = ".products.row article")
    private List<WebElement> ListOfProducts;

    @FindBy(xpath = "//ul[@id='ui-id-1']/li[@class='ui-menu-item']/a[@id='ui-id-6']/span[@class='product']")
    private WebElement dropdownResult;

    @FindBy(xpath = "//div[@class='product-description']/h3/a")
    private List<WebElement> productsNameList;

    @FindBy(xpath = "//span[text()='Sign in']")
    private WebElement signInBtn;

    @FindBy(css = ".no-account a")
    private WebElement registerBtn;


    public WebElement getRandomProduct() {
//        int sizeOfList = ListOfProducts.size();
//        System.out.println(sizeOfList);
//        int randomProductIndex = ThreadLocalRandom.current().nextInt(sizeOfList) % ListOfProducts.size();
//        WebElement randomProduct = ListOfProducts.get(randomProductIndex);
//        return randomProduct;
        WebElement randomProduct = getRandomElement(ListOfProducts);
        log.info("***** Random product is chosen *****");
        return randomProduct;


    }

    public String getProductsName(WebElement element){
        String productsName = String.valueOf(element.findElement(By.xpath("//div[@class='product-description']/h3/a")).getText());
        return productsName;
    }

    public void verifyProductInSearchDropdown(WebElement selectedElement) {
        String actualProductName = dropdownResult.getText();
        String expectedProductName = selectedElement.getText();
        Assert.assertEquals(expectedProductName, actualProductName);
    }

    public void choseSignInOption(){
        clickOnElement(signInBtn);
    }

    public void choseRegisterOption(){
        clickOnElement(registerBtn);
    }


}
