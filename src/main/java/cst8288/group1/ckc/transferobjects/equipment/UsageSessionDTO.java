package cst8288.group1.ckc.transferobjects.equipment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Transfer object carrying data for an equipment usage session.
 * A session is active while its checkout time is {@code null}.
 *
 * @author Andrew Caelan Roberge-Toll
 */
public class UsageSessionDTO implements Serializable {

    private int sessionId;
    private int equipmentId;
    private int userId;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;

    /**
     * Creates an empty usage session transfer object; fill it with the setters.
     */
    public UsageSessionDTO() {
    }

    /**
     * Returns the session id.
     *
     * @return the session id
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session id.
     *
     * @param sessionId the session id
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Returns the equipment id.
     *
     * @return the equipment id
     */
    public int getEquipmentId() {
        return equipmentId;
    }

    /**
     * Sets the equipment id.
     *
     * @param equipmentId the equipment id
     */
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * Returns the user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the check-in date and time.
     *
     * @return the check-in date and time
     */
    public LocalDateTime getCheckInAt() {
        return checkInAt;
    }

    /**
     * Sets the check-in date and time.
     *
     * @param checkInAt the check-in date and time
     */
    public void setCheckInAt(LocalDateTime checkInAt) {
        this.checkInAt = checkInAt;
    }

    /**
     * Returns the checkout date and time.
     *
     * @return the checkout date and time, or {@code null} for an active session
     */
    public LocalDateTime getCheckOutAt() {
        return checkOutAt;
    }

    /**
     * Sets the checkout date and time.
     *
     * @param checkOutAt the checkout date and time, or {@code null} for an active session
     */
    public void setCheckOutAt(LocalDateTime checkOutAt) {
        this.checkOutAt = checkOutAt;
    }

    /**
     * Returns the value represented by the database-generated active equipment column.
     *
     * @return the equipment id for an active session, or {@code null} after checkout
     */
    public Integer getActiveEquipmentId() {
        return checkOutAt == null ? equipmentId : null;
    }
}
