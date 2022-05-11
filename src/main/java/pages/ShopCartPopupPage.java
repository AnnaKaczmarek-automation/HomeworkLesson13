package pages;

import models.Product;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class ShopCartPopupPage extends BasePage {

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

    @FindBy(xpath = "//div[@id='blockcart-modal']/div[@class='modal-dialog']/div[@class='modal-content']")
    private WebElement shopCartPopUp;

    @FindBy(xpath = "//button[@class='btn btn-secondary']")
    private WebElement continueShoppingBtn;

    @FindBy(css = ".btn.btn-primary .material-icons.rtl-no-flip")
    private WebElement proceedToCheckoutBtn;

    public WebElement getShopCartPopUp() {
        return shopCartPopUp;
    }

    public void proceedToCheckoutBtn() {
        clickOnElement(proceedToCheckoutBtn);
    }

    public void continueShopping() {
        clickOnElement(continueShoppingBtn);
        log.info("***** Continue shopping button was chosen *****");
    }

    public Integer getNumberFromItemsAmountInfo() {
        int number = Integer.parseInt(itemsAmountInfo.getText().replaceAll("[^0-9]", ""));
        return number;
    }
    public void verifyShopCartData(Product product) throws InterruptedException, IOException, AWTException {
        waitUntilVisibilityOfElement(shopCartPopUp);
        highlightElements(displayedName);
        String actualProductName = displayedName.getText();
        log.info("Actual product name is: " + actualProductName);
        highlightElements(displayedPrice);
        double actualProductPrice = Double.parseDouble(displayedPrice.getText().substring(1));
        log.info("Actual product price is: " + actualProductPrice);

        int actualQuantity = 0;
        List<WebElement> spanElNumber = driver.findElements(By.xpath("//div[@class='col-md-6']/span"));
        if (spanElNumber.size() == 2) {
            highlightElements(displayedQuantity2Span);
            actualQuantity = Integer.parseInt(displayedQuantity2Span.getText());
            log.info("Actual quantity of product in a cart is: " + actualQuantity);
        }
        if (spanElNumber.size() == 1) {
            highlightElements(displayedQuantity1Span);
            actualQuantity = Integer.parseInt(displayedQuantity1Span.getText());
            log.info("Actual quantity of product in a cart is: " + actualQuantity);
        }

        int numberInAmountInfo = 0;
        if (actualQuantity == 1) {
            highlightElements(itemsAmountInfo);
            String valueInString = itemsAmountInfo.getText().substring(9, itemsAmountInfo.getText().length() - 19);
            numberInAmountInfo = Integer.parseInt(StringUtils.trim(valueInString));
        }
        if (actualQuantity > 1) {
            highlightElements(itemsAmountInfo);
            numberInAmountInfo = Integer.parseInt(itemsAmountInfo.getText().substring(10, itemsAmountInfo.getText().length() - 20));
        }

        highlightElements(totalAmountInfo);
        double actualTotalAmount = Double.parseDouble(totalAmountInfo.getText().substring(1));
        highlightElements(shippingInfo);
        Thread.sleep(4000);

        SoftAssertions softAssertions = new SoftAssertions();
        double shippingPrice = 0;
        if (shippingInfo.getText().substring(1).equals("ree")) {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "JPG", new File("screenshot.jpg"));
            shippingPrice = 0;
            double totalAmount = (actualProductPrice * actualQuantity) + shippingPrice;
            softAssertions.assertThat(actualTotalAmount).isEqualTo(totalAmount);
        } else {
            shippingPrice = Double.parseDouble(shippingInfo.getText().substring(1));
            double totalAmount = (actualProductPrice * actualQuantity) + shippingPrice;
            softAssertions.assertThat(actualTotalAmount).isEqualTo(totalAmount);
        }

        softAssertions.assertThat(actualProductName).isEqualTo(product.getName());
        softAssertions.assertThat(actualProductPrice).isEqualTo(product.getPrice());
        softAssertions.assertThat(actualQuantity).isEqualTo(product.getQuantity());
        softAssertions.assertThat(numberInAmountInfo).isEqualTo(product.getQuantity());

        log.info("***** Product data are correct *****");
    }

    public void switchToLastOpenedWindow() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='blockcart-modal']/div[@class='modal-dialog']/div[@class='modal-content']")));

        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
        log.info("<<<<<<<<<< Switch to last opened window");
    }
}
