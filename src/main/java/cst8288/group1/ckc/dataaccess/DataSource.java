package cst8288.group1.ckc.dataaccess;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Thread-safe singleton that owns the application's database connection.
 *
 * <p>The enum guarantees one instance. Settings come from {@code database.properties}; a system
 * property with the same key overrides the file, which is how tests point the application at their
 * own database container. One JDBC connection is created lazily and shared; it is recreated only
 * when it is closed or no longer valid. Callers must not close the returned connection because this
 * class manages its lifecycle.</p>
 *
 * @author Dmytro Dehtiarov
 */
public enum DataSource {
    INSTANCE;

    private static final int VALIDATION_TIMEOUT_SECONDS = 2;

    private final String url;
    private final String user;
    private final String password;

    private Connection connection;

    DataSource() {
        Properties prop = new Properties();
        try (InputStream input = DataSource.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IllegalStateException("Cannot find database.properties");
            }
            prop.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read database.properties", e);
        }

        url = setting(prop, "db.url");
        user = setting(prop, "db.user");
        password = setting(prop, "db.password");

        try {
            // Register the driver because DriverManager may not find it inside a WAR on Payara
            Class.forName(setting(prop, "db.driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot load database driver", e);
        }
    }

    /**
     * Returns a setting, preferring a system property over the file.
     *
     * @param prop values read from database.properties
     * @param key name of the setting
     * @return the configured value
     * @throws IllegalStateException if the setting is defined in neither place
     */
    private static String setting(Properties prop, String key) {
        String value = System.getProperty(key, prop.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Missing database setting: " + key);
        }
        return value;
    }

    /**
     * Returns the shared connection, opening a new one when needed.
     *
     * @return an open database connection; the caller must not close it
     * @throws SQLException if the connection cannot be opened
     */
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()
                || !connection.isValid(VALIDATION_TIMEOUT_SECONDS)) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}