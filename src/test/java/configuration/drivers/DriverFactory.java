package configuration.drivers;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class DriverFactory {

    protected ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    protected abstract WebDriver createDriver();
    Logger log = LoggerFactory.getLogger("DriverFactory");

    public void quitDriver() {
        if (null != drivers.get()) {
            try {
                drivers.get().quit(); // First quit WebDriver session gracefully
                drivers.remove(); // Remove WebDriver reference from the ThreadLocal variable.
            } catch (Exception e) {
//                System.err("Unable to gracefully quit WebDriver.", e); // We'll replace this with actual Loggers later - don't worry !
                log.info("System is not able to correctly quit WebDriver");
            }

        }
    }

//    public WebDriver getDriver(Browser browser) {
//
//    }
}
