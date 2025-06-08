package com.jllt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class configReader {
    private static final Properties properties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(configReader.class);

    static {
        logger.info("Initializing configReader");
        try (InputStream input = configReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
            } else {
                properties.load(input);
                logger.info("Successfully loaded config.properties");
            }
        } catch (IOException ex) {
            logger.error("Error loading config.properties", ex);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getEnvironmentProperty(String key) {
        String env = System.getenv("TEST_ENV");
        if (env == null) {
            env = System.getProperty("env", properties.getProperty("env"));
        }

        String project = System.getenv("TEST_PROJECT");
        if (project == null) {
            project = System.getProperty("project", properties.getProperty("project"));
        }

        String fullKey = project.toLowerCase() + "." + env.toUpperCase() + "." + key;
        String value = properties.getProperty(fullKey);

        if (value == null) {
            logger.error("Property {} not found for {} in {} environment", key, project, env);
            throw new RuntimeException("Missing config property: " + fullKey);
        }

        logger.info("Retrieved property: {} = {}", fullKey, value);
        return value;
    }
}
