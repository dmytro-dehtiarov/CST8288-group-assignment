package cst8288.group1.ckc.dataaccess.maintenance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.DataSource;
import cst8288.group1.ckc.dataaccess.DatabaseTest;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceTask;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Database tests for MaintenanceTaskDaoImpl against the MySQL test container.
 *
 * @author Othman Slassi
 */
class MaintenanceTaskDaoImplTest extends DatabaseTest {

    private static final int TECH_ID = 1;
    private static final int OTHER_TECH_ID = 2;
    private static final int EQUIPMENT_ID = 1;
    private static final int OTHER_EQUIPMENT_ID = 2;
    private static final LocalDateTime MONDAY = LocalDateTime.of(2026, 11, 23, 9, 0);

    private MaintenanceTaskDao dao;

    @BeforeEach
    void insertTechsAndEquipment() throws SQLException {
        Connection conn = DataSource.INSTANCE.getConnection();
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO users (user_id, name, email, password_hash, role) VALUES "
                    + "(1, 'Ken Okafor', 'ken@ckc.test', 'x', 'KITCHEN_TECH'), "
                    + "(2, 'Lea Tran', 'lea@ckc.test', 'x', 'KITCHEN_TECH')");
            st.executeUpdate("INSERT INTO equipment (equipment_id, asset_tag, make, model, category, "
                    + "credit_rate_per_hour, maintenance_threshold_hours) VALUES "
                    + "(1, 'OVN-001', 'Rational', 'iCombi Pro', 'OVEN', 12.00, 500), "
                    + "(2, 'MIX-001', 'Hobart', 'HL200', 'MIXER', 6.00, 300)");
        }
        dao = new MaintenanceTaskDaoImpl();
    }

    @Test
    void addReturnsTheNewIdAndFindByIdReadsEveryColumn() throws DaoException {
        MaintenanceTask task = task(EQUIPMENT_ID, "Descale steam generator", MONDAY);
        task.setTechId(TECH_ID);
        task.setCompletedAt(MONDAY.plusHours(2));
        task.setMinutesLogged(120);

        int id = dao.add(task);
        MaintenanceTask found = dao.findById(id);

        assertTrue(id > 0);
        assertEquals(id, found.getTaskId());
        assertEquals(EQUIPMENT_ID, found.getEquipmentId());
        assertEquals(TECH_ID, found.getTechId());
        assertEquals("Descale steam generator", found.getDescription());
        assertEquals(MONDAY, found.getScheduledFor());
        assertEquals(MONDAY.plusHours(2), found.getCompletedAt());
        assertEquals(120, found.getMinutesLogged());
    }

    @Test
    void addDoesNotChangeTheGivenObject() throws DaoException {
        MaintenanceTask task = task(EQUIPMENT_ID, "Replace belt", MONDAY);

        int id = dao.add(task);

        assertNotEquals(0, id);
        assertEquals(0, task.getTaskId());
    }

    @Test
    void nullColumnsAreReadBackAsNull() throws DaoException {
        int id = dao.add(task(EQUIPMENT_ID, "Replace belt", MONDAY));

        MaintenanceTask found = dao.findById(id);

        assertNull(found.getTechId());
        assertNull(found.getCompletedAt());
        assertNull(found.getMinutesLogged());
    }

    @Test
    void addThrowsWhenTheEquipmentDoesNotExist() {
        assertThrows(DaoException.class, () -> dao.add(task(99, "Replace belt", MONDAY)));
    }

    @Test
    void findByIdReturnsNullForAnUnknownId() throws DaoException {
        assertNull(dao.findById(99));
    }

    @Test
    void findAllReturnsAnEmptyListWhenThereAreNoTasks() throws DaoException {
        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    void findAllOrdersByScheduledForThenById() throws DaoException {
        int later = dao.add(task(EQUIPMENT_ID, "Later", MONDAY.plusDays(1)));
        int earlierFirst = dao.add(task(EQUIPMENT_ID, "Earlier one", MONDAY));
        int earlierSecond = dao.add(completed(task(OTHER_EQUIPMENT_ID, "Earlier two", MONDAY)));

        List<MaintenanceTask> tasks = dao.findAll();

        assertEquals(List.of(earlierFirst, earlierSecond, later), ids(tasks));
    }

    @Test
    void findOpenReturnsOnlyTasksNotCompletedEarliestFirst() throws DaoException {
        int later = dao.add(task(EQUIPMENT_ID, "Later", MONDAY.plusDays(2)));
        dao.add(completed(task(EQUIPMENT_ID, "Done", MONDAY)));
        MaintenanceTask taken = task(OTHER_EQUIPMENT_ID, "Taken", MONDAY.plusDays(1));
        taken.setTechId(TECH_ID);
        int takenId = dao.add(taken);

        List<MaintenanceTask> open = dao.findOpen();

        assertEquals(List.of(takenId, later), ids(open));
    }

    @Test
    void findOpenReturnsAnEmptyListWhenEveryTaskIsCompleted() throws DaoException {
        dao.add(completed(task(EQUIPMENT_ID, "Done", MONDAY)));

        assertTrue(dao.findOpen().isEmpty());
    }

    @Test
    void findByTechReturnsOpenAndCompletedTasksOfThatTechEarliestFirst() throws DaoException {
        MaintenanceTask open = task(EQUIPMENT_ID, "Open", MONDAY.plusDays(1));
        open.setTechId(TECH_ID);
        int openId = dao.add(open);
        int doneId = dao.add(completed(task(OTHER_EQUIPMENT_ID, "Done", MONDAY)));
        MaintenanceTask other = task(EQUIPMENT_ID, "Other tech", MONDAY);
        other.setTechId(OTHER_TECH_ID);
        dao.add(other);
        dao.add(task(EQUIPMENT_ID, "Not taken", MONDAY));

        List<MaintenanceTask> tasks = dao.findByTech(TECH_ID);

        assertEquals(List.of(doneId, openId), ids(tasks));
    }

    @Test
    void findByTechReturnsAnEmptyListForATechWithoutTasks() throws DaoException {
        dao.add(task(EQUIPMENT_ID, "Not taken", MONDAY));

        assertTrue(dao.findByTech(TECH_ID).isEmpty());
    }

    @Test
    void updateSavesEveryColumn() throws DaoException {
        int id = dao.add(task(EQUIPMENT_ID, "Replace belt", MONDAY));
        MaintenanceTask changed = dao.findById(id);
        changed.setEquipmentId(OTHER_EQUIPMENT_ID);
        changed.setTechId(OTHER_TECH_ID);
        changed.setDescription("Replace belt and rollers");
        changed.setScheduledFor(MONDAY.plusDays(1));
        changed.setCompletedAt(MONDAY.plusDays(1).plusHours(1));
        changed.setMinutesLogged(45);

        dao.update(changed);
        MaintenanceTask found = dao.findById(id);

        assertEquals(OTHER_EQUIPMENT_ID, found.getEquipmentId());
        assertEquals(OTHER_TECH_ID, found.getTechId());
        assertEquals("Replace belt and rollers", found.getDescription());
        assertEquals(MONDAY.plusDays(1), found.getScheduledFor());
        assertEquals(MONDAY.plusDays(1).plusHours(1), found.getCompletedAt());
        assertEquals(45, found.getMinutesLogged());
    }

    @Test
    void updateCanSetNullableColumnsBackToNull() throws DaoException {
        int id = dao.add(completed(task(EQUIPMENT_ID, "Done", MONDAY)));
        MaintenanceTask changed = dao.findById(id);
        changed.setTechId(null);
        changed.setCompletedAt(null);
        changed.setMinutesLogged(null);

        dao.update(changed);
        MaintenanceTask found = dao.findById(id);

        assertNull(found.getTechId());
        assertNull(found.getCompletedAt());
        assertNull(found.getMinutesLogged());
    }

    @Test
    void updateWithUnchangedValuesDoesNotThrow() throws DaoException {
        int id = dao.add(task(EQUIPMENT_ID, "Replace belt", MONDAY));

        dao.update(dao.findById(id));

        assertEquals("Replace belt", dao.findById(id).getDescription());
    }

    @Test
    void updateThrowsWhenNoTaskHasTheId() {
        MaintenanceTask missing = task(EQUIPMENT_ID, "Replace belt", MONDAY);
        missing.setTaskId(99);

        assertThrows(DaoException.class, () -> dao.update(missing));
    }

    /**
     * Builds an open, untaken task.
     */
    private static MaintenanceTask task(int equipmentId, String description,
            LocalDateTime scheduledFor) {
        MaintenanceTask task = new MaintenanceTask();
        task.setEquipmentId(equipmentId);
        task.setDescription(description);
        task.setScheduledFor(scheduledFor);
        return task;
    }

    /**
     * Marks a task as taken by TECH_ID and completed one hour after it was scheduled.
     */
    private static MaintenanceTask completed(MaintenanceTask task) {
        task.setTechId(TECH_ID);
        task.setCompletedAt(task.getScheduledFor().plusHours(1));
        task.setMinutesLogged(60);
        return task;
    }

    /**
     * Returns the ids of the tasks, in the same order.
     */
    private static List<Integer> ids(List<MaintenanceTask> tasks) {
        return tasks.stream().map(MaintenanceTask::getTaskId).toList();
    }
}
