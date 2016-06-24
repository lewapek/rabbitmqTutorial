package pl.edu.agh.rabbitmq.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationProperties {
    private static final String PROPERTIES_FILE_NAME = "config.properties";
    private static final String RABBIT_HOST_KEY = "rabbit.host";

    private Properties properties = new Properties();

    public ConfigurationProperties() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("No properties file " + PROPERTIES_FILE_NAME + " found in classpath");
        }
    }

    public String getRabbitHost() {
        return properties.getProperty(RABBIT_HOST_KEY);
    }
}
