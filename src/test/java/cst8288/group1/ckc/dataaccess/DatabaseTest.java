package cst8288.group1.ckc.dataaccess;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.MountableFile;

/**
 * Base class for tests that need a real database. It starts one MySQL container for the whole
 * test run, loads db/ckc.sql into it and points DataSource at it.
 *
 * <p>Unit tests must not touch DataSource: it reads its settings once, on first use, so a test
 * that uses it before the container starts would lock it to the local database.</p>
 *
 * @author Dmytro Dehtiarov
 */
public abstract class DatabaseTest {

    private static MySQLContainer mysql;   // Shares one container across all test classes

    @BeforeAll
    static void startDatabase() {
        assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
                "Docker is not running, database tests are skipped");
        if (mysql == null) {
            mysql = new MySQLContainer("mysql:8.4")
                    .withDatabaseName("ckc_db")
                    .withUsername("cst8288")
                    .withPassword("cst8288")
                    // MySQL runs every script in this folder once, as root, before it accepts connections
                    .withCopyFileToContainer(MountableFile.forHostPath("db/ckc.sql"),
                            "/docker-entrypoint-initdb.d/ckc.sql");
            mysql.start();
            // DataSource reads these on first use, so I set them before any test touches it
            System.setProperty("db.url", mysql.getJdbcUrl());
            System.setProperty("db.user", mysql.getUsername());
            System.setProperty("db.password", mysql.getPassword());
        }
    }
}