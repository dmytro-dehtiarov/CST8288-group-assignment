package cst8288.group1.ckc.business.maintenance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cst8288.group1.ckc.business.ValidationException;
import cst8288.group1.ckc.dataaccess.maintenance.MaintenanceAlertDao;
import cst8288.group1.ckc.dataaccess.maintenance.MaintenanceTaskDao;
import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceTask;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the rules of MaintenanceBusinessLogic that depend only on the maintenance module.
 * Fake DAOs keep the rows in memory, so no database is used.
 *
 * @author Othman Slassi
 */
class MaintenanceBusinessLogicTest {

    private static final int EQUIPMENT_ID = 4;
    private static final int OTHER_EQUIPMENT_ID = 5;
    private static final int TECH_ID = 10;
    private static final int OTHER_TECH_ID = 11;
    private static final LocalDateTime MONDAY = LocalDateTime.of(2026, 11, 23, 9, 0);

    private FakeTaskDao taskDao;
    private FakeAlertDao alertDao;
    private MaintenanceBusinessLogic logic;

    @BeforeEach
    void setUp() {
        taskDao = new FakeTaskDao();
        alertDao = new FakeAlertDao();
        logic = new MaintenanceBusinessLogic(taskDao, alertDao);
    }

    @Test
    void constructorRejectsNullDaos() {
        assertThrows(NullPointerException.class, () -> new MaintenanceBusinessLogic(null, alertDao));
        assertThrows(NullPointerException.class, () -> new MaintenanceBusinessLogic(taskDao, null));
    }

    @Test
    void getOpenAlertsReturnsTheListOfTheDaoAsItIs() throws Exception {
        alertDao.openList = List.of(new MaintenanceAlert(), new MaintenanceAlert());

        assertSame(alertDao.openList, logic.getOpenAlerts());
    }

    @Test
    void getOpenTasksReturnsTheListOfTheDaoAsItIs() throws Exception {
        taskDao.openList = List.of(new MaintenanceTask(), new MaintenanceTask());

        assertSame(taskDao.openList, logic.getOpenTasks());
    }

    @Test
    void scheduleTaskSavesAnOpenUntakenTaskAndReturnsItsId() throws Exception {
        int id = logic.scheduleTask(EQUIPMENT_ID, "Replace conveyor belt", MONDAY, null);

        MaintenanceTask saved = taskDao.tasks.get(id);
        assertEquals(1, taskDao.added.size());
        assertEquals(EQUIPMENT_ID, saved.getEquipmentId());
        assertEquals("Replace conveyor belt", saved.getDescription());
        assertEquals(MONDAY, saved.getScheduledFor());
        assertNull(saved.getTechId());
        assertNull(saved.getCompletedAt());
        assertNull(saved.getMinutesLogged());
        assertTrue(alertDao.links.isEmpty());
    }

    @Test
    void scheduleTaskTrimsTheDescription() throws Exception {
        int id = logic.scheduleTask(EQUIPMENT_ID, "  Replace belt  ", MONDAY, null);

        assertEquals("Replace belt", taskDao.tasks.get(id).getDescription());
    }

    @Test
    void scheduleTaskAccepts255CharactersAfterTrimming() throws Exception {
        String description = "  " + "a".repeat(255) + "  ";

        int id = logic.scheduleTask(EQUIPMENT_ID, description, MONDAY, null);

        assertEquals(255, taskDao.tasks.get(id).getDescription().length());
    }

    @Test
    void scheduleTaskRejectsANullDescription() {
        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, null, MONDAY, null));
    }

    @Test
    void scheduleTaskRejectsABlankDescription() {
        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, "   ", MONDAY, null));
    }

    @Test
    void scheduleTaskRejectsADescriptionLongerThan255CharactersAfterTrimming() {
        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, "a".repeat(256), MONDAY, null));
    }

    @Test
    void scheduleTaskRejectsAMissingScheduledTime() {
        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, "Replace belt", null, null));
    }

    @Test
    void scheduleTaskRejectsAnUnknownAlert() {
        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, "Replace belt", MONDAY, 99));
    }

    @Test
    void scheduleTaskRejectsAResolvedAlert() {
        MaintenanceAlert alert = alertDao.store(EQUIPMENT_ID, AlertReason.DIAGNOSTIC);
        alert.setResolvedAt(MONDAY);

        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, "Replace belt", MONDAY,
                alert.getAlertId()));
    }

    @Test
    void scheduleTaskRejectsAnAlertOfAnotherPieceOfEquipment() {
        MaintenanceAlert alert = alertDao.store(OTHER_EQUIPMENT_ID, AlertReason.DIAGNOSTIC);

        assertRejected(() -> logic.scheduleTask(EQUIPMENT_ID, "Replace belt", MONDAY,
                alert.getAlertId()));
    }

    @Test
    void scheduleTaskRejectsAnAlertAlreadyHandledByATask() {
        MaintenanceAlert alert = alertDao.store(EQUIPMENT_ID, AlertReason.DIAGNOSTIC);
        alert.setTaskId(7);

        ValidationException e = assertThrows(ValidationException.class,
                () -> logic.scheduleTask(EQUIPMENT_ID, "Replace belt", MONDAY,
                        alert.getAlertId()));

        assertEquals("Alert " + alert.getAlertId() + " is already handled by task 7",
                e.getMessage());
        assertTrue(taskDao.added.isEmpty());
        assertTrue(alertDao.links.isEmpty());
        assertEquals(7, alert.getTaskId());
    }

    @Test
    void scheduleTaskLinksTheAlertToTheNewTask() throws Exception {
        MaintenanceAlert alert = alertDao.store(EQUIPMENT_ID, AlertReason.DIAGNOSTIC);

        int id = logic.scheduleTask(EQUIPMENT_ID, "Replace belt", MONDAY, alert.getAlertId());

        assertEquals(1, taskDao.added.size());
        assertEquals(List.of(alert.getAlertId() + "->" + id), alertDao.links);
    }

    @Test
    void takeTaskAssignsAnOpenFreeTaskToTheTech() throws Exception {
        MaintenanceTask task = taskDao.store(null, null);

        logic.takeTask(task.getTaskId(), TECH_ID);

        assertEquals(1, taskDao.updated.size());
        MaintenanceTask saved = taskDao.updated.get(0);
        assertEquals(task.getTaskId(), saved.getTaskId());
        assertEquals(TECH_ID, saved.getTechId());
        assertEquals(EQUIPMENT_ID, saved.getEquipmentId());
        assertEquals("Replace belt", saved.getDescription());
        assertEquals(MONDAY, saved.getScheduledFor());
        assertNull(saved.getCompletedAt());
        assertNull(saved.getMinutesLogged());
    }

    @Test
    void takeTaskAlreadyHeldByTheSameTechChangesNothing() throws Exception {
        MaintenanceTask task = taskDao.store(TECH_ID, null);

        logic.takeTask(task.getTaskId(), TECH_ID);

        assertTrue(taskDao.updated.isEmpty());
        assertEquals(TECH_ID, taskDao.tasks.get(task.getTaskId()).getTechId());
    }

    @Test
    void takeTaskRejectsAnUnknownTask() {
        ValidationException e = assertThrows(ValidationException.class,
                () -> logic.takeTask(99, TECH_ID));

        assertTrue(e.getMessage().contains("does not exist"));
        assertTrue(taskDao.updated.isEmpty());
    }

    @Test
    void takeTaskRejectsACompletedTask() {
        MaintenanceTask task = taskDao.store(null, MONDAY.plusHours(1));

        ValidationException e = assertThrows(ValidationException.class,
                () -> logic.takeTask(task.getTaskId(), TECH_ID));

        assertTrue(e.getMessage().contains("completed"));
        assertTrue(taskDao.updated.isEmpty());
    }

    @Test
    void takeTaskRejectsATaskTakenByAnotherTech() {
        MaintenanceTask task = taskDao.store(OTHER_TECH_ID, null);

        ValidationException e = assertThrows(ValidationException.class,
                () -> logic.takeTask(task.getTaskId(), TECH_ID));

        assertTrue(e.getMessage().contains("another Kitchen-Tech"));
        assertTrue(taskDao.updated.isEmpty());
        assertEquals(OTHER_TECH_ID, taskDao.tasks.get(task.getTaskId()).getTechId());
    }

    /**
     * Checks that the call throws a ValidationException with a message and saves nothing.
     */
    private void assertRejected(ThrowingCall call) {
        ValidationException e = assertThrows(ValidationException.class, call::run);
        assertTrue(e.getMessage() != null && !e.getMessage().isBlank());
        assertTrue(taskDao.added.isEmpty());
        assertTrue(alertDao.links.isEmpty());
    }

    /**
     * A call that may throw a checked exception.
     */
    @FunctionalInterface
    private interface ThrowingCall {
        void run() throws Exception;
    }

    /**
     * In-memory task DAO that records what is added and updated.
     */
    private static final class FakeTaskDao implements MaintenanceTaskDao {
        private final Map<Integer, MaintenanceTask> tasks = new LinkedHashMap<>();
        private final List<MaintenanceTask> added = new ArrayList<>();
        private final List<MaintenanceTask> updated = new ArrayList<>();
        private List<MaintenanceTask> openList = new ArrayList<>();
        private int nextId = 1;

        /**
         * Stores a task for EQUIPMENT_ID scheduled on MONDAY and returns it.
         */
        MaintenanceTask store(Integer techId, LocalDateTime completedAt) {
            MaintenanceTask task = new MaintenanceTask();
            task.setTaskId(nextId++);
            task.setEquipmentId(EQUIPMENT_ID);
            task.setTechId(techId);
            task.setDescription("Replace belt");
            task.setScheduledFor(MONDAY);
            task.setCompletedAt(completedAt);
            task.setMinutesLogged(completedAt == null ? null : 60);
            tasks.put(task.getTaskId(), task);
            return task;
        }

        @Override
        public MaintenanceTask findById(int taskId) {
            MaintenanceTask stored = tasks.get(taskId);
            if (stored == null) {
                return null;
            }
            // Returns a copy, like a real DAO that reads a new object from each row
            MaintenanceTask copy = new MaintenanceTask();
            copy.setTaskId(stored.getTaskId());
            copy.setEquipmentId(stored.getEquipmentId());
            copy.setTechId(stored.getTechId());
            copy.setDescription(stored.getDescription());
            copy.setScheduledFor(stored.getScheduledFor());
            copy.setCompletedAt(stored.getCompletedAt());
            copy.setMinutesLogged(stored.getMinutesLogged());
            return copy;
        }

        @Override
        public List<MaintenanceTask> findAll() {
            return new ArrayList<>(tasks.values());
        }

        @Override
        public List<MaintenanceTask> findOpen() {
            return openList;
        }

        @Override
        public List<MaintenanceTask> findByTech(int techId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int add(MaintenanceTask task) {
            int id = nextId++;
            added.add(task);
            tasks.put(id, task);
            return id;
        }

        @Override
        public void update(MaintenanceTask task) {
            updated.add(task);
            tasks.put(task.getTaskId(), task);
        }
    }

    /**
     * In-memory alert DAO that records each link as "alertId->taskId".
     */
    private static final class FakeAlertDao implements MaintenanceAlertDao {
        private final Map<Integer, MaintenanceAlert> alerts = new LinkedHashMap<>();
        private final List<String> links = new ArrayList<>();
        private List<MaintenanceAlert> openList = new ArrayList<>();
        private int nextId = 1;

        /**
         * Stores an open alert created on MONDAY and returns it.
         */
        MaintenanceAlert store(int equipmentId, AlertReason reason) {
            MaintenanceAlert alert = new MaintenanceAlert();
            alert.setAlertId(nextId++);
            alert.setEquipmentId(equipmentId);
            alert.setReason(reason);
            alert.setCreatedAt(MONDAY);
            alerts.put(alert.getAlertId(), alert);
            return alert;
        }

        @Override
        public MaintenanceAlert findById(int alertId) {
            return alerts.get(alertId);
        }

        @Override
        public List<MaintenanceAlert> findOpen() {
            return openList;
        }

        @Override
        public List<MaintenanceAlert> findOpenByEquipment(int equipmentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MaintenanceAlert findOpenByEquipmentAndReason(int equipmentId, AlertReason reason) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int add(MaintenanceAlert alert) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void linkTask(int alertId, int taskId) {
            links.add(alertId + "->" + taskId);
            alerts.get(alertId).setTaskId(taskId);
        }

        @Override
        public void resolve(int alertId, LocalDateTime resolvedAt) {
            throw new UnsupportedOperationException();
        }
    }
}
