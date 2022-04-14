package configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserEnvironment {

    private String browserName= "chrome";
    private  boolean headlessBrowser;
    private  int webElementTimeOut;

    private static Logger logger = LoggerFactory.getLogger("BrowserEnvironment.class");
    private WebDriver driver;

    public BrowserEnvironment(){
        this.headlessBrowser=false;
        this.webElementTimeOut = 15;
        this.browserName= PropertiesStore.BROWSER.isSpecified() ? PropertiesStore.BROWSER.getValue() : this.browserName;
        this.initBrowserSettings();

    }

    private void initBrowserSettings() {
        this.webElementTimeOut = PropertiesStore.BROWSER_WEBELEMENT_TIMEOUT.isSpecified() ? PropertiesStore.BROWSER_WEBELEMENT_TIMEOUT.getIntValue() : this.webElementTimeOut;
        this.headlessBrowser = PropertiesStore.HEADLESS.getBoolean();
    }
    public WebDriver getDriver() {
        WebDriver driver;
        switch (this.browserName) {
            case "chrome":
                ChromeOptions optionsChrome = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                optionsChrome.addArguments("start-maximized");
                driver = new ChromeDriver(optionsChrome);
                driver.get(System.getProperty("appUrl"));
                break;
            case "firefox":
                FirefoxOptions optionsFirefox = new FirefoxOptions();
                WebDriverManager.firefoxdriver().setup();
                optionsFirefox.addArguments("start-maximized");
                driver = new FirefoxDriver(optionsFirefox);
                driver.get(System.getProperty("appUrl"));
                break;
            default:
                InternetExplorerOptions optionsdefault = new InternetExplorerOptions();
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver(optionsdefault);
                driver.get(System.getProperty("appUrl"));
        }
        this.driver=driver;
        return this.driver;
    }
}

