package cst8288.group1.ckc.dataaccess.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access contract for the {@code maintenance_alerts} table.
 *
 * <p>Business logic depends on this interface, not on the JDBC implementation, so unit tests can
 * pass a fake DAO. An alert is open while {@code resolved_at IS NULL}, and the table allows only
 * one open alert per equipment and reason. Implementations check no business rule: they read and
 * write rows and wrap every {@link java.sql.SQLException} in a {@link DaoException}.</p>
 *
 * @author Othman Slassi
 */
public interface MaintenanceAlertDao {

    /**
     * Finds one alert by its id.
     *
     * @param alertId the {@code alert_id} to look for
     * @return the alert, or {@code null} if no alert has that id
     * @throws DaoException if the query fails
     */
    MaintenanceAlert findById(int alertId) throws DaoException;

    /**
     * Returns the alerts that are not resolved yet.
     *
     * <p>Alerts are ordered by {@code created_at}, the newest first.</p>
     *
     * @return the open alerts; an empty list if there are none
     * @throws DaoException if the query fails
     */
    List<MaintenanceAlert> findOpen() throws DaoException;

    /**
     * Returns the open alerts of one piece of equipment.
     *
     * <p>There are at most one per {@link AlertReason}. Alerts are ordered by {@code created_at},
     * the newest first.</p>
     *
     * @param equipmentId the {@code equipment_id}
     * @return the open alerts of that equipment; an empty list if there are none
     * @throws DaoException if the query fails
     */
    List<MaintenanceAlert> findOpenByEquipment(int equipmentId) throws DaoException;

    /**
     * Finds the open alert of one piece of equipment for one reason.
     *
     * <p>The table allows only one open alert per equipment and reason, so there is never more than
     * one match. Callers use this before {@link #add(MaintenanceAlert)} to avoid a duplicate.</p>
     *
     * @param equipmentId the {@code equipment_id}
     * @param reason the reason to look for, not {@code null}
     * @return the open alert, or {@code null} if there is none
     * @throws DaoException if the query fails
     */
    MaintenanceAlert findOpenByEquipmentAndReason(int equipmentId, AlertReason reason)
            throws DaoException;

    /**
     * Inserts a new alert.
     *
     * <p>The {@code alertId} of the given object is ignored because the database generates it; the
     * object itself is not changed.</p>
     *
     * @param alert the alert to save; {@code createdAt} must not be {@code null}
     * @return the new {@code alert_id}
     * @throws DaoException if the insert fails, for example when an open alert already exists for
     *     the same equipment and reason
     */
    int add(MaintenanceAlert alert) throws DaoException;

    /**
     * Links an alert to the maintenance task that handles it.
     *
     * @param alertId the {@code alert_id} to change
     * @param taskId the {@code task_id} to store in the alert
     * @throws DaoException if the update fails, no alert has that id, or the task does not exist
     */
    void linkTask(int alertId, int taskId) throws DaoException;

    /**
     * Marks an open alert as resolved.
     *
     * <p>Only an open alert changes: an alert that is already resolved keeps its first resolution
     * time, and an unknown id changes nothing.</p>
     *
     * @param alertId the {@code alert_id} to resolve
     * @param resolvedAt the time to store in {@code resolved_at}
     * @throws DaoException if the update fails
     */
    void resolve(int alertId, LocalDateTime resolvedAt) throws DaoException;
}
