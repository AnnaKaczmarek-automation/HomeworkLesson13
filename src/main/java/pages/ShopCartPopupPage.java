package pages;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class ShopCartPopupPage extends BasePage{

    private DecimalFormat dFormat;

    public ShopCartPopupPage(WebDriver driver) {
        super(driver);
    }
    Logger log = LoggerFactory.getLogger("ShopCartPopupPage.class");

//name, price, quantity, there are X items in your cart, Total products value
    @FindBy(xpath = "//div[@class='col-md-6']/h6")
    private WebElement displayedName;

    @FindBy(xpath = "//div[@class='col-md-6']/p")
    private WebElement displayedPrice;

    @FindBy(xpath = "//div[@class='col-md-6']/span[2]/strong")
    private WebElement displayedQuantity2Span;

    @FindBy(xpath = "//div[@class='col-md-6']/span/strong")
    private WebElement displayedQuantity1Span;

    @FindBy(xpath = "//div[@class='cart-content']/p[1]")
    private WebElement itemsAmountInfo;

    @FindBy(xpath = "//div[@class='cart-content']/p[4]/span[2]")
    private WebElement totalAmountInfo;

    @FindBy(xpath = "//div[@class='cart-content']/p[3]/span[2]")
    private WebElement shippingInfo;

//    @FindBy(xpath = "//div[@id='blockcart-modal']/div[1]/div[1]")
    @FindBy(xpath = "//div[@id='blockcart-modal']/div[@class='modal-dialog']/div[@class='modal-content']")
    private WebElement shopCartPopUp;

    @FindBy(xpath = "//button[@class='btn btn-secondary']")
    private WebElement continueShoppingBtn;

    public WebElement getShopCartPopUp() {
        return shopCartPopUp;
    }

    public void continueShopping(){
        clickOnElement(continueShoppingBtn);
    }
    public void verifyShopCartData(String name, double price, int quantity) throws InterruptedException {
//        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(shopCartPopUp));//przy tej mwtodzie wyswietla się popop ale test sie wywala
//        //bo niby go nie widzi. pokombinowac z metodą wykorzystujacą driver.getHandles();
//        waitUntilVisibilityOfElement(shopCartPopUp);

        waitUntilVisibilityOfElement(shopCartPopUp);
        highlightElements(displayedName);
        String actualProductName = displayedName.getText();
        log.info("Actual product name is: " + actualProductName);
        highlightElements(displayedPrice);
        double actualProductPrice = Double.parseDouble(displayedPrice.getText().substring(1));
        log.info("Actual product price is: " + actualProductPrice);


        int actualQuantity = 0;
        List<WebElement> spanElNumber = driver.findElements(By.xpath("//div[@class='col-md-6']/span"));
        if(spanElNumber.size()==2){
            highlightElements(displayedQuantity2Span);
            actualQuantity = Integer.parseInt(displayedQuantity2Span.getText());
            log.info("Actual quantity of product in a cart is: " + actualQuantity);
        }
        if(spanElNumber.size()==1){
            highlightElements(displayedQuantity1Span);
            actualQuantity = Integer.parseInt(displayedQuantity1Span.getText());
            log.info("Actual quantity of product in a cart is: " + actualQuantity);
        }

        int numberInAmountInfo =0;
        if(actualQuantity == 1 ){

            highlightElements(itemsAmountInfo);
            String valueInString = itemsAmountInfo.getText().substring(9,itemsAmountInfo.getText().length()-19);
            numberInAmountInfo = Integer.parseInt(StringUtils.trim(valueInString));
        }
        if(actualQuantity > 1){
            highlightElements(itemsAmountInfo);
            numberInAmountInfo = Integer.parseInt(itemsAmountInfo.getText().substring(10,itemsAmountInfo.getText().length()-20));
        }

        highlightElements(totalAmountInfo);
        double actualTotalAmount = Double.parseDouble(totalAmountInfo.getText().substring(1));
        highlightElements(shippingInfo);
        Thread.sleep(4000);
        double shippingPrice = Double.parseDouble(shippingInfo.getText().substring(1));
//        dFormat = new DecimalFormat("#,###.##");
//        double shippingDouble = Double.parseDouble(dFormat.format(shippingPrice));

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualProductName).isEqualTo(name);
        softAssertions.assertThat(actualProductPrice).isEqualTo(price);
        softAssertions.assertThat(actualQuantity).isEqualTo(quantity);
        softAssertions.assertThat(numberInAmountInfo).isEqualTo(quantity);
        double totalAmount = (actualProductPrice * actualQuantity) + shippingPrice;
        softAssertions.assertThat(actualTotalAmount).isEqualTo(totalAmount);

    }

    public void switchToOpenedWindow(String parentWindow, WebElement popup){

//        String subWindowHandler = null;
//
//        Set<String> handles = driver.getWindowHandles(); // get all window handles
//        Iterator<String> iterator = handles.iterator();
//        while (iterator.hasNext()){
//            subWindowHandler = iterator.next();
//        }
//        driver.switchTo().window(subWindowHandler); // switch to popup window
//        System.out.println("window is switched");

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(shopCartPopUp));
        Set<String> childWindows = driver.getWindowHandles();
        int count = childWindows.size();
        for(String child : childWindows){
            if(!parentWindow.equalsIgnoreCase(child)){
                System.out.println("childs window id is: " + child);
                driver.switchTo().window(child);
//
            }
        }

// Now you are in the popup window, perform necessary actions here
    }

    public void switchToLastOpenedWindow() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='blockcart-modal']/div[@class='modal-dialog']/div[@class='modal-content']")));

        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
        log.info("<<<<<<<<<< Switch to last opened window");
    }

}
