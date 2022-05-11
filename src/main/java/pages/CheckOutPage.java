package pages;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class CheckOutPage extends BasePage {
    public CheckOutPage(WebDriver driver) {
        super(driver);
    }

    Logger log = LoggerFactory.getLogger("CheckOutPage.class");
    BasketPage basketPage = new BasketPage(driver);

    @FindBy(css = "#checkout-addresses-step")
    private WebElement addressForm;

    @FindBy(css = ".form-fields .form-group.row div [name='alias']")
    private WebElement aliasInput;

    @FindBy(css = ".form-fields .form-group.row div [name='firstname']")
    private WebElement firstNameInput;

    @FindBy(css = ".form-fields .form-group.row div [name='lastname']")
    private WebElement lastNameInput;

    @FindBy(css = ".form-fields .form-group.row div [name='company']")
    private WebElement companyInput;

    @FindBy(css = ".form-fields .form-group.row div [name='address1']")
    private WebElement addressInput;

    @FindBy(css = ".form-fields .form-group.row div [name='address2']")
    private WebElement addressNumberInput;

    @FindBy(css = ".form-fields .form-group.row div [name='city']")
    private WebElement cityInput;

    @FindBy(css = ".form-fields .form-group.row div [name='id_state']")
    private WebElement stateInput;

    @FindBy(css = ".form-fields .form-group.row div [name='postcode']")
    private WebElement postalCodeInput;

    @FindBy(css = ".form-fields .form-group.row div [name='id_country']")
    private WebElement countryInput;

    @FindBy(css = ".form-fields .form-group.row div [name='phone']")
    private WebElement phoneInput;

    @FindBy(css = "#use_same_address")
    private WebElement addressCheckBox;

    @FindBy(xpath = "//button[@class='continue btn btn-primary float-xs-right'][@name='confirm-addresses']")
    private WebElement continueBtn;

    @FindBy(css = "#checkout-delivery-step")
    private WebElement shippingForm;

    @FindBy(css = "#cart-subtotal-shipping span:nth-child(2)")
    private WebElement shippingValue;

    @FindBy(xpath = "//button[@class='continue btn btn-primary float-xs-right'][@name='confirmDeliveryOption']")
    private WebElement shippingContinueBtn;

    @FindBy(xpath = "//div[@class='row delivery-option']/label[@for='delivery_option_2']")
    private WebElement shippingDeliveryOption;

    @FindBy(xpath = "//div[@class='row delivery-option']/label[@for='delivery_option_1']")
    private WebElement shippingShopOption;

    @FindBy(css = "#delivery_option_1")
    private WebElement shopRadioBtn;

    @FindBy(css = "#delivery_option_2")
    private WebElement deliveryRadioBtn;


    @FindBy(css = "#payment-option-1-container")
    private WebElement paymentCheckBtn;

    @FindBy(css = "#payment-option-2-additional-information section ")
    private WebElement bankInfoSection;

    @FindBy(css = "#payment-option-2")
    private WebElement paymentBankBtn;

    @FindBy(css = "#payment-option-1")
    private WebElement checkRadioBtn;

    @FindBy(css = "#payment-option-2")
    private WebElement bankRadioBtn;

    @FindBy(css = "#cta-terms-and-conditions-0")
    private WebElement termsInfo;

    @FindBy(css = "#conditions-to-approve ul li")
    private WebElement termsBtn;

    @FindBy(css = ".modal.fade.in .modal-content")
    private WebElement termsOfUseFrame;

    @FindBy(css = ".js-modal-content")
    private WebElement termsOfUseContent;

    @FindBy(css = ".btn.btn-primary.center-block")
    private WebElement placeOrderBtn;

    @FindBy(css = "#modal .modal-content  .close")
    private WebElement crossBtn;

    @FindBy(css = "row delivery-option")
    private List<WebElement> shippingOptionList;

    @FindBy(css = ".payment-option.clearfix")
    private List<WebElement> paymentOptionList;

    @FindBy(css = ".cart-summary-line.cart-total span:nth-child(2)")
    private WebElement totalPrice;

    public void fillAddressForm() {
        waitUntilVisibilityOfElement(addressForm);
        log.info("Address form is displayed");
        setAliasValue();
        setFirstNameValue();
        setLastNameValue();
        setCompanyInput();
        setAddressStreetInput();
        setAddressNumberInput();
        setCityInput();
        setStateInput();
        setPostalCodeValue();
        setCountryValue();
        setPhoneValue();
        clickOnElement(continueBtn);
    }

    public void setPhoneValue() {

        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
        String number = fakeValuesService.numerify("########");


        setValueIntoInputBox(phoneInput, number);
    }

    public void setCountryValue() {
        waitUntilVisibilityOfElement(stateInput);
        Select country = new Select(driver.findElement(By.cssSelector(".form-fields .form-group.row div [name='id_country']")));
        country.selectByVisibleText(System.getProperty("country"));
    }

    public void setPostalCodeValue() {
        waitUntilVisibilityOfElement(postalCodeInput);
        setValueIntoInputBox(postalCodeInput, System.getProperty("postalCode"));
    }

    public void setStateInput() {
        waitUntilVisibilityOfElement(stateInput);
        Select state = new Select(driver.findElement(By.cssSelector(".form-fields .form-group.row div [name='id_state']")));
        state.selectByVisibleText(System.getProperty("state"));
    }

    public void setCityInput() {
        waitUntilVisibilityOfElement(cityInput);
        setValueIntoInputBox(cityInput, System.getProperty("city"));
    }

    public void setAddressNumberInput() {
        waitUntilVisibilityOfElement(addressNumberInput);
        setValueIntoInputBox(addressNumberInput, System.getProperty("addressNumber"));
    }

    public void setAddressStreetInput() {
        waitUntilVisibilityOfElement(addressInput);
        setValueIntoInputBox(addressInput, System.getProperty("street"));
    }

    public void setAliasValue() {
        waitUntilVisibilityOfElement(aliasInput);
        setValueIntoInputBox(aliasInput, System.getProperty("alias"));
        log.info("");
    }

    public void setFirstNameValue() {
        waitUntilVisibilityOfElement(firstNameInput);
        setValueIntoInputBox(firstNameInput, System.getProperty("firstName"));
    }

    public void setLastNameValue() {
        waitUntilVisibilityOfElement(lastNameInput);
        setValueIntoInputBox(lastNameInput, System.getProperty("secondName"));
    }

    public void setCompanyInput() {
        waitUntilVisibilityOfElement(companyInput);
        setValueIntoInputBox(companyInput, System.getProperty("company"));
    }

    public void chooseShippingMethod() {
        waitUntilVisibilityOfElement(shippingForm);
        String chosenOption = System.getProperty("shippingMethod");
        System.out.println(chosenOption);

        if (chosenOption.equals("shop")) {
            waitUntilVisibilityOfElement(shippingShopOption);
            shippingShopOption.click();
        }
        if (chosenOption.equals("delivery")) {
            waitUntilVisibilityOfElement(shippingDeliveryOption);
            shippingDeliveryOption.click();
        }
        clickOnElement(shippingContinueBtn);
    }

    public double getShippingValue() {
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        String displayedValue = shippingValue.getText().substring(1);
        double shippingValue = 0;
        if (StringUtils.isNumeric(displayedValue)) {
            double shipping = Double.parseDouble(displayedValue);
            shippingValue = Double.parseDouble(dFormat.format(shipping));
        } else {
            shippingValue = 0;
        }
        return shippingValue;
    }

    public double getTotalCost(){
        double totalCost = Double.parseDouble(totalPrice.getText().substring(1));
        return totalCost;
    }

    public String getChosenShippingInfo() {
        String expectedOption = System.getProperty("shippingMethod");
        String chosenOption = null;
        if (expectedOption.equals("shop")) {
            chosenOption = driver.findElement(By.cssSelector(".h6.carrier-name")).getText();
        }
        if (expectedOption.equals("delivery")) {
            chosenOption = driver.findElement(By.cssSelector(".h6.carrier-name")).getText();
        }
        return chosenOption;
    }

    public void choosePaymentOption() {
        String paymentOption = System.getProperty("payment");
        if (paymentOption.equals("check")) {
            paymentCheckBtn.click();
            log.info("Payment option was chosen");
            waitUntilVisibilityOfElement(bankInfoSection);
        }
        if (paymentOption.equals("bank")) {
            paymentBankBtn.click();
            log.info("Payment option was chosen");
            waitUntilVisibilityOfElement(bankInfoSection);
        }
    }

    public String getChosenPaymentOption() {
        String paymentOption = System.getProperty("payment");
        String chosenOption = null;
        if (paymentOption.equals("check")) {
            chosenOption = "Check";
            log.info(chosenOption + " was chosen payment option");
        }
        if (paymentOption.equals("bank")) {
            chosenOption = "Bank";
            log.info(chosenOption + " was chosen payment option");
        }
        return chosenOption;
    }

    public void openTermsOfUseInfo() {
        clickOnElement(termsInfo);
    }

    public void verifyTermsOfUsePopupInfo() {
        waitUntilVisibilityOfElement(termsOfUseContent);
        String content = termsOfUseContent.getText();
        if (content.isEmpty()) {
            log.info("Terms of use popup window is empty");
        }
        if (content.matches("[a-zA-Z]+")) {
            log.info("Terms of use popup correctly contains text");
        }
    }

    public void confirmTermsOfService() {
        clickOnElement(termsBtn);
        log.info("Terms of use were checked");
    }

    public void choosePlaceOrderButton() {
        waitUntilElementIsClickable(placeOrderBtn);
        clickOnElement(placeOrderBtn);
        log.info("Place order button was chosen");
    }

    public void closeTermsOfUsePopup() {
        clickOnElement(crossBtn);
        log.info("Terms of use popup was closed");
    }

    public void switchToLastOpenedWindow() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal.fade.in .modal-content")));

        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
        log.info("<<<<<<<<<< Switch to last opened window");
    }


}
