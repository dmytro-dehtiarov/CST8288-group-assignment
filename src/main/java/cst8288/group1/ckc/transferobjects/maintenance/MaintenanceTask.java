package cst8288.group1.ckc.transferobjects.maintenance;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Transfer object for one row of the {@code maintenance_tasks} table.
 *
 * <p>A Kitchen-Tech schedules a task for a piece of equipment, takes it, and completes it by
 * logging the minutes worked. A task is open while {@code completedAt} is {@code null}, and it is
 * free to take while {@code techId} is {@code null}. This class only carries data; the rules live in
 * {@code MaintenanceBusinessLogic}.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private int taskId;
    private int equipmentId;
    private Integer techId;
    private String description;
    private LocalDateTime scheduledFor;
    private LocalDateTime completedAt;
    private Integer minutesLogged;

    /**
     * Creates an empty task; fill it with the setters.
     */
    public MaintenanceTask() {
    }

    /**
     * Returns the task id.
     *
     * @return the {@code task_id}, or 0 if the task is not saved yet
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * Sets the task id.
     *
     * @param taskId the {@code task_id} given by the database
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Returns the id of the equipment this task services.
     *
     * @return the {@code equipment_id}
     */
    public int getEquipmentId() {
        return equipmentId;
    }

    /**
     * Sets the id of the equipment this task services.
     *
     * @param equipmentId the {@code equipment_id}
     */
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * Returns the id of the Kitchen-Tech who took the task.
     *
     * @return the {@code tech_id}, or {@code null} while no Kitchen-Tech has taken the task
     */
    public Integer getTechId() {
        return techId;
    }

    /**
     * Sets the id of the Kitchen-Tech who took the task.
     *
     * @param techId the {@code tech_id}, or {@code null} if nobody has taken the task
     */
    public void setTechId(Integer techId) {
        this.techId = techId;
    }

    /**
     * Returns what has to be done.
     *
     * @return the description, at most 255 characters
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets what has to be done.
     *
     * @param description the description, at most 255 characters
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns when the task is planned.
     *
     * @return the planned date and time
     */
    public LocalDateTime getScheduledFor() {
        return scheduledFor;
    }

    /**
     * Sets when the task is planned.
     *
     * @param scheduledFor the planned date and time
     */
    public void setScheduledFor(LocalDateTime scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    /**
     * Returns when the task was completed.
     *
     * @return the completion time, or {@code null} while the task is open
     */
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    /**
     * Sets when the task was completed.
     *
     * @param completedAt the completion time, or {@code null} for an open task
     */
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Returns the minutes the Kitchen-Tech worked on the task.
     *
     * @return the minutes logged, or {@code null} while the task is open
     */
    public Integer getMinutesLogged() {
        return minutesLogged;
    }

    /**
     * Sets the minutes the Kitchen-Tech worked on the task.
     *
     * @param minutesLogged the minutes logged, or {@code null} for an open task
     */
    public void setMinutesLogged(Integer minutesLogged) {
        this.minutesLogged = minutesLogged;
    }
}
