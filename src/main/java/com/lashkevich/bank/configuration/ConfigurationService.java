package com.lashkevich.bank.configuration;

import com.lashkevich.bank.util.BankUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {

    private static final Logger log = LogManager.getLogger(ConfigurationService.class);

    private static final String CONFIG_PATH = "src/main/resources/database.properties";

    private static final String PREF_KEY_JDBC_USERNAME = "jdbc.username";
    private static final String PREF_KEY_JDBC_PASSWORD = "jdbc.password";
    private static final String PREF_KEY_JDBC_DATABASE = "jdbc.database";
    private static final String PREF_KEY_JDBC_DRIVER_NAME = "jdbc.driver.name";
    private static final String PREF_KEY_JDBC_URL = "jdbc.url";

    private static final Properties prop = new Properties();

    private ConfigurationService() {
    }

    static {
        InputStream input = null;

        try {
            input = new FileInputStream(CONFIG_PATH);
            prop.load(input);
        } catch (Exception e) {
            log.error("Could not load configuration " + CONFIG_PATH, e);
        } finally {
            BankUtils.closeQuietly(input);
        }
    }

    public static String getJdbcUsername() {
        try {
            return prop.getProperty(PREF_KEY_JDBC_USERNAME);
        } catch (Exception e) {
            String defaultValue = "bank";
            log.info("Can not get jdbc username. Leave default value '{}'", defaultValue, e);
            return defaultValue;
        }
    }

    public static String getJdbcPassword() {
        try {
            return prop.getProperty(PREF_KEY_JDBC_PASSWORD);
        } catch (Exception e) {
            String defaultValue = "bank";
            log.info("Can not get jdbc password. Leave default value '{}'", defaultValue, e);
            return defaultValue;
        }
    }

    public static String getJdbcDatabase() {
        try {
            return prop.getProperty(PREF_KEY_JDBC_DATABASE);
        } catch (Exception e) {
            String defaultValue = "bank";
            log.info("Can not get jdbc database. Leave default value '{}'", defaultValue, e);
            return defaultValue;
        }
    }

    public static String getJdbcDriverName() {
        try {
            return prop.getProperty(PREF_KEY_JDBC_DRIVER_NAME);
        } catch (Exception e) {
            String defaultValue = "bank";
            log.info("Can not get jdbc driver name. Leave default value '{}'", defaultValue, e);
            return defaultValue;
        }
    }

    public static String getJdbcUrl() {
        try {
            return prop.getProperty(PREF_KEY_JDBC_URL);
        } catch (Exception e) {
            String defaultValue = "jdbc:postgresql://127.0.0.1:5432/bank";
            log.info("Can not get jdbc url. Leave default value '{}'", defaultValue, e);
            return defaultValue;
        }
    }

}
