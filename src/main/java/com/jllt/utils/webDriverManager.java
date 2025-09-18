package com.jllt.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class webDriverManager {
    private static WebDriver driver;
    private static ChromeDriverService service;
    private static final String USER_DATA_DIR_PREFIX = "/tmp/chrome_user_data_";

    static {
        // Disable verbose HTTP wire logging
        Logger.getLogger("org.apache.hc.client5.http.wire").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http.wire").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    private static void initializeDriver() {
        try {
            // Create ChromeDriverService with specific configurations
            ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder();
            serviceBuilder.withSilent(true);  // Reduce logging
            serviceBuilder.withVerbose(false); // Disable verbose logging

            service = serviceBuilder.build();

            ChromeOptions options = new ChromeOptions();

            // Network and SSL configurations
            options.addArguments("--headless=new");                // Use new headless mode
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-plugins");
            options.addArguments("--disable-images");
            options.addArguments("--disable-notifications");

            // Network specific options
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--ignore-ssl-errors");
            options.addArguments("--ignore-certificate-errors-spki-list");
            options.addArguments("--ignore-ssl-errors-ignore-ssl-errors");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.addArguments("--disable-logging");
            options.addArguments("--log-level=3");  // Fatal errors only
            options.addArguments("--silent");
            options.addArguments("--disable-background-networking");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-client-side-phishing-detection");
            options.addArguments("--disable-crash-reporter");
            options.addArguments("--no-crash-upload");
            options.addArguments("--disable-default-apps");
            options.addArguments("--disable-sync");
            options.addArguments("--no-first-run");
            options.addArguments("--disable-prompt-on-repost");
            options.addArguments("--disable-hang-monitor");

            // Proxy and network settings
            options.addArguments("--no-proxy-server");
            options.addArguments("--disable-proxy-certificate-handler");

            // Window and performance settings
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--start-maximized");

            // Unique user data directory
            String userDataDir = USER_DATA_DIR_PREFIX + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
            options.addArguments("--user-data-dir=" + userDataDir);

            // Disable automation detection
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

            // Set preferences to reduce network activity
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.default_content_setting_values.geolocation", 2);
            prefs.put("profile.default_content_setting_values.notifications", 2);
            prefs.put("profile.default_content_settings.popups", 0);
            prefs.put("profile.managed_default_content_settings.images", 2);
            prefs.put("profile.default_content_setting_values.media_stream", 2);
            prefs.put("profile.default_content_setting_values.cookies", 2);
            options.setExperimentalOption("prefs", prefs);

            // Logging preferences to reduce debug output
            Map<String, Object> logPrefs = new HashMap<>();
            logPrefs.put("performance", "OFF");
            logPrefs.put("browser", "OFF");
            logPrefs.put("driver", "OFF");
            options.setCapability("goog:loggingPrefs", logPrefs);

            // Create driver with service
            driver = new ChromeDriver(service, options);

            // Set timeouts
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
            driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(30));

            System.out.println("✅ Chrome WebDriver initialized successfully");

        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            e.printStackTrace();

            // Try ultra-minimal configuration
            tryUltraMinimalInit();
        }
    }

    private static void tryUltraMinimalInit() {
        try {
            System.out.println("Trying ultra-minimal Chrome configuration...");

            ChromeOptions options = new ChromeOptions();

            // Absolute minimal options
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-logging");
            options.addArguments("--log-level=3");
            options.addArguments("--silent");
            options.addArguments("--window-size=1920,1080");

            // Minimal user data directory
            options.addArguments("--user-data-dir=/tmp/chrome_ultra_" + System.currentTimeMillis());

            driver = new ChromeDriver(options);

            System.out.println("Ultra-minimal Chrome WebDriver initialized");

        } catch (Exception e2) {
            System.err.println("Ultra-minimal initialization failed: " + e2.getMessage());
            throw new RuntimeException("All WebDriver initialization attempts failed", e2);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                System.out.println("Quitting WebDriver...");
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }

        if (service != null) {
            try {
                service.stop();
            } catch (Exception e) {
                System.err.println("Error stopping service: " + e.getMessage());
            } finally {
                service = null;
            }
        }

        cleanupTempFiles();
    }

    private static void cleanupTempFiles() {
        try {
            File tmpDir = new File("/tmp");
            File[] chromeFiles = tmpDir.listFiles((dir, name) ->
                    name.startsWith("chrome_user_data_") ||
                            name.startsWith("chrome_ultra_") ||
                            name.startsWith("scoped_dir"));

            if (chromeFiles != null) {
                for (File file : chromeFiles) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    }
                }
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    private static void deleteDirectory(File directory) {
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        } catch (Exception e) {
            // Ignore cleanup errors in CI
        }
    }
}
