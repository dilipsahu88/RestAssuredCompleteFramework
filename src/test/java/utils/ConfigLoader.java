package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    // Static block loads the file once when the class is first accessed
    static {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    // Generic method to fetch any property with CI/CD fallback
    public static String getProperty(String key) {
        // 1. Check JVM system properties (-Dapi.key=...)
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }

        // 2. Check OS environment variables (converts "api.key" -> "API_KEY")
        String envKey = key.toUpperCase().replace('.', '_');
        value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            return value;
        }

        // 3. Fall back to local config.properties
        value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Property '" + key + "' is missing in " + CONFIG_FILE + " and environment variables.");
        }
        return value;
    }

    // Dedicated helper method specifically for your API key
    public static String getApiKey() {
        return getProperty("api.key");
    }

    public static String getBaseUri() {
        return getProperty("base.uri");
    }
}