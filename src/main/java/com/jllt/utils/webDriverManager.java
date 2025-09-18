package com.jllt.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class webDriverManager {
    private static WebDriver driver;
    private static String userDataDir;

    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    private static void initializeDriver() {
        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Create unique user data directory for each test run
            userDataDir = createUniqueUserDataDir();

            options.addArguments(
                    "--disable-gpu",
                    "--disable-dev-shm-usage",
                    "--disable-notifications",
                    "--block-new-web-contents",
                    "--incognito",
                    "--no-sandbox",
                    "--disable-extensions",
                    "--disable-plugins",
                    "--disable-images",
                    "--disable-javascript-harmony-shipping",
                    "--disable-background-timer-throttling",
                    "--disable-renderer-backgrounding",
                    "--disable-backgrounding-occluded-windows",
                    "--disable-features=TranslateUI,BlinkGenPropertyTrees",
                    "--user-data-dir=" + userDataDir
            );

            // Add headless mode for CI/CD environments
            if (isRunningInCIEnvironment()) {
                options.addArguments("--headless");
            }

            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--disable-blink-features=AutomationControlled");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();

        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }

    private static String createUniqueUserDataDir() {
        try {
            // Create unique directory in system temp folder
            String tempDir = System.getProperty("java.io.tmpdir");
            String uniqueDir = tempDir + File.separator + "chrome_user_data_" + UUID.randomUUID().toString();
            Path userDataPath = Path.of(uniqueDir);
            Files.createDirectories(userDataPath);
            return uniqueDir;
        } catch (Exception e) {
            System.err.println("Failed to create user data directory: " + e.getMessage());
            // Fallback to default temp directory
            return System.getProperty("java.io.tmpdir") + File.separator + "chrome_" + System.currentTimeMillis();
        }
    }

    private static boolean isRunningInCIEnvironment() {
        return System.getenv("CI") != null ||
                System.getenv("JENKINS_URL") != null ||
                System.getenv("GITHUB_ACTIONS") != null ||
                System.getProperty("headless") != null;
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driver = null;
                // Clean up user data directory
                cleanupUserDataDir();
            }
        }
    }

    private static void cleanupUserDataDir() {
        if (userDataDir != null) {
            try {
                File dir = new File(userDataDir);
                if (dir.exists()) {
                    deleteDirectory(dir);
                }
            } catch (Exception e) {
                System.err.println("Failed to cleanup user data directory: " + e.getMessage());
            } finally {
                userDataDir = null;
            }
        }
    }

    private static void deleteDirectory(File directory) {
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
    }
}
