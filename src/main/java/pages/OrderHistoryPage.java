package pages;

import models.Order;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryPage extends BasePage{
    public OrderHistoryPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(css = ".table.table-striped.table-bordered.table-labeled.hidden-sm-down tbody tr")
    private List<WebElement> listOfOrders;

    @FindBy(css = "td.text-sm-center.order-actions a:nth-child(1)")
    private WebElement detailsBtn;


    public String findOrder(){
        String displayedOrderRef = null;
        for (WebElement row : listOfOrders) {
            displayedOrderRef = row.findElement(By.cssSelector("tbody th:nth-child(1)")).getText();

        }
        return displayedOrderRef;
    }
    //check if date, total price, payment and status is correct
    public Order getDisplayedOrderDetails() {
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        Order order = null;
        for (WebElement row : listOfOrders) {
            String displayedDate = row.findElement(By.cssSelector("tr td:nth-child(2)")).getText();
            String totalPrice = row.findElement(By.cssSelector("tr td:nth-child(3)")).getText().substring(1);
            double totalPriceDouble = Double.parseDouble(totalPrice);
            double formattedPrice = Double.parseDouble(dFormat.format(totalPrice));
            String paymentInfo = row.findElement(By.cssSelector("tr td:nth-child(4)")).getText();
            String statusInfo = row.findElement(By.cssSelector("tr td:nth-child(5)")).getText();

            order = new Order(displayedDate, formattedPrice, paymentInfo, statusInfo);
        }
        return order;
    }

    public void chooseDetailsOption(){
        waitUntilVisibilityOfElement(detailsBtn);
        detailsBtn.click();
    }
}
