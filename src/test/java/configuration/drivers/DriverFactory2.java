package configuration.drivers;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.managers.EdgeDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import io.github.bonigarcia.wdm.managers.InternetExplorerDriverManager;

public  class DriverFactory2 {

    private ChromeDriverManager getChromeDriverManager(Browser browser) {
        if (browser == Browser.CHROME) {
        }
        return new ChromeDriverManager();
    }

    private FirefoxDriverManager getFirefoxDriverManager(Browser browser) {
        if (browser == Browser.FIREFOX) {
        }
        return new FirefoxDriverManager();
    }

    private EdgeDriverManager getEdgeDriverManager(Browser browser) {
        if (browser == Browser.EDGE) {
        }

        return new EdgeDriverManager();
    }

    private InternetExplorerDriverManager getInternetExplorerDriverManager(Browser browser) {
        if (browser == Browser.IE) {
        }
        return new InternetExplorerDriverManager();
    }

}
