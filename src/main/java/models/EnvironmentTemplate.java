package models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentTemplate {
    private boolean active;

    private Map<String, Object> envProperties = new LinkedHashMap<>();

    public EnvironmentTemplate() {
    }

    public boolean isActive() {
        return active;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return envProperties;
    }

    @JsonAnySetter
    void setProperties(String key, Object value) {
        envProperties.put(key, value);
    }
}

