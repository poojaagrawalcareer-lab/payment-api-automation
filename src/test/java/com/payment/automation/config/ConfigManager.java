package com.payment.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {

        String environment =
                System.getProperty("env", "local");

        String configFile =
                "local".equalsIgnoreCase(environment)
                        ? "config-qa.properties"
                        : "config-" + environment + ".properties";

        try (InputStream input =
                     ConfigManager.class
                             .getClassLoader()
                             .getResourceAsStream(configFile)) {

            if (input == null) {
                throw new RuntimeException(
                        configFile + " not found");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load " + configFile, e);
        }
    }

    public static String get(String key) {

        // 1. System property
        String systemProperty = System.getProperty(key);

        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        // 2. Environment variable
        String environmentVariable = null;

        if ("auth.token".equals(key)) {
            environmentVariable = System.getenv("AUTH_TOKEN");
        }

        if (environmentVariable != null && !environmentVariable.isBlank()) {
            return environmentVariable;
        }

        // 3. Properties file
        return properties.getProperty(key);
    }
}