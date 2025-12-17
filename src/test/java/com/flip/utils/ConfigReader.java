package com.flip.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "C:\\newaut\\Testing\\src\\main\\resources\\config.properties";

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() throws IOException {
        properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }

    public static String getUrl() {
        return properties.getProperty("url");
    }

    // Generic method to get any property
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
