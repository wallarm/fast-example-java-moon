package net.dvwa.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    private static Map<String, String> items = null;

    private static void readConfig() throws IOException {
        properties.load(new FileInputStream("config.properties"));
    }

    private static Properties getConfig() {
        if (properties.size() == 0) {
            try {
                readConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static String get(String key) {
        if (items == null) {
            items = (Map) getConfig();
        }
        return items.get(key);
    }
}
