package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TopMenuPage extends BasePage {
    public TopMenuPage(WebDriver driver) {
        super(driver);
    }

    Logger log = LoggerFactory.getLogger("TopMenu.class");

    @FindBy(css = ".account .hidden-sm-down")
    private WebElement userAccountBtn;

    @FindBy(css = ".links a:nth-child(3)")
    private WebElement orderHistoryBtn;


    public void openUserAccount() {
        clickOnElement(userAccountBtn);
        log.info("***** User account is opened *****");
    }

    public void openOrderHistory() {
        clickOnElement(orderHistoryBtn);
        log.info("***** Order history is opened *****");
    }

}
