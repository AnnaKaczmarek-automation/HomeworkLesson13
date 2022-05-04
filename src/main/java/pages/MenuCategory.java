package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MenuCategory extends BasePage {


    public MenuCategory(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//ul[@id='top-menu']/li/a")
    private List<WebElement> categoriesList;

    public List<WebElement> getListOfCategories() {
        return categoriesList;
    }

    public Integer getAmountOfCategories(List<WebElement> categories) {
        int categoriesAmount = categories.size();
        return categoriesAmount;
    }

    public List<WebElement> getSubCategoriesList(WebElement category) {
        List<WebElement> subCategoriesList = category.findElements(By.xpath(".//div[@class='popover sub-menu js-sub-menu collapse']/ul[@class='top-menu']/li/a"));
        return subCategoriesList;
    }

    public void getRandomCategory(){
        clickOnElement(getRandomElement(categoriesList));
    }


}
