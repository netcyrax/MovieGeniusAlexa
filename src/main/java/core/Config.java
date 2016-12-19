package core;

import java.io.IOException;
import java.util.Properties;

/**
 * For retrieving dynamic configuration parameters.
 */
public class Config {

    private static final String PROPERTY_FILE = "/config.properties";

    private static final String PARAM_APP_ID = "appId";
    private static final String PARAM_QUERY_URL = "queryUrl";

    private Config() {
    }

    /**
     * Returns the Alexa app id.
     */
    public static String appId() {
        return readConfig(PARAM_APP_ID);
    }

    /**
     * Returns the query url.
     */
    public static String queryUrl(String genre) {
        return String.format(readConfig(PARAM_QUERY_URL), genre);
    }

    private static String readConfig(String property) {
        Properties prop = new Properties();
        try {
            prop.load(Config.class.getResourceAsStream(PROPERTY_FILE));
            return prop.getProperty(property);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
