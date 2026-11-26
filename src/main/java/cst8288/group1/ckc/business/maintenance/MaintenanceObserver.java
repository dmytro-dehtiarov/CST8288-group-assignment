package cst8288.group1.ckc.business.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;

/**
 * Observer in the Observer pattern: reacts when a piece of equipment needs maintenance.
 *
 * <p>Implementations register with {@link MaintenanceMonitor#addObserver(MaintenanceObserver)}.
 * The monitor calls {@link #onMaintenanceNeeded(MaintenanceAlert)} once for each alert it raises and
 * does not know what the observer does with it, so a new reaction is added by writing a new
 * observer, without changing the monitor. Current observers are {@link MaintenanceAlertLog}, which
 * saves the alert, and {@link EquipmentStatusUpdater}, which takes the equipment out of service
 * when a sensor reports a fault.</p>
 *
 * @author Othman Slassi
 */
public interface MaintenanceObserver {

    /**
     * Reacts to an alert raised by the monitor.
     *
     * <p>The alert is not saved yet when the monitor sends it, so its {@code alertId} may be 0. The
     * same object is passed to every observer in turn.</p>
     *
     * @param alert the alert that was raised, never {@code null}
     * @throws DaoException if the reaction needs the database and the database operation fails
     */
    void onMaintenanceNeeded(MaintenanceAlert alert) throws DaoException;
}
