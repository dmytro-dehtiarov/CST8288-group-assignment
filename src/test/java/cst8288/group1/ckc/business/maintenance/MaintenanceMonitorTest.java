package cst8288.group1.ckc.business.maintenance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.maintenance.MaintenanceAlertDao;
import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Observer pattern of the maintenance module: MaintenanceMonitor as the subject
 * and MaintenanceAlertLog as an observer. No database is used.
 *
 * @author Othman Slassi
 */
class MaintenanceMonitorTest {

    private static final int EQUIPMENT_ID = 7;
    private static final int THRESHOLD = 500;

    private MaintenanceMonitor monitor;
    private RecordingObserver observer;

    @BeforeEach
    void setUp() {
        monitor = new MaintenanceMonitor();
        observer = new RecordingObserver(null);
        monitor.addObserver(observer);
    }

    @Test
    void raisesNoAlertBelowTheThreshold() throws DaoException {
        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, 499.9, THRESHOLD, null);

        assertTrue(alerts.isEmpty());
        assertTrue(observer.received.isEmpty());
    }

    @Test
    void ignoresBlankFaultDetails() throws DaoException {
        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, 10, THRESHOLD, "   ");

        assertTrue(alerts.isEmpty());
    }

    @Test
    void raisesWearAlertAtTheThreshold() throws DaoException {
        LocalDateTime before = LocalDateTime.now();
        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, THRESHOLD, THRESHOLD,
                null);

        assertEquals(1, alerts.size());
        MaintenanceAlert alert = alerts.get(0);
        assertEquals(EQUIPMENT_ID, alert.getEquipmentId());
        assertEquals(AlertReason.WEAR, alert.getReason());
        assertTrue(alert.getDetails().contains("500.0"));
        assertTrue(alert.getDetails().contains("500 hours"));
        assertNotNull(alert.getCreatedAt());
        assertFalse(alert.getCreatedAt().isBefore(before));
        assertEquals(alerts, observer.received);
    }

    @Test
    void raisesDiagnosticAlertWhenAFaultIsReported() throws DaoException {
        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, 10, THRESHOLD,
                "Door sensor open");

        assertEquals(1, alerts.size());
        assertEquals(AlertReason.DIAGNOSTIC, alerts.get(0).getReason());
        assertEquals("Door sensor open", alerts.get(0).getDetails());
        assertNotNull(alerts.get(0).getCreatedAt());
        assertEquals(alerts, observer.received);
    }

    @Test
    void raisesBothAlertsWhenBothApply() throws DaoException {
        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, 600, THRESHOLD,
                "Overheating");

        assertEquals(2, alerts.size());
        assertEquals(AlertReason.WEAR, alerts.get(0).getReason());
        assertEquals(AlertReason.DIAGNOSTIC, alerts.get(1).getReason());
        assertEquals(alerts, observer.received);
    }

    @Test
    void notifiesEveryObserverInRegistrationOrder() throws DaoException {
        List<String> calls = new ArrayList<>();
        MaintenanceMonitor ordered = new MaintenanceMonitor();
        RecordingObserver first = new RecordingObserver(calls);
        RecordingObserver second = new RecordingObserver(calls);
        ordered.addObserver(first);
        ordered.addObserver(second);

        List<MaintenanceAlert> alerts = ordered.checkEquipment(EQUIPMENT_ID, 600, THRESHOLD,
                "Overheating");

        assertEquals(alerts, first.received);
        assertEquals(alerts, second.received);
        assertEquals(List.of(first.name + ":WEAR", second.name + ":WEAR",
                first.name + ":DIAGNOSTIC", second.name + ":DIAGNOSTIC"), calls);
    }

    @Test
    void doesNotNotifyARemovedObserver() throws DaoException {
        RecordingObserver other = new RecordingObserver(null);
        monitor.addObserver(other);
        monitor.removeObserver(observer);

        monitor.checkEquipment(EQUIPMENT_ID, 600, THRESHOLD, null);

        assertTrue(observer.received.isEmpty());
        assertEquals(1, other.received.size());
    }

    @Test
    void rejectsNegativeHours() {
        assertThrows(IllegalArgumentException.class,
                () -> monitor.checkEquipment(EQUIPMENT_ID, -1, THRESHOLD, null));
    }

    @Test
    void rejectsHoursThatAreNotANumber() {
        assertThrows(IllegalArgumentException.class,
                () -> monitor.checkEquipment(EQUIPMENT_ID, Double.NaN, THRESHOLD, null));
    }

    @Test
    void rejectsAThresholdOfZeroOrLess() {
        assertThrows(IllegalArgumentException.class,
                () -> monitor.checkEquipment(EQUIPMENT_ID, 10, 0, null));
        assertThrows(IllegalArgumentException.class,
                () -> monitor.checkEquipment(EQUIPMENT_ID, 10, -5, null));
        assertTrue(observer.received.isEmpty());
    }

    @Test
    void alertLogSavesANewAlert() throws DaoException {
        FakeAlertDao dao = new FakeAlertDao();
        monitor.addObserver(new MaintenanceAlertLog(dao));

        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, 600, THRESHOLD, null);

        assertEquals(1, dao.saved.size());
        assertSame(alerts.get(0), dao.saved.get(0));
        assertEquals(1, alerts.get(0).getAlertId());
    }

    @Test
    void alertLogSkipsAnAlertThatIsAlreadyOpen() throws DaoException {
        FakeAlertDao dao = new FakeAlertDao();
        MaintenanceAlert open = new MaintenanceAlert();
        open.setAlertId(42);
        open.setEquipmentId(EQUIPMENT_ID);
        open.setReason(AlertReason.WEAR);
        open.setCreatedAt(LocalDateTime.now().minusDays(1));
        dao.saved.add(open);
        monitor.addObserver(new MaintenanceAlertLog(dao));

        List<MaintenanceAlert> alerts = monitor.checkEquipment(EQUIPMENT_ID, 600, THRESHOLD,
                "Overheating");

        assertEquals(2, dao.saved.size());
        assertSame(open, dao.saved.get(0));
        assertEquals(AlertReason.DIAGNOSTIC, dao.saved.get(1).getReason());
        assertEquals(0, alerts.get(0).getAlertId());
    }

    /**
     * Observer that records the alerts it receives and, optionally, the order of the calls.
     */
    private static final class RecordingObserver implements MaintenanceObserver {
        private static int count;

        private final String name = "observer" + (++count);
        private final List<MaintenanceAlert> received = new ArrayList<>();
        private final List<String> calls;

        RecordingObserver(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public void onMaintenanceNeeded(MaintenanceAlert alert) {
            received.add(alert);
            if (calls != null) {
                calls.add(name + ":" + alert.getReason());
            }
        }
    }

    /**
     * In-memory alert DAO that supports only the operations MaintenanceAlertLog uses.
     */
    private static final class FakeAlertDao implements MaintenanceAlertDao {
        private final List<MaintenanceAlert> saved = new ArrayList<>();

        @Override
        public MaintenanceAlert findOpenByEquipmentAndReason(int equipmentId, AlertReason reason) {
            for (MaintenanceAlert alert : saved) {
                if (alert.getEquipmentId() == equipmentId && alert.getReason() == reason
                        && alert.getResolvedAt() == null) {
                    return alert;
                }
            }
            return null;
        }

        @Override
        public int add(MaintenanceAlert alert) {
            saved.add(alert);
            return saved.size();
        }

        @Override
        public MaintenanceAlert findById(int alertId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<MaintenanceAlert> findOpen() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<MaintenanceAlert> findOpenByEquipment(int equipmentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void linkTask(int alertId, int taskId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void resolve(int alertId, LocalDateTime resolvedAt) {
            throw new UnsupportedOperationException();
        }
    }
}
