package cst8288.group1.ckc.dataaccess.maintenance;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.DataSource;
import cst8288.group1.ckc.dataaccess.DatabaseTest;
import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Database tests for MaintenanceAlertDaoImpl against the MySQL test container.
 *
 * @author Othman Slassi
 */
class MaintenanceAlertDaoImplTest extends DatabaseTest {

    private static final int EQUIPMENT_ID = 1;
    private static final int OTHER_EQUIPMENT_ID = 2;
    private static final int TASK_ID = 1;
    private static final LocalDateTime MONDAY = LocalDateTime.of(2026, 11, 23, 9, 0);

    private MaintenanceAlertDao dao;

    @BeforeEach
    void insertTechEquipmentAndTask() throws SQLException {
        Connection conn = DataSource.INSTANCE.getConnection();
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO users (user_id, name, email, password_hash, role) VALUES "
                    + "(1, 'Ken Okafor', 'ken@ckc.test', 'x', 'KITCHEN_TECH')");
            st.executeUpdate("INSERT INTO equipment (equipment_id, asset_tag, make, model, category, "
                    + "credit_rate_per_hour, maintenance_threshold_hours) VALUES "
                    + "(1, 'OVN-001', 'Rational', 'iCombi Pro', 'OVEN', 12.00, 500), "
                    + "(2, 'MIX-001', 'Hobart', 'HL200', 'MIXER', 6.00, 300)");
            st.executeUpdate("INSERT INTO maintenance_tasks (task_id, equipment_id, description, "
                    + "scheduled_for) VALUES (1, 1, 'Replace door seal', '2026-11-24 09:00:00')");
        }
        dao = new MaintenanceAlertDaoImpl();
    }

    @Test
    void addReturnsTheNewIdAndFindByIdReadsEveryColumn() throws DaoException {
        MaintenanceAlert alert = alert(EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY);
        alert.setDetails("Door sensor open");
        alert.setResolvedAt(MONDAY.plusDays(1));
        alert.setTaskId(TASK_ID);

        int id = dao.add(alert);
        MaintenanceAlert found = dao.findById(id);

        assertTrue(id > 0);
        assertEquals(id, found.getAlertId());
        assertEquals(EQUIPMENT_ID, found.getEquipmentId());
        assertEquals(AlertReason.DIAGNOSTIC, found.getReason());
        assertEquals("Door sensor open", found.getDetails());
        assertEquals(MONDAY, found.getCreatedAt());
        assertEquals(MONDAY.plusDays(1), found.getResolvedAt());
        assertEquals(TASK_ID, found.getTaskId());
    }

    @Test
    void addDoesNotChangeTheGivenObject() throws DaoException {
        MaintenanceAlert alert = alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY);

        dao.add(alert);

        assertEquals(0, alert.getAlertId());
    }

    @Test
    void nullColumnsAreReadBackAsNull() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));

        MaintenanceAlert found = dao.findById(id);

        assertNull(found.getDetails());
        assertNull(found.getResolvedAt());
        assertNull(found.getTaskId());
    }

    @Test
    void addThrowsForASecondOpenAlertWithTheSameEquipmentAndReason() throws DaoException {
        dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));

        assertThrows(DaoException.class,
                () -> dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY.plusHours(1))));
    }

    @Test
    void addAcceptsAnOpenAlertForAnotherReasonOrAfterTheFirstIsResolved() throws DaoException {
        int first = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        dao.add(alert(EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY));
        dao.resolve(first, MONDAY.plusDays(1));

        int second = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY.plusDays(2)));

        assertEquals(second, dao.findOpenByEquipmentAndReason(EQUIPMENT_ID, AlertReason.WEAR)
                .getAlertId());
    }

    @Test
    void addThrowsWhenTheEquipmentDoesNotExist() {
        assertThrows(DaoException.class, () -> dao.add(alert(99, AlertReason.WEAR, MONDAY)));
    }

    @Test
    void findByIdReturnsNullForAnUnknownId() throws DaoException {
        assertNull(dao.findById(99));
    }

    @Test
    void findOpenReturnsOnlyOpenAlertsNewestFirst() throws DaoException {
        int oldest = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        int newest = dao.add(alert(OTHER_EQUIPMENT_ID, AlertReason.WEAR, MONDAY.plusDays(2)));
        int sameTimeFirst = dao.add(alert(EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY.plusDays(1)));
        int sameTimeSecond = dao.add(alert(OTHER_EQUIPMENT_ID, AlertReason.DIAGNOSTIC,
                MONDAY.plusDays(1)));
        MaintenanceAlert resolved = alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY.plusDays(3));
        resolved.setResolvedAt(MONDAY.plusDays(4));
        dao.add(resolved);

        List<MaintenanceAlert> open = dao.findOpen();

        assertEquals(List.of(newest, sameTimeSecond, sameTimeFirst, oldest), ids(open));
    }

    @Test
    void findOpenReturnsAnEmptyListWhenThereAreNoOpenAlerts() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        dao.resolve(id, MONDAY.plusDays(1));

        assertTrue(dao.findOpen().isEmpty());
    }

    @Test
    void findOpenByEquipmentReturnsOnlyOpenAlertsOfThatEquipmentNewestFirst()
            throws DaoException {
        int wear = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        int diagnostic = dao.add(alert(EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY.plusHours(1)));
        dao.add(alert(OTHER_EQUIPMENT_ID, AlertReason.WEAR, MONDAY.plusHours(2)));

        List<MaintenanceAlert> open = dao.findOpenByEquipment(EQUIPMENT_ID);

        assertEquals(List.of(diagnostic, wear), ids(open));
    }

    @Test
    void findOpenByEquipmentLeavesOutResolvedAlerts() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        dao.resolve(id, MONDAY.plusDays(1));

        assertTrue(dao.findOpenByEquipment(EQUIPMENT_ID).isEmpty());
    }

    @Test
    void findOpenByEquipmentAndReasonFindsTheMatchingOpenAlert() throws DaoException {
        dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        int diagnostic = dao.add(alert(EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY));
        dao.add(alert(OTHER_EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY));

        MaintenanceAlert found = dao.findOpenByEquipmentAndReason(EQUIPMENT_ID,
                AlertReason.DIAGNOSTIC);

        assertEquals(diagnostic, found.getAlertId());
    }

    @Test
    void findOpenByEquipmentAndReasonReturnsNullWhenOnlyAResolvedAlertMatches()
            throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        dao.resolve(id, MONDAY.plusDays(1));
        dao.add(alert(EQUIPMENT_ID, AlertReason.DIAGNOSTIC, MONDAY));

        assertNull(dao.findOpenByEquipmentAndReason(EQUIPMENT_ID, AlertReason.WEAR));
    }

    @Test
    void linkTaskStoresTheTaskId() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));

        dao.linkTask(id, TASK_ID);

        assertEquals(TASK_ID, dao.findById(id).getTaskId());
    }

    @Test
    void linkTaskToTheSameTaskAgainDoesNotThrow() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        dao.linkTask(id, TASK_ID);

        assertDoesNotThrow(() -> dao.linkTask(id, TASK_ID));
    }

    @Test
    void linkTaskThrowsWhenNoAlertHasTheId() {
        assertThrows(DaoException.class, () -> dao.linkTask(99, TASK_ID));
    }

    @Test
    void linkTaskThrowsWhenTheTaskDoesNotExist() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));

        assertThrows(DaoException.class, () -> dao.linkTask(id, 99));
        assertNull(dao.findById(id).getTaskId());
    }

    @Test
    void resolveSetsTheResolutionTime() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));

        dao.resolve(id, MONDAY.plusDays(1));

        assertEquals(MONDAY.plusDays(1), dao.findById(id).getResolvedAt());
    }

    @Test
    void resolveDoesNotChangeAnAlreadyResolvedAlert() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));
        dao.resolve(id, MONDAY.plusDays(1));

        dao.resolve(id, MONDAY.plusDays(5));

        assertEquals(MONDAY.plusDays(1), dao.findById(id).getResolvedAt());
    }

    @Test
    void resolveOfAnUnknownIdChangesNothing() throws DaoException {
        int id = dao.add(alert(EQUIPMENT_ID, AlertReason.WEAR, MONDAY));

        assertDoesNotThrow(() -> dao.resolve(99, MONDAY.plusDays(1)));
        assertNull(dao.findById(id).getResolvedAt());
    }

    /**
     * Builds an open alert without details and without task.
     */
    private static MaintenanceAlert alert(int equipmentId, AlertReason reason,
            LocalDateTime createdAt) {
        MaintenanceAlert alert = new MaintenanceAlert();
        alert.setEquipmentId(equipmentId);
        alert.setReason(reason);
        alert.setCreatedAt(createdAt);
        return alert;
    }

    /**
     * Returns the ids of the alerts, in the same order.
     */
    private static List<Integer> ids(List<MaintenanceAlert> alerts) {
        return alerts.stream().map(MaintenanceAlert::getAlertId).toList();
    }
}
