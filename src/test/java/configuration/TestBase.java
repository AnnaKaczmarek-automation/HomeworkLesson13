package configuration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBase {
    protected static WebDriver driver;
    private static Logger log = LoggerFactory.getLogger("BaseData.BaseTest.class");
    private  static BrowserEnvironment browserEnvironment;
    protected  static EnvironmentProperty environmentProperty;

    @BeforeAll
    static void beforeAll() {
        environmentProperty = EnvironmentProperty.getInstance();
        browserEnvironment = new BrowserEnvironment();
        driver= browserEnvironment.getDriver();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
        log.info("***** Driver closed *****");
    }
}

