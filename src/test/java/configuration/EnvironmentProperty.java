package configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EnvironmentProperty {

    private final String app_environment;
    private static Logger logger = LoggerFactory.getLogger("EnvironmentProperty");
    private final configuration.BrowserEnvironment browserEnvironment;
    private String firstName = "Karolina";
    private String secondName = "Kowalska";
    private String gender = "female";
    private String alias = "Kowal";
    private String company = "Sii";
    private String street = "Happy";
    private String addressNumber = "15a";
    private String city = "Rome";
    private String state = "Hawaii";
    private String postalCode = "43-472";
    private String country = "Poland";
    private String shippingMethod = "shop";
    private String payment = "bank";



    private EnvironmentProperty() {
        this.app_environment = initAppEnvironment();
        this.browserEnvironment = new configuration.BrowserEnvironment();
        this.initEnv();
        this.initProperties();

    }

    public void initProperties(){
        this.firstName = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.firstName;
        this.secondName = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.secondName;
        this.gender = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.gender;
        this.alias = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.alias;
        this.company = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.company;
        this.street = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.street;
        this.addressNumber = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.addressNumber;
        this.city = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.city;
        this.state = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.state;
        this.postalCode = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.postalCode;
        this.country = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.country;
        this.shippingMethod = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.shippingMethod;
        this.payment = PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : this.payment;


    }
    private static String initAppEnvironment() {
        return PropertiesStore.ENVIRONMENT.isSpecified() ? PropertiesStore.ENVIRONMENT.getValue() : "";
    }

    public static EnvironmentProperty getInstance() {
        return EnvironmentProperty.EnvironmentPropertySingleton.INSTANCE;
    }

    private void initEnv() {
        if (!this.app_environment.isEmpty()) {
            logger.debug(">>>>>>> Environment name : " + this.app_environment);
            loadAllEnvPropertiesToSystem(this.app_environment);
        } else {
            logger.error("Please provide \"environment\" property");
            assertThat(true, equalTo(false));
        }
    }

    private void loadAllEnvPropertiesToSystem(String app_environment) {
        setSystemPropertiesFromPathUrl(app_environment);
    }

    private static void setSystemPropertiesFromPathUrl(String directoryName) {
        URL url = EnvironmentProperty.class.getClassLoader().getResource(directoryName);
        if (url != null) {
            Properties environmentProperties = new Properties();
            try {
                Stream<Path> files = Files.walk(Paths.get(url.toURI()));

                try {
                    ((List) files.filter((x$0) -> {
                        return Files.isRegularFile(x$0, new LinkOption[0]);
                    }).collect(Collectors.toList())).forEach((path) -> {
                        try {
                            environmentProperties.load(new FileInputStream(path.toString()));
                        } catch (IOException var3) {
                            logger.error("error 1");
                        }
                    });
                } catch (Exception e) {
                    logger.error("error 2");
                } finally {
                    if (files != null) {
                        try {
                            files.close();
                        } catch (Throwable var13) {
                            logger.error("error 3");
                        }
                    } else {
                        files.close();
                    }
                }
            } catch (Exception r) {
                logger.error("error 4");
            }
            logger.debug("#### Loading property from uri {}", url.toString());
            environmentProperties.forEach((propertyName, propertyValue) -> {
                if (System.getProperty(propertyName.toString()) == null) {
                    System.setProperty(propertyName.toString(), propertyValue.toString());
                    logger.debug("****Loading environment property {} = {} ", propertyName.toString(), propertyValue.toString());
                }
            });
            logger.debug("#### Properties loaded from {} : {} ", directoryName, environmentProperties.size());
        } else {
            logger.warn("No such property directory '{}' present in the resources ,make sure you are providing correct directory name.", directoryName);
        }
    }

    private static class EnvironmentPropertySingleton {
        private static final EnvironmentProperty INSTANCE = new EnvironmentProperty();
        //private EnvironmentPropertySingleton() {
        //}
    }
    public int getIntValue(String key) {
        return Integer.parseInt(key);
    }
}