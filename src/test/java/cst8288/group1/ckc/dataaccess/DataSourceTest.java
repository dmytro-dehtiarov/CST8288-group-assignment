package cst8288.group1.ckc.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

/**
 * Checks that DataSource reaches the test container, caches its connection and recovers
 * after the connection is closed.
 *
 * @author Dmytro Dehtiarov
 */
class DataSourceTest extends DatabaseTest {

    @Test
    void usesTheTestContainerNotTheLocalDatabase() throws SQLException {
        Connection conn = DataSource.INSTANCE.getConnection();
        assertEquals(System.getProperty("db.url"), conn.getMetaData().getURL());
    }

    @Test
    void returnsTheSameConnectionWhileItIsOpen() throws SQLException {
        assertSame(DataSource.INSTANCE.getConnection(), DataSource.INSTANCE.getConnection());
    }

    @Test
    void opensANewConnectionAfterTheOldOneIsClosed() throws SQLException {
        Connection first = DataSource.INSTANCE.getConnection();
        first.close();
        Connection second = DataSource.INSTANCE.getConnection();
        assertNotSame(first, second);
        assertFalse(second.isClosed());
    }
}