package pages;

import models.Order;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryPage extends BasePage{
    public OrderHistoryPage(WebDriver driver) {
        super(driver);
    }

    Logger log = LoggerFactory.getLogger("OrderHistoryPage.class");

    @FindBy(css = ".table.table-striped.table-bordered.table-labeled.hidden-sm-down tbody tr")
    private List<WebElement> listOfOrders;

    @FindBy(css = "td.text-sm-center.order-actions a:nth-child(1)")
    private WebElement detailsBtn;

    @FindBy(css = "td span.label.label-pill.bright")
    private WebElement statusNotification;

    public String findOrder(){
        String displayedOrderRef = null;
        for (WebElement row : listOfOrders) {
            String reference = row.findElement(By.cssSelector("tbody th:nth-child(1)")).getText();
            displayedOrderRef = StringUtils.removeStart(reference,"");
        }
        return displayedOrderRef;
    }
    public Order getDisplayedOrderDetails() {
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        Order order = null;
        for (WebElement row : listOfOrders) {
            String displayedDate = row.findElement(By.cssSelector("tr td:nth-child(2)")).getText();
            String totalPrice = row.findElement(By.cssSelector("tr td:nth-child(3)")).getText().substring(1);
            double totalPriceDouble = Double.parseDouble(totalPrice);
            double formattedPrice = Double.parseDouble(dFormat.format(totalPriceDouble));
            String paymentInfo = row.findElement(By.cssSelector("tr td:nth-child(4)")).getText();
            String statusInfo = row.findElement(By.cssSelector("tr td:nth-child(5)")).getText();
            order = new Order(displayedDate, formattedPrice, paymentInfo, statusInfo);
        }
        return order;
    }

    public void chooseDetailsOption(){
        waitUntilVisibilityOfElement(detailsBtn);
        detailsBtn.click();
        log.info("*****'Details' option was chosen *****");
    }
}
