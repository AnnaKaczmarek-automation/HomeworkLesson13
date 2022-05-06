package browser;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.LinkedHashMap;
import java.util.Map;

public class BrowsersTemplate {

    private Map<String, Object> browserProperties = new LinkedHashMap<>();

    public BrowsersTemplate() {
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



