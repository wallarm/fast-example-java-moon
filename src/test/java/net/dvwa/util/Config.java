package net.dvwa.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    private static void readConfig() throws IOException {
        properties.load(new FileInputStream("config.properties"));
    }

    public static Properties getConfig() {
        if (properties.size() == 0) {
            try {
                readConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
