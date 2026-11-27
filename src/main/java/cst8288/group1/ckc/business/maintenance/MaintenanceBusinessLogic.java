package cst8288.group1.ckc.business.maintenance;

import cst8288.group1.ckc.business.ValidationException;
import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.maintenance.MaintenanceAlertDao;
import cst8288.group1.ckc.dataaccess.maintenance.MaintenanceTaskDao;
import cst8288.group1.ckc.transferobjects.enums.TransactionSource;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceTask;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Business rules of the maintenance module: maintenance tasks, open alerts and the activity report.
 *
 * <p>Servlets call this class, never the DAOs directly. It receives its DAOs in the constructor,
 * so unit tests can pass fake ones. A broken rule is reported with a {@link ValidationException}
 * whose message is shown to the user; a database failure is reported with a
 * {@link DaoException}.</p>
 *
 * <p>Role checks are done by the controller before calling these methods: only Kitchen-Techs may
 * schedule, take and complete tasks, and only Trainers and Kitchen-Techs see the activity report.
 * This class trusts the {@code techId} and {@code userId} it receives.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceBusinessLogic {

    private static final int MAX_DESCRIPTION_LENGTH = 255;

    private final MaintenanceTaskDao taskDao;
    private final MaintenanceAlertDao alertDao;

    /**
     * Creates the business logic on top of its DAOs.
     *
     * @param taskDao the DAO for maintenance tasks
     * @param alertDao the DAO for maintenance alerts
     * @throws NullPointerException if a DAO is {@code null}
     */
    public MaintenanceBusinessLogic(MaintenanceTaskDao taskDao, MaintenanceAlertDao alertDao) {
        this.taskDao = Objects.requireNonNull(taskDao, "taskDao");
        this.alertDao = Objects.requireNonNull(alertDao, "alertDao");
    }

    /**
     * Returns the alerts that are not resolved yet, for the Kitchen-Tech dashboard.
     *
     * <p>Returns {@link MaintenanceAlertDao#findOpen()} as it is, newest first.</p>
     *
     * @return the open alerts; an empty list if there are none
     * @throws DaoException if the alerts cannot be read
     */
    public List<MaintenanceAlert> getOpenAlerts() throws DaoException {
        return alertDao.findOpen();
    }

    /**
     * Returns the tasks that are not completed yet, for the Kitchen-Tech dashboard.
     *
     * <p>Returns {@link MaintenanceTaskDao#findOpen()} as it is, earliest scheduled first. It
     * includes tasks already taken by a Kitchen-Tech.</p>
     *
     * @return the open tasks; an empty list if there are none
     * @throws DaoException if the tasks cannot be read
     */
    public List<MaintenanceTask> getOpenTasks() throws DaoException {
        return taskDao.findOpen();
    }

    /**
     * Schedules a new maintenance task for a piece of equipment, optionally to handle an alert.
     *
     * <p>Rules: {@code description} is required, must not be blank and is at most 255 characters
     * after trimming; {@code scheduledFor} is required. When {@code alertId} is given, the alert
     * must exist, be open, belong to the same {@code equipmentId} and not be handled by a task
     * yet (its {@code taskId} must be {@code null}). The new task is saved open
     * and not taken by anyone ({@code techId}, {@code completedAt} and {@code minutesLogged} are
     * {@code null}). When {@code alertId} is given, the alert is then linked to the new task with
     * {@link MaintenanceAlertDao#linkTask(int, int)}. All checks run before anything is saved.</p>
     *
     * @param equipmentId the equipment to service
     * @param description what has to be done
     * @param scheduledFor when the task is planned
     * @param alertId the open alert this task handles, or {@code null} for a task without alert
     * @return the id of the new task
     * @throws ValidationException if a rule above is broken, for example when the alert is already
     *     handled by a task; the message says which one
     * @throws DaoException if the task or the link cannot be saved, for example when the equipment
     *     does not exist
     */
    public int scheduleTask(int equipmentId, String description, LocalDateTime scheduledFor,
            Integer alertId) throws ValidationException, DaoException {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Description is required");
        }
        String trimmed = description.trim();
        if (trimmed.length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Description must be at most "
                    + MAX_DESCRIPTION_LENGTH + " characters");
        }
        if (scheduledFor == null) {
            throw new ValidationException("Scheduled date and time is required");
        }
        if (alertId != null) {
            MaintenanceAlert alert = alertDao.findById(alertId);
            if (alert == null) {
                throw new ValidationException("Alert " + alertId + " does not exist");
            }
            if (alert.getResolvedAt() != null) {
                throw new ValidationException("Alert " + alertId + " is already resolved");
            }
            if (alert.getEquipmentId() != equipmentId) {
                throw new ValidationException("Alert " + alertId
                        + " belongs to another piece of equipment");
            }
            if (alert.getTaskId() != null) {
                throw new ValidationException("Alert " + alertId + " is already handled by task "
                        + alert.getTaskId());
            }
        }

        MaintenanceTask task = new MaintenanceTask();
        task.setEquipmentId(equipmentId);
        task.setDescription(trimmed);
        task.setScheduledFor(scheduledFor);
        int taskId = taskDao.add(task);
        if (alertId != null) {
            alertDao.linkTask(alertId, taskId);
        }
        return taskId;
    }

    /**
     * Assigns an open task to the Kitchen-Tech who takes it.
     *
     * <p>Rules: the task must exist, must be open (not completed) and must not be taken by another
     * Kitchen-Tech. Taking a task this Kitchen-Tech already holds changes nothing. On success the
     * task's {@code techId} is set and the task is saved with
     * {@link MaintenanceTaskDao#update(MaintenanceTask)}.</p>
     *
     * @param taskId the task to take
     * @param techId the {@code user_id} of the Kitchen-Tech taking it
     * @throws ValidationException if the task does not exist, is completed or is taken by another
     *     Kitchen-Tech
     * @throws DaoException if the task cannot be read or saved
     */
    public void takeTask(int taskId, int techId) throws ValidationException, DaoException {
        MaintenanceTask task = taskDao.findById(taskId);
        if (task == null) {
            throw new ValidationException("Task " + taskId + " does not exist");
        }
        if (task.getCompletedAt() != null) {
            throw new ValidationException("Task " + taskId + " is already completed");
        }
        if (task.getTechId() != null) {
            if (task.getTechId() == techId) {
                return;
            }
            throw new ValidationException("Task " + taskId
                    + " is already taken by another Kitchen-Tech");
        }
        task.setTechId(techId);
        taskDao.update(task);
    }

    /**
     * Completes a task, resolves its alerts, pays the Kitchen-Tech and records the maintenance on
     * the equipment.
     *
     * <p>Rules: the task must exist, must be open and must be taken by this Kitchen-Tech;
     * {@code minutesLogged} must be more than 0. On success, with one {@code now} used for every
     * timestamp: the task's {@code completedAt} is set to now and {@code minutesLogged} is stored,
     * then the task is saved; every open alert of the task's equipment whose {@code taskId} is this
     * task is resolved at now; the Kitchen-Tech is credited; and the equipment's
     * {@code last_maintenance_at} is always set to now. The equipment's status is set back to
     * {@code AVAILABLE} only if the equipment has no other open alert after this task's alerts are
     * resolved; otherwise it keeps its current status. Not implemented yet.</p>
     *
     * <p>Depends on: ledger module, {@code LedgerBusinessLogic.record(techId,
     * TransactionSource.MAINTENANCE, minutes, null, description)}, where {@code minutes} is
     * {@code minutesLogged}; the ledger computes the credit for the Kitchen-Tech.</p>
     *
     * <p>Depends on: equipment module, operations to set the equipment's
     * {@code last_maintenance_at} and its status.</p>
     *
     * @param taskId the task to complete
     * @param techId the {@code user_id} of the Kitchen-Tech completing it
     * @param minutesLogged the minutes worked, more than 0
     * @throws ValidationException if the task does not exist, is already completed, is not taken by
     *     this Kitchen-Tech, or {@code minutesLogged} is 0 or less
     * @throws DaoException if the task, the alerts, the credit or the equipment cannot be saved
     */
    public void completeTask(int taskId, int techId, int minutesLogged)
            throws ValidationException, DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Builds the FR-06 activity report: the credits a user earned in a month, grouped by activity.
     *
     * <p>Meant for Trainers and Kitchen-Techs. Only {@code CREDIT} transactions created in the
     * given calendar month count; debits and payments are ignored. The amounts of each
     * {@link TransactionSource} are added up. Sources with no credit in the month are left out, and
     * the map iterates in the declaration order of {@link TransactionSource}. Not implemented
     * yet.</p>
     *
     * <p>Depends on: ledger module, {@code LedgerBusinessLogic.getStatement(userId, month)} for
     * the user's transactions of the month.</p>
     *
     * @param userId the {@code user_id} of the Trainer or Kitchen-Tech
     * @param month the calendar month to report, not {@code null}
     * @return total credits per activity; an empty map if the user earned nothing that month
     * @throws IllegalArgumentException if {@code month} is {@code null}
     * @throws DaoException if the transactions cannot be read
     */
    public Map<TransactionSource, BigDecimal> getActivityReport(int userId, YearMonth month)
            throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
