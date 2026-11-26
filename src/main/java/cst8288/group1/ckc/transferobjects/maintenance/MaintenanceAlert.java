package cst8288.group1.ckc.transferobjects.maintenance;

import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Transfer object for one row of the {@code maintenance_alerts} table.
 *
 * <p>An alert says that a piece of equipment needs maintenance, either because of wear or because
 * a sensor reported a fault. It is open while {@code resolvedAt} is {@code null}; the database
 * allows only one open alert per equipment and reason. {@code taskId} points to the maintenance
 * task that handles the alert, once a Kitchen-Tech has scheduled one. This class only carries
 * data.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    private int alertId;
    private int equipmentId;
    private AlertReason reason;
    private String details;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private Integer taskId;

    /**
     * Creates an empty alert; fill it with the setters.
     */
    public MaintenanceAlert() {
    }

    /**
     * Returns the alert id.
     *
     * @return the {@code alert_id}, or 0 if the alert is not saved yet
     */
    public int getAlertId() {
        return alertId;
    }

    /**
     * Sets the alert id.
     *
     * @param alertId the {@code alert_id} given by the database
     */
    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    /**
     * Returns the id of the equipment that needs maintenance.
     *
     * @return the {@code equipment_id}
     */
    public int getEquipmentId() {
        return equipmentId;
    }

    /**
     * Sets the id of the equipment that needs maintenance.
     *
     * @param equipmentId the {@code equipment_id}
     */
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * Returns why the alert was raised.
     *
     * @return {@link AlertReason#WEAR} or {@link AlertReason#DIAGNOSTIC}
     */
    public AlertReason getReason() {
        return reason;
    }

    /**
     * Sets why the alert was raised.
     *
     * @param reason {@link AlertReason#WEAR} or {@link AlertReason#DIAGNOSTIC}
     */
    public void setReason(AlertReason reason) {
        this.reason = reason;
    }

    /**
     * Returns the human-readable explanation of the alert.
     *
     * @return the details, at most 255 characters, or {@code null} if none were given
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets the human-readable explanation of the alert.
     *
     * @param details the details, at most 255 characters, or {@code null}
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Returns when the alert was raised.
     *
     * @return the creation time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets when the alert was raised.
     *
     * @param createdAt the creation time
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns when the alert was resolved.
     *
     * @return the resolution time, or {@code null} while the alert is open
     */
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    /**
     * Sets when the alert was resolved.
     *
     * @param resolvedAt the resolution time, or {@code null} for an open alert
     */
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    /**
     * Returns the id of the maintenance task that handles this alert.
     *
     * @return the {@code task_id}, or {@code null} if no task is linked yet
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * Sets the id of the maintenance task that handles this alert.
     *
     * @param taskId the {@code task_id}, or {@code null} if no task is linked
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
