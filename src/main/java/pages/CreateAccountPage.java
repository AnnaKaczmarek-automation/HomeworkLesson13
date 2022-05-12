package pages;
import com.github.rkumsher.date.RandomDateUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.time.LocalDate;

public class CreateAccountPage extends BasePage {
    public CreateAccountPage(WebDriver driver) {
        super(driver);
    }

    private com.github.rkumsher.date.RandomDateUtils RandomDateUtils;

    Logger log = LoggerFactory.getLogger("CreateAccountPage.class");
    @FindBy(css = ".custom-radio input[value='1']")
    private WebElement mrRadioBtn;

    @FindBy(css = ".custom-radio input[value='2']")
    private WebElement mrsRadioBtn;

    @FindBy(xpath = "//input[@class='form-control'][@name='firstname']")
    private WebElement nameInput;

    @FindBy(xpath = "//input[@class='form-control'][@name='lastname']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@class='form-control'][@name='email']")
    private WebElement mailInput;

    @FindBy(css = ".form-control.js-child-focus.js-visible-password")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@class='form-control'][@name='birthday']")
    private WebElement birthInput;

    @FindBy(xpath = "//input[@name='psgdpr']")
    private WebElement acceptingPolicyBtn;

    @FindBy(css = ".btn.btn-primary.form-control-submit.float-xs-right")
    private WebElement saveBtn;

    @FindBy(css = ".register-form")
    private WebElement registerSection;

    @FindBy(xpath = "//input[@type='checkbox'][@name='customer_privacy']")
    private WebElement customerPrivacyBtn;

    public void fillInTheForm(String gender, String name, String lastName, String mail) {
        waitUntilVisibilityOfElement(registerSection);
        if (gender.equals("male")) {
            highlightElements(mrRadioBtn);
            mrRadioBtn.click();
        }
        if (gender.equals("female")) {
            highlightElements(mrRadioBtn);
            mrsRadioBtn.click();
        }
        log.info("Gender radiobutton was chosen");
        nameInput.sendKeys(name);
        log.info("Name is typed in");
        lastNameInput.sendKeys(lastName);
        log.info("LastName is typed in");
        mailInput.sendKeys(mail);
        log.info("Mail is typed in");
        passwordInput.sendKeys(generatePassword(8));
        log.info("Password is typed in");
        birthInput.sendKeys(generateRandomDate());
        log.info("Birth date is typed in");
//        clickOnElement(acceptingPolicyBtn);
        log.info("Policy is accepted");
        acceptingPolicyBtn.click();
        customerPrivacyBtn.click();
        saveBtn.click();
        log.info("Save button was chosen");
    }

    private String generateRandomDate() {
        LocalDate birthDate = com.github.rkumsher.date.RandomDateUtils.randomPastLocalDate();
        return String.valueOf(birthDate);
    }

    private String generatePassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
