package cst8288.group1.ckc.business.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject in the Observer pattern: decides whether a piece of equipment needs maintenance and tells
 * every registered {@link MaintenanceObserver}.
 *
 * <p>The equipment module calls {@link #checkEquipment(int, double, int, String)} when a user checks
 * out a piece of equipment. It passes the hours used since the last service, which it computes from
 * its usage sessions, and the fault text from its sensor adapter, or {@code null} if the sensor
 * reported no fault. The monitor builds the alerts and hands them to the observers; it does not
 * know what the observers do with them and it never touches the database itself. Whoever builds the
 * monitor registers the observers, for example {@link MaintenanceAlertLog} and
 * {@link EquipmentStatusUpdater}.</p>
 *
 * <p>The observer list is safe to share between threads, so one monitor can serve every
 * request.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceMonitor {

    private final CopyOnWriteArrayList<MaintenanceObserver> observers =
            new CopyOnWriteArrayList<>();

    /**
     * Creates a monitor with no observers.
     */
    public MaintenanceMonitor() {
    }

    /**
     * Registers an observer.
     *
     * <p>Observers are notified in the order they were added. Adding an observer that is already
     * registered does nothing, so it is never notified twice for the same alert.</p>
     *
     * @param observer the observer to add
     * @throws NullPointerException if {@code observer} is {@code null}
     */
    public void addObserver(MaintenanceObserver observer) {
        Objects.requireNonNull(observer, "observer");
        observers.addIfAbsent(observer);
    }

    /**
     * Unregisters an observer, which then receives no more alerts.
     *
     * <p>Removing an observer that is not registered does nothing.</p>
     *
     * @param observer the observer to remove
     */
    public void removeObserver(MaintenanceObserver observer) {
        observers.remove(observer);
    }

    /**
     * Checks a piece of equipment at check-out, builds the alerts it needs and notifies every
     * observer.
     *
     * <p>A {@link AlertReason#WEAR} alert is built when {@code hoursSinceService} is at least
     * {@code thresholdHours}; its details state the hours and the threshold. A
     * {@link AlertReason#DIAGNOSTIC} alert is built when {@code faultDetails} is neither
     * {@code null} nor blank; its details are {@code faultDetails}. Both can be built in one call,
     * the wear alert first. Each alert gets the current time as {@code createdAt}; it is not saved,
     * so its id stays 0 until an observer saves it.</p>
     *
     * <p>Every registered observer receives each alert, in registration order. If an observer
     * throws, the exception goes to the caller and the remaining observers are not notified.</p>
     *
     * @param equipmentId the {@code equipment_id} that was checked out
     * @param hoursSinceService hours of use since the last maintenance, 0 or more
     * @param thresholdHours the equipment's {@code maintenance_threshold_hours}, more than 0
     * @param faultDetails the fault reported by the sensor, or {@code null} if there is none
     * @return the alerts that were built, in the order they were sent; an empty list if the
     *     equipment needs no maintenance
     * @throws IllegalArgumentException if {@code hoursSinceService} is negative or not a number, or
     *     {@code thresholdHours} is 0 or less
     * @throws DaoException if an observer fails to save its reaction
     */
    public List<MaintenanceAlert> checkEquipment(int equipmentId, double hoursSinceService,
            int thresholdHours, String faultDetails) throws DaoException {
        // The negated form also rejects NaN
        if (!(hoursSinceService >= 0)) {
            throw new IllegalArgumentException("Hours since service must be 0 or more");
        }
        if (thresholdHours <= 0) {
            throw new IllegalArgumentException("Threshold hours must be more than 0");
        }

        LocalDateTime now = LocalDateTime.now();
        List<MaintenanceAlert> alerts = new ArrayList<>();
        if (hoursSinceService >= thresholdHours) {
            String details = String.format(Locale.ROOT,
                    "%.1f hours used since last service, threshold is %d hours",
                    hoursSinceService, thresholdHours);
            alerts.add(buildAlert(equipmentId, AlertReason.WEAR, details, now));
        }
        if (faultDetails != null && !faultDetails.isBlank()) {
            alerts.add(buildAlert(equipmentId, AlertReason.DIAGNOSTIC, faultDetails, now));
        }

        for (MaintenanceAlert alert : alerts) {
            for (MaintenanceObserver observer : observers) {
                observer.onMaintenanceNeeded(alert);
            }
        }
        return List.copyOf(alerts);
    }

    /**
     * Builds an open, unsaved alert.
     *
     * @param equipmentId the equipment the alert is about
     * @param reason why the alert is raised
     * @param details the text to store with the alert
     * @param createdAt when the alert is raised
     * @return the new alert
     */
    private static MaintenanceAlert buildAlert(int equipmentId, AlertReason reason, String details,
            LocalDateTime createdAt) {
        MaintenanceAlert alert = new MaintenanceAlert();
        alert.setEquipmentId(equipmentId);
        alert.setReason(reason);
        alert.setDetails(details);
        alert.setCreatedAt(createdAt);
        return alert;
    }
}
