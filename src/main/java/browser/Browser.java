package browser;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedHashMap;
import java.util.Map;

public class Browser {
    private Map<String, Object> browserProperties = new LinkedHashMap<>();
    private static Logger logger = LoggerFactory.getLogger("Browser.class");

    public Browser() {
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return browserProperties;
    }

    @JsonAnySetter
    void setProperties(String key, Object value) {
        browserProperties.put(key, value);
    }
}

